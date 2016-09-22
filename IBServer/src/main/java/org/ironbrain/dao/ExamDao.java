package org.ironbrain.dao;

import org.hibernate.criterion.Restrictions;
import org.ironbrain.IB;
import org.ironbrain.SessionData;
import org.ironbrain.core.Exam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@SuppressWarnings("unchecked")
@Transactional
public class ExamDao extends BaseDao {
    @Autowired
    protected SessionData data;

    public Exam create(int count) {
        Exam exam = new Exam();
        exam.setStartMs(IB.getNowMs());
        exam.setUser(data.getUser().getId());
        exam.setDone(false);
        exam.setCount(count);
        int id = (int) getSess().save(exam);
        exam.setId(id);
        return exam;
    }

    public Exam getLastUndoneExam() {
        List<Exam> exams;

        exams = getSess().createCriteria(Exam.class)
                .add(Restrictions.eq("user", data.getUser().getId()))
                .add(Restrictions.eq("done", false))
                .setMaxResults(1)
                .list();

        if (exams.isEmpty()) {
            return null;
        } else {
            return exams.get(0);
        }
    }

    public List<Exam> getDoneExams() {
        List<Exam> exams;

        exams = getSess().createCriteria(Exam.class)
                .add(Restrictions.eq("user", data.getUser().getId()))
                .add(Restrictions.eq("done", true))
                .list();

        return exams;
    }

    public Exam get(int id) {
        Exam someExam = (Exam) getSess().get(Exam.class, id);
        return someExam;
    }

    public void update(Exam exam) {
        getSess().update(exam);
    }
}
