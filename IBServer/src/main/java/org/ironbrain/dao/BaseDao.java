package org.ironbrain.dao;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseDao {
    @Autowired
    protected SessionFactory sessionFactory;

    protected Session getSess(){
        return sessionFactory.getCurrentSession();
    }
}
