package com.orderapp.supplier.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class SupplierWalletSettlement {
    private Long id;
    private Long supplierId;
    private String settlementMonth;
    private BigDecimal totalIncome;
    private BigDecimal withdrawableAmount;
    private BigDecimal frozenAmount;
    private BigDecimal orderAmount;
    private BigDecimal deliveryFee;
    private BigDecimal installationFee;
    private BigDecimal refundDeduction;
    private BigDecimal aftersaleDeduction;
    private String status;
    private LocalDateTime createdAt;

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

    public String getSettlementMonth() {
        return settlementMonth;
    }

    public void setSettlementMonth(String settlementMonth) {
        this.settlementMonth = settlementMonth;
    }

    public BigDecimal getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(BigDecimal totalIncome) {
        this.totalIncome = totalIncome;
    }

    public BigDecimal getWithdrawableAmount() {
        return withdrawableAmount;
    }

    public void setWithdrawableAmount(BigDecimal withdrawableAmount) {
        this.withdrawableAmount = withdrawableAmount;
    }

    public BigDecimal getFrozenAmount() {
        return frozenAmount;
    }

    public void setFrozenAmount(BigDecimal frozenAmount) {
        this.frozenAmount = frozenAmount;
    }

    public BigDecimal getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }

    public BigDecimal getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(BigDecimal deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public BigDecimal getInstallationFee() {
        return installationFee;
    }

    public void setInstallationFee(BigDecimal installationFee) {
        this.installationFee = installationFee;
    }

    public BigDecimal getRefundDeduction() {
        return refundDeduction;
    }

    public void setRefundDeduction(BigDecimal refundDeduction) {
        this.refundDeduction = refundDeduction;
    }

    public BigDecimal getAftersaleDeduction() {
        return aftersaleDeduction;
    }

    public void setAftersaleDeduction(BigDecimal aftersaleDeduction) {
        this.aftersaleDeduction = aftersaleDeduction;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}