package org.ironbrain.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.ironbrain.utils.DateUtils;

import javax.persistence.*;

@Table(name = "Users")
@Entity
public class User {
    public static final String FILES_DIR = "files";
    public static final String COMMONS_DIR = "commons";

    @Id
    @GeneratedValue
    private Integer id;

    private String login;

    private String password;

    private Integer root;

    public Boolean getExtended() {
        return extended;
    }

    public void setExtended(Boolean extended) {
        this.extended = extended;
    }

    private Boolean extended;

    public User(){

    }
    public User(Integer id){
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String email;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getRoot() {
        return root;
    }

    public void setRoot(Integer root) {
        this.root = root;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    private String port = "9993";

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    private Boolean admin = false;

    public long getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(long registerDate) {
        this.registerDate = registerDate;
    }

    private long registerDate = System.currentTimeMillis();

    @Transient
    public String getRegisterDateStr(){
        return DateUtils.getNiceDate(registerDate);
    }
}
