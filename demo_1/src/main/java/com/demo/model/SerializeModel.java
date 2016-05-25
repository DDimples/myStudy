package com.demo.model;

import java.io.ObjectStreamField;
import java.io.Serializable;

/**
 * Created by 程祥 on 16/4/22.
 * Function：序列化demo model
 */
public class SerializeModel implements Serializable {

    private static final ObjectStreamField[] serialPersistentFields = {
            new ObjectStreamField("name", String.class), new ObjectStreamField("address", String.class)
    };

    private transient String tempField;

    private String unTempField;

    private String name;
    private String address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTempField() {
        return tempField;
    }

    public void setTempField(String tempField) {
        this.tempField = tempField;
    }

    public String getUnTempField() {
        return unTempField;
    }

    public void setUnTempField(String unTempField) {
        this.unTempField = unTempField;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SerializeModel{");
        sb.append("tempField='").append(tempField).append('\'');
        sb.append(", unTempField='").append(unTempField).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", address='").append(address).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
