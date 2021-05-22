package com.example.aliatnetwork;

public class TicketListView {

    private String TICKET_ID;
    private String DEPARTMENT;
    private String SUBJECT;
    private String LONGITUDE;
    private String LATITUDE;
    private String STATUS;
    

    public TicketListView(String TICKET_ID, String DEPARTMENT,String SUBJECT,String LONGITUDE,String LATITUDE,String STATUS) {

        this.TICKET_ID = TICKET_ID;
        this.DEPARTMENT= DEPARTMENT;
        this.SUBJECT = SUBJECT;
        this.LONGITUDE=LONGITUDE;
        this.LATITUDE=LATITUDE;
        this.STATUS=STATUS;


    }

    public String getTICKET_ID() {
        return TICKET_ID;
    }

    public String getDEPARTMENT() {
        return DEPARTMENT;
    }

    public String getSUBJECT() {
        return SUBJECT;
    }

    public String getLONGITUDE() {
        return LONGITUDE;
    }

    public String getLATITUDE() {
        return LATITUDE;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setTICKET_ID(String TICKET_ID) {
        this.TICKET_ID = TICKET_ID;
    }

    public void setDEPARTMENT(String DEPARTMENT) {
        this.DEPARTMENT = DEPARTMENT;
    }

    public void setSUBJECT(String SUBJECT) {
        this.SUBJECT = SUBJECT;
    }

    public void setLONGITUDE(String LONGITUDE) {
        this.LONGITUDE = LONGITUDE;
    }

    public void setLATITUDE(String LATITUDE) {
        this.LATITUDE = LATITUDE;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    @Override
    public String toString() {
        return "TicketListView{" +
                ", ticketId='" + TICKET_ID + '\'' +
                ", department='" + DEPARTMENT + '\'' +
                ", subject='" + SUBJECT + '\'' +
                ", longitude='" + LONGITUDE + '\'' +
                ", latitude='" + LATITUDE + '\'' +
                ", status='" + STATUS + '\'' +
                '}';
    }
}
