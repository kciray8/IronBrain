package org.ironbrain;

import org.ironbrain.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
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
        modelMap.addAttribute("ms", Long.toString(System.currentTimeMillis() - ms));
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
        return "addPage";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/exam")
    public String getExamPage(ModelMap modelMap) {
        List<Remind> reminds = getReminds();
        modelMap.addAttribute("reminds", reminds);
        modelMap.addAttribute("data", data);
        modelMap.addAttribute("ib", ib);

        Exam lastExam = getLastUndoneExam();
        if (lastExam != null) {
            Try tempTry = getTempTry(lastExam.getId());
            if (tempTry == null) {
                List<Try> tries = getTriesFromExam(lastExam.getId());
                Try lastTry = tries.get(tries.size() - 1);
                int tempAttempt = lastTry.getAttemptNum();
                int nextAttempt = lastTry.getAttemptNum() + 1;
                List<Try> newAttempt = new ArrayList<>();
                tries.forEach(someTry -> {
                    if (!someTry.getCorrect()){
                        //Try newTry =
                    }
                });
            }

            modelMap.addAttribute("exam", getLastUndoneExam());

            modelMap.addAttribute("tempTry", tempTry);
            Ticket ticket = getTicket(tempTry.getTicket());
            modelMap.addAttribute("ticket", ticket);
        }

        return "examPage";
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