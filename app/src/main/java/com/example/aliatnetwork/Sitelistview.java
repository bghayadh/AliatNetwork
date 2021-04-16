package com.example.aliatnetwork;
import oracle.sql.TIMESTAMP;
public class Sitelistview {
    private String WAREID;
    private String SITEID;
    private String WARENAME;
    private String WARELAT;
    private String WARELNG;
    private String WADDRESS;

    public Sitelistview(String WAREID, String SITEID,String WARENAME,String WADDRESS, String WARELAT,String WARELNG) {

        this.WAREID = WAREID;
        this.SITEID= SITEID;
        this.WARENAME = WARENAME;
        this. WADDRESS= WADDRESS;
        this.WARELAT = WARELAT;
        this.WARELNG= WARELNG;

    }

    public String getWAREID() {
        return WAREID;
    }

    public String getSITEID() {
        return SITEID;
    }

    public String getWARENAME() {
        return WARENAME;
    }

    public String getWADDRESS() {
        return WADDRESS;
    }

    public String getWARELAT() {
        return WARELAT;
    }

    public String getWARELNG() {
        return WARELNG;
    }



    public void setWAREID(String WAREID) {
        this.WAREID = WAREID;
    }

    public void setSITEID(String SITEID) {
        this.SITEID = SITEID;
    }

    public void setWARENAME(String WARENAME) {
        this.WARENAME = WARENAME;
    }

    public void setWADDRESS(String WADDRESS) {
        this.WADDRESS = WADDRESS;
    }

    public void setWARELAT(String WARELAT) {
        this.WARELAT = WARELAT;
    }

    public void setWARELNG(String WARELNG) {
        this.WARELNG = WARELNG;
    }



    @Override
    public String toString() {
        return "Sitelistview{" +
                ", wareid='" + WAREID + '\'' +
                ", siteid='" + SITEID + '\'' +
                ", warename='" + WARENAME + '\'' +
                ", wareaddr='" + WADDRESS + '\'' +
                ", warelat='" + WARELAT + '\'' +
                ", warelng='" + WARELNG + '\'' +
                '}';
    }

}
