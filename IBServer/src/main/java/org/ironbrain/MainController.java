package org.ironbrain;

import org.ironbrain.core.Section;
import org.ironbrain.core.Ticket;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class MainController extends APIController {
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
        modelMap.addAttribute("ms", Long.toString(System.currentTimeMillis() - ms));

        /*
        modelMap.addAttribute("debug_data", " "+getUser().getId()+ " "+
                getUser().getLogin()  + " "+getUser().getEmail() );*/

        if (tic != null) {
            Ticket ticket = getTicket(tic);
            modelMap.addAttribute("ticket", ticket);
            Section ticketSection = getSectionFromTicket(ticket.getId());
            modelMap.addAttribute("ticketSection", ticketSection);
        }
        return "addPage";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public String getRoot() {
        if(getUser() != null) {
            return "redirect:/add?sec=" + getUser().getRoot();
        }else {
            return "redirect:/add?sec=1";
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/login_and_redirect")
    public String loginAndRedirect(@RequestParam String login, @RequestParam String password) {
        Result result = login(login, password);
        if(result.isOk()) {
            return "redirect:/add?sec=" + getUser().getRoot();
        }else {
            return null;
        }
    }
}