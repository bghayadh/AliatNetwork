package com.example.aliatnetwork;

public class ShopsListView {

    private String SHOPS_ID;
    private String LONGTITUDE;
    private String LATITUDE;
    private String ADDRESS;
    private String SHOP_NAME;
    private String OWNER;

    public ShopsListView(String SHOPS_ID, String LONGTITUDE, String LATITUDE, String ADDRESS, String SHOP_NAME, String OWNER) {
        this.SHOPS_ID = SHOPS_ID;
        this.LONGTITUDE = LONGTITUDE;
        this.LATITUDE = LATITUDE;
        this.ADDRESS = ADDRESS;
        this.SHOP_NAME = SHOP_NAME;
        this.OWNER = OWNER;
    }

    public String getSHOPS_ID() {
        return SHOPS_ID;
    }

    public void setSHOPS_ID(String SHOPS_ID) {
        this.SHOPS_ID = SHOPS_ID;
    }

    public String getLONGTITUDE() {
        return LONGTITUDE;
    }

    public void setLONGTITUDE(String LONGTITUDE) {
        this.LONGTITUDE = LONGTITUDE;
    }

    public String getLATITUDE() {
        return LATITUDE;
    }

    public void setLATITUDE(String LATITUDE) {
        this.LATITUDE = LATITUDE;
    }

    public String getADDRESS() {
        return ADDRESS;
    }

    public void setADDRESS(String ADDRESS) {
        this.ADDRESS = ADDRESS;
    }

    public String getSHOP_NAME() {
        return SHOP_NAME;
    }

    public void setSHOP_NAME(String SHOP_NAME) {
        this.SHOP_NAME = SHOP_NAME;
    }

    public String getOWNER() {
        return OWNER;
    }

    public void setOWNER(String OWNER) {
        this.OWNER = OWNER;
    }
}
