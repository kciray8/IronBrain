package org.ironbrain.core;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class DirectionToField implements Serializable, IFieldMapper {
    @Id
    @GeneratedValue
    private Integer id;

    public Integer getDirection_id() {
        return direction_id;
    }

    public void setDirection_id(Integer direction_id) {
        this.direction_id = direction_id;
    }

    private Integer direction_id;

    @Override
    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    @ManyToOne
    @JoinColumn
    private Field field;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    private Boolean inverse = false;

    public Boolean getInverse() {
        return inverse;
    }

    public void setInverse(Boolean inverse) {
        this.inverse = inverse;
    }
}

