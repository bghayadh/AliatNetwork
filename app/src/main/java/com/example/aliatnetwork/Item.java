package com.example.aliatnetwork;

public class Item {

    String itemName;
    String itemCode;
    String itemModel;
    String itemPartNumber;
    String serialNumber;
    String barcodeNumber;
    String quantity;
    int ScanId;

    public int getScanId() {
        return ScanId;
    }

    public void setScanId(int scanId) {
        ScanId = scanId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemModel() {
        return itemModel;
    }

    public void setItemModel(String itemModel) {
        this.itemModel = itemModel;
    }

    public String getItemPartNumber() {
        return itemPartNumber;
    }

    public void setItemPartNumber(String itemPartNumber) {
        this.itemPartNumber = itemPartNumber;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getBarcodeNumber() {
        return barcodeNumber;
    }

    public void setBarcodeNumber(String barcodeNumber) {
        this.barcodeNumber = barcodeNumber;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public Item(String itemName, String itemCode, String itemModel, String itemPartNumber, String serialNumber, String barcodeNumber, String quantity,int ScanId) {
        this.itemName = itemName;
        this.itemCode = itemCode;
        this.itemModel = itemModel;
        this.itemPartNumber = itemPartNumber;
        this.serialNumber = serialNumber;
        this.barcodeNumber = barcodeNumber;
        this.quantity = quantity;
        this.ScanId=ScanId;
    }
}

