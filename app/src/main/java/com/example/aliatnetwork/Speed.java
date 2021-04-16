package com.example.aliatnetwork;

import oracle.sql.TIMESTAMP;

public class Speed {
    private String spddownload;
    private String spdupload;
    private String spdlat;
    private String spdlng;
    private String spddate;

    public Speed(String spddownload,String spdupload, String spdlat, String spdlng,String spddate) {
        this.spddownload = spddownload;
        this.spdupload = spdupload;
        this.spdlat = spdlat;
        this.spdlng = spdlng;
        this.spddate= spddate;
    }


    public String getSpddownload() {
        return spddownload;
    }

    public String getSpdupload() {
        return spdupload;
    }

    public String getSpdlat() {
        return spdlat;
    }

    public String getSpdlng() {
        return spdlng;
    }

    public String getSpddate() {
        return spddate;
    }

    public void setSpddownload(String naspddownloadme) {
        this.spddownload = spddownload;
    }

    public void setSpdupload(String spdupload) {
        this.spdupload = spdupload;
    }

    public void setSpdlatl(String spdlat) {
        this.spdlat = spdlat;
    }

    public void setSpdlng(String spdlng) {
        this.spdlng = spdlng;
    }

    public void setSpddate(String spddate) {
        this.spddate = spddate;
    }

    @Override
    public String toString() {
        return "Speed{" +
                "dwnlaod='" + spddownload + '\'' +
                ", upload='" + spdupload + '\'' +
                ", lat='" + spdlat + '\'' +
                ", lng='" + spdlng + '\'' +
                ", sdate='" + spddate + '\'' +
                '}';
    }
}

