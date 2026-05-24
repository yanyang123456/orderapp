package com.orderapp.supplier.service;

import com.orderapp.supplier.entity.*;

import java.util.List;
import java.util.Map;

public interface SupplierAppService {

    Map<String, Object> login(String mobile, String code);

    Map<String, Object> dashboardOverview();

    Map<String, Object> walletOverview(String month);

    Map<String, Object> walletTransactions(Integer page, Integer pageSize, String type);

    Map<String, Object> products(Integer page, Integer pageSize, String status, String keyword);

    Map<String, Object> createProduct(SupplierProduct product);

    Map<String, Object> submitQuote(SupplierQuote quote);

    Map<String, Object> fulfillments(String date, String groupBy, Long warehouseId, String status);

    Map<String, Object> shipFulfillment(Long deliveryId, SupplierFulfillment fulfillment);

    Map<String, Object> installations(String date, String status);

    Map<String, Object> completeInstallation(Long installationId, SupplierInstallation installation);

    Map<String, Object> aftersales(Integer page, Integer pageSize, String type, String status);

    Map<String, Object> handleAftersale(Long caseId, SupplierAftersaleCase aftersaleCase);

    Map<String, Object> reviews(String type, String status, Integer page, Integer pageSize);

    Map<String, Object> analyticsOverview(String range);
}
