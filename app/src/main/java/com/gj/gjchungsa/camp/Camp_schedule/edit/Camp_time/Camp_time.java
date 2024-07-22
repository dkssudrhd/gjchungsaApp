package com.gj.gjchungsa.camp.Camp_schedule.edit.Camp_time;

import java.io.Serializable;

public class Camp_time implements Serializable {
    int camp_time_no;           //식별을 위한 int
    String camp_time_create;    //만든 시간
    String camp_time_content;   // 내용
    int camp_schedule_no;       // 연결된 캠프
    int camp_mark_no;           // 연결된 캠프 마크
    String Camp_mark_title;     // 연결된 캠프 마크의 제목
    int camp_time_day;          // 몇 일차인지
    int camp_time_day_play;     // 몇 번째 인지
    String camp_time_situation = "not"; // not, insert, update 로 식별
    String Camp_mark_image_url1 = null;

    public String getCamp_mark_image_url1() {
        return Camp_mark_image_url1;
    }

    public void setCamp_mark_image_url1(String camp_mark_image_url1) {
        Camp_mark_image_url1 = camp_mark_image_url1;
    }

    public Camp_time(int camp_time_day, int camp_time_day_play) {
        this.camp_time_day = camp_time_day;
        this.camp_time_day_play = camp_time_day_play;
        camp_time_situation = "not";
    }

    public String getCamp_mark_title() {
        return Camp_mark_title;
    }

    public void setCamp_mark_title(String camp_mark_title) {
        this.Camp_mark_title = camp_mark_title;
    }

    public String getCamp_time_situation() {
        return camp_time_situation;
    }

    public void setCamp_time_situation(String camp_time_situation) {
        this.camp_time_situation = camp_time_situation;
    }

    public Camp_time() {
        camp_time_no = 0;
        camp_time_day =1000;
    }

    public Camp_time(int camp_time_no, String camp_time_create, String camp_time_content,
                      int camp_schedule_no, int camp_mark_no, int camp_time_day, int camp_time_day_play) {
        this.camp_time_no = camp_time_no;
        this.camp_time_create = camp_time_create;
        this.camp_time_content = camp_time_content;
        this.camp_schedule_no = camp_schedule_no;
        this.camp_mark_no = camp_mark_no;
        this.camp_time_day = camp_time_day;
        this.camp_time_day_play = camp_time_day_play;
    }

    public int getCamp_time_no() {
        return camp_time_no;
    }

    public void setCamp_time_no(int camp_time_no) {
        this.camp_time_no = camp_time_no;
    }

    public String getCamp_time_create() {
        return camp_time_create;
    }

    public void setCamp_time_create(String camp_time_create) {
        this.camp_time_create = camp_time_create;
    }

    public String getCamp_time_content() {
        return camp_time_content;
    }

    public void setCamp_time_content(String camp_time_content) {
        this.camp_time_content = camp_time_content;
    }


    public int getCamp_schedule_no() {
        return camp_schedule_no;
    }

    public void setCamp_schedule_no(int camp_schedule_no) {
        this.camp_schedule_no = camp_schedule_no;
    }

    public int getCamp_mark_no() {
        return camp_mark_no;
    }

    public void setCamp_mark_no(int camp_mark_no) {
        this.camp_mark_no = camp_mark_no;
    }

    public int getCamp_time_day() {
        return camp_time_day;
    }

    public void setCamp_time_day(int camp_time_day) {
        this.camp_time_day = camp_time_day;
    }

    public int getCamp_time_day_play() {
        return camp_time_day_play;
    }

    public void setCamp_time_day_play(int camp_time_day_play) {
        this.camp_time_day_play = camp_time_day_play;
    }
}
