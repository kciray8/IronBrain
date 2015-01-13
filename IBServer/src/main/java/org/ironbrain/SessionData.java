package org.ironbrain;

import org.ironbrain.core.User;

import java.io.File;

public class SessionData {
    public SessionData() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    private User user;

    public Integer getUserId() {
        return getUser().getId();
    }

    public boolean testOwner(int owner) {
        return getUserId() == owner;
    }

    public Integer getBufferSectionId() {
        return bufferSectionId;
    }

    public void setBufferSectionId(Integer bufferSectionId) {
        this.bufferSectionId = bufferSectionId;
    }

    private Integer bufferSectionId;

    public File getFilesDir() {
        String rootPath = System.getProperty("catalina.home");
        return new File(rootPath, "files");
    }

    public File getHomeDir() {
        File userHomeDir = new File(getFilesDir(), user.getLogin());

        if (!userHomeDir.exists()) {
            userHomeDir.mkdirs();
        }
        return userHomeDir;
    }
}
