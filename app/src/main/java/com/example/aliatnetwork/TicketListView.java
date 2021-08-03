package com.example.aliatnetwork;

public class TicketListView {
    private String SITE_ID;
    private String SITE_NAME;
    private String TICKET_ID;
    private String SUBJECT;
    private String STATUS;
    private String DESCRIPTION;
    

    public TicketListView(String TICKET_ID,String SUBJECT,String STATUS,String SITE_ID,String SITE_NAME,String DESCRIPTION) {

        this.TICKET_ID = TICKET_ID;
        this.SUBJECT = SUBJECT;
        this.STATUS=STATUS;
        this.SITE_ID=SITE_ID;
        this.SITE_NAME=SITE_NAME;
        this.DESCRIPTION=DESCRIPTION;


    }

    public String getSITE_ID() {
        return SITE_ID;
    }

    public void setSITE_ID(String SITE_ID) {
        this.SITE_ID = SITE_ID;
    }

    public String getDESCRIPTION() {
        return DESCRIPTION;
    }

    public void setDESCRIPTION(String DESCRIPTION) {
        this.DESCRIPTION = DESCRIPTION;
    }

    public String getSITE_NAME() {
        return SITE_NAME;
    }

    public void setSITE_NAME(String SITE_NAME) {
        this.SITE_NAME = SITE_NAME;
    }

    public String getTICKET_ID() {
        return TICKET_ID;
    }


    public String getSUBJECT() {
        return SUBJECT;
    }


    public String getSTATUS() {
        return STATUS;
    }

    public void setTICKET_ID(String TICKET_ID) {
        this.TICKET_ID = TICKET_ID;
    }


    public void setSUBJECT(String SUBJECT) {
        this.SUBJECT = SUBJECT;
    }


    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    @Override
    public String toString() {
        return "TicketListView{" +
                ", ticketId='" + TICKET_ID + '\'' +
                ", siteId='" + SITE_ID + '\'' +
                ", siteName='" + SITE_NAME + '\'' +
                ", description='" + DESCRIPTION + '\'' +
                ", subject='" + SUBJECT + '\'' +
                ", status='" + STATUS + '\'' +
                '}';
    }
}
