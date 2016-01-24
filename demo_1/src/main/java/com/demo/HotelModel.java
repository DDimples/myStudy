package com.demo;

/**
 * Created by 程祥 on 16/1/23.
 * Function：
 */
public class HotelModel implements Comparable<HotelModel>{

    private String name;
    private String pinyin;
    private int num;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int compareTo(HotelModel o) {
        return o.getNum()-num;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("HotelModel{");
        sb.append("name='").append(name).append('\'');
        sb.append(", pinyin='").append(pinyin).append('\'');
        sb.append(", num=").append(num);
        sb.append('}');
        return sb.toString();
    }
}
