package org.ironbrain.dao;

import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.ironbrain.Result;
import org.ironbrain.core.Section;
import org.ironbrain.core.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
@SuppressWarnings("unchecked")
@Transactional
public class SectionDao extends BaseDao {
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
        Section section = (Section) getSess().createCriteria(Section.class)
                .add(Restrictions.eq("ticket", ticketId)).uniqueResult();

        return section;
    }

    public List<Section> getSections(int id, User user) {

        List<Section> section;
        if (user != null) {
            section = getSess().createCriteria(Section.class)
                    .add(Restrictions.eq("parent", id))
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
                sections.add(section);
                if (section.getParent() == null) {
                    break;
                }
                tempSection = section.getParent();
            }
        } while (true);

        Collections.reverse(sections);
        return sections;
    }

    public Result addSection(Integer parent, String label, User user) {
        Result result = new Result();

        Section section = new Section();
        section.setParent(parent);
        section.setLabel(label);
        section.setOwner(user.getId());
        int id = (int) getSess().save(section);
        section.setId(id);

        result.setData(section);

        return result;
    }

    public Result deleteSection(int id) {
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
