package com.gj.gjchungsa.sermon.edit;

import java.io.Serializable;

public class Preach_catalogue_s implements Serializable {

    String bbs_s_catalogue;
    String bbs_b_catalogue;

    public String getBbs_s_catalogue() {
        return bbs_s_catalogue;
    }

    public void setBbs_s_catalogue(String bbs_s_catalogue) {
        this.bbs_s_catalogue = bbs_s_catalogue;
    }

    public String getBbs_b_catalogue() {
        return bbs_b_catalogue;
    }

    public void setBbs_b_catalogue(String bbs_b_catalogue) {
        this.bbs_b_catalogue = bbs_b_catalogue;
    }
}
