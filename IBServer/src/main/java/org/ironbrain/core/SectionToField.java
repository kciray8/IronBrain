package org.ironbrain.core;


import javax.persistence.*;
import java.io.Serializable;

@Entity
public class SectionToField implements Serializable, IFieldMapper {
    @Id
    @GeneratedValue
    private Integer id;

    //private Integer sectionId;

    private Boolean inverse = false;

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    @ManyToOne
    @JoinColumn(name = "fieldId")
    public Field field;

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    @ManyToOne
    @JoinColumn(name = "sectionId")
    public Section section;

    /*
    public Integer getSectionId() {
        return sectionId;
    }

    public void setSectionId(Integer ticketId) {
        this.sectionId = ticketId;
    }*/

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getInverse() {
        return inverse;
    }

    public void setInverse(Boolean inverse) {
        this.inverse = inverse;
    }
}
