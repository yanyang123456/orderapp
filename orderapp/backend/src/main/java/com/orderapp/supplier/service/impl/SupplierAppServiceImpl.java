package com.orderapp.supplier.service.impl;

import com.orderapp.supplier.entity.*;
import com.orderapp.supplier.mapper.*;
import com.orderapp.supplier.service.SupplierAppService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class SupplierAppServiceImpl implements SupplierAppService {
    private static final Long SID = 10001L;
    private final SupplierMapper supplierMapper;
    private final SupplierAuthCodeMapper authCodeMapper;
    private final SupplierDashboardAlertMapper alertMapper;
    private final SupplierWalletSettlementMapper settlementMapper;
    private final SupplierWalletTransactionMapper transactionMapper;
    private final SupplierProductMapper productMapper;
    private final SupplierQuoteMapper quoteMapper;
    private final SupplierFulfillmentMapper fulfillmentMapper;
    private final SupplierInstallationMapper installationMapper;
    private final SupplierAftersaleCaseMapper aftersaleMapper;
    private final SupplierReviewMapper reviewMapper;
    private final SupplierAnalyticsDailyMapper analyticsMapper;
    private final SupplierProductMetricMapper metricMapper;
    private final ClientOrderMapper clientOrderMapper;

    public SupplierAppServiceImpl(SupplierMapper supplierMapper, SupplierAuthCodeMapper authCodeMapper, SupplierDashboardAlertMapper alertMapper, SupplierWalletSettlementMapper settlementMapper, SupplierWalletTransactionMapper transactionMapper, SupplierProductMapper productMapper, SupplierQuoteMapper quoteMapper, SupplierFulfillmentMapper fulfillmentMapper, SupplierInstallationMapper installationMapper, SupplierAftersaleCaseMapper aftersaleMapper, SupplierReviewMapper reviewMapper, SupplierAnalyticsDailyMapper analyticsMapper, SupplierProductMetricMapper metricMapper, ClientOrderMapper clientOrderMapper) {
        this.supplierMapper = supplierMapper;
        this.authCodeMapper = authCodeMapper;
        this.alertMapper = alertMapper;
        this.settlementMapper = settlementMapper;
        this.transactionMapper = transactionMapper;
        this.productMapper = productMapper;
        this.quoteMapper = quoteMapper;
        this.fulfillmentMapper = fulfillmentMapper;
        this.installationMapper = installationMapper;
        this.aftersaleMapper = aftersaleMapper;
        this.reviewMapper = reviewMapper;
        this.analyticsMapper = analyticsMapper;
        this.metricMapper = metricMapper;
        this.clientOrderMapper = clientOrderMapper;
    }

    public Map<String, Object> login(String mobile, String code) {
        Supplier supplierCondition = new Supplier();
        supplierCondition.setContactMobile(mobile);
        List<Supplier> suppliers = supplierMapper.selectByCondition(supplierCondition);
        if (suppliers.isEmpty()) throw new IllegalArgumentException("供应商不存在");
        SupplierAuthCode codeCondition = new SupplierAuthCode();
        codeCondition.setMobile(mobile);
        codeCondition.setCode(code);
        if (authCodeMapper.selectByCondition(codeCondition).isEmpty()) throw new IllegalArgumentException("验证码错误");
        Supplier supplier = suppliers.get(0);
        return m("token", Optional.ofNullable(supplier.getToken()).orElse("mock-token"), "supplier_id", supplier.getId(), "supplier_name", supplier.getSupplierName(), "contact_mobile", supplier.getContactMobile());
    }

    public Map<String, Object> dashboardOverview() {
        List<SupplierDashboardAlert> alerts = alertMapper.selectByCondition(withSupplier(new SupplierDashboardAlert()));
        SupplierFulfillment f = withSupplier(new SupplierFulfillment()); f.setStatus("pending");
        List<SupplierFulfillment> fs = fulfillmentMapper.selectByCondition(f);
        List<SupplierInstallation> is = List.of();
        SupplierAnalyticsDaily ad = withSupplier(new SupplierAnalyticsDaily());
        List<SupplierAnalyticsDaily> ads = analyticsMapper.selectByCondition(ad);
        SupplierAnalyticsDaily latest = ads.isEmpty() ? null : ads.get(0);
        return m("todo_count", alerts.size() + fs.size(), "pending_shipments", fs.size(), "pending_installations", 0, "sales_amount", latest == null ? BigDecimal.ZERO : latest.getSalesAmount(), "fulfillment_rate", latest == null ? BigDecimal.ZERO : latest.getFulfillmentRate(), "alerts", alerts);
    }

    public Map<String, Object> walletOverview(String month) {
        SupplierWalletSettlement c = withSupplier(new SupplierWalletSettlement());
        if (notBlank(month)) c.setSettlementMonth(month);
        List<SupplierWalletSettlement> list = settlementMapper.selectByCondition(c);
        return m("settlement", list.isEmpty() ? null : list.get(0));
    }

    public Map<String, Object> walletTransactions(Integer page, Integer pageSize, String type) {
        SupplierWalletTransaction c = withSupplier(new SupplierWalletTransaction());
        if (notBlank(type) && !"all".equals(type)) c.setDirection(type);
        return page(transactionMapper.selectByCondition(c), page, pageSize);
    }

    public Map<String, Object> products(Integer page, Integer pageSize, String status, String keyword) {
        SupplierProduct c = withSupplier(new SupplierProduct());
        if (notBlank(status) && !"all".equals(status)) c.setStatus(status);
        if (notBlank(keyword)) c.setName(keyword);
        return page(productMapper.selectByCondition(c), page, pageSize);
    }

    @Transactional
    public Map<String, Object> createProduct(SupplierProduct product) {
        product.setSupplierId(SID);
        product.setStatus("active");
        product.setReviewStatus("approved");
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        if (!notBlank(product.getCategory())) product.setCategory("未分类");
        if (product.getPrice() == null) product.setPrice(BigDecimal.ZERO);
        if (product.getStock() == null) product.setStock(0);
        if (product.getSpotStock() == null) product.setSpotStock(product.getStock());
        if (product.getSupportInstallation() == null) product.setSupportInstallation(false);
        productMapper.insert(product);
        return m("product_id", product.getId(), "status", "active", "review_status", "approved");
    }

    @Transactional
    public Map<String, Object> submitQuote(SupplierQuote quote) {
        quote.setSupplierId(SID); quote.setReviewStatus("pending"); quote.setCreatedAt(LocalDateTime.now()); quote.setUpdatedAt(LocalDateTime.now());
        quoteMapper.insert(quote);
        createReview("quote", quote.getId(), "报价审核 · 商品" + quote.getProductId());
        return m("quote_id", quote.getId(), "review_status", "pending");
    }

    public Map<String, Object> fulfillments(String date, String groupBy, Long warehouseId, String status) {
        SupplierFulfillment c = withSupplier(new SupplierFulfillment());
        if (notBlank(date)) c.setAppointmentDate(LocalDate.parse(date));
        if (warehouseId != null) c.setWarehouseId(warehouseId);
        if (notBlank(status)) c.setStatus(status);
        List<SupplierFulfillment> list = fulfillmentMapper.selectByCondition(c);
        int qty = list.stream().mapToInt(x -> Optional.ofNullable(x.getQuantity()).orElse(0)).sum();
        BigDecimal volume = list.stream().map(x -> Optional.ofNullable(x.getVolume()).orElse(BigDecimal.ZERO)).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal weight = list.stream().map(x -> Optional.ofNullable(x.getWeight()).orElse(BigDecimal.ZERO)).reduce(BigDecimal.ZERO, BigDecimal::add);
        return m("summary", m("order_count", list.size(), "item_count", qty, "total_volume", volume, "total_weight", weight), "list", list);
    }

    @Transactional
    public Map<String, Object> shipFulfillment(Long deliveryId, SupplierFulfillment fulfillment) {
        SupplierFulfillment current = fulfillmentMapper.selectById(deliveryId);
        if (current == null) throw new IllegalArgumentException("??????");
        fulfillment.setId(deliveryId); fulfillment.setStatus("shipped"); fulfillment.setShippedAt(LocalDateTime.now()); fulfillment.setUpdatedAt(LocalDateTime.now());
        fulfillmentMapper.updateById(fulfillment);
        ClientOrder order = clientOrderMapper.selectByOrderNo(current.getOrderNo());
        if (order != null) {
            order.setOrderStatus("shipping");
            order.setDeliveryStatus("shipping");
            order.setUpdatedAt(LocalDateTime.now());
            clientOrderMapper.updateById(order);
        }
        return m("delivery_id", deliveryId, "status", "shipped", "order_status", "shipping");
    }

    public Map<String, Object> installations(String date, String status) {
        SupplierInstallation c = withSupplier(new SupplierInstallation());
        if (notBlank(date)) c.setAppointmentDate(LocalDate.parse(date));
        if (notBlank(status)) c.setStatus(status);
        return m("list", installationMapper.selectByCondition(c));
    }

    @Transactional
    public Map<String, Object> completeInstallation(Long installationId, SupplierInstallation installation) {
        installation.setId(installationId); installation.setStatus("completed"); installation.setCompletedAt(LocalDateTime.now()); installation.setUpdatedAt(LocalDateTime.now());
        installationMapper.updateById(installation);
        return m("installation_id", installationId, "status", "completed");
    }

    public Map<String, Object> aftersales(Integer page, Integer pageSize, String type, String status) {
        SupplierAftersaleCase c = withSupplier(new SupplierAftersaleCase());
        if (notBlank(type) && !"all".equals(type)) c.setType(type);
        if (notBlank(status)) c.setStatus(status);
        return page(aftersaleMapper.selectByCondition(c), page, pageSize);
    }

    @Transactional
    public Map<String, Object> handleAftersale(Long caseId, SupplierAftersaleCase aftersaleCase) {
        aftersaleCase.setId(caseId); aftersaleCase.setStatus("reviewing"); aftersaleCase.setReviewStatus("pending"); aftersaleCase.setUpdatedAt(LocalDateTime.now());
        aftersaleMapper.updateById(aftersaleCase);
        createReview("aftersale", caseId, "售后方案审核 · " + caseId);
        return m("case_id", caseId, "review_status", "pending");
    }

    public Map<String, Object> reviews(String type, String status, Integer page, Integer pageSize) {
        SupplierReview c = withSupplier(new SupplierReview());
        if (notBlank(type) && !"all".equals(type)) c.setReviewType(type);
        if (notBlank(status) && !"all".equals(status)) c.setStatus(status);
        List<SupplierReview> list = reviewMapper.selectByCondition(c);
        return m("list", slice(list, page, pageSize), "total", list.size(), "stats", m("pending", reviewCount("pending"), "rejected", reviewCount("rejected"), "approved", reviewCount("approved")));
    }

    public Map<String, Object> analyticsOverview(String range) {
        SupplierAnalyticsDaily c = withSupplier(new SupplierAnalyticsDaily());
        List<SupplierAnalyticsDaily> list = analyticsMapper.selectByCondition(c);
        SupplierAnalyticsDaily latest = list.isEmpty() ? null : list.get(0);
        BigDecimal sales = list.stream().map(x -> Optional.ofNullable(x.getSalesAmount()).orElse(BigDecimal.ZERO)).reduce(BigDecimal.ZERO, BigDecimal::add);
        int views = list.stream().mapToInt(x -> Optional.ofNullable(x.getProductViews()).orElse(0)).sum();
        int orders = list.stream().mapToInt(x -> Optional.ofNullable(x.getOrderCount()).orElse(0)).sum();
        SupplierProductMetric mc = withSupplier(new SupplierProductMetric());
        return m("product_views", views, "order_count", orders, "sales_amount", sales, "return_rate", latest == null ? BigDecimal.ZERO : latest.getReturnRate(), "aftersale_rate", latest == null ? BigDecimal.ZERO : latest.getAftersaleRate(), "fulfillment_rate", latest == null ? BigDecimal.ZERO : latest.getFulfillmentRate(), "trend", list.stream().map(SupplierAnalyticsDaily::getSalesAmount).toList(), "top_products", metricMapper.selectByCondition(mc));
    }

    private void createReview(String type, Long bizId, String title) { SupplierReview r = new SupplierReview(); r.setSupplierId(SID); r.setReviewType(type); r.setBizId(bizId); r.setTitle(title); r.setStatus("pending"); r.setCreatedAt(LocalDateTime.now()); r.setUpdatedAt(LocalDateTime.now()); reviewMapper.insert(r); }
    private int reviewCount(String status) { SupplierReview c = withSupplier(new SupplierReview()); c.setStatus(status); return reviewMapper.selectByCondition(c).size(); }
    private Map<String, Object> page(List<?> list, Integer page, Integer pageSize) { return m("list", slice(list, page, pageSize), "total", list.size(), "page", p(page), "page_size", ps(pageSize)); }
    private List<?> slice(List<?> list, Integer page, Integer pageSize) { int from = Math.min((p(page) - 1) * ps(pageSize), list.size()); int to = Math.min(from + ps(pageSize), list.size()); return list.subList(from, to); }
    private int p(Integer page) { return page == null || page < 1 ? 1 : page; }
    private int ps(Integer pageSize) { return pageSize == null || pageSize < 1 ? 10 : pageSize; }
    private boolean notBlank(String value) { return value != null && !value.isBlank(); }
    private Map<String, Object> m(Object... args) { Map<String, Object> map = new LinkedHashMap<>(); for (int i = 0; i < args.length; i += 2) map.put(String.valueOf(args[i]), args[i + 1]); return map; }
    private <T> T withSupplier(T entity) { try { entity.getClass().getMethod("setSupplierId", Long.class).invoke(entity, SID); } catch (ReflectiveOperationException ignored) { } return entity; }
}
