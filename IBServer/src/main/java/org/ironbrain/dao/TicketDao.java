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

    public Result updateTicket(int id, String answers, String questions, String label) {
        Ticket ticket = (Ticket) getSess().get(Ticket.class, id);
        ticket.setQuestions(questions);
        ticket.setAnswers(answers);
        getSess().update(ticket);

        Section section = api.getSectionFromTicket(id);
        section.setLabel(label);
        getSess().update(section);

        return new Result();
    }

    public Section addTicket(int sectionId, User user) {
        Long genNum = api.getChildCount(sectionId) + 1;

        Ticket ticket = new Ticket();
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
}
