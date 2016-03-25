package com.mystudy.web.model.request;

/**
 * Created by 程祥 on 16/3/23.
 * Function：
 */
public class DataTableRequest {

    public static String DRAW="draw";
    public static String START="start";
    public static String LENGTH="length";
    public static String SEARCH_VALUE="search[value]";
    public static String SEARCH_REGEX="search[regex]";
    public static String ORDER_COLUMN="order[0][column]";
    public static String ORDER_DIR="order[0][column]";
    public static String COLUMNS_0_DATA="columns[0][data]";
    public static String COLUMNS_0_NAME="columns[0][name]";
    public static String COLUMNS_0_SEARCHABLE="columns[0][searchable]";
    public static String COLUMNS_0_ORDERABLE="columns[0][orderable]";
    public static String COLUMNS_0_SEARCH_VALUE="columns[0][search][value]";
    public static String COLUMNS_0_SEARCH_REGEX="columns[0][search][regex]";



    private Integer draw;
    private Integer start;
    private Integer length;
    private String search_value;
    private String search_regex;
    private String order_column;
    private String order_dir;
    private String columns_0_data;
    private String columns_0_name;
    private String columns_0_searchable;
    private String columns_0_orderable;
    private String columns_0_search_value;
    private String columns_0_search_regex;

    public Integer getDraw() {
        return draw;
    }

    public void setDraw(Integer draw) {
        this.draw = draw;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public String getSearch_value() {
        return search_value;
    }

    public void setSearch_value(String search_value) {
        this.search_value = search_value;
    }

    public String getSearch_regex() {
        return search_regex;
    }

    public void setSearch_regex(String search_regex) {
        this.search_regex = search_regex;
    }

    public String getOrder_column() {
        return order_column;
    }

    public void setOrder_column(String order_column) {
        this.order_column = order_column;
    }

    public String getOrder_dir() {
        return order_dir;
    }

    public void setOrder_dir(String order_dir) {
        this.order_dir = order_dir;
    }

    public String getColumns_0_data() {
        return columns_0_data;
    }

    public void setColumns_0_data(String columns_0_data) {
        this.columns_0_data = columns_0_data;
    }

    public String getColumns_0_name() {
        return columns_0_name;
    }

    public void setColumns_0_name(String columns_0_name) {
        this.columns_0_name = columns_0_name;
    }

    public String getColumns_0_searchable() {
        return columns_0_searchable;
    }

    public void setColumns_0_searchable(String columns_0_searchable) {
        this.columns_0_searchable = columns_0_searchable;
    }

    public String getColumns_0_orderable() {
        return columns_0_orderable;
    }

    public void setColumns_0_orderable(String columns_0_orderable) {
        this.columns_0_orderable = columns_0_orderable;
    }

    public String getColumns_0_search_value() {
        return columns_0_search_value;
    }

    public void setColumns_0_search_value(String columns_0_search_value) {
        this.columns_0_search_value = columns_0_search_value;
    }

    public String getColumns_0_search_regex() {
        return columns_0_search_regex;
    }

    public void setColumns_0_search_regex(String columns_0_search_regex) {
        this.columns_0_search_regex = columns_0_search_regex;
    }
}
