package com.gj.gjchungsa.camp.Camp_comment;

import java.io.Serializable;

public class Camp_comment implements Serializable {
    int camp_comment_no;
    String camp_comment_content;
    String camp_comment_creation;
    int Camp_mark_no;
    String chungsa_user_email;
    String chungsa_user_name;
    int parent_camp_comment_no;

    public int getParent_camp_comment_no() {
        return parent_camp_comment_no;
    }

    public void setParent_camp_comment_no(int parent_camp_comment_no) {
        this.parent_camp_comment_no = parent_camp_comment_no;
    }

    public String getChungsa_user_name() {
        return chungsa_user_name;
    }

    public void setChungsa_user_name(String chungsa_user_name) {
        this.chungsa_user_name = chungsa_user_name;
    }

    public int getCamp_comment_no() {
        return camp_comment_no;
    }

    public void setCamp_comment_no(int camp_comment_no) {
        this.camp_comment_no = camp_comment_no;
    }


    public String getCamp_comment_content() {
        return camp_comment_content;
    }

    public void setCamp_comment_content(String camp_comment_content) {
        this.camp_comment_content = camp_comment_content;
    }

    public String getCamp_comment_creation() {
        return camp_comment_creation;
    }

    public void setCamp_comment_creation(String camp_comment_creation) {
        this.camp_comment_creation = camp_comment_creation;
    }

    public int getCamp_mark_no() {
        return Camp_mark_no;
    }

    public void setCamp_mark_no(int camp_mark_no) {
        Camp_mark_no = camp_mark_no;
    }

    public String getChungsa_user_email() {
        return chungsa_user_email;
    }

    public void setChungsa_user_email(String chungsa_user_email) {
        this.chungsa_user_email = chungsa_user_email;
    }
}
