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
        modelMap.addAttribute("parentSection", mainSection.getParent());
        modelMap.addAttribute("thisSection", mainSection.getId());

        modelMap.addAttribute("ms", Long.toString(System.currentTimeMillis() - ms));

        if (tic != null) {
            Ticket ticket = getTicket(tic);
            modelMap.addAttribute("ticket", ticket);
        }
        return "addPage";
    }
}