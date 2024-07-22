package com.gj.gjchungsa.schedule;

import java.io.Serializable;

public class Schedule implements Serializable {
    int schedule_no;
    String schedule_url;
    String chungsa_user_email;

    String schedule_name;

    public String getSchedule_name() {
        return schedule_name;
    }

    public void setSchedule_name(String schedule_name) {
        this.schedule_name = schedule_name;
    }

    public int getSchedule_no() {
        return schedule_no;
    }

    public void setSchedule_no(int schedule_no) {
        this.schedule_no = schedule_no;
    }

    public String getSchedule_url() {
        return schedule_url;
    }

    public void setSchedule_url(String schedule_url) {
        this.schedule_url = schedule_url;
    }
}