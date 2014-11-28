package org.ironbrain;

import org.ironbrain.core.Section;
import org.ironbrain.core.Ticket;
import org.ironbrain.core.User;
import org.ironbrain.dao.RemindDao;
import org.ironbrain.dao.SectionDao;
import org.ironbrain.dao.TicketDao;
import org.ironbrain.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SessionAttributes({APIController.USER})
public class APIController {
    public static final String USER = "user";

    @Autowired
    protected SessionData data;

    @Autowired
    private SectionDao sectionDao;

    @Autowired
    private TicketDao ticketDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private RemindDao remindDao;

    protected User getUser() {
        return data.getUser();
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/get_sections")
    public List<Section> getSections(@RequestParam int sec) {
        return sectionDao.getSections(sec, getUser());
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/get_section")
    public Section getSection(@RequestParam int sec) {
        return sectionDao.getSection(sec, getUser());
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/get_section_from_ticket")
    public Section getSectionFromTicket(@RequestParam int ticketId) {
        return sectionDao.getSectionFromTicket(ticketId);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/get_path")
    public List<Section> getPath(@RequestParam int sec) {
        return sectionDao.getPath(sec);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/add_section")
    public Result addSection(@RequestParam String label, Integer parent) {
        return sectionDao.addSection(parent, label, getUser());
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
    public Result updateTicket(@RequestParam int id, @RequestParam String answers,
                               @RequestParam String questions, @RequestParam String label) {

        return ticketDao.updateTicket(id, answers, questions, label);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/add_ticket")
    public Section addTicket(@RequestParam int section) {
        return ticketDao.addTicket(section, getUser());
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/get_child_count")
    public Long getChildCount(@RequestParam int section) {
        return sectionDao.getChildCount(section);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/login")
    @ResponseBody
    public Result login(@RequestParam String login, @RequestParam String password) {
        User user = userDao.getUserByLogin(login);
        if (user == null) {
            return Result.getError("Нет такого пользователя!");
        }
        if (user.getPassword().equals(password)) {
            data.setUser(user);
            return Result.getOk();
        } else {
            return Result.getError("Неверный пароль!");
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/logout")
    public String logout() {
        data.setUser(null);
        return "redirect:/add?sec=1";
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/add_remind")
    public String addRemind(@RequestParam int section) {
        remindDao.addRemind(section, getUser().getId());

        return "OK";
    }
}
