package org.ironbrain.dao;

import org.hibernate.criterion.Restrictions;
import org.ironbrain.APIController;
import org.ironbrain.IB;
import org.ironbrain.Result;
import org.ironbrain.core.*;
import org.omg.CORBA.IntHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class DirectionDao extends BaseDao {
    @Autowired
    SectionDao sectionDao;

    @Autowired
    FieldDao fieldDao;

    @Autowired
    RemindDao remindDao;

    @Autowired
    APIController api;

    public Direction addDirection(String name) {
        Direction direction = new Direction();
        direction.setOwner(data.getUserId());
        direction.setName(name);
        int id = (int) getSess().save(direction);
        direction.setId(id);

        return direction;
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

    public Result recalculateDirection(Integer id) {

        Direction direction = getDirection(id);

        Set<Section> allSections = getAllTSections(direction);

        direction.setTicketsCount(allSections.size());

        Set<Section> needToRemindSec = getNeedToRemindSections(allSections);
        double needToRemindPercent = ((double) needToRemindSec.size()) / allSections.size();
        double knowPercent = 1 - needToRemindPercent;

        direction.setKnowPercent(knowPercent);
        direction.setTicketKnownCount(direction.getTicketsCount() - needToRemindSec.size());

        getSess().save(direction);

        return Result.getOk();
    }

    public List<Field> getFields(Direction direction) {
        List<Field> ourFields = new LinkedList<>();
        List<DirectionToField> fields = direction.getDirectionToFields();
        fields.forEach(dirToField -> {
            ourFields.add(dirToField.getField());
        });
        return ourFields;
    }

    boolean inverseCheck;

    private void recursiveCollectTSections(Section section, List<Field> filerFields,
                                           Set<Section> result, int count) {
        //Check if this Section has inversion fields and return if it true
        inverseCheck = false;
        section.getSectionToFields().forEach(secToField -> {
            if (secToField.getInverse()) {
                Field field = secToField.getField();
                if (filerFields.contains(field)) {
                    inverseCheck = true;
                }
            }
        });
        if (inverseCheck) {
            return;
        }

        if (section.getTicket() != null) {
            section.setNum(count);
            result.add(section);
        }

        IntHolder childCount = new IntHolder(1);
        sectionDao.getChildren(section.getId()).forEach(child -> {
            recursiveCollectTSections(child, filerFields, result, childCount.value);
            childCount.value++;
        });
    }

    public Set<Section> getAllTSections(Direction direction) {
        Set<Section> allSections = new TreeSet<>();

        long ms = IB.getNowMs();
        List<Field> directionFields = getFields(direction);
        directionFields.forEach(field -> {
            List<SectionToField> sectionToFields = field.getSectionToFields();
            sectionToFields.forEach(secToField -> {
                Section section = secToField.getSection();
                recursiveCollectTSections(section, directionFields, allSections, 1);
            });
        });

        System.out.println("RECALC & " + (IB.getNowMs() - ms));

        return allSections;
    }

    public double getKnowPercent(Direction direction) {
        Set<Section> sections = getAllTSections(direction);
        Set<Section> needToRemindSec = getNeedToRemindSections(sections);
        double needToRemindPercent = ((double) needToRemindSec.size()) / sections.size();
        double knowPercent = 1 - needToRemindPercent;

        return knowPercent;
    }

    public Set<Section> getNeedToRemindSections(Direction direction) {
        Set<Section> sections = getAllTSections(direction);
        return getNeedToRemindSections(sections);
    }

    public void recalcluateAllDirections() {
        getDirections().forEach(direction -> {
            recalculateDirection(direction.getId());
        });
    }

    public void sliceAndAddToRemind(Direction direction, int count) {
        Set<Section> needToRemind = getNeedToRemindSections(direction);
        ArrayList<Section> result = new ArrayList<>();
        AtomicInteger realCount = new AtomicInteger(count);
        if (realCount.intValue() > needToRemind.size()) {
            realCount.set(needToRemind.size());
        }

        IntHolder tempLevel = new IntHolder(1);
        List<Section> levelSections = new ArrayList<>();
        while ((result.size() < realCount.intValue()) && (!needToRemind.isEmpty())) {
            System.out.println("---- " + result.size() + "    " + needToRemind.size() + "   ->  " + levelSections.size());

            levelSections.clear();
            needToRemind.forEach(section -> {

                System.out.println("=== " + section.getNum() + "    " + tempLevel.value);
                if (section.getNum() == tempLevel.value) {
                    if (result.size() + levelSections.size() < realCount.intValue()) {
                        levelSections.add(section);
                    }
                }
            });
            result.addAll(levelSections);
            needToRemind.removeAll(levelSections);
            tempLevel.value++;
        }

        result.forEach(sec -> {
            Remind remind = remindDao.addRemind(sec);
        });
    }

    private Set<Section> getNeedToRemindSections(Set<Section> source) {
        long nowMs = IB.getNowMs();
        TreeSet<Section> needToRemind = new TreeSet<>();

        source.forEach(section -> {
            if ((section.getRemind() == null) || (section.getRemind() < nowMs)) {
                needToRemind.add(section);
            }
        });

        return needToRemind;
    }
}
