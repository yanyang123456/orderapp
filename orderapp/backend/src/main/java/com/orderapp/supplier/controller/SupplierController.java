package com.orderapp.supplier.controller;

import com.orderapp.supplier.common.response.ApiResponse;
import com.orderapp.supplier.entity.*;
import com.orderapp.supplier.service.SupplierAppService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/supplier")
public class SupplierController {

    private final SupplierAppService supplierAppService;

    public SupplierController(SupplierAppService supplierAppService) {
        this.supplierAppService = supplierAppService;
    }

    @PostMapping("/auth/login")
    public ApiResponse<Map<String, Object>> login(@RequestBody Map<String, String> body) {
        return ApiResponse.ok(supplierAppService.login(body.get("mobile"), body.get("code")), "登录成功");
    }

    @GetMapping("/dashboard/overview")
    public ApiResponse<Map<String, Object>> dashboardOverview() {
        return ApiResponse.ok(supplierAppService.dashboardOverview(), "获取工作台成功");
    }

    @GetMapping("/wallet/overview")
    public ApiResponse<Map<String, Object>> walletOverview(@RequestParam(required = false) String month) {
        return ApiResponse.ok(supplierAppService.walletOverview(month), "获取钱包成功");
    }

    @GetMapping("/wallet/transactions")
    public ApiResponse<Map<String, Object>> walletTransactions(@RequestParam(defaultValue = "1") Integer page,
                                                               @RequestParam(name = "page_size", defaultValue = "10") Integer pageSize,
                                                               @RequestParam(defaultValue = "all") String type) {
        return ApiResponse.ok(supplierAppService.walletTransactions(page, pageSize, type), "获取收入明细成功");
    }

    @GetMapping("/products")
    public ApiResponse<Map<String, Object>> products(@RequestParam(defaultValue = "1") Integer page,
                                                     @RequestParam(name = "page_size", defaultValue = "10") Integer pageSize,
                                                     @RequestParam(defaultValue = "all") String status,
                                                     @RequestParam(required = false) String keyword) {
        return ApiResponse.ok(supplierAppService.products(page, pageSize, status, keyword), "获取商品列表成功");
    }

    @PostMapping("/products")
    public ApiResponse<Map<String, Object>> createProduct(@RequestBody SupplierProduct product) {
        return ApiResponse.ok(supplierAppService.createProduct(product), "商品创建成功");
    }

    @PostMapping("/quotes")
    public ApiResponse<Map<String, Object>> submitQuote(@RequestBody SupplierQuote quote) {
        return ApiResponse.ok(supplierAppService.submitQuote(quote), "报价已提交审核");
    }

    @GetMapping("/fulfillments")
    public ApiResponse<Map<String, Object>> fulfillments(@RequestParam(required = false) String date,
                                                         @RequestParam(name = "group_by", required = false, defaultValue = "area") String groupBy,
                                                         @RequestParam(name = "warehouse_id", required = false) Long warehouseId,
                                                         @RequestParam(required = false, defaultValue = "pending") String status) {
        return ApiResponse.ok(supplierAppService.fulfillments(date, groupBy, warehouseId, status), "获取配送单成功");
    }

    @PostMapping("/fulfillments/{deliveryId}/ship")
    public ApiResponse<Map<String, Object>> shipFulfillment(@PathVariable Long deliveryId, @RequestBody SupplierFulfillment fulfillment) {
        return ApiResponse.ok(supplierAppService.shipFulfillment(deliveryId, fulfillment), "确认发货成功");
    }

    @GetMapping("/installations")
    public ApiResponse<Map<String, Object>> installations(@RequestParam(required = false) String date,
                                                           @RequestParam(required = false, defaultValue = "pending") String status) {
        return ApiResponse.ok(supplierAppService.installations(date, status), "获取安装订单成功");
    }

    @PostMapping("/installations/{installationId}/complete")
    public ApiResponse<Map<String, Object>> completeInstallation(@PathVariable Long installationId, @RequestBody SupplierInstallation installation) {
        return ApiResponse.ok(supplierAppService.completeInstallation(installationId, installation), "安装完成已提交");
    }

    @GetMapping("/aftersales")
    public ApiResponse<Map<String, Object>> aftersales(@RequestParam(defaultValue = "1") Integer page,
                                                       @RequestParam(name = "page_size", defaultValue = "10") Integer pageSize,
                                                       @RequestParam(defaultValue = "all") String type,
                                                       @RequestParam(defaultValue = "pending") String status) {
        return ApiResponse.ok(supplierAppService.aftersales(page, pageSize, type, status), "获取售后工单成功");
    }

    @PostMapping("/aftersales/{caseId}/handle")
    public ApiResponse<Map<String, Object>> handleAftersale(@PathVariable Long caseId, @RequestBody SupplierAftersaleCase aftersaleCase) {
        return ApiResponse.ok(supplierAppService.handleAftersale(caseId, aftersaleCase), "售后方案已提交审核");
    }

    @GetMapping("/reviews")
    public ApiResponse<Map<String, Object>> reviews(@RequestParam(defaultValue = "all") String type,
                                                    @RequestParam(defaultValue = "all") String status,
                                                    @RequestParam(defaultValue = "1") Integer page,
                                                    @RequestParam(name = "page_size", defaultValue = "10") Integer pageSize) {
        return ApiResponse.ok(supplierAppService.reviews(type, status, page, pageSize), "获取审核列表成功");
    }

    @GetMapping("/analytics/overview")
    public ApiResponse<Map<String, Object>> analyticsOverview(@RequestParam(required = false, defaultValue = "30d") String range) {
        return ApiResponse.ok(supplierAppService.analyticsOverview(range), "获取经营数据成功");
    }
}
