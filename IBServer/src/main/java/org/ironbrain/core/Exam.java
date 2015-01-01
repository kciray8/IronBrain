package org.ironbrain.core;

import org.ironbrain.utils.DateUtils;

import javax.persistence.*;

@Table(name = "Exam")
@Entity
public class Exam {
    @Id
    @GeneratedValue
    private Integer id;

    private Boolean done = false;

    private Integer user;

    public Boolean getDone() {
        return done;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    private Integer count;

    private Long startMs;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public Integer getUser() {
        return user;
    }

    public void setUser(Integer user) {
        this.user = user;
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

    private Long endMs;

    @Transient
    public long getDurationMin() {

        return (endMs - startMs) / (1000 * 60);
    }

    @Transient
    public String getName(){
        return DateUtils.getNiceDate(startMs);
    }
}
