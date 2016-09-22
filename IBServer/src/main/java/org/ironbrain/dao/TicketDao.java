package org.ironbrain.dao;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.ironbrain.APIController;
import org.ironbrain.IB;
import org.ironbrain.Result;
import org.ironbrain.core.Section;
import org.ironbrain.core.Ticket;
import org.ironbrain.core.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Repository
@SuppressWarnings("unchecked")
@Transactional
public class TicketDao extends BaseDao {
    @Autowired
    APIController api;

    @Autowired
    SectionDao sectionDao;

    public Ticket getTicket(int id) {
        Ticket ticket = (Ticket) getSess().get(Ticket.class, id);
        return ticket;
    }

    public Result updateTicket(int id, String answers, String questions, String customInfo, String label, long clientVersionDate) {
        Result result;

        Ticket ticket = (Ticket) getSess().get(Ticket.class, id);

        if (!data.testOwner(ticket.getOwner())) {
            throw new RuntimeException("Access denied");
        }

        if (ticket.getEditDate() > clientVersionDate) {
            result = Result.getError("Есть более новая версия!");
            result.setSubRes("versionConflict");

            return result;
        }
        result = Result.getOk();

        ticket.setQuestions(questions);
        ticket.setAnswers(answers);
        ticket.setEditDate(IB.getNowMs());
        ticket.setCustomInfo(customInfo);

        getSess().update(ticket);

        Section section = api.getSectionFromTicket(id);
        section.setLabel(label);
        getSess().update(section);

        result.setData(ticket.getEditDate());

        return result;
    }

    public Pair<Section, Ticket> addTicket(int sectionId) {
        return addTicket(sectionId, data.getUser());
    }

    public Pair<Section, Ticket> addTicket(int sectionId, User user) {
        Section parentSection = sectionDao.getSection(sectionId, user);
        if (!data.testOwner(parentSection.getOwner())) {
            throw new RuntimeException("Access denied");
        }

        Long genNum = api.getChildCount(sectionId) + 1;

        Ticket ticket = new Ticket();
        ticket.setCreateDate(IB.getNowMs());
        ticket.setEditDate(ticket.getCreateDate());
        int ticketId = (int) getSess().save(ticket);
        ticket.setId(ticketId);
        ticket.setOwner(user.getId());

        Section section = new Section();
        section.setLabel("Билет " + genNum);
        section.setParent(sectionId);
        section.setTicket(ticketId);
        section.setOwner(user.getId());
        int ticketSectionId = (int) getSess().save(section);
        section.setId(ticketSectionId);

        ticket.setPath(api.getPathToSection(section.getId()));

        api.addRemind(ticketSectionId, null);

        return new ImmutablePair<>(section, ticket);
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

    public void updateTicket(Ticket ticket) {
        getSess().update(ticket);
    }

    public Result checkTicket(int id, long clientVersionDate) {
        Result result;

        Ticket ticket = (Ticket) getSess().get(Ticket.class, id);

        if (ticket.getEditDate() > clientVersionDate) {
            result = Result.getError("Есть более новая версия!");
            result.setSubRes("versionConflict");

            return result;
        } else {
            result = Result.getOk();
        }

        return result;
    }

    boolean firstIter = true;

    public List<Ticket> query(String query) {
        query = query.trim();

        List<String> words = Arrays.asList((query.split(" ")));

        StringBuilder dbQuery = new StringBuilder("FROM Ticket as ticket WHERE ((ticket.owner = " + data.getUserId() + ") AND ");

        firstIter = true;
        words.forEach(word -> {
            if (!firstIter) {
                dbQuery.append(" AND ");
            }
            dbQuery.append("(ticket.answers LIKE '%").append(word).append("%'");
            dbQuery.append(" OR ticket.questions LIKE '%").append(word).append("%'");
            dbQuery.append(" OR ticket.customInfo LIKE '%").append(word).append("%'");
            dbQuery.append(" OR ticket.path LIKE '%").append(word).append("%'").append(")");

            firstIter = false;
        });

        dbQuery.append(")");
        Query queryResult = getSess().createQuery(dbQuery.toString());


        return queryResult.list();
    }

    public List<Ticket> getAllTickets() {
        return getSess().createCriteria(Ticket.class).list();
    }

    public List<Ticket> getAllTicketsFromEnd(int limit) {
        return getSess().createCriteria(Ticket.class).addOrder(Order.desc("id")).setMaxResults(limit).list();
    }
}
