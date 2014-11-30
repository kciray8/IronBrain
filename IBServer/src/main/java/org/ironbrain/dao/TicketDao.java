package org.ironbrain.dao;

import org.ironbrain.APIController;
import org.ironbrain.Result;
import org.ironbrain.core.Section;
import org.ironbrain.core.Ticket;
import org.ironbrain.core.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@SuppressWarnings("unchecked")
@Transactional
public class TicketDao extends BaseDao {
    @Autowired
    APIController api;

    public Ticket getTicket(int id) {
        Ticket ticket = (Ticket) getSess().get(Ticket.class, id);
        return ticket;
    }

    public Result updateTicket(int id, String answers, String questions, String label, long clientVersionDate) {
        Result result;

        Ticket ticket = (Ticket) getSess().get(Ticket.class, id);

        System.out.println("update ..." + ticket.getEditDate() + "  " + clientVersionDate);

        if(ticket.getEditDate() > clientVersionDate){
            result = Result.getError("Есть более новая версия!");
            result.setSubRes("versionConflict");

            return result;
        }else {
            result = Result.getOk();
            result.setData(System.currentTimeMillis());
        }

        ticket.setQuestions(questions);
        ticket.setAnswers(answers);
        ticket.setEditDate(System.currentTimeMillis());
        getSess().update(ticket);

        Section section = api.getSectionFromTicket(id);
        section.setLabel(label);
        getSess().update(section);

        return result;
    }

    public Section addTicket(int sectionId, User user) {
        Long genNum = api.getChildCount(sectionId) + 1;

        Ticket ticket = new Ticket();
        ticket.setCreateDate(System.currentTimeMillis());
        ticket.setRemind(ticket.getCreateDate());
        ticket.setEditDate(ticket.getCreateDate());
        int ticketId = (int) getSess().save(ticket);
        ticket.setOwner(user.getId());

        Section section = new Section();
        section.setLabel("Билет " + genNum);
        section.setParent(sectionId);
        section.setTicket(ticketId);
        section.setOwner(user.getId());
        int ticketSessionId = (int) getSess().save(section);
        section.setId(ticketSessionId);

        api.addRemind(ticketSessionId);

        return section;
    }

    public String getTicketLabel(int ticketId) {
        Section section = api.getSectionFromTicket(ticketId);
        return section.getLabel();
    }

    public void updateTicket(int id, long editDate) {
        Ticket ticket = (Ticket) getSess().get(Ticket.class, id);

        ticket.setEditDate(editDate);
        getSess().update(ticket);
    }

    public Result checkTicket(int id, long clientVersionDate) {
        Result result;

        Ticket ticket = (Ticket) getSess().get(Ticket.class, id);

        if(ticket.getEditDate() > clientVersionDate){
            result = Result.getError("Есть более новая версия!");
            result.setSubRes("versionConflict");

            return result;
        }else {
            result = Result.getOk();
        }

        return result;
    }
}
