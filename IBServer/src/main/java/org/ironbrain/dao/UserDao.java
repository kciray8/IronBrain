package org.ironbrain.dao;

import org.apache.commons.validator.routines.EmailValidator;
import org.hibernate.criterion.Restrictions;
import org.ironbrain.Result;
import org.ironbrain.core.Section;
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

    public User getUser(){
        return data.getUser();
    }

    public User getUserByLogin(String login) {
        User user = (User) getSess().createCriteria(User.class)
                .add(Restrictions.eq("login", login)).uniqueResult();
        return user;
    }

    public Result registerUser(String login, String password, String email, Boolean extended) {
        User existUser = getUserByLogin(login);

        if (login.length() < 4) {
            return Result.getError("Логин слишком короткий!");
        }

        if (existUser != null) {
            return Result.getError("Пользователь с таким логином уже существует!");
        }

        if (password.length() < 4) {
            return Result.getError("Пароль слишком короткий!");
        }

        if (!EmailValidator.getInstance().isValid(email)) {
            return Result.getError("Не корректный почтовый ящик!");
        }

        User newUser = new User();
        newUser.setLogin(login);
        newUser.setPassword(password);
        newUser.setEmail(email);
        newUser.setExtended(extended);

        int id = (int) getSess().save(newUser);
        newUser.setId(id);

        createUserData(newUser);
        getSess().update(newUser);

        data.setUser(newUser);
        return Result.getOk();
    }

    private void createUserData(User newUser) {
        Section rootSection = new Section();
        rootSection.setOwner(newUser.getId());
        rootSection.setLabel("Билеты");
        int rootSectionId = (int) getSess().save(rootSection);
        newUser.setRoot(rootSectionId);

        Section groupSection = new Section();
        groupSection.setParent(rootSectionId);
        groupSection.setOwner(newUser.getId());
        groupSection.setLabel("Группы");
        getSess().save(groupSection);

        Section timeSection = new Section();
        timeSection.setParent(rootSectionId);
        timeSection.setOwner(newUser.getId());
        timeSection.setLabel("Время");
        timeSection.setType(Section.Type.TIME.ordinal());
        getSess().save(timeSection);
    }

    public Result updateProfile(String newPassword, String newPasswordConfirm,
                                Boolean extendedProfile, String port, String email) {
        if(!newPassword.equals("")){
            if(newPassword.equals(newPasswordConfirm)){
                getUser().setPassword(newPassword);
            }else{
                return Result.getError("Пароли не совпадают!");
            }
        }
        getUser().setExtended(extendedProfile);
        getUser().setPort(port);
        getUser().setEmail(email);

        getSess().update(data.getUser());

        return Result.getOk();
    }
}
