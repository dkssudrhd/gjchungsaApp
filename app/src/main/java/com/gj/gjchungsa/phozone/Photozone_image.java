package com.gj.gjchungsa.phozone;

import java.io.Serializable;

public class Photozone_image implements Serializable {
    int photozone_image_no;
    String photozone_image_url;
    int photozone_no;
    int photozone_image_order;

    public int getPhotozone_image_order() {
        return photozone_image_order;
    }

    public void setPhotozone_image_order(int photozone_image_order) {
        this.photozone_image_order = photozone_image_order;
    }

    public int getPhotozone_image_no() {
        return photozone_image_no;
    }

    public void setPhotozone_image_no(int photozone_image_no) {
        this.photozone_image_no = photozone_image_no;
    }

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

}
