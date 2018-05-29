package com.example.models;

import javax.persistence.*;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private byte[] userDao;

    public User() {
    }

    public User(final byte[] userDao) {
        this.userDao = userDao;
    }

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public byte[] getUserDao() {
        return userDao;
    }

    public void setUserDao(final byte[] userDao) {
        this.userDao = userDao;
    }
}
