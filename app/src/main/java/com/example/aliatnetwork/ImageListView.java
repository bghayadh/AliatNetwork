package com.example.aliatnetwork;

public class ImageListView {

    private int imageIcon;
    private String imagePath;
    private int Delete;

    public ImageListView(int delete,int imageIcon,  String imagePath) {
        this.imageIcon = imageIcon;
        this.imagePath = imagePath;
        Delete = delete;
    }

    public int getImageIcon() {
        return imageIcon;
    }

    public void setImageIcon(int imageIcon) {
        this.imageIcon = imageIcon;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getDelete() {
        return Delete;
    }

    public void setDelete(int delete) {
        Delete = delete;
    }
}
