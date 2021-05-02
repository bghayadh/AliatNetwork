package com.example.aliatnetwork;

public class ImageListView {
    public ImageListView(String wareID, String imagePath) {
        this.wareID = wareID;
        this.imagePath = imagePath;
    }

    private String wareID;
    private String imagePath;




    public String getWareID() {
        return wareID;
    }

    public void setWareID(String wareID) {
        this.wareID = wareID;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
