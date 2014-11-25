package org.ironbrain.dao;

import org.ironbrain.Result;
import org.ironbrain.core.Section;
import org.ironbrain.core.Ticket;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@SuppressWarnings("unchecked")
@Transactional
public class TicketDao extends BaseDao {

    public Ticket getTicket(int id) {
        Ticket ticket = (Ticket) getSess().get(Ticket.class, id);
        return ticket;
    }

    public Result updateTicket(int id, String answers, String questions) {
        Ticket ticket = (Ticket) getSess().get(Ticket.class, id);
        ticket.setQuestions(questions);
        ticket.setAnswers(answers);
        getSess().update(ticket);

        return new Result();
    }

    public Section addTicket(int sectionId) {
        Ticket ticket = new Ticket();
        int ticketId = (int) getSess().save(ticket);

        Section section = new Section();
        section.setLabel("билет1");
        section.setName("name");
        section.setParent(sectionId);
        section.setTicket(ticketId);
        int ticketSessionId = (int) getSess().save(section);
        section.setId(ticketSessionId);

        return section;
    }
}
