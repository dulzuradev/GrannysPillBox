package com.floorists.grannyspillbox.classes;

import java.util.Date;

public class Medication {

    public long id;
    public String name;
    public String description;
    public String serialNo;
    public String uom;
    public String imageUrl;


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

    public void setImageUrl(String url) { this.imageUrl = url; }

    public String getImageUrl() { return imageUrl; }


