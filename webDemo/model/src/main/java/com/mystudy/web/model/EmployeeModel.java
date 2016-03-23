package com.mystudy.web.model;

import java.io.Serializable;

/**
 * Created by 程祥 on 16/3/23.
 * Function：
 */
public class EmployeeModel implements Serializable{

    public EmployeeModel() {
    }

    public EmployeeModel(String name, String position, String salary, String start_date, String office, String extn) {
        this.name = name;
        this.position = position;
        this.salary = salary;
        this.start_date = start_date;
        this.office = office;
        this.extn = extn;
    }

    private String name;
    private String position;
    private String salary;
    private String start_date;
    private String office;
    private String extn;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public String getExtn() {
        return extn;
    }

    public void setExtn(String extn) {
        this.extn = extn;
    }
}
