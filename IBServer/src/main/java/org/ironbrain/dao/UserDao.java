package org.ironbrain.dao;

import org.hibernate.criterion.Restrictions;
import org.ironbrain.core.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@SuppressWarnings("unchecked")
@Transactional
public class UserDao extends BaseDao {
    public User getUser(int id) {
        User user = (User) getSess().get(User.class, id);
        return user;
    }

    public User getUserByLogin(String login) {
        User user = (User) getSess().createCriteria(User.class)
                .add(Restrictions.eq("login", login)).uniqueResult();
        return user;
    }
}
