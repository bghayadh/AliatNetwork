package com.example.aliatnetwork;

public class ShopsImageListView {

    private int IMAGE_ICON;
    private String IMAGE_PATH;
    private int DELETE;


    public ShopsImageListView(int IMAGE_ICON, String IMAGE_PATH, int DELETE) {
        this.IMAGE_ICON = IMAGE_ICON;
        this.IMAGE_PATH = IMAGE_PATH;
        this.DELETE = DELETE;
    }

    public int getIMAGE_ICON() {
        return IMAGE_ICON;
    }

    public void setIMAGE_ICON(int IMAGE_ICON) {
        this.IMAGE_ICON = IMAGE_ICON;
    }

    public String getIMAGE_PATH() {
        return IMAGE_PATH;
    }

    public void setIMAGE_PATH(String IMAGE_PATH) {
        this.IMAGE_PATH = IMAGE_PATH;
    }

    public int getDELETE() {
        return DELETE;
    }

    public void setDELETE(int DELETE) {
        this.DELETE = DELETE;
    }
}
