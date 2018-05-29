package com.example.models;

import java.io.Serializable;

public class UserDao implements Serializable {

    private static final long serialVersionUID = 123L;

    private String name;
    private String dep;
    private transient Long salary; //don't serialize the salary field

    @Override
    public String toString() {
        return "UserDao{" +
                "name='" + name + '\'' +
                ", dep='" + dep + '\'' +
                ", salary=" + salary +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getDep() {
        return dep;
    }

    public void setDep(final String dep) {
        this.dep = dep;
    }

    public Long getSalary() {
        return salary;
    }

    public void setSalary(final Long salary) {
        this.salary = salary;
    }
}
