package com.gj.gjchungsa.camp;

import java.io.Serializable;

public class Camp_mark implements Serializable {
    int Camp_mark_no;
    String Camp_mark_type;
    String Camp_mark_where;
    String Camp_mark_where_detail;
    String Camp_mark_title;
    String Camp_mark_content;
    int Camp_mark_madin;
    String Camp_mark_link;
    double Camp_mark_x;
    double Camp_mark_y;
    String Camp_mark_creation;
    String Camp_mark_image_url1 = null;
    String Camp_mark_image_url2 = null;
    String Camp_mark_image_url3 = null;
    String Camp_mark_image_url4 = null;
    String chungsa_user_email;
    String chungsa_user_name;
    String Camp_mark_phone;

    public Camp_mark(int camp_mark_no, String camp_mark_type, String camp_mark_where, String camp_mark_where_detail,
                     String camp_mark_title, String camp_mark_content, int camp_mark_madin, String camp_mark_link,
                     double camp_mark_x, double camp_mark_y, String camp_mark_creation, String camp_mark_image_url1,
                     String camp_mark_image_url2, String camp_mark_image_url3, String camp_mark_image_url4,
                     String chungsa_user_email, String chungsa_user_name, String camp_mark_phone) {
        Camp_mark_no = camp_mark_no;
        Camp_mark_type = camp_mark_type;
        Camp_mark_where = camp_mark_where;
        Camp_mark_where_detail = camp_mark_where_detail;
        Camp_mark_title = camp_mark_title;
        Camp_mark_content = camp_mark_content;
        Camp_mark_madin = camp_mark_madin;
        Camp_mark_link = camp_mark_link;
        Camp_mark_x = camp_mark_x;
        Camp_mark_y = camp_mark_y;
        Camp_mark_creation = camp_mark_creation;
        Camp_mark_image_url1 = camp_mark_image_url1;
        Camp_mark_image_url2 = camp_mark_image_url2;
        Camp_mark_image_url3 = camp_mark_image_url3;
        Camp_mark_image_url4 = camp_mark_image_url4;
        this.chungsa_user_email = chungsa_user_email;
        this.chungsa_user_name = chungsa_user_name;
        Camp_mark_phone = camp_mark_phone;
    }

    public String getCamp_mark_phone() {
        return Camp_mark_phone;
    }

    public void setCamp_mark_phone(String camp_mark_phone) {
        Camp_mark_phone = camp_mark_phone;
    }

    public String getChungsa_user_email() {
        return chungsa_user_email;
    }

    public void setChungsa_user_email(String chungsa_user_email) {
        this.chungsa_user_email = chungsa_user_email;
    }

    public String getChungsa_user_name() {
        return chungsa_user_name;
    }

    public void setChungsa_user_name(String chungsa_user_name) {
        this.chungsa_user_name = chungsa_user_name;
    }

    public int getCamp_mark_no() {
        return Camp_mark_no;
    }

    public void setCamp_mark_no(int camp_mark_no) {
        Camp_mark_no = camp_mark_no;
    }

    public String getCamp_mark_type() {
        return Camp_mark_type;
    }

    public void setCamp_mark_type(String camp_mark_type) {
        Camp_mark_type = camp_mark_type;
    }

    public String getCamp_mark_where() {
        return Camp_mark_where;
    }

    public void setCamp_mark_where(String camp_mark_where) {
        Camp_mark_where = camp_mark_where;
    }

    public String getCamp_mark_where_detail() {
        return Camp_mark_where_detail;
    }

    public void setCamp_mark_where_detail(String camp_mark_where_detail) {
        Camp_mark_where_detail = camp_mark_where_detail;
    }

    public String getCamp_mark_title() {
        return Camp_mark_title;
    }

    public void setCamp_mark_title(String camp_mark_title) {
        Camp_mark_title = camp_mark_title;
    }

    public String getCamp_mark_content() {
        return Camp_mark_content;
    }

    public void setCamp_mark_content(String camp_mark_content) {
        Camp_mark_content = camp_mark_content;
    }

    public int getCamp_mark_madin() {
        return Camp_mark_madin;
    }

    public void setCamp_mark_madin(int camp_mark_madin) {
        Camp_mark_madin = camp_mark_madin;
    }

    public String getCamp_mark_link() {
        return Camp_mark_link;
    }

    public void setCamp_mark_link(String camp_mark_link) {
        Camp_mark_link = camp_mark_link;
    }

    public double getCamp_mark_x() {
        return Camp_mark_x;
    }

    public void setCamp_mark_x(double camp_mark_x) {
        Camp_mark_x = camp_mark_x;
    }

    public double getCamp_mark_y() {
        return Camp_mark_y;
    }

    public void setCamp_mark_y(double camp_mark_y) {
        Camp_mark_y = camp_mark_y;
    }

    public String getCamp_mark_creation() {
        return Camp_mark_creation;
    }

    public void setCamp_mark_creation(String camp_mark_creation) {
        Camp_mark_creation = camp_mark_creation;
    }

    public String getCamp_mark_image_url1() {
        return Camp_mark_image_url1;
    }

    public void setCamp_mark_image_url1(String camp_mark_image_url1) {
        Camp_mark_image_url1 = camp_mark_image_url1;
    }

    public String getCamp_mark_image_url2() {
        return Camp_mark_image_url2;
    }

    public void setCamp_mark_image_url2(String camp_mark_image_url2) {
        Camp_mark_image_url2 = camp_mark_image_url2;
    }

    public String getCamp_mark_image_url3() {
        return Camp_mark_image_url3;
    }

    public void setCamp_mark_image_url3(String camp_mark_image_url3) {
        Camp_mark_image_url3 = camp_mark_image_url3;
    }

    public String getCamp_mark_image_url4() {
        return Camp_mark_image_url4;
    }

    public void setCamp_mark_image_url4(String camp_mark_image_url4) {
        Camp_mark_image_url4 = camp_mark_image_url4;
    }
}
