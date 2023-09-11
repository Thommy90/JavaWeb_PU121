package step.learning.services.db;

import java.util.UUID;

public class User {
    private UUID id;
    private String login;
    private String pass;
    private String name;
    private int age;
    private char state;
    private String country;
    private String phone;
    private String email;
    private String avaUrl;

/*
CREATE TABLE IF NOT EXISTS users (`id` CHAR(36) PRIMARY KEY, `login` VARCHAR(256)," `pass` VARCHAR(256),
                                   `name` VARCHAR(256), `age` INT, `state` CHAR(1),
                                    `country` VARCHAR(256),`phone` VARCHAR(256),`email` VARCHAR(256),
                                    `ava_url` VARCHAR(256),)
 */

    public User(UUID id, int age, String login, String pass, String name, char state, String country, String phone, String email, String avaUrl) {
        this.id = id;
        this.age = age;
        this.login = login;
        this.pass = pass;
        this.name = name;
        this.state = state;
        this.country = country;
        this.phone = phone;
        this.email = email;
        this.avaUrl = avaUrl;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public char getState() {
        return state;
    }

    public void setState(char state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvaUrl() {
        return avaUrl;
    }

    public void setAvaUrl(String avaUrl) {
        this.avaUrl = avaUrl;
    }
}
