package com.example.aliatnetwork;
import oracle.sql.TIMESTAMP;
public class Coverage {
    private String covsignal;
    private String covlat;
    private String covlng;
    private String covdate;

    public Coverage(String covsignal, String covlat, String covlng,String covdate) {

        this.covsignal = covsignal;
        this.covlat = covlat;
        this.covlng = covlng;
        this.covdate= covdate;
    }


    public String getCovsignal() {
        return covsignal;
    }

    public String getCovlat() {
        return covlat;
    }

    public String getCovlng() {
        return covlng;
    }

    public String getCovdate() {
        return covdate;
    }



    public void setCovsignal(String covsignal) {
        this.covsignal = covsignal;
    }

    public void setCovlat(String covlat) {
        this.covlat = covlat;
    }

    public void setCovlng(String covlng) {
        this.covlng = covlng;
    }

    public void setCovdate(String covdate) {
        this.covdate = covdate;
    }

    @Override
    public String toString() {
        return "Coverage{" +
                ", signal='" + covsignal + '\'' +
                ", lat='" + covlat + '\'' +
                ", lng='" + covlng + '\'' +
                ", cdate='" + covdate + '\'' +
                '}';
    }

}
