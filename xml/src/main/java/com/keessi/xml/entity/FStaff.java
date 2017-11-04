package com.keessi.xml.entity;

import java.io.Serializable;

public class FStaff implements Serializable {
    private Integer id;
    private String username;
    private String password;
    private String name;
    private String phone;
    private Integer level;
    private Integer salary;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getSalary() {
        return salary;
    }

    public void setSalary(Integer salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "FStaff " +
                "{" +
                "id=" + id +
                ",username=" + username +
                ",name=" + name +
                ",phone=" + phone +
                ",level=" + level +
                ",salary=" + salary +
                "}";
    }
}
