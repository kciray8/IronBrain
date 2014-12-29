package org.ironbrain;

import com.google.common.base.Joiner;
import org.ironbrain.core.*;
import org.ironbrain.dao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SessionAttributes({APIController.USER})
public class APIController {
    public static final String USER = "user";

    @Autowired
    protected SessionData data;

    @Autowired
    protected SectionDao sectionDao;

    @Autowired
    protected FieldDao fieldDao;

    @Autowired
    protected TicketDao ticketDao;

    @Autowired
    protected UserDao userDao;

    @Autowired
    protected RemindDao remindDao;

    @Autowired
    protected ExamDao examDao;

    @Autowired
    protected TryDao tryDao;

    @Autowired
    protected SectionToFieldDao secToFDao;

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
    @RequestMapping(method = RequestMethod.GET, value = "/get_ticket_label")
    public String getTicketLabel(@RequestParam int id) {
        return ticketDao.getTicketLabel(id);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/update_ticket")
    public Result updateTicket(@RequestParam int id, @RequestParam String answers,
                               @RequestParam String questions, @RequestParam String label,
                               @RequestParam long clientVersionDate) {

        return ticketDao.updateTicket(id, answers, questions, label, clientVersionDate);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/check_ticket")
    public Result checkTicket(@RequestParam int id, @RequestParam long clientVersionDate) {
        return ticketDao.checkTicket(id, clientVersionDate);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/update_ticket_edit_date")
    public void updateTicketEditDate(@RequestParam int id, @RequestParam long editDate) {

        ticketDao.updateTicket(id, editDate);
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
        return "redirect:/.";
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/add_remind")
    public String addRemind(Integer section, Integer ticket) {
        if (section != null) {
            remindDao.addRemind(section, getUser().getId());
        }
        if (ticket != null) {
            remindDao.addRemindTicket(ticket);
        }

        return "OK";
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/get_reminds")
    public List<Remind> getReminds() {
        return remindDao.getReminds(getUser());
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/delete_remind")
    public Result deleteRemind(@RequestParam int id) {
        return remindDao.delete(id);
    }


    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/remind")
    public Result remind(@RequestParam String ids) {
        List<Remind> reminds = new ArrayList<>();

        String[] idArray = ids.split(",");
        for (String idStr : idArray) {
            int id = new Integer(idStr);
            reminds.add(remindDao.get(id));
        }

        Exam exam = examDao.create(reminds.size());

        i = 1;
        reminds.forEach(remind -> {
            Try someTry = tryDao.create(remind.getTicket(), exam.getId(), i++, 0);
        });

        return Result.getOk();
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/get_last_undone_exam")
    public Exam getLastUndoneExam() {
        return examDao.getLastUndoneExam();
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/get_tries_from_exam")
    public List<Try> getTriesFromExam(int id) {
        return tryDao.getTriesFromExam(id);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/get_temp_try")
    public Try getTempTry(int id) {
        return tryDao.getTempTry(id);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/try_done")
    public Result tryDone(@RequestParam int id, @RequestParam boolean correct, Boolean examDone) {
        Try someTry = tryDao.getTry(id);
        someTry.setEndMs(System.currentTimeMillis());

        if (correct) {
            someTry.setCorrect(true);
        }
        someTry.setDone(true);
        tryDao.updateTry(someTry);

        return Result.getOk();
    }

    int i;

    @RequestMapping(method = RequestMethod.GET, value = "/to_next_attempt")
    public void toNextAttempt() {
        Exam lastExam = getLastUndoneExam();
        List<Try> tries = getTriesFromExam(lastExam.getId());
        Try lastTry = tries.get(tries.size() - 1);

        i = 0;
        int lastAttemptNum = lastTry.getAttemptNum();
        tries.forEach(someTry -> {
            if ((!someTry.getCorrect()) && (someTry.getAttemptNum() == lastAttemptNum)) {
                Try newTry = tryDao.createNextAttempt(someTry, ++i);
            }
        });

        lastExam.setCount(i);
        examDao.update(lastExam);
    }


    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/query")
    public List<Ticket> query(@RequestParam String query) {
        return ticketDao.query(query);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/update_all_paths")
    public String updateAllPath() {
        List<Ticket> tickets = ticketDao.getAllTickets();
        StringBuilder res = new StringBuilder();

        tickets.forEach(ticket -> {
            Section section = sectionDao.getSectionFromTicket(ticket.getId());
            if (section != null) {
                List<String> path = getPath(section.getId())
                        .stream()
                        .map(Section::getLabel)
                        .collect(Collectors.toList());
                String pathString = Joiner.on("→").join(path);

                res.append(pathString);
                ticket.setPath(pathString);
                ticketDao.updateTicket(ticket);
            }
        });

        return res.toString();
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/add_field")
    public Result addField(@RequestParam String name) {
        return fieldDao.addField(name);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/get_fields")
    public List<Field> getFields() {
        return fieldDao.getFields();
    }


    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/add_field_to_section")
    public Result addFieldToSection(@RequestParam Integer fieldId, @RequestParam Integer sectionId) {
        return fieldDao.addFieldToSection(fieldId, sectionId);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/invert_field")
    public Result invertField(@RequestParam Integer fieldToSecId) {
        return secToFDao.invertField(fieldToSecId);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/ del_secToField")
    public Result deleteSectionToField(@RequestParam Integer sectionToFieldId) {
        return secToFDao.deleteSectionToField(sectionToFieldId);
    }
}
