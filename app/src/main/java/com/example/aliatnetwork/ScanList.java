package com.example.aliatnetwork;

public class ScanList {
    private String Item;
    private String Barcode;
    private String SerialNb;
    private String quantity;


    public ScanList(String item, String barcode, String serialNb, String quantity) {
        Item = item;
        Barcode = barcode;
        SerialNb = serialNb;
        this.quantity = quantity;
    }

    public String getItem() {
        return Item;
    }

    public void setItem(String item) {
        Item = item;
    }

    public String getBarcode() {
        return Barcode;
    }

    public void setBarcode(String barcode) {
        Barcode = barcode;
    }

    public String getSerialNb() {
        return SerialNb;
    }

    public void setSerialNb(String serialNb) {
        SerialNb = serialNb;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
