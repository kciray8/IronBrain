package org.ironbrain.core;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Direction {
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Id
    @GeneratedValue
    private Integer id;

    public Integer getTicketsCount() {
        return ticketsCount;
    }

    public void setTicketsCount(Integer ticketsCount) {
        this.ticketsCount = ticketsCount;
    }

    private Integer ticketsCount = 0;

    public List<DirectionToField> getDirectionToFields() {
        return directionToFields;
    }

    public void setDirectionToFields(List<DirectionToField> directionToFields) {
        this.directionToFields = directionToFields;
    }

    @OneToMany
    @JoinColumn(name = "direction_id")
    private List<DirectionToField> directionToFields = new ArrayList<>();

    public Integer getOwner() {
        return owner;
    }

    public void setOwner(Integer owner) {
        this.owner = owner;
    }

    private Integer owner;

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}