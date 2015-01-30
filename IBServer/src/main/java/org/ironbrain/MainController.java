package org.ironbrain;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.ironbrain.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Controller
public class MainController extends APIController {
    public static final String USER_URL = "user";

    @Autowired
    IB ib;

    @RequestMapping(method = RequestMethod.GET, value = "/add")
    public String getAddPage(ModelMap modelMap, Integer sec, Integer tic) {
        long ms = IB.getNowMs();
        if (data.getUser() == null) {
            return "redirect:/main";
        }
        if (sec == null) {
            sec = getUser().getRoot();
        }

        List<Section> sections = getSections(sec);
        List<Section> path = getPath(sec);
        List<Field> allUserFields = getFields();
        Section mainSection = getSection(sec);

        modelMap.addAttribute("sections", sections);
        modelMap.addAttribute("path", path);
        modelMap.addAttribute("section", mainSection);
        modelMap.addAttribute("data", data);
        modelMap.addAttribute("ib", ib);

        long pageGenDate = IB.getNowMs();
        modelMap.addAttribute("pageGenerateDate", pageGenDate);

        Section targetSection = mainSection;

        if (tic != null) {
            Ticket ticket = getTicket(tic);
            targetSection = getSectionFromTicket(ticket.getId());

            ticket.setEditDate(pageGenDate);
            updateTicketEditDate(ticket.getId(), pageGenDate);

            modelMap.addAttribute("ticket", ticket);

            path.add(targetSection);
            modelMap.addAttribute("ticketSection", targetSection);
        }

        //Add fields
        List<SectionToField> secToFlds = targetSection.getSectionToFields();

        List<Field> ticketFields = new ArrayList<>();
        secToFlds.forEach(secToFld -> {
            ticketFields.add(secToFld.field);
        });

        modelMap.addAttribute("secToFlds", secToFlds);

        List<Field> unusedFields = new LinkedList<>(allUserFields);
        unusedFields.removeAll(ticketFields);
        modelMap.addAttribute("unusedFields", unusedFields);

        modelMap.addAttribute("ms", Long.toString(IB.getNowMs() - ms));

        if (data.getBufferSectionId() != null) {
            modelMap.addAttribute("bufferSectionId", data.getBufferSectionId());
        }

        return "addPage";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/main")
    public String getMainPage(ModelMap modelMap) {
        modelMap.addAttribute("ib", ib);
        modelMap.addAttribute("data", data);

        return "mainPage";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/about")
    public String getAboutPage(ModelMap modelMap) {
        modelMap.addAttribute("data", data);
        modelMap.addAttribute("ib", ib);

        return "aboutPage";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/register")
    public String getRegisterPage(ModelMap modelMap) {
        modelMap.addAttribute("data", data);

        return "registerPage";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/direction")
    public String getDirectionPage(ModelMap modelMap, Integer id) {
        long ms = IB.getNowMs();
        List<Field> allUserFields = getFields();
        List<Direction> directions = directionDao.getDirections();
        modelMap.addAttribute("data", data);
        modelMap.addAttribute("directions", directions);

        if (id != null) {
            Direction direction = directionDao.getDirection(id);
            modelMap.addAttribute("direction", direction);

            List<DirectionToField> directionToFields = direction.getDirectionToFields();
            modelMap.addAttribute("directionToFields", directionToFields);

            List<Field> directionFields = new ArrayList<>();
            directionToFields.forEach(dirToFld -> {
                directionFields.add(dirToFld.getField());
            });

            List<Field> unusedFields = new LinkedList<>(allUserFields);
            unusedFields.removeAll(directionFields);
            modelMap.addAttribute("unusedFields", unusedFields);
        }

        modelMap.addAttribute("ms", Long.toString(IB.getNowMs() - ms));
        return "directionPage";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/guest_login")
    public String guestLogin() {
        String login = RandomStringUtils.randomAlphanumeric(6);
        String password = RandomStringUtils.randomAlphanumeric(RandomUtils.nextInt(5, 15));

        registerUser("guest_" + login,
                password, "guest@ironbrain.org", true);

        return "redirect:/main";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/edit_ticket")
    public String editTicket(@RequestParam Integer id) {
        Section section = getSectionFromTicket(id);
        return "redirect:/add?sec=" + section.getParent() + "&tic=" + id;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/exam{examID}")
    public String getExamResult(ModelMap modelMap, @PathVariable(value = "examID") int examID) {
        Exam exam = examDao.get(examID);
        modelMap.addAttribute("exam", exam);

        List<Try> tries = tryDao.getTriesFromExam(examID);
        modelMap.addAttribute("tries", tries);
        modelMap.addAttribute("data", data);

        List<Exam> exams = examDao.getDoneExams();
        modelMap.addAttribute("exams", exams);

        return "examResultPage";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/exam")
    public String getExamPage(ModelMap modelMap) {
        long ms = IB.getNowMs();
        List<Remind> reminds = getReminds();
        modelMap.addAttribute("reminds", reminds);
        modelMap.addAttribute("data", data);
        modelMap.addAttribute("ib", ib);

        List<Exam> exams = examDao.getDoneExams();
        modelMap.addAttribute("exams", exams);

        Exam lastExam = getLastUndoneExam();
        boolean weEndExam = reloadOrEndExamIfNeed(lastExam, modelMap);
        if (weEndExam) {
            return "redirect:/exam" + lastExam.getId();
        }

        modelMap.addAttribute("ms", Long.toString(IB.getNowMs() - ms));
        return "examPage";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/profile")
    public String getProfilePage(ModelMap modelMap) {
        modelMap.addAttribute("data", data);
        modelMap.addAttribute("user", data.getUser());
        modelMap.addAttribute("ib", ib);
        return "profilePage";
    }

    /**
     * @param modelMap optional
     * @return true - if we end exam
     */
    public boolean reloadOrEndExamIfNeed(Exam lastExam, ModelMap modelMap) {

        if (lastExam != null) {
            Try tempTry = getTempTry(lastExam.getId());
            if (tempTry == null) {
                toNextAttempt();
                lastExam = getLastUndoneExam();//Refresh exam data
                tempTry = getTempTry(lastExam.getId());

                //All tickets done!
                if (tempTry == null) {
                    lastExam.setDone(true);
                    lastExam.setEndMs(IB.getNowMs());
                    examDao.update(lastExam);
                    //directionDao.recalcluateAllDirections();
                    return true;
                }
            }

            Ticket ticket = getTicket(tempTry.getTicket());
            if (modelMap != null) {
                modelMap.addAttribute("ticket", ticket);
                modelMap.addAttribute("exam", lastExam);
                modelMap.addAttribute("tempTry", tempTry);
            }
        }
        return false;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/search")
    public String getSearch(ModelMap modelMap, String query) {
        modelMap.addAttribute("data", data);
        if (query != null) {
            modelMap.addAttribute("query", query);
        }

        return "searchPage";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public String getRoot() {
        if (getUser() != null) {
            return "redirect:/add?sec=" + getUser().getRoot();
        } else {
            return "redirect:/add?sec=1";
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/login_and_redirect")
    public String loginAndRedirect(@RequestParam String login, @RequestParam String password) {
        Result result = login(login, password);
        if (result.isOk()) {
            return "redirect:/add?sec=" + getUser().getRoot();
        } else {
            return "redirect:/";
        }
    }


    /**
     * Example - http://localhost:8080/user/kciray/files/commons/2015_01_13__19_39_10_375.jpg
     */
    @RequestMapping(value = "/user/{user_name}", method = RequestMethod.GET)
    @ResponseBody
    void getFile(@PathVariable String user_name, String path, HttpServletResponse response) {
        if (!data.getUser().getLogin().equals(user_name)) {
            throw new AccessDeniedException();
        }
        File file = new File(data.getHomeDir(), path);

        String contentType = null;
        try {
            contentType = Files.probeContentType(file.toPath());

            byte[] fileByteArray = FileUtils.readFileToByteArray(file);
            response.setContentType(contentType);
            response.getOutputStream().write(fileByteArray);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/documentation")
    public String getDocumentationPage(ModelMap modelMap) {
        modelMap.addAttribute("data", data);
        return "documentationPage";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/all_users")
    public String getAllUsers(ModelMap modelMap) {
        modelMap.addAttribute("data", data);
        if(!data.getUser().getAdmin()){
            throw new AccessDeniedException();
        }

        List<User> users = userDao.getUsers();
        Collections.reverse(users);
        modelMap.addAttribute("users", users);

        return "allUsersPage";
    }


    @RequestMapping(method = RequestMethod.GET, value = "/all_tickets")
    public String getAllTickets(ModelMap modelMap) {
        modelMap.addAttribute("data", data);
        if(!data.getUser().getAdmin()){
            throw new AccessDeniedException();
        }

        List<Ticket> tickets = ticketDao.getAllTicketsFromEnd(20);
        modelMap.addAttribute("tickets", tickets);

        return "allTicketsPage";
    }

}