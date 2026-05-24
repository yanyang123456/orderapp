package com.orderapp.supplier.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class SupplierProduct {
    private Long id;
    private Long supplierId;
    private String name;
    private String images;
    private Long categoryId;
    private String category;
    private String description;
    private String material;
    private String color;
    private String size;
    private String model;
    private String style;
    private BigDecimal price;
    private Integer stock;
    private Integer spotStock;
    private Integer presaleCycleDays;
    private Integer customCycleDays;
    private Integer minOrderQuantity;
    private String deliveryAreas;
    private String packageInfo;
    private Boolean supportInstallation;
    private String status;
    private String reviewStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getSpotStock() {
        return spotStock;
    }

    public void setSpotStock(Integer spotStock) {
        this.spotStock = spotStock;
    }

    public Integer getPresaleCycleDays() {
        return presaleCycleDays;
    }

    public void setPresaleCycleDays(Integer presaleCycleDays) {
        this.presaleCycleDays = presaleCycleDays;
    }

    public Integer getCustomCycleDays() {
        return customCycleDays;
    }

    public void setCustomCycleDays(Integer customCycleDays) {
        this.customCycleDays = customCycleDays;
    }

    public Integer getMinOrderQuantity() {
        return minOrderQuantity;
    }

    public void setMinOrderQuantity(Integer minOrderQuantity) {
        this.minOrderQuantity = minOrderQuantity;
    }

    public String getDeliveryAreas() {
        return deliveryAreas;
    }

    public void setDeliveryAreas(String deliveryAreas) {
        this.deliveryAreas = deliveryAreas;
    }

    public String getPackageInfo() {
        return packageInfo;
    }

    public void setPackageInfo(String packageInfo) {
        this.packageInfo = packageInfo;
    }

    public Boolean getSupportInstallation() {
        return supportInstallation;
    }

    public void setSupportInstallation(Boolean supportInstallation) {
        this.supportInstallation = supportInstallation;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReviewStatus() {
        return reviewStatus;
    }

    public void setReviewStatus(String reviewStatus) {
        this.reviewStatus = reviewStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

}