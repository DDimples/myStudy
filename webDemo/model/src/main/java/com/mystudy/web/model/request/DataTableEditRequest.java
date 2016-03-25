package com.mystudy.web.model.request;

import java.util.List;

/**
 * Created by 程祥 on 16/3/25.
 * Function：
 */
public class DataTableEditRequest<T> {

    private String action;
    private List<T> dataList;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }
}
