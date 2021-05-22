package com.example.aliatnetwork;

public class SiteId {
    private String SITE_ID;
    private String WARE_NAME;

    public SiteId(String SITE_ID,String WARE_NAME) {
        this.SITE_ID = SITE_ID;
        this.WARE_NAME=WARE_NAME;

    }

    public String getSITE_ID() {
        return SITE_ID;
    }


    public void setSITE_ID(String SITE_ID) {
        this.SITE_ID = SITE_ID;
    }

    public String getWARE_NAME() {
        return WARE_NAME;
    }

    public void setWARE_NAME(String WARE_NAME) {
        this.WARE_NAME = WARE_NAME;
    }
}
