package com.gj.gjchungsa.camp.Camp_schedule;

import java.io.Serializable;

public class Camp_schedule implements Serializable {
    int camp_schedule_no;                       // 구분을 위한 primary key
    String camp_schedule_title;                 // 제목
    String camp_schedule_content;               // 내용
    String camp_schedule_when;                  // 시작날짜
    String camp_schedule_creation;              // 작성일
    String camp_schedule_type;                  // 몇박 몇일
    String camp_schedule_where;                 // 캠프 지역
    int camp_schedule_adult_no;                 // 어른 몇 명
    int camp_schedule_kid_no;                   // 아이 몇 명
    String chungsa_user_email;                  // 작성자의 이메일
    String camp_schedule_what_family;           // 몇 가정교회인지
    String camp_schedule_family_name;           // 가정교회 양육사 이름
    String camp_schedule_how_much;              // 총 금액
    String camp_schedule_image_url1;            // 사진 첫번째
    String camp_schedule_image_url2;            // 사진 두번째
    String camp_schedule_image_url3;            // 사진 세번째
    String camp_schedule_image_url4;            // 사진 네번째
    String chungsa_user_name;                   // 작성자 이름   //현재이름으로 뜸

    public String getCamp_schedule_how_much() {
        return camp_schedule_how_much;
    }

    public void setCamp_schedule_how_much(String camp_schedule_how_much) {
        this.camp_schedule_how_much = camp_schedule_how_much;
    }

    public String getChungsa_user_name() {
        return chungsa_user_name;
    }

    public void setChungsa_user_name(String chungsa_user_name) {
        this.chungsa_user_name = chungsa_user_name;
    }

    public String getCamp_schedule_image_url1() {
        return camp_schedule_image_url1;
    }

    public void setCamp_schedule_image_url1(String camp_schedule_image_url1) {
        this.camp_schedule_image_url1 = camp_schedule_image_url1;
    }

    public String getCamp_schedule_image_url2() {
        return camp_schedule_image_url2;
    }

    public void setCamp_schedule_image_url2(String camp_schedule_image_url2) {
        this.camp_schedule_image_url2 = camp_schedule_image_url2;
    }

    public String getCamp_schedule_image_url3() {
        return camp_schedule_image_url3;
    }

    public void setCamp_schedule_image_url3(String camp_schedule_image_url3) {
        this.camp_schedule_image_url3 = camp_schedule_image_url3;
    }

    public String getCamp_schedule_image_url4() {
        return camp_schedule_image_url4;
    }

    public void setCamp_schedule_image_url4(String camp_schedule_image_url4) {
        this.camp_schedule_image_url4 = camp_schedule_image_url4;
    }

    public int getCamp_schedule_no() {
        return camp_schedule_no;
    }

    public void setCamp_schedule_no(int camp_schedule_no) {
        this.camp_schedule_no = camp_schedule_no;
    }

    public String getCamp_schedule_title() {
        return camp_schedule_title;
    }

    public void setCamp_schedule_title(String camp_schedule_title) {
        this.camp_schedule_title = camp_schedule_title;
    }

    public String getCamp_schedule_content() {
        return camp_schedule_content;
    }

    public void setCamp_schedule_content(String camp_schedule_content) {
        this.camp_schedule_content = camp_schedule_content;
    }

    public String getCamp_schedule_when() {
        return camp_schedule_when;
    }

    public void setCamp_schedule_when(String camp_schedule_when) {
        this.camp_schedule_when = camp_schedule_when;
    }

    public String getCamp_schedule_creation() {
        return camp_schedule_creation;
    }

    public void setCamp_schedule_creation(String camp_schedule_creation) {
        this.camp_schedule_creation = camp_schedule_creation;
    }

    public String getCamp_schedule_type() {
        return camp_schedule_type;
    }

    public void setCamp_schedule_type(String camp_schedule_type) {
        this.camp_schedule_type = camp_schedule_type;
    }

    public String getCamp_schedule_where() {
        return camp_schedule_where;
    }

    public void setCamp_schedule_where(String camp_schedule_where) {
        this.camp_schedule_where = camp_schedule_where;
    }

    public int getCamp_schedule_adult_no() {
        return camp_schedule_adult_no;
    }

    public void setCamp_schedule_adult_no(int camp_schedule_adult_no) {
        this.camp_schedule_adult_no = camp_schedule_adult_no;
    }

    public int getCamp_schedule_kid_no() {
        return camp_schedule_kid_no;
    }

    public void setCamp_schedule_kid_no(int camp_schedule_kid_no) {
        this.camp_schedule_kid_no = camp_schedule_kid_no;
    }

    public String getChungsa_user_email() {
        return chungsa_user_email;
    }

    public void setChungsa_user_email(String chungsa_user_email) {
        this.chungsa_user_email = chungsa_user_email;
    }

    public String getCamp_schedule_what_family() {
        return camp_schedule_what_family;
    }

    public void setCamp_schedule_what_family(String camp_schedule_what_family) {
        this.camp_schedule_what_family = camp_schedule_what_family;
    }

    public String getCamp_schedule_family_name() {
        return camp_schedule_family_name;
    }

    public void setCamp_schedule_family_name(String camp_schedule_family_name) {
        this.camp_schedule_family_name = camp_schedule_family_name;
    }

    public Camp_schedule(int camp_schedule_no, String camp_schedule_title, String camp_schedule_content, String camp_schedule_when, String camp_schedule_creation, String camp_schedule_type, String camp_schedule_where, int camp_schedule_adult_no, int camp_schedule_kid_no, String chungsa_user_email, String camp_schedule_what_family, String camp_schedule_family_name) {
        this.camp_schedule_no = camp_schedule_no;
        this.camp_schedule_title = camp_schedule_title;
        this.camp_schedule_content = camp_schedule_content;
        this.camp_schedule_when = camp_schedule_when;
        this.camp_schedule_creation = camp_schedule_creation;
        this.camp_schedule_type = camp_schedule_type;
        this.camp_schedule_where = camp_schedule_where;
        this.camp_schedule_adult_no = camp_schedule_adult_no;
        this.camp_schedule_kid_no = camp_schedule_kid_no;
        this.chungsa_user_email = chungsa_user_email;
        this.camp_schedule_what_family = camp_schedule_what_family;
        this.camp_schedule_family_name = camp_schedule_family_name;
    }
}
