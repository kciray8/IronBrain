package org.ironbrain.core;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Field {
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @GeneratedValue
    @Id
    private Integer id;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    private String label;

    public Integer getOwner() {
        return owner;
    }

    public void setOwner(Integer owner) {
        this.owner = owner;
    }

    private Integer owner;

    public List<SectionToField> getSectionToFields() {
        return sectionToFields;
    }

    public void setSectionToFields(List<SectionToField> sectionToFields) {
        this.sectionToFields = sectionToFields;
    }

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "fieldId")
    private List<SectionToField> sectionToFields = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Field field = (Field) o;

        if (!id.equals(field.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

}