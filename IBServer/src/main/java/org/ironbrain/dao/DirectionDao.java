package org.ironbrain.dao;

import org.hibernate.criterion.Restrictions;
import org.ironbrain.Result;
import org.ironbrain.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Repository
public class DirectionDao extends BaseDao {
    @Autowired
    SectionDao sectionDao;

    public Result addDirection(String name) {
        Direction direction = new Direction();
        direction.setOwner(data.getUserId());
        direction.setName(name);
        int id = (int) getSess().save(direction);

        return Result.getOk(id);
    }

    public Direction getDirection(Integer id) {
        Direction direction = (Direction) getSess().get(Direction.class, id);
        if (!data.testOwner(direction.getOwner())) {
            return null;
        }
        return direction;
    }

    public List<Direction> getDirections() {
        return getSess()
                .createCriteria(Direction.class)
                .add(Restrictions.eq("owner", data.getUserId()))
                .list();
    }

    Set<Section> allSections;
    List<Field> ourFields;

    public Result recalculateDirection(Integer id) {
        allSections = new TreeSet<>();
        ourFields = new LinkedList<>();

        Direction direction = getDirection(id);
        List<DirectionToField> fields = direction.getDirectionToFields();

        fields.forEach(dirToField -> {
            ourFields.add(dirToField.getField());
        });

        fields.forEach(dirToField -> {
            Field field = dirToField.getField();
            List<SectionToField> sectionToFields = field.getSectionToFields();
            sectionToFields.forEach(secToField -> {
                Section section = secToField.getSection();
                forAllSectionsInDirection(section);
            });
        });

        direction.setTicketsCount(allSections.size());
        getSess().save(direction);

        return Result.getOk();
    }

    boolean inverseCheck;
    private void forAllSectionsInDirection(Section section) {
        //Check if this Section has inversion fields and return if it true
        inverseCheck = false;
        section.getSectionToFields().forEach(secToField -> {
            if (secToField.getInverse()) {
                Field field = secToField.getField();
                if(ourFields.contains(field)){
                    inverseCheck = true;
                }
            }
        });
        if(inverseCheck){
            return;
        }

        if (section.getTicket() != null) {
            allSections.add(section);
        }

        sectionDao.getChildren(section.getId()).forEach(child -> {
            forAllSectionsInDirection(child);
        });
    }
}
