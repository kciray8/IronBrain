package org.ironbrain;

import org.ironbrain.core.User;

public class SessionData{
    public SessionData() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    private User user;

    public int getUserId(){
        return getUser().getId();
    }
}
