package org.example.entity;

import com.alibaba.excel.annotation.ExcelProperty;

public class ExcelData {
    @ExcelProperty("名称")
    private String name;
    @ExcelProperty("车型")
    private String vehicleModel;
    @ExcelProperty("所属产品")
    private String product;
    @ExcelProperty("PCC 分类")
    private String pccCategory;
    private String description;
    private byte[] picData;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getPccCategory() {
        return pccCategory;
    }

    public void setPccCategory(String pccCategory) {
        this.pccCategory = pccCategory;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getPicData() {
        return picData;
    }

    public void setPicData(byte[] picData) {
        this.picData = picData;
    }
}