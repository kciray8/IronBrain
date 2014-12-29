package org.ironbrain.dao;

import org.ironbrain.Result;
import org.ironbrain.core.DirectionToField;
import org.springframework.stereotype.Repository;

@Repository
@SuppressWarnings("unchecked")
public class DirectionToFieldDao extends BaseDao {
    public Result invertField(Integer dirToFieldId) {
        DirectionToField directionToField = (DirectionToField) getSess().get(DirectionToField.class, dirToFieldId);
        directionToField.setInverse(!directionToField.getInverse());
        getSess().save(directionToField);

        return Result.getOk(directionToField.getInverse());
    }

    public Result deleteDirectionToField(Integer directionToFieldId) {
        DirectionToField directionToField = new DirectionToField();
        directionToField.setId(directionToFieldId);
        getSess().delete(directionToField);

        return Result.getOk();
    }
}