package org.ironbrain;

import org.ironbrain.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class MainController extends APIController {
    @Autowired
    IronBrain ib;

    @RequestMapping(method = RequestMethod.GET, value = "/add")
    public String getAddPage(ModelMap modelMap, @RequestParam int sec, Integer tic) {
        long ms = System.currentTimeMillis();
        List<Section> sections = getSections(sec);
        List<Section> path = getPath(sec);
        Section mainSection = getSection(sec);

        modelMap.addAttribute("sections", sections);
        modelMap.addAttribute("path", path);
        modelMap.addAttribute("section", mainSection);
        modelMap.addAttribute("data", data);
        modelMap.addAttribute("ib", ib);

        long pageGenDate = System.currentTimeMillis();
        modelMap.addAttribute("pageGenerateDate", pageGenDate);

        if (tic != null) {
            Ticket ticket = getTicket(tic);
            ticket.setEditDate(pageGenDate);
            updateTicketEditDate(ticket.getId(), pageGenDate);

            modelMap.addAttribute("ticket", ticket);
            Section ticketSection = getSectionFromTicket(ticket.getId());
            modelMap.addAttribute("ticketSection", ticketSection);
        }

        modelMap.addAttribute("ms", Long.toString(System.currentTimeMillis() - ms));
        return "addPage";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/exam")
    public String getExamPage(ModelMap modelMap) {
        long ms = System.currentTimeMillis();
        List<Remind> reminds = getReminds();
        modelMap.addAttribute("reminds", reminds);
        modelMap.addAttribute("data", data);
        modelMap.addAttribute("ib", ib);

        Exam lastExam = getLastUndoneExam();

        if (lastExam != null) {
            Try tempTry = getTempTry(lastExam.getId());
            if (tempTry == null) {
                toNextAttempt();
                tempTry = getTempTry(lastExam.getId());

                //All tickets done!
                if (tempTry == null) {
                    lastExam.setDone(true);
                    lastExam.setEndMs(System.currentTimeMillis());
                    examDao.update(lastExam);
                    return "redirect:/exam" + lastExam.getId();
                }
            }

            modelMap.addAttribute("exam", lastExam);
            modelMap.addAttribute("tempTry", tempTry);
            Ticket ticket = getTicket(tempTry.getTicket());
            modelMap.addAttribute("ticket", ticket);
        }

        modelMap.addAttribute("ms", Long.toString(System.currentTimeMillis() - ms));
        return "examPage";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/search")
    public String getSearch(ModelMap modelMap, String query) {
        modelMap.addAttribute("data", data);
        if(query != null){
            modelMap.addAttribute("query", query);
        }

        return "searchPage";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/exam{examID}")
    public String getExamResult(ModelMap modelMap, @PathVariable(value="examID") int examID) {
        Exam exam = examDao.get(examID);
        modelMap.addAttribute("exam", exam);

        List<Try> tries = tryDao.getTriesFromExam(examID);
        modelMap.addAttribute("tries", tries);

        return "examResultPage";
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
            return null;
        }
    }
}