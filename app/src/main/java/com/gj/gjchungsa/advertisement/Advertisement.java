package com.gj.gjchungsa.advertisement;

import java.io.Serializable;

public class Advertisement implements Serializable {
    int advertisement_no;
    String advertisement_image_url;
    String advertisement_url;

    public int getAdvertisement_no() {
        return advertisement_no;
    }

    public void setAdvertisement_no(int advertisement_no) {
        this.advertisement_no = advertisement_no;
    }

    public String getAdvertisement_image_url() {
        return advertisement_image_url;
    }

    public void setAdvertisement_image_url(String advertisement_image_url) {
        this.advertisement_image_url = advertisement_image_url;
    }

    public String getAdvertisement_url() {
        return advertisement_url;
    }

    public void setAdvertisement_url(String advertisement_url) {
        this.advertisement_url = advertisement_url;
    }
}
