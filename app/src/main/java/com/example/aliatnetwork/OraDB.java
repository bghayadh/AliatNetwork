package com.example.aliatnetwork;

public class OraDB {
    private String oraurl;
    private String orausername;
    private String orapwd;



    public OraDB() {
        oraurl = "jdbc:oracle:thin:@192.168.80.57:1524:ALM";
        orausername = "alm";
        orapwd = "alm";
    }


    public String getoraurl() {
        return oraurl;
    }

    public String getorausername() {
        return orausername;
    }

    public String getorapwd() {
        return orapwd;
    }



}
