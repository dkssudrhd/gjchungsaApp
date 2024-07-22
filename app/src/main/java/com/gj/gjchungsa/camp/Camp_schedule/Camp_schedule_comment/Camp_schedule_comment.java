package com.gj.gjchungsa.camp.Camp_schedule.Camp_schedule_comment;

public class Camp_schedule_comment {        //스케줄 댓글
    int camp_schedule_comment_no;           //식별을 위한 것
    String camp_schedule_comment_content;   //내용
    String camp_schedule_comment_creation;  //만든시간
    int camp_schedule_no;                   //연관된 캠프 식별 넘버
    String chungsa_user_email;              //만든 유저 이메일
    int parent_camp_schedule_comment_no;    //대댓글일 때 부모의 넘버
    String chungsa_user_name;               // 유저 이름

    public String getChungsa_user_name() {
        return chungsa_user_name;
    }

    public void setChungsa_user_name(String chungsa_user_name) {
        this.chungsa_user_name = chungsa_user_name;
    }

    public int getCamp_schedule_comment_no() {
        return camp_schedule_comment_no;
    }

    public void setCamp_schedule_comment_no(int camp_schedule_comment_no) {
        this.camp_schedule_comment_no = camp_schedule_comment_no;
    }

    public String getCamp_schedule_comment_content() {
        return camp_schedule_comment_content;
    }

    public void setCamp_schedule_comment_content(String camp_schedule_comment_content) {
        this.camp_schedule_comment_content = camp_schedule_comment_content;
    }

    public String getCamp_schedule_comment_creation() {
        return camp_schedule_comment_creation;
    }

    public void setCamp_schedule_comment_creation(String camp_schedule_comment_creation) {
        this.camp_schedule_comment_creation = camp_schedule_comment_creation;
    }

    public int getCamp_schedule_no() {
        return camp_schedule_no;
    }

    public void setCamp_schedule_no(int camp_schedule_no) {
        this.camp_schedule_no = camp_schedule_no;
    }

    public String getChungsa_user_email() {
        return chungsa_user_email;
    }

    public void setChungsa_user_email(String chungsa_user_email) {
        this.chungsa_user_email = chungsa_user_email;
    }

    public int getParent_camp_schedule_comment_no() {
        return parent_camp_schedule_comment_no;
    }

    public void setParent_camp_schedule_comment_no(int parent_camp_schedule_comment_no) {
        this.parent_camp_schedule_comment_no = parent_camp_schedule_comment_no;
    }
}
