package com.gj.gjchungsa.phozone;

import java.io.Serializable;

public class Photozone implements Serializable {
    int photozone_no;               //식별을 위한 primary key
    String photozone_title;         //제목
    String photozone_content;       //내용
    String photozone_creation;      //만든시간
    String chungsa_user_email;      //작성자 이메일
    String chungsa_user_name;       //작성자 이름
    String photozone_image_url;     //미리보기 사진

    public String getPhotozone_image_url() {
        return photozone_image_url;
    }

    public void setPhotozone_image_url(String photozone_image_url) {
        this.photozone_image_url = photozone_image_url;
    }

    public int getPhotozone_no() {
        return photozone_no;
    }

    public void setPhotozone_no(int photozone_no) {
        this.photozone_no = photozone_no;
    }

    public String getPhotozone_title() {
        return photozone_title;
    }

    public void setPhotozone_title(String photozone_title) {
        this.photozone_title = photozone_title;
    }

    public String getPhotozone_content() {
        return photozone_content;
    }

    public void setPhotozone_content(String photozone_content) {
        this.photozone_content = photozone_content;
    }

    public String getPhotozone_creation() {
        return photozone_creation;
    }

    public void setPhotozone_creation(String photozone_creation) {
        this.photozone_creation = photozone_creation;
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

}
