package org.ironbrain.dao;

import com.google.common.base.Joiner;
import org.hibernate.criterion.Restrictions;
import org.ironbrain.APIController;
import org.ironbrain.Result;
import org.ironbrain.SessionData;
import org.ironbrain.core.Remind;
import org.ironbrain.core.Section;
import org.ironbrain.core.Ticket;
import org.ironbrain.core.User;
import org.ironbrain.utils.HtmlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Repository
@SuppressWarnings("unchecked")
@Transactional
public class RemindDao extends BaseDao {
    @Autowired
    APIController api;

    @Autowired
    private TicketDao ticketDao;

    @Autowired
    private SectionDao sectionDao;

    @Autowired
    protected SessionData data;

    public void addRemind(Section section, int user) {
        //One ticket
        if (section.getTicket() != null) {
            Ticket ticket = ticketDao.getTicket(section.getTicket());

            Remind remind = new Remind();
            remind.setTicket(section.getTicket());
            remind.setUser(user);
            remind.setLabel(section.getLabel());

            remind.setShortText(HtmlUtils.getShortText(ticket.getQuestions(), 50));

            List<String> pathList = new ArrayList<>();
            api.getPath(section.getId()).forEach(sec -> {
                pathList.add(sec.getLabel());
            });
            pathList.remove(pathList.size() - 1);
            String path = Joiner.on(" → ").join(pathList);
            remind.setPath(path);

            getSess().save(remind);
        } else {
            List<Section> children = api.getSections(section.getId());
            children.forEach(child -> {
                addRemind(child.getId(), user);
            });
        }
    }

    public Remind get(int id) {
        Remind remind = (Remind) getSess().get(Remind.class, id);
        return remind;
    }

    public void addRemind(int sectionId, int user) {
        Section section = api.getSection(sectionId);
        addRemind(section, user);
    }

    public List<Remind> getReminds() {
        return getReminds(data.getUser());
    }

    public List<Remind> getReminds(User user) {
        List<Remind> reminds;

        reminds = getSess().createCriteria(Remind.class)
                .add(Restrictions.eq("user", user.getId()))
                .list();

        return reminds;
    }

    public Result delete(int id) {
        Remind remind = new Remind();
        remind.setId(id);
        getSess().delete(remind);

        return Result.getOk();
    }

    public void deleteWithTicketId(Integer ticket) {
        List<Remind> reminds = getSess().createCriteria(Remind.class)
                .add(Restrictions.eq("user", data.getUser().getId()))
                .add(Restrictions.eq("ticket", ticket))
                .list();

        reminds.forEach(remind -> {
            getSess().delete(remind);
        });
    }

    public void addRemindTicket(Integer ticketId) {
        Section section = sectionDao.getSectionFromTicket(ticketId);
        addRemind(section, data.getUser().getId());
    }
}
