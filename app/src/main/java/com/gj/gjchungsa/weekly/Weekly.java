package com.gj.gjchungsa.weekly;

import java.io.Serializable;

public class Weekly implements Serializable {
    int weekly_no;
    String weekly_when;
    String weekly_image1;
    String weekly_image2;
    String weekly_image3;

    public Weekly(int weekly_no, String weekly_when, String weekly_image1, String weekly_image2, String weekly_image3) {
        this.weekly_no = weekly_no;
        this.weekly_when = weekly_when;
        this.weekly_image1 = weekly_image1;
        this.weekly_image2 = weekly_image2;
        this.weekly_image3 = weekly_image3;
    }

    public int getWeekly_no() {
        return weekly_no;
    }

    public void setWeekly_no(int weekly_no) {
        this.weekly_no = weekly_no;
    }

    public String getWeekly_when() {
        return weekly_when;
    }

    public void setWeekly_when(String weekly_when) {
        this.weekly_when = weekly_when;
    }

    public String getWeekly_image1() {
        return weekly_image1;
    }

    public void setWeekly_image1(String weekly_image1) {
        this.weekly_image1 = weekly_image1;
    }

    public String getWeekly_image2() {
        return weekly_image2;
    }

    public void setWeekly_image2(String weekly_image2) {
        this.weekly_image2 = weekly_image2;
    }

    public String getWeekly_image3() {
        return weekly_image3;
    }

    public void setWeekly_image3(String weekly_image3) {
        this.weekly_image3 = weekly_image3;
    }
}
