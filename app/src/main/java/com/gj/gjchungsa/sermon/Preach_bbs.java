package com.gj.gjchungsa.sermon;

import java.io.Serializable;

public class Preach_bbs implements Serializable {
    int bbs_no;
    String bbs_title;
    String bbs_parse;
    String bbs_content;
    String bbs_b_catalogue;
    String bbs_s_catalogue;
    String bbs_preacher;
    String bbs_when;
    String bbs_creation;

    int bbs_views;
    String bbs_line;
    String bbs_image_url;
    String bbs_video_type;
    String chungsa_user_email;

    public Preach_bbs(int bbs_no, String bbs_title, String bbs_parse, String bbs_content,
                      String bbs_b_catalogue, String bbs_s_catalogue, String bbs_preacher, String bbs_when, String bbs_creation,
                      int bbs_views, String bbs_line,  String bbs_image_url, String bbs_video_type,
                      String chungsa_user_email) {
        this.bbs_no = bbs_no;
        this.bbs_views = bbs_views;
        this.bbs_title = bbs_title;
        this.bbs_parse = bbs_parse;
        this.bbs_content = bbs_content;
        this.bbs_b_catalogue = bbs_b_catalogue;
        this.bbs_s_catalogue = bbs_s_catalogue;
        this.bbs_preacher = bbs_preacher;
        this.bbs_when = bbs_when;
        this.bbs_creation = bbs_creation;
        this.bbs_line = bbs_line;
        this.bbs_image_url = bbs_image_url;
        this.bbs_video_type = bbs_video_type;
        this.chungsa_user_email = chungsa_user_email;
    }

    public String getChungsa_user_email() {
        return chungsa_user_email;
    }

    public void setChungsa_user_email(String chungsa_user_email) {
        this.chungsa_user_email = chungsa_user_email;
    }

    public String getBbs_video_type() {
        return bbs_video_type;
    }

    public void setBbs_video_type(String bbs_video_type) {
        this.bbs_video_type = bbs_video_type;
    }

    public String getBbs_creation() {
        return bbs_creation;
    }

    public void setBbs_creation(String bbs_creation) {
        this.bbs_creation = bbs_creation;
    }

    public String getBbs_image_url() {
        return bbs_image_url;
    }

    public void setBbs_image_url(String bbs_image_url) {
        this.bbs_image_url = bbs_image_url;
    }

    public int getBbs_no() {
        return bbs_no;
    }

    public void setBbs_no(int bbs_no) {
        this.bbs_no = bbs_no;
    }

    public int getBbs_views() {
        return bbs_views;
    }

    public void setBbs_views(int bbs_views) {
        this.bbs_views = bbs_views;
    }


    public String getBbs_title() {
        return bbs_title;
    }

    public void setBbs_title(String bbs_title) {
        this.bbs_title = bbs_title;
    }

    public String getBbs_parse() {
        return bbs_parse;
    }

    public void setBbs_parse(String bbs_parse) {
        this.bbs_parse = bbs_parse;
    }

    public String getBbs_content() {
        return bbs_content;
    }

    public void setBbs_content(String bbs_content) {
        this.bbs_content = bbs_content;
    }

    public String getBbs_b_catalogue() {
        return bbs_b_catalogue;
    }

    public void setBbs_b_catalogue(String bbs_b_catalogue) {
        this.bbs_b_catalogue = bbs_b_catalogue;
    }

    public String getBbs_s_catalogue() {
        return bbs_s_catalogue;
    }

    public void setBbs_s_catalogue(String bbs_s_catalogue) {
        this.bbs_s_catalogue = bbs_s_catalogue;
    }

    public String getBbs_preacher() {
        return bbs_preacher;
    }

    public void setBbs_preacher(String bbs_preacher) {
        this.bbs_preacher = bbs_preacher;
    }

    public String getBbs_when() {
        return bbs_when;
    }

    public void setBbs_when(String bbs_when) {
        this.bbs_when = bbs_when;
    }

    public String getBbs_line() {
        return bbs_line;
    }

    public void setBbs_line(String bbs_line) {
        this.bbs_line = bbs_line;
    }
}
