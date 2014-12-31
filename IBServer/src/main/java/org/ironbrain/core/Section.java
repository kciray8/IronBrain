package org.ironbrain.core;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Table(name = "Sections")
@Entity
public class Section implements Comparable<Section> {
    @Id
    @GeneratedValue
    private Integer id;

    private Integer parent;

    private Integer ticket;

    private String label;

    public Long getRemindDate() {
        return remindDate;
    }

    public void setRemindDate(Long remindDate) {
        this.remindDate = remindDate;
    }

    private Long remindDate;

    /**
     * 0 - simple
     * 1 - TIME
     */
    private Integer type;

    public Section() {

    }

    public Section(Integer id) {

    }

    public Section(Integer id, Integer parent, String label) {
        this.parent = parent;
        this.label = label;
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParent() {
        return parent;
    }

    public void setParent(Integer parent) {
        this.parent = parent;
    }


    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Integer getTicket() {
        return ticket;
    }

    public void setTicket(Integer ticket) {
        this.ticket = ticket;
    }

    @JsonIgnore
    public String getFramedLabel() {
        if (ticket == null) {
            return "[" + label + "]";
        } else {
            return label;
        }
    }

    private Integer owner;

    public Integer getOwner() {
        return owner;
    }

    public void setOwner(Integer owner) {
        this.owner = owner;
    }


    @OneToMany
    @JoinColumn(name = "sectionId")
    private List<SectionToField> sectionToFields = new LinkedList<>();

    public List<SectionToField> getSectionToFields() {
        return sectionToFields;
    }

    public void setSectionToFields(List<SectionToField> sectionToFields) {
        this.sectionToFields = sectionToFields;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }



    /*
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "sectiontofield",
            joinColumns = {@JoinColumn(name = "sectionId")},
            inverseJoinColumns = {@JoinColumn(name = "fieldId")})
    private List<Field> fields = new LinkedList<>();

    public List<Field> getFields() {
        return this.fields;
    }

    public void setFields(List<Field> categories) {
        this.fields = categories;
    }*/

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Section section = (Section) o;

        if (!id.equals(section.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public int compareTo(Section o) {
        return this.getId() - o.getId();
    }
}
