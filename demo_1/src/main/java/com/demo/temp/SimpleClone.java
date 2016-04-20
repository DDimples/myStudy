package com.demo.temp;

import com.demo.HotelModel;

/**
 * Created by 程祥 on 16/4/20.
 * Function：
 */
public class SimpleClone implements Cloneable{

    private String name;
    private HotelModel hotelModel =new HotelModel();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHotelNum(int num){
        hotelModel.setNum(num);
    }
    public int getHotelNum(){
        return hotelModel.getNum();
    }

    public HotelModel getHotelModel() {
        return hotelModel;
    }

    public void setHotelModel(HotelModel hotelModel) {
        this.hotelModel = hotelModel;
    }

    @Override
    public Object clone() {
        SimpleClone cloneObj = null;
        try {
            cloneObj= (SimpleClone)super.clone();
            // 如果不对其内部对象进行复制，则改对象还是引用之前的，
            // 两个对象之间的操作都会改变hotelModel的值
//            cloneObj.hotelModel = hotelModel.clone();
        } catch (CloneNotSupportedException e) {
            throw new ClassCastException(e.getMessage());
        }
        return  cloneObj;
    }
}
