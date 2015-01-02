package com.springapp.mvc;

import com.google.common.base.Joiner;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.ironbrain.IB;
import org.ironbrain.MainController;
import org.ironbrain.Result;
import org.ironbrain.SessionData;
import org.ironbrain.core.*;
import org.ironbrain.dao.AllDao;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("file:src/main/webapp/WEB-INF/mvc-dispatcher-servlet.xml")
public class AppTests extends AllDao {
    private MockMvc mockMvc;

    @Autowired
    protected SessionData data;

    @Autowired
    MainController api;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    protected WebApplicationContext wac;

    @Before
    public void setup() {
        this.mockMvc = webAppContextSetup(this.wac).build();
    }

    @Test
    public void base() throws Exception {
        mockMvc.perform(get("/main"))
                .andExpect(status().isOk())
                .andExpect(view().name("mainPage"));
    }

    User user;
    Section rootSection;

    @Test
    public void integration() throws Exception {
        IB.setMsOffset(0);

        String login = RandomStringUtils.randomAlphanumeric(RandomUtils.nextInt(5, 15));
        String password = RandomStringUtils.randomAlphanumeric(RandomUtils.nextInt(5, 15));

        Result register = userDao.registerUser(login, password, "name@domain.com", true);
        log(register.getMessage());
        Assert.assertEquals(Result.State.OK, register.getRes());
        log(String.format("autologin - http://localhost:8080/login_and_redirect?login=%s&password=%s", login, password));

        user = data.getUser();
        Assert.assertEquals(user.getLogin(), login);

        rootSection = sectionDao.getSection(user.getRoot());
        Section timeSection = sectionDao.getTimeSection();
        Assert.assertEquals(rootSection.getId(), timeSection.getParent());
        Assert.assertEquals(user.getId(), rootSection.getOwner());

        createTicketsAndDayExam();
        createDirectionAndFullExam();
    }

    /**
     * 1)Create section "IT" in root
     * 2)Create section "Java" in IT and set field "Java for it"
     * 3)Add ticket "java questions N" - "java answers N" to "Java" section
     * 4)Add ticket "java extra questions" - "java extra answers" to Time and set fields "Java" and "HTML"
     * 5)Delete tickets #1 and tickets #5 from list to remind
     * 6)Start exam with 9 tickets
     * 7)Attempt #1 - correct answer only on first and last try
     * 8)Attempt #2 - correct answer - even numbers AND last
     * 9)Attempt #3 - only last try incorrect
     * 10)Attempt #4 - all correct
     */
    Section javaSection;
    Field htmlField;
    Field javaField;
    Section itSection;

    private void createTicketsAndDayExam() {
        List<Section> rootChildren = sectionDao.getChildren(user.getRoot());
        Section groupSection = rootChildren.get(0);

        itSection = sectionDao.addSection(groupSection.getId(), "IT").getData();
        javaSection = sectionDao.addSection(itSection.getId(), "Java").getData();

        javaField = fieldDao.getField(fieldDao.addField("Java").getData());
        Assert.assertEquals("Java", javaField.getLabel());
        SectionToField fieldToSection = fieldDao.addFieldToSection(javaField.getId(), javaSection.getId());

        int firstTicket = -1;
        int fiveTicket = -1;
        for (int i = 1; i < 11; i++) {
            Ticket ticketN = addTicketToSection(javaSection, "java questions " + i, "java answers " + i);
            if (i == 1) {
                firstTicket = ticketN.getId();
            }
            if (i == 5) {
                fiveTicket = ticketN.getId();
            }
        }

        Ticket fiveTicketObj = ticketDao.getTicket(fiveTicket);
        Assert.assertTrue(fiveTicketObj.getPath().contains("Java"));
        Assert.assertTrue(fiveTicketObj.getPath().contains("IT"));

        List<Remind> reminds = remindDao.getReminds();
        Assert.assertEquals(10, reminds.size());//All 10 tickets was added to remind list

        Pair<Section, Ticket> extraTicketData = api.addTicketToTime();
        Ticket extraTicket = extraTicketData.getRight();
        extraTicket.setQuestions("java extra questions");
        extraTicket.setAnswers("java extra answers");
        ticketDao.updateTicket(extraTicket);

        htmlField = fieldDao.getField(fieldDao.addField("Html").getData());
        fieldDao.addFieldToSection(htmlField.getId(), extraTicketData.getLeft().getId());
        fieldDao.addFieldToSection(javaField.getId(), extraTicketData.getLeft().getId());

        //Delete 1 and 5 ticket from remind list
        remindDao.deleteWithTicketId(firstTicket);
        remindDao.deleteWithTicketId(fiveTicket);

        //Now we have 9 ticket to remind
        reminds = remindDao.getReminds();
        Assert.assertEquals(9, reminds.size());

        //We have remind list with 2,3,4,6,7,8,9,10 and extra tickets
        //Now user checket all remind

        List<Integer> ids = new ArrayList<>();
        reminds.forEach(rem -> {
            ids.add(rem.getId());
        });

        String idsStr = Joiner.on(",").join(ids);
        api.remind(idsStr);

        //Exam started...
        //Attempt #1 - tickets 2,3,4,6,7,8,9,10 and extra tickets  - all 9
        Exam exam = examDao.getLastUndoneExam();
        Assert.assertEquals(9, exam.getCount().intValue());
        Assert.assertEquals(false, exam.getDone());
        Assert.assertEquals(data.getUserId(), exam.getUser().intValue());

        Try firstTry = tryDao.getTempTry(exam.getId());
        Assert.assertEquals(1, firstTry.getNum().intValue());
        Assert.assertEquals(1, firstTry.getAttemptNum().intValue());

        String firstTryAnswers = ticketDao.getTicket(firstTry.getTicket()).getAnswers();
        Assert.assertEquals("java answers 2", firstTryAnswers);

        //Tries
        api.tryDone(firstTry.getId(), true, Ticket.REMIND_LATER);//#1
        doneTempTrue(exam, false);//#2
        doneTempTrue(exam, false);//#3
        doneTempTrue(exam, false);//#4
        doneTempTrue(exam, false);//#5
        doneTempTrue(exam, false);//#6
        doneTempTrue(exam, false);//#7
        doneTempTrue(exam, false);//#8
        doneTempTrue(exam, true);//#9

        api.reloadOrEndExamIfNeed(examDao.getLastUndoneExam(), null);

        //Attempt #2 - tickets 3,4,6,7,8,9,10
        //Answer true only even num AND last (4,7,9,10):
        reminds = remindDao.getReminds();
        Assert.assertEquals(7, reminds.size());

        doneTempTrue(exam, false);//#1
        doneTempTrue(exam, true);//#2
        doneTempTrue(exam, false);//#3
        doneTempTrue(exam, true);//#4
        doneTempTrue(exam, false);//#5
        doneTempTrue(exam, true);//#6
        doneTempTrue(exam, true);//#7

        //Attempt #3 - tickets 3,6,8
        reminds = remindDao.getReminds();
        Assert.assertEquals(3, reminds.size());
        Assert.assertEquals("java answers 3", ticketDao.getTicket(reminds.get(0).getTicket()).getAnswers());
        Assert.assertEquals("java answers 6", ticketDao.getTicket(reminds.get(1).getTicket()).getAnswers());
        Assert.assertEquals("java answers 8", ticketDao.getTicket(reminds.get(2).getTicket()).getAnswers());

        api.reloadOrEndExamIfNeed(examDao.getLastUndoneExam(), null);
        doneTempTrue(exam, true);//#1
        doneTempTrue(exam, true);//#2
        doneTempTrue(exam, false);//#3

        //Attempt #4 - ticket 8
        api.reloadOrEndExamIfNeed(examDao.getLastUndoneExam(), null);
        doneTempTrue(exam, true);//#1

        boolean doneOk = api.reloadOrEndExamIfNeed(examDao.getLastUndoneExam(), null);
        Assert.assertTrue(doneOk);
    }

    private Ticket addTicketToSection(Section section, String questions, String answers) {
        Ticket ticketN = ticketDao.addTicket(section.getId()).getRight();
        ticketN.setQuestions(questions);
        ticketN.setAnswers(answers);
        ticketDao.updateTicket(ticketN);
        return ticketN;
    }

    /**
     * 1)Add new ticket to "Java" section "special java questions" - "special java answers"
     * 2)Add field "Java" to special ticket and inverse it
     * 3)Add direction "Java and Html"
     * 4)Create section "Html" with subsections "1" and "2"
     * 5)In it - create 5 tickets about html
     * 6)Update direction percent and ensure that it 0%
     */
    private void createDirectionAndFullExam() {
        Pair<Section, Ticket> ticketData = ticketDao.addTicket(javaSection.getId());
        Ticket specialTicket = ticketData.getRight();
        specialTicket.setQuestions("special java questions");
        specialTicket.setAnswers("special java answers");
        ticketDao.updateTicket(specialTicket);

        SectionToField sectionToField = fieldDao.addFieldToSection(javaField.getId(), ticketData.getKey().getId());
        secToFDao.invertField(sectionToField.getId());

        Direction direction = directionDao.addDirection("Java and Html");
        fieldDao.addFieldToDirection(javaField.getId(), direction.getId());
        fieldDao.addFieldToDirection(htmlField.getId(), direction.getId());

        Section htmlSection = sectionDao.addSection(itSection.getId(), "Html").getData();
        fieldDao.addFieldToSection(htmlField.getId(), htmlSection.getId());

        Section html1 = sectionDao.addSection(htmlSection.getId(), "1").getData();
        addTicketToSection(html1, "questions html.1", "answers html.1");
        addTicketToSection(html1, "questions html.2", "answers html.2");

        Section html2 = sectionDao.addSection(htmlSection.getId(), "2").getData();
        addTicketToSection(html2, "questions htm2.1", "answers htm2.1");
        addTicketToSection(html2, "questions htm2.2", "answers htm2.2");
        addTicketToSection(html2, "questions htm2.3", "answers htm2.3");

        //Update direction data
        directionDao.recalculateDirection(direction.getId());
        direction = directionDao.getDirection(direction.getId());
        Assert.assertEquals(16, direction.getTicketsCount().intValue());

        //Now we have 11 tickets in direction:
        //ticket 1-10 AND extra ticket
        double knowPercent = directionDao.getKnowPercent(direction);
        Assert.assertEquals(knowPercent, 0, 0.001);

        directionDao.sliceAndAddToRemind(direction, 6);
    }

    private void doneTempTrue(Exam exam, boolean result) {
        Try tempTry = tryDao.getTempTry(exam.getId());
        if (result) {
            api.tryDone(tempTry.getId(), result, Ticket.REMIND_LATER);
        } else {
            api.tryDone(tempTry.getId(), result, Ticket.REMIND_NOW);
        }
    }

    private void log(Object obj) {
        if (obj != null) {
            System.out.println(obj);
        } else {
            System.out.println("NULL");
        }
    }
}
