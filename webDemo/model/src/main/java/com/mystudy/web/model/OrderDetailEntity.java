package com.mystudy.web.model;

import java.io.Serializable;

/**
 * Created by 程祥 on 16/1/12.
 * Function：
 */
public class OrderDetailEntity implements Serializable{

    private String name;
    private String nameColor;
    private String nameSize;
    private boolean nameBold;
    private String val;
    private String valColor;
    private String valSize;
    private boolean valBold;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameColor() {
        return nameColor;
    }

    public void setNameColor(String nameColor) {
        this.nameColor = nameColor;
    }

    public String getNameSize() {
        return nameSize;
    }

    public void setNameSize(String nameSize) {
        this.nameSize = nameSize;
    }

    public boolean isNameBold() {
        return nameBold;
    }

    public void setNameBold(boolean nameBold) {
        this.nameBold = nameBold;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public String getValColor() {
        return valColor;
    }

    public void setValColor(String valColor) {
        this.valColor = valColor;
    }

    public String getValSize() {
        return valSize;
    }

    public void setValSize(String valSize) {
        this.valSize = valSize;
    }

    public boolean isValBold() {
        return valBold;
    }

    public void setValBold(boolean valBold) {
        this.valBold = valBold;
    }
}
