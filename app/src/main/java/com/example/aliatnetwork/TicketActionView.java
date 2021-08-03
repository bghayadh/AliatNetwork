package com.example.aliatnetwork;

public class TicketActionView {

    private String ACTION_ID;
    private String ACTION;
    private String STATUS;
    private String EMPLOYEE;

    public String getACTION_ID() {
        return ACTION_ID;
    }

    public void setACTION_ID(String ACTION_ID) {
        this.ACTION_ID = ACTION_ID;
    }

    public String getEMPLOYEE() {
        return EMPLOYEE;
    }

    public void setEMPLOYEE(String EMPLOYEE) {
        this.EMPLOYEE = EMPLOYEE;
    }

    public String getACTION() {
        return ACTION;
    }

    public void setACTION(String ACTION) {
        this.ACTION = ACTION;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    public TicketActionView(String ACTION_ID, String ACTION, String STATUS,String EMPLOYEE) {
        this.ACTION_ID = ACTION_ID;
        this.ACTION = ACTION;
        this.STATUS = STATUS;
        this.EMPLOYEE = EMPLOYEE;
    }


}
