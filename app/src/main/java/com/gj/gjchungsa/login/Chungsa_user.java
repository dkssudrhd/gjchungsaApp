package com.gj.gjchungsa.login;

import java.io.Serializable;

public class Chungsa_user implements Serializable {
    String chungsa_user_email;
    String chungsa_user_nickname;
    String chungsa_user_name;
    String chungsa_user_grade;      //최고관리자, 중간관리자, 일반관리자, 청사교인, 일반사용자
    String chungsa_user_position;

    public Chungsa_user(){

    }
    public Chungsa_user(String chungsa_user_email, String chungsa_user_nickname, String chungsa_user_name,
                        String chungsa_user_grade, String chungsa_user_position) {
        this.chungsa_user_email = chungsa_user_email;
        this.chungsa_user_nickname = chungsa_user_nickname;
        this.chungsa_user_name = chungsa_user_name;
        this.chungsa_user_grade = chungsa_user_grade;
        this.chungsa_user_position = chungsa_user_position;
    }

    public String getChungsa_user_email() {
        return chungsa_user_email;
    }

    public void setChungsa_user_email(String chungsa_user_email) {
        this.chungsa_user_email = chungsa_user_email;
    }

    public String getChungsa_user_nickname() {
        return chungsa_user_nickname;
    }

    public void setChungsa_user_nickname(String chungsa_user_nikname) {
        this.chungsa_user_nickname = chungsa_user_nikname;
    }

    public String getChungsa_user_name() {
        return chungsa_user_name;
    }

    public void setChungsa_user_name(String chungsa_user_name) {
        this.chungsa_user_name = chungsa_user_name;
    }

    public String getChungsa_user_grade() {
        return chungsa_user_grade;
    }

    public void setChungsa_user_grade(String chungsa_user_grade) {
        this.chungsa_user_grade = chungsa_user_grade;
    }

    public String getChungsa_user_position() {
        return chungsa_user_position;
    }

    public void setChungsa_user_position(String chungsa_user_position) {
        this.chungsa_user_position = chungsa_user_position;
    }
}
