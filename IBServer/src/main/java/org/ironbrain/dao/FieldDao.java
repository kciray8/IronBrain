package org.ironbrain.dao;

import org.hibernate.criterion.Restrictions;
import org.ironbrain.Result;
import org.ironbrain.core.Field;
import org.ironbrain.core.SectionToField;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@SuppressWarnings("unchecked")
public class FieldDao extends BaseDao {
    public Result addField(String name) {
        Field field = new Field();
        field.setLabel(name);
        field.setOwner(data.getUserId());
        int id = (int) getSess().save(field);

        return Result.getOk(id);
    }

    public Result addFieldToSection(Integer fieldId, Integer sectionId) {
        SectionToField sectionToField = new SectionToField();
        sectionToField.setSectionId(sectionId);
        Field field = getField(fieldId);
        sectionToField.setField(field);
        int id = (int) getSess().save(sectionToField);

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
