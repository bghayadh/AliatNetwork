package com.example.aliatnetwork;

public class ImageListView {

    private String imagePath;
    private int imageIcon;

    public ImageListView(String imagePath, int imageIcon) {
        this.imagePath = imagePath;
        this.imageIcon = imageIcon;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getImageIcon() {
        return imageIcon;
    }

    public void setImageIcon(int imageIcon) {
        this.imageIcon = imageIcon;
    }
}
