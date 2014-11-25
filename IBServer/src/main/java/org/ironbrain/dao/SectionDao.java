package org.ironbrain.dao;

import org.hibernate.criterion.Restrictions;
import org.ironbrain.Result;
import org.ironbrain.core.Section;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
@SuppressWarnings("unchecked")
@Transactional
public class SectionDao extends BaseDao{
    public Section getSection(int id) {
        Section section = (Section) getSess().get(Section.class, id);
        return section;
    }

    public List<Section> getSections(int id) {
        List<Section> section = getSess().createCriteria(Section.class)
                .add(Restrictions.eq("parent", id)).list();

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

    public Result addSection(int parent, String label) {
        Result result = new Result();

        Section section = new Section();
        section.setParent(parent);
        section.setName(label);
        section.setLabel(label);
        int id = (int)getSess().save(section);
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
}
