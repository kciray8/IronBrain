package org.ironbrain.dao;

import org.ironbrain.APIController;
import org.ironbrain.core.Remind;
import org.ironbrain.core.Section;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@SuppressWarnings("unchecked")
@Transactional
public class RemindDao extends BaseDao {
    @Autowired
    APIController api;

    public void addRemind(Section section, int user) {
        //One ticket
        if (section.getTicket() != null) {
            Remind remind = new Remind();
            remind.setTicket(section.getTicket());
            remind.setUser(user);
            getSess().save(remind);
        } else {
            List<Section> children = api.getSections(section.getId());
            children.forEach(child -> {
                addRemind(child.getId(), user);
            });
        }
    }

    public void addRemind(int sectionId, int user) {
        Section section = api.getSection(sectionId);
        addRemind(section, user);
    }
}
