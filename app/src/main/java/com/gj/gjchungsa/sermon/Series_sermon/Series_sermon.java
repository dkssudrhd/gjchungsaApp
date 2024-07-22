package com.gj.gjchungsa.sermon.Series_sermon;

import java.io.Serializable;

public class Series_sermon implements Serializable {

    int series_no;
    String series_name;
    String series_image;

    public int getSeries_no() {
        return series_no;
    }

    public void setSeries_no(int series_no) {
        this.series_no = series_no;
    }

    public String getSeries_name() {
        return series_name;
    }

    public void setSeries_name(String series_name) {
        this.series_name = series_name;
    }

    public String getSeries_image() {
        return series_image;
    }

    public void setSeries_image(String series_image) {
        this.series_image = series_image;
    }
}
