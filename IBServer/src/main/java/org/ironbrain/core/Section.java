package org.ironbrain.core;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "Sections")
@Entity
public class Section{
    @Id
    @GeneratedValue
    private Integer id;

    private Integer parent;

    private Integer ticket;

    private String label;


    public Section(){

    }

    public Section(Integer id){

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
    public String getFramedLabel(){
        if(ticket == null){
            return "[" +label + "]";
        }else{
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
}
