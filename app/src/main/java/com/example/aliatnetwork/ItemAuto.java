package com.example.aliatnetwork;

import java.util.Objects;

public class ItemAuto {

    String itemName;
    String itemCode;
    String itemModel;
    String itemPartNumber;
    String serialNumber;
    String barcodeNumber;
    String quantity;


    public ItemAuto(String itemName, String itemCode, String itemModel, String itemPartNumber, String serialNumber, String barcodeNumber, String quantity) {
        this.itemName = itemName;
        this.itemCode = itemCode;
        this.itemModel = itemModel;
        this.itemPartNumber = itemPartNumber;
        this.serialNumber = serialNumber;
        this.barcodeNumber = barcodeNumber;
        this.quantity = quantity;
    }

    public String getItemName() {
        return Objects.toString(itemName, "");
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemCode() {
        return Objects.toString(itemCode, "");
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemModel() {
        return Objects.toString(itemModel, "");
    }

    public void setItemModel(String itemModel) {
        this.itemModel = itemModel;
    }

    public String getItemPartNumber() {
        return Objects.toString(itemPartNumber, "");
    }

    public void setItemPartNumber(String itemPartNumber) {
        this.itemPartNumber = itemPartNumber;
    }

    public String getSerialNumber() {

        return Objects.toString(serialNumber, "");
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getBarcodeNumber() {

        return Objects.toString(barcodeNumber, "");
    }

    public void setBarcodeNumber(String barcodeNumber) {
        this.barcodeNumber = barcodeNumber;
    }

    public String getQuantity() {

        return Objects.toString(quantity, "");
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
