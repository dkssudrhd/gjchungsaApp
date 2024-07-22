package com.gj.gjchungsa.camp.ResultSerachKeyword;

import java.io.Serializable;

public class Camp_mark_Place  implements Serializable {
    int place_no;
    String place_name;
    String phone;
    String road_address_name;
    String x;
    String y;
    String place_url;
    //String id;
    //String category_name;
    //String category_group_code;
//    String category_group_name;
    //String address_name;
    //String distanc;

    public int getPlace_no() {
        return place_no;
    }

    public void setPlace_no(int place_no) {
        this.place_no = place_no;
    }

    public String getPlace_name() {
        return place_name;
    }

    public void setPlace_name(String place_name) {
        this.place_name = place_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRoad_address_name() {
        return road_address_name;
    }

    public void setRoad_address_name(String road_address_name) {
        this.road_address_name = road_address_name;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public String getPlace_url() {
        return place_url;
    }

    public void setPlace_url(String place_url) {
        this.place_url = place_url;
    }


    public Camp_mark_Place(int place_no, String place_name, String phone, String road_address_name, String x, String y, String place_url) {
        this.place_no = place_no;
        this.place_name = place_name;
        this.phone = phone;
        this.road_address_name = road_address_name;
        this.x = x;
        this.y = y;
        this.place_url = place_url;
    }
}
