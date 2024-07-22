package com.gj.gjchungsa.phozone.Photozone_comment;

public class Photozone_comment {        // 포토존 댓글
    int photozone_comment_no;           // 식별
    String photozone_comment_content;   // 내용
    String photozone_comment_creation;  // 만든시간
    int photozone_no;                   // 연관된 캠프 식별 넘버
    String chungsa_user_email;          // 만든 유저 이메일
    int parent_photozone_comment_no;    // 대댓글일 때 부모의 넘버
    String chungsa_user_name;           // 유저 이름

    public int getPhotozone_comment_no() {
        return photozone_comment_no;
    }

    public void setPhotozone_comment_no(int photozone_comment_no) {
        this.photozone_comment_no = photozone_comment_no;
    }

    public String getPhotozone_comment_content() {
        return photozone_comment_content;
    }

    public void setPhotozone_comment_content(String photozone_comment_content) {
        this.photozone_comment_content = photozone_comment_content;
    }

    public String getPhotozone_comment_creation() {
        return photozone_comment_creation;
    }

    public void setPhotozone_comment_creation(String photozone_comment_creation) {
        this.photozone_comment_creation = photozone_comment_creation;
    }

    public int getPhotozone_no() {
        return photozone_no;
    }

    public void setPhotozone_no(int photozone_no) {
        this.photozone_no = photozone_no;
    }

    public String getChungsa_user_email() {
        return chungsa_user_email;
    }

    public void setChungsa_user_email(String chungsa_user_email) {
        this.chungsa_user_email = chungsa_user_email;
    }

    public int getParent_photozone_comment_no() {
        return parent_photozone_comment_no;
    }

    public void setParent_photozone_comment_no(int parent_photozone_comment_no) {
        this.parent_photozone_comment_no = parent_photozone_comment_no;
    }

    public String getChungsa_user_name() {
        return chungsa_user_name;
    }

    public void setChungsa_user_name(String chungsa_user_name) {
        this.chungsa_user_name = chungsa_user_name;
    }
}
