package org.ironbrain.core;

import org.ironbrain.IB;

import javax.persistence.*;
import java.util.Calendar;

@Table(name = "Tickets")
@Entity
public class Ticket {
    public static final String REMIND_NOW = "rNow";
    public static final String REMIND_LATER = "rLater";
    public static final String REMIND_DAY = "rDay";
    public static final String REMIND_WEEK = "rWeek";
    public static final String REMIND_MONTH = "rMonth";
    public static final String REMIND_HALF_YEAR = "rHalfYear";
    public static final String REMIND_YEAR = "rYear";

    @Id
    @GeneratedValue
    private Integer id;

    public Long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Long createDate) {
        this.createDate = createDate;
    }

    public Long getEditDate() {
        return editDate;
    }

    public void setEditDate(Long editDate) {
        this.editDate = editDate;
    }

    private Long createDate;

    private Long editDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Lob
    private String questions = "";

    public String getQuestions() {
        return questions;
    }

    public void setQuestions(String questions) {
        this.questions = questions;
    }

    @Lob
    private String answers = "";

    public String getAnswers() {
        return answers;
    }

    public void setAnswers(String answers) {
        this.answers = answers;
    }

    public String getCustomInfo() {
        return customInfo;
    }

    public void setCustomInfo(String customInfo) {
        this.customInfo = customInfo;
    }

    private String customInfo = "";

    private Integer owner;

    public Integer getOwner() {
        return owner;
    }

    public void setOwner(Integer owner) {
        this.owner = owner;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    private String path = "";

    public static long getMsFromState(String state) {
        Calendar calendar = IB.getNowCalendar();

        switch (state) {
            case REMIND_NOW:
            case REMIND_LATER:
                break;
            case REMIND_DAY:
                calendar.add(Calendar.DATE, 1);
                break;
            case REMIND_WEEK:
                calendar.add(Calendar.WEEK_OF_MONTH, 1);
                break;
            case REMIND_MONTH:
                calendar.add(Calendar.MONTH, 1);
                break;
            case REMIND_HALF_YEAR:
                calendar.add(Calendar.MONTH, 6);
                break;
            case REMIND_YEAR:
                calendar.add(Calendar.YEAR, 1);
                break;
        }

        return calendar.getTimeInMillis();
    }

}
