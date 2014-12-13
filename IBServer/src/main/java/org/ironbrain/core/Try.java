package org.ironbrain.core;

import javax.persistence.*;

@Table(name = "Try")
@Entity
public class Try {
    @Id
    @GeneratedValue
    private Integer id;

    private Integer user;

    private Boolean correct = false;

    private Long startMs;

    private Long endMs;

    public Integer getAttemptNum() {
        return attemptNum;
    }

    public void setAttemptNum(Integer attemptNum) {
        this.attemptNum = attemptNum;
    }

    private Integer attemptNum;

    private Integer exam;

    private Boolean done = false;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUser() {
        return user;
    }

    public void setUser(Integer user) {
        this.user = user;
    }

    public Boolean getCorrect() {
        return correct;
    }

    public void setCorrect(Boolean correct) {
        this.correct = correct;
    }

    public Long getStartMs() {
        return startMs;
    }

    public void setStartMs(Long startMs) {
        this.startMs = startMs;
    }

    public Long getEndMs() {
        return endMs;
    }

    public void setEndMs(Long endMs) {
        this.endMs = endMs;
    }

    public Integer getExam() {
        return exam;
    }

    public void setExam(Integer exam) {
        this.exam = exam;
    }

    public Boolean getDone() {
        return done;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Integer getTicket() {
        return ticket;
    }

    public void setTicket(Integer ticket) {
        this.ticket = ticket;
    }

    private Integer num;

    private Integer ticket;

    @Transient
    public long getDurationSec() {
        return (endMs - startMs) / 1000;
    }
}
