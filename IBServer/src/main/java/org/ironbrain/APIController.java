package org.ironbrain;

import org.ironbrain.core.Section;
import org.ironbrain.core.Ticket;
import org.ironbrain.dao.SectionDao;
import org.ironbrain.dao.TicketDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

public class APIController {
    @Autowired
    private SectionDao sectionDao;

    @Autowired
    private TicketDao ticketDao;

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/get_sections")
    public List<Section> getSections(@RequestParam int sec) {
        return sectionDao.getSections(sec);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/get_section")
    public Section getSection(@RequestParam int sec) {
        return sectionDao.getSection(sec);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/get_path")
    public List<Section> getPath(@RequestParam int sec) {
        return sectionDao.getPath(sec);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/add_section")
    public Result addSection(@RequestParam int parent, @RequestParam String label) {
        return sectionDao.addSection(parent, label);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/delete_section")
    public Result deleteSection(@RequestParam int id) {
        return sectionDao.deleteSection(id);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/get_ticket")
    public Ticket getTicket(@RequestParam int id) {
        return ticketDao.getTicket(id);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/update_ticket")
    public Result updateTicket(@RequestParam int id, @RequestParam String answers, @RequestParam String questions) {
        return ticketDao.updateTicket(id, answers, questions);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/add_ticket")
    public Section addTicket(@RequestParam int section) {
        return ticketDao.addTicket(section);
    }
}
