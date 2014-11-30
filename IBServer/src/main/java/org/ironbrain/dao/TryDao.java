package org.ironbrain.dao;

import org.hibernate.criterion.Restrictions;
import org.ironbrain.SessionData;
import org.ironbrain.core.Try;
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

    public Try create(int ticketId, int examId, int num, int attemptNum) {
        Try someTry = new Try();
        someTry.setStartMs(System.currentTimeMillis());
        someTry.setUser(data.getUser().getId());
        someTry.setCorrect(false);
        someTry.setDone(false);
        someTry.setNum(0);
        someTry.setExam(examId);
        someTry.setTicket(ticketId);
        someTry.setNum(num);
        someTry.setAttemptNum(attemptNum);
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
            return tries.get(0);
        }
    }
}