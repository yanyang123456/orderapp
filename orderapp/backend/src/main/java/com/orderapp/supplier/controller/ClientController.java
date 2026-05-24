package com.orderapp.supplier.controller;

import com.orderapp.supplier.common.response.ApiResponse;
import com.orderapp.supplier.service.ClientAppService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/client")
public class ClientController {

    private final ClientAppService clientAppService;

    public ClientController(ClientAppService clientAppService) {
        this.clientAppService = clientAppService;
    }

    @GetMapping("/home")
    public ApiResponse<Map<String, Object>> home() {
        return ApiResponse.ok(clientAppService.home(), "获取首页成功");
    }

    @GetMapping("/products")
    public ApiResponse<Map<String, Object>> products(@RequestParam(defaultValue = "1") Integer page,
                                                     @RequestParam(name = "page_size", defaultValue = "10") Integer pageSize,
                                                     @RequestParam(required = false) String keyword,
                                                     @RequestParam(name = "category_id", required = false) Long categoryId,
                                                     @RequestParam(required = false) String category,
                                                     @RequestParam(name = "min_price", required = false) BigDecimal minPrice,
                                                     @RequestParam(name = "max_price", required = false) BigDecimal maxPrice,
                                                     @RequestParam(required = false) String material,
                                                     @RequestParam(required = false) String style,
                                                     @RequestParam(name = "support_installation", required = false) Boolean supportInstallation) {
        return ApiResponse.ok(clientAppService.products(page, pageSize, keyword, categoryId, category, minPrice, maxPrice, material, style, supportInstallation), "获取商品列表成功");
    }

    @GetMapping("/products/{productId}")
    public ApiResponse<Map<String, Object>> productDetail(@PathVariable Long productId) {
        return ApiResponse.ok(clientAppService.productDetail(productId), "获取商品详情成功");
    }

    @PostMapping("/cart/items")
    public ApiResponse<Map<String, Object>> addCartItem(@RequestBody Map<String, Object> body) {
        return ApiResponse.ok(clientAppService.addCartItem(body), "加入购物车成功");
    }

    @GetMapping("/cart/items")
    public ApiResponse<Map<String, Object>> cartItems() {
        return ApiResponse.ok(clientAppService.cartItems(), "获取购物车成功");
    }

    @PutMapping("/cart/items/{cartItemId}")
    public ApiResponse<Map<String, Object>> updateCartItem(@PathVariable Long cartItemId, @RequestBody Map<String, Object> body) {
        return ApiResponse.ok(clientAppService.updateCartItem(cartItemId, body), "更新购物车成功");
    }

    @PostMapping("/orders")
    public ApiResponse<Map<String, Object>> createOrder(@RequestBody Map<String, Object> body) {
        return ApiResponse.ok(clientAppService.createOrder(body), "订单创建成功");
    }

    @PostMapping("/orders/{orderId}/pay")
    public ApiResponse<Map<String, Object>> payOrder(@PathVariable Long orderId, @RequestBody Map<String, Object> body) {
        return ApiResponse.ok(clientAppService.payOrder(orderId, body), "支付成功");
    }

    @GetMapping("/orders")
    public ApiResponse<Map<String, Object>> orders(@RequestParam(defaultValue = "1") Integer page,
                                                   @RequestParam(name = "page_size", defaultValue = "10") Integer pageSize,
                                                   @RequestParam(defaultValue = "all") String status) {
        return ApiResponse.ok(clientAppService.orders(page, pageSize, status), "获取订单列表成功");
    }

    @GetMapping("/orders/{orderId}")
    public ApiResponse<Map<String, Object>> orderDetail(@PathVariable Long orderId) {
        return ApiResponse.ok(clientAppService.orderDetail(orderId), "获取订单详情成功");
    }

    @PostMapping("/orders/{orderId}/receive")
    public ApiResponse<Map<String, Object>> receiveOrder(@PathVariable Long orderId) {
        return ApiResponse.ok(clientAppService.receiveOrder(orderId), "确认收货成功");
    }

    @PostMapping("/aftersales")
    public ApiResponse<Map<String, Object>> createAftersale(@RequestBody Map<String, Object> body) {
        return ApiResponse.ok(clientAppService.createAftersale(body), "售后申请提交成功");
    }

    @GetMapping("/aftersales")
    public ApiResponse<Map<String, Object>> aftersales(@RequestParam(defaultValue = "1") Integer page,
                                                       @RequestParam(name = "page_size", defaultValue = "10") Integer pageSize,
                                                       @RequestParam(defaultValue = "all") String status) {
        return ApiResponse.ok(clientAppService.aftersales(page, pageSize, status), "获取售后列表成功");
    }

    @GetMapping("/profile/overview")
    public ApiResponse<Map<String, Object>> profileOverview() {
        return ApiResponse.ok(clientAppService.profileOverview(), "获取我的页面成功");
    }
}
