# 家具客户端 H5 API 接口文档

## 通用约定

**基础路径：** `/api/client`  
**认证方式：** `Authorization: Bearer <token>`  
**说明：** 客户端接口和供应商端接口可共用同一个后端服务，路径通过 `/api/client` 与 `/api/supplier` 区分。

通用响应：

```json
{ "error": 0, "body": {}, "message": "success", "success": true }
```

| 参数名 | 类型 | 必填 | 说明 | 示例值 |
|---|---|---|---|---|
| error | int | 是 | 错误码，0 成功 | 0 |
| body | object | 是 | 业务数据 | {} |
| message | string | 是 | 响应消息 | success |
| success | bool | 是 | 是否成功 | true |

## 1. 客户端首页

**接口名称：** 获取客户端首页  
**功能描述：** 获取搜索推荐、家具分类、活动专区、推荐商品、热销商品和订单提醒。  
**接口地址：** `/api/client/home`  
**请求方式：** `GET`

### 请求参数

无

### 响应参数

```json
{
  "error": 0,
  "body": {
    "banners": [{ "id": 1, "title": "春季焕新季", "image": "https://example.com/banner.jpg", "link_type": "activity", "link_id": 1 }],
    "categories": [{ "id": 12, "name": "沙发", "icon": "sofa" }],
    "recommend_products": [{ "id": 101, "name": "北欧云朵模块沙发", "price": 6899, "image": "https://example.com/product.jpg", "category": "沙发", "stock": 126 }],
    "hot_products": [{ "id": 102, "name": "意式岩板餐桌", "price": 4299, "image": "https://example.com/product.jpg", "sales": 86 }],
    "order_reminders": { "pending_payment": 1, "pending_delivery": 2, "pending_installation": 1, "aftersale_processing": 1 }
  },
  "message": "获取首页成功",
  "success": true
}
```

## 2. 商品列表

**接口名称：** 获取商品列表  
**功能描述：** 查询可购买的家具商品，支持搜索、分类、价格、材质、尺寸、风格和安装服务筛选。  
**接口地址：** `/api/client/products`  
**请求方式：** `GET`

### 请求参数

```json
{ "page": 1, "page_size": 10, "keyword": "沙发", "category_id": 12, "min_price": 1000, "max_price": 8000, "material": "棉麻", "style": "现代简约", "support_installation": true }
```

| 参数名 | 类型 | 必填 | 说明 | 示例值 |
|---|---|---|---|---|
| page | int | 否 | 页码，默认 1 | 1 |
| page_size | int | 否 | 每页数量，默认 10 | 10 |
| keyword | string | 否 | 搜索关键词 | 沙发 |
| category_id | int | 否 | 分类 ID | 12 |
| min_price | number | 否 | 最低价 | 1000 |
| max_price | number | 否 | 最高价 | 8000 |
| material | string | 否 | 材质 | 棉麻 |
| style | string | 否 | 风格 | 现代简约 |
| support_installation | bool | 否 | 是否支持安装 | true |

### 响应参数

```json
{
  "error": 0,
  "body": {
    "list": [{ "id": 101, "name": "北欧云朵模块沙发", "image": "https://example.com/product.jpg", "category": "沙发", "material": "棉麻", "color": "燕麦白", "size": "280×96×78cm", "model": "SF-ND-280", "style": "现代简约", "price": 6899, "stock": 126, "support_installation": true }],
    "total": 1,
    "page": 1,
    "page_size": 10
  },
  "message": "获取商品列表成功",
  "success": true
}
```

## 3. 商品详情

**接口名称：** 获取商品详情  
**功能描述：** 获取商品基础信息、图片、库存周期、包装、安装和配送区域。  
**接口地址：** `/api/client/products/{product_id}`  
**请求方式：** `GET`

### 请求参数

| 参数名 | 类型 | 必填 | 说明 | 示例值 |
|---|---|---|---|---|
| product_id | int | 是 | 商品 ID | 101 |

### 响应参数

```json
{
  "error": 0,
  "body": {
    "id": 101,
    "name": "北欧云朵模块沙发",
    "images": ["https://example.com/product.jpg"],
    "description": "高回弹海绵坐垫",
    "category": "沙发",
    "material": "棉麻",
    "color": "燕麦白",
    "size": "280×96×78cm",
    "model": "SF-ND-280",
    "style": "现代简约",
    "price": 6899,
    "stock": 126,
    "spot_stock": 126,
    "presale_cycle_days": 15,
    "custom_cycle_days": 45,
    "min_order_quantity": 1,
    "delivery_areas": ["华东"],
    "package_info": "三层纸箱",
    "support_installation": true
  },
  "message": "获取商品详情成功",
  "success": true
}
```

## 4. 加入购物车

**接口名称：** 加入购物车  
**功能描述：** 将商品加入购物车。  
**接口地址：** `/api/client/cart/items`  
**请求方式：** `POST`

### 请求参数

```json
{ "product_id": 101, "quantity": 1, "support_installation": true }
```

### 响应参数

```json
{ "error": 0, "body": { "cart_item_id": 3001 }, "message": "加入购物车成功", "success": true }
```

## 5. ?????

**?????** ???????  
**?????** ????????????  
**?????** `/api/client/cart/items`  
**?????** `GET`

### ????

```json
{
  "error": 0,
  "body": {
    "list": [{ "cart_item_id": 3001, "product_id": 101, "name": "????????", "price": 6899, "quantity": 1, "image": "https://example.com/product.jpg", "support_installation": true }],
    "total_amount": 6899,
    "total_count": 1
  },
  "message": "???????",
  "success": true
}
```

## 5.1 ???????

**?????** ???????  
**?????** ??????????? `quantity <= 0` ?????????  
**?????** `/api/client/cart/items/{cart_item_id}`  
**?????** `PUT`

### ????

```json
{ "quantity": 2 }
```

### ????

```json
{ "error": 0, "body": { "cart_item_id": 3001, "quantity": 2, "removed": false }, "message": "???????", "success": true }
```

????

```json
{ "error": 0, "body": { "cart_item_id": 3001, "removed": true }, "message": "???????", "success": true }
```

## 6. 创建订单

**接口名称：** 创建订单  
**功能描述：** 客户提交订单，创建订单记录；后续后端可生成供应商配送单、安装单、钱包流水和经营数据。  
**接口地址：** `/api/client/orders`  
**请求方式：** `POST`

### 请求参数

```json
{
  "items": [{ "product_id": 101, "quantity": 1, "support_installation": true }],
  "receiver_name": "张三",
  "receiver_mobile": "13800000000",
  "receiver_address": "杭州市西湖区文三路 18 号",
  "delivery_method": "home_delivery",
  "appointment_date": "2024-05-18",
  "appointment_time": "10:00-12:00",
  "remark": "请提前电话联系"
}
```

### 响应参数

```json
{ "error": 0, "body": { "order_id": 4001, "order_no": "CO202405180001", "pay_amount": 6899, "status": "pending_payment" }, "message": "订单创建成功", "success": true }
```

## 7. 支付订单

**接口名称：** 支付订单  
**功能描述：** 模拟或提交订单支付，支付成功后订单进入待发货。  
**接口地址：** `/api/client/orders/{order_id}/pay`  
**请求方式：** `POST`

### 请求参数

```json
{ "pay_channel": "wechat" }
```

### 响应参数

```json
{ "error": 0, "body": { "order_id": 4001, "status": "pending_delivery" }, "message": "支付成功", "success": true }
```

## 8. 订单列表

**接口名称：** 获取订单列表  
**功能描述：** 查询客户订单，支持按状态筛选。  
**接口地址：** `/api/client/orders`  
**请求方式：** `GET`

### 请求参数

```json
{ "page": 1, "page_size": 10, "status": "all" }
```

### 响应参数

```json
{
  "error": 0,
  "body": {
    "list": [{ "order_id": 4001, "order_no": "CO202405180001", "status": "pending_delivery", "pay_amount": 6899, "product_summary": "北欧云朵模块沙发 ×1", "appointment_time": "2024-05-18 10:00-12:00" }],
    "total": 1
  },
  "message": "获取订单列表成功",
  "success": true
}
```

## 9. 订单详情

**接口名称：** 获取订单详情  
**功能描述：** 查看订单商品、支付、配送、安装和收货地址信息。  
**接口地址：** `/api/client/orders/{order_id}`  
**请求方式：** `GET`

### 响应参数

```json
{
  "error": 0,
  "body": {
    "order_id": 4001,
    "order_no": "CO202405180001",
    "status": "shipping",
    "pay_amount": 6899,
    "receiver_address": "杭州市西湖区文三路 18 号",
    "items": [{ "product_id": 101, "name": "北欧云朵模块沙发", "price": 6899, "quantity": 1 }],
    "delivery": { "status": "shipped", "driver_name": "张师傅", "driver_mobile": "13900000000", "shipped_at": "2024-05-18 08:20:00" },
    "installation": { "status": "pending", "installer_name": "周师傅", "appointment_time": "2024-05-18 16:30-18:00" }
  },
  "message": "获取订单详情成功",
  "success": true
}
```

## 10. 确认收货

**接口名称：** 确认收货  
**功能描述：** 客户确认配送完成。  
**接口地址：** `/api/client/orders/{order_id}/receive`  
**请求方式：** `POST`

### 响应参数

```json
{ "error": 0, "body": { "order_id": 4001, "status": "received" }, "message": "确认收货成功", "success": true }
```

## 11. 提交售后申请

**接口名称：** 提交售后申请  
**功能描述：** 客户发起运输破损、少件、错发、质量问题、安装异常、退换货等售后申请。  
**接口地址：** `/api/client/aftersales`  
**请求方式：** `POST`

### 请求参数

```json
{ "order_id": 4001, "type": "运输破损", "description": "岩板餐桌边角破损", "solution": "redispatch", "photos": ["https://example.com/photo.jpg"] }
```

### 响应参数

```json
{ "error": 0, "body": { "case_id": 6001, "case_no": "AS24051809", "status": "pending" }, "message": "售后申请提交成功", "success": true }
```

## 12. 售后列表

**接口名称：** 获取售后列表  
**功能描述：** 查看客户售后申请和处理进度。  
**接口地址：** `/api/client/aftersales`  
**请求方式：** `GET`

### 请求参数

```json
{ "page": 1, "page_size": 10, "status": "all" }
```

### 响应参数

```json
{ "error": 0, "body": { "list": [{ "case_id": 6001, "case_no": "AS24051809", "type": "运输破损", "description": "岩板餐桌边角破损", "status": "pending", "solution": "redispatch" }], "total": 1 }, "message": "获取售后列表成功", "success": true }
```

## 13. 我的页面概览

**接口名称：** 获取我的页面概览  
**功能描述：** 获取客户资料、订单数量、售后数量、收藏、浏览历史、优惠券等概览数据。  
**接口地址：** `/api/client/profile/overview`  
**请求方式：** `GET`

### 响应参数

```json
{
  "error": 0,
  "body": {
    "user": { "user_id": 1, "nickname": "张三", "mobile": "13800000000", "level": "普通会员", "points": 1280 },
    "stats": { "orders": 12, "aftersales": 3, "favorites": 8, "history": 24, "coupons": 3 }
  },
  "message": "获取我的页面成功",
  "success": true
}
```
