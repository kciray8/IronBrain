package org.ironbrain.dao;

import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.ironbrain.Result;
import org.ironbrain.SessionData;
import org.ironbrain.core.Section;
import org.ironbrain.core.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
@SuppressWarnings("unchecked")
@Transactional
public class SectionDao extends BaseDao {
    @Autowired
    protected SessionData data;

    public Section getSection(int id) {
        return getSection(id, data.getUser());
    }


    public Section getSection(int id, User user) {
        Section section = (Section) getSess().get(Section.class, id);
        if (user != null) {
            if (!section.getOwner().equals(user.getId())) {
                return null;
            }
        }
        return section;
    }

    public Section getSectionFromTicket(int ticketId) {
        System.out.println("parentId " + ticketId);

        Section section = (Section) getSess().createCriteria(Section.class)
                .add(Restrictions.eq("ticket", ticketId)).uniqueResult();

        return section;
    }

    public Section getTimeSection() {
        Section section = (Section) getSess().createCriteria(Section.class)
                .add(Restrictions.eq("owner", data.getUserId()))
                .add(Restrictions.eq("type", 1)).uniqueResult();

        return section;
    }

    public List<Section> getChildren(int parentId) {
        return getChildren(parentId, data.getUser());
    }

    public List<Section> getChildren(int parentId, User user) {

        List<Section> section;
        if (user != null) {
            section = getSess().createCriteria(Section.class)
                    .add(Restrictions.eq("parent", parentId))
                    .add(Restrictions.eq("owner", user.getId()))
                    .list();
        } else {
            section = new ArrayList<>();
        }

        return section;
    }

    public List<Section> getPath(int sec) {
        List<Section> sections = new ArrayList<>();
        int tempSection = sec;
        do {
            Section section = (Section) getSess().get(Section.class, tempSection);
            if (section != null) {

                //Detect recursion
                if (sections.contains(section)) {
                    break;
                }

                sections.add(section);
                if (section.getParent() == null) {
                    break;
                }
                tempSection = section.getParent();
            } else {
                break;
            }
        } while (true);

        Collections.reverse(sections);
        return sections;
    }

    public void update(Section section) {
        getSess().update(section);
    }

    public Result<Section> addSection(Integer parent, String label) {
        return addSection(parent, label, data.getUser());
    }

    public Result<Section> addSection(Integer parent, String label, User user) {
        Result<Section> result = new Result<>();

        Section section = new Section();
        section.setParent(parent);
        section.setLabel(label);
        section.setOwner(user.getId());
        int id = (int) getSess().save(section);
        section.setId(id);

        result.setData(section);

        return result;
    }

    public Result<Section> deleteSection(int id) {
        Result result = new Result();

        Section section = new Section();
        section.setId(id);
        getSess().delete(section);
        result.setData(section);

        return result;
    }

    public Long getChildCount(int section) {
        return (Long) getSess().createCriteria(Section.class)
                .setProjection(Projections.rowCount()).add(Restrictions.eq("parent", section)).uniqueResult();
    }
}
