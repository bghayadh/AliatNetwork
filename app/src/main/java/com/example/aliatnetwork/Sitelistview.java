package com.example.aliatnetwork;
import oracle.sql.TIMESTAMP;
public class Sitelistview {
    private String WAREID;
    private String SITEID;
    private String WARENAME;
    private String WADDRESS;

    public Sitelistview(String WAREID, String SITEID,String WARENAME,String WADDRESS) {

        this.WAREID = WAREID;
        this.SITEID= SITEID;
        this.WARENAME = WARENAME;
        this. WADDRESS= WADDRESS;


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





    @Override
    public String toString() {
        return "Sitelistview{" +
                ", wareid='" + WAREID + '\'' +
                ", siteid='" + SITEID + '\'' +
                ", warename='" + WARENAME + '\'' +
                ", wareaddr='" + WADDRESS + '\'' +
                '}';
    }

}
