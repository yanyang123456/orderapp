package com.orderapp.supplier.service;

import java.math.BigDecimal;
import java.util.Map;

public interface ClientAppService {

    Map<String, Object> home();

    Map<String, Object> products(Integer page,
                                 Integer pageSize,
                                 String keyword,
                                 Long categoryId,
                                 String category,
                                 BigDecimal minPrice,
                                 BigDecimal maxPrice,
                                 String material,
                                 String style,
                                 Boolean supportInstallation);

    Map<String, Object> productDetail(Long productId);

    Map<String, Object> addCartItem(Map<String, Object> body);

    Map<String, Object> cartItems();

    Map<String, Object> updateCartItem(Long cartItemId, Map<String, Object> body);

    Map<String, Object> createOrder(Map<String, Object> body);

    Map<String, Object> payOrder(Long orderId, Map<String, Object> body);

    Map<String, Object> orders(Integer page, Integer pageSize, String status);

    Map<String, Object> orderDetail(Long orderId);

    Map<String, Object> receiveOrder(Long orderId);

    Map<String, Object> createAftersale(Map<String, Object> body);

    Map<String, Object> aftersales(Integer page, Integer pageSize, String status);

    Map<String, Object> profileOverview();
}
