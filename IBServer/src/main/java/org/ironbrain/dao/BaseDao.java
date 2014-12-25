package org.ironbrain.dao;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.ironbrain.SessionData;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseDao {
    @Autowired
    protected SessionData data;

    @Autowired
    protected SessionFactory sessionFactory;

    protected Session getSess(){
        return sessionFactory.getCurrentSession();
    }
}
