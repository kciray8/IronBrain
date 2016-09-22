package org.ironbrain.dao;

import org.ironbrain.Result;
import org.ironbrain.core.SectionToField;
import org.springframework.stereotype.Repository;

@Repository
@SuppressWarnings("unchecked")
public class SectionToFieldDao extends BaseDao {
    public Result invertField(Integer fieldToSecId) {
        SectionToField sectionToField = (SectionToField) getSess().get(SectionToField.class, fieldToSecId);
        sectionToField.setInverse(!sectionToField.getInverse());
        getSess().save(sectionToField);

        return Result.getOk(sectionToField.getInverse());
    }

    public Result deleteSectionToField(Integer sectionToFieldId) {
        SectionToField sectionToField = new SectionToField();
        sectionToField.setId(sectionToFieldId);
        getSess().delete(sectionToField);

        return Result.getOk();
    }
}