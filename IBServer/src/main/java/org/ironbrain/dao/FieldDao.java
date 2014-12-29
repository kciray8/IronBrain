package org.ironbrain.dao;

import org.hibernate.criterion.Restrictions;
import org.ironbrain.Result;
import org.ironbrain.core.DirectionToField;
import org.ironbrain.core.Field;
import org.ironbrain.core.Section;
import org.ironbrain.core.SectionToField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@SuppressWarnings("unchecked")
public class FieldDao extends BaseDao {
    @Autowired
    private SectionDao sectionDao;

    public Result addField(String name) {
        Field field = new Field();
        field.setLabel(name);
        field.setOwner(data.getUserId());
        int id = (int) getSess().save(field);

        return Result.getOk(id);
    }

    public Result addFieldToSection(Integer fieldId, Integer sectionId) {
        SectionToField sectionToField = new SectionToField();

        Section section = sectionDao.getSection(sectionId, data.getUser());
        sectionToField.setSection(section);

        Field field = getField(fieldId);
        sectionToField.setField(field);

        int id = (int) getSess().save(sectionToField);

        return Result.getOk(id);
    }

    public Result addFieldToDirection(Integer fieldId, Integer directionId) {
        DirectionToField directionToField = new DirectionToField();
        directionToField.setDirection_id(directionId);

        Field field = getField(fieldId);
        directionToField.setField(field);

        int id = (int) getSess().save(directionToField);
        return Result.getOk(id);
    }

    public Field getField(Integer id) {
        return (Field) getSess().get(Field.class, id);
    }

    public List<Field> getFields() {
        List<Field> fields;

        fields = getSess().createCriteria(Field.class)
                .add(Restrictions.eq("owner", data.getUserId()))
                .list();

        return fields;
    }
}
