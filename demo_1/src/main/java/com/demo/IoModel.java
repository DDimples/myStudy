package com.demo;

import java.util.Objects;

/**
 * Created by 程祥 on 16/1/23.
 * Function：
 */
public class IoModel {
    private String ip;
    private int times;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IoModel model = (IoModel) o;
        return Objects.equals(times, model.times) &&
                Objects.equals(ip, model.ip);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ip, times);
    }
}
