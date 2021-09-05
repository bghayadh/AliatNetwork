package com.example.aliatnetwork;

public class SerialNumber {

    String serialNumber;
    String itemCode;


    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }





    public SerialNumber(String serialNumber, String itemCode) {
        this.serialNumber = serialNumber;
        this.itemCode = itemCode;

    }
}
