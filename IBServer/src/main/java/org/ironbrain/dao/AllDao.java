package org.ironbrain.dao;

import org.springframework.beans.factory.annotation.Autowired;

public class AllDao {
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
    protected DirectionDao directionDao;

    @Autowired
    protected SectionToFieldDao secToFDao;

    @Autowired
    protected DirectionToFieldDao dirToFDao;
}
