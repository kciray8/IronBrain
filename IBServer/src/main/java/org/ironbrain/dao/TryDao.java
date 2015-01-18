package org.ironbrain.dao;

import org.hibernate.criterion.Restrictions;
import org.ironbrain.IB;
import org.ironbrain.SessionData;
import org.ironbrain.core.Ticket;
import org.ironbrain.core.Try;
import org.ironbrain.utils.HtmlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@SuppressWarnings("unchecked")
@Transactional
public class TryDao extends BaseDao {
    @Autowired
    protected SessionData data;

    @Autowired
    private RemindDao remindDao;


    public Try create(Ticket ticket, int examId, int num, int attemptNum) {
        Try someTry = new Try();
        someTry.setShortText(HtmlUtils.getEscapedShortText(ticket.getQuestions(), 100));
        someTry.setUser(data.getUser().getId());
        someTry.setCorrect(false);
        someTry.setDone(false);
        someTry.setExam(examId);
        someTry.setTicket(ticket.getId());
        someTry.setPathToSection(ticket.getPath());
        someTry.setNum(num);
        someTry.setAttemptNum(attemptNum);
        int id = (int) getSess().save(someTry);
        someTry.setId(id);
        return someTry;
    }

    public Try createNextAttempt(Try lastTry, int num) {
        Try someTry = new Try();
        someTry.setUser(data.getUser().getId());
        someTry.setPathToSection(lastTry.getPathToSection());
        someTry.setCorrect(false);
        someTry.setDone(false);
        someTry.setShortText(lastTry.getShortText());
        someTry.setNum(0);
        someTry.setExam(lastTry.getExam());
        someTry.setTicket(lastTry.getTicket());
        someTry.setNum(num);
        someTry.setAttemptNum(lastTry.getAttemptNum() + 1);
        int id = (int) getSess().save(someTry);
        someTry.setId(id);
        return someTry;
    }

    public List<Try> getTriesFromExam(int examId) {
        List<Try> tries = getSess().createCriteria(Try.class)
                .add(Restrictions.eq("user", data.getUserId()))
                .add(Restrictions.eq("exam", examId)).list();

        return tries;
    }

    public Try getTry(int id) {
        Try someTry = (Try) getSess().get(Try.class, id);
        return someTry;
    }

    public void updateTry(Try someTry) {
        getSess().update(someTry);
        if (someTry.getCorrect()) {
            remindDao.deleteWithTicketId(someTry.getTicket());
        }
    }

    public Try getTempTry(int examId) {
        List<Try> tries = getSess().createCriteria(Try.class)
                .add(Restrictions.eq("user", data.getUserId()))
                .add(Restrictions.eq("done", false))
                .setMaxResults(1)
                .add(Restrictions.eq("exam", examId)).list();

        if (tries.isEmpty()) {
            return null;
        } else {
            Try tempTry = tries.get(0);
            if (tempTry.getStartMs() == null) {
                tempTry.setStartMs(IB.getNowMs());
                getSess().save(tempTry);
            }

            return tempTry;
        }
    }
}