package com.floorists.grannyspillbox.classes;

public class Medication {

    public long id;
    public String name;
    public String description;
    public String serialNo;
    public String uom;
    public String imageUrl;
    public double qty;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }


    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public void setImageUrl(String url) {
        this.imageUrl = url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public double getQty() {
        return qty;
    }

    public void setQty(double qty) {
        this.qty = qty;
    }
}


