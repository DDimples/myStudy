package com.mystudy.web.model;

import java.io.Serializable;

/**
 * Created by 程祥 on 16/3/23.
 * Function：
 */
public class EmployeeModel implements Serializable,Comparable<EmployeeModel>{



    public EmployeeModel() {
    }

    public EmployeeModel(String id,String name, String position, String salary, String start_date, String office, String extn) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.salary = salary;
        this.start_date = start_date;
        this.office = office;
        this.extn = extn;
    }

//    @NoT
    public String id;
    public String name;
    public String position;
    public String salary;
    public String start_date;
    public String office;
    public String extn;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public int compareTo(EmployeeModel o) {
        return id.compareTo(o.id);
    }

    public void copyValue(EmployeeModel model){
        this.name = model.name;
        this.salary = model.salary;
        this.extn = model.extn;
        this.office = model.office;
        this.start_date = model.start_date;
        this.position = model.position;
    }
}
