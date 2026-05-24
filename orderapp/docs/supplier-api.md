# 家具供应商 H5 API 接口文档

## 通用约定

**基础路径：** `/api/supplier`  
**认证方式：** `Authorization: Bearer <token>`

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

## 1. 供应商登录

**接口名称：** 供应商登录  
**功能描述：** 手机号验证码登录。  
**接口地址：** `/api/supplier/auth/login`  
**请求方式：** `POST`

请求参数：

```json
{ "mobile": "13800000000", "code": "123456" }
```

响应参数：

```json
{ "error": 0, "body": { "token": "mock-token", "supplier_id": 10001, "supplier_name": "林木家具" }, "message": "登录成功", "success": true }
```

| 参数名 | 类型 | 必填 | 说明 | 示例值 |
|---|---|---|---|---|
| mobile | string | 是 | 手机号 | 13800000000 |
| code | string | 是 | 验证码 | 123456 |
| body.token | string | 是 | 访问令牌 | mock-token |
| body.supplier_id | int | 是 | 供应商 ID | 10001 |
| body.supplier_name | string | 是 | 供应商名称 | 林木家具 |

## 2. 工作台概览

**接口名称：** 获取工作台概览  
**功能描述：** 获取待办、履约和经营摘要。  
**接口地址：** `/api/supplier/dashboard/overview`  
**请求方式：** `GET`

请求参数：无

响应参数：

```json
{ "error": 0, "body": { "todo_count": 18, "pending_shipments": 12, "pending_installations": 3, "sales_amount": 86420, "fulfillment_rate": 96.8, "alerts": [] }, "message": "获取工作台成功", "success": true }
```

| 参数名 | 类型 | 必填 | 说明 | 示例值 |
|---|---|---|---|---|
| body.todo_count | int | 是 | 待办总数 | 18 |
| body.pending_shipments | int | 是 | 待发货数 | 12 |
| body.pending_installations | int | 是 | 待安装数 | 3 |
| body.sales_amount | number | 是 | 销售金额 | 86420 |
| body.fulfillment_rate | number | 是 | 履约及时率 | 96.8 |
| body.alerts | array | 是 | 待办提醒 | [] |

## 3. 钱包概览

**接口名称：** 获取钱包概览  
**功能描述：** 获取收入、可提现、结算拆分。  
**接口地址：** `/api/supplier/wallet/overview`  
**请求方式：** `GET`

请求参数：

```json
{ "month": "2024-05" }
```

响应参数：

```json
{ "error": 0, "body": { "total_income": 128560.8, "withdrawable_amount": 92340, "frozen_amount": 8216, "order_amount": 119800, "delivery_fee": 6240, "installation_fee": 4860, "refund_deduction": 1120, "aftersale_deduction": 1220 }, "message": "获取钱包成功", "success": true }
```

| 参数名 | 类型 | 必填 | 说明 | 示例值 |
|---|---|---|---|---|
| month | string | 否 | 结算月份 | 2024-05 |
| body.total_income | number | 是 | 收入总额 | 128560.8 |
| body.withdrawable_amount | number | 是 | 可提现金额 | 92340 |
| body.frozen_amount | number | 是 | 冻结金额 | 8216 |
| body.order_amount | number | 是 | 订单金额 | 119800 |
| body.delivery_fee | number | 是 | 配送费 | 6240 |
| body.installation_fee | number | 是 | 安装费 | 4860 |
| body.refund_deduction | number | 是 | 退款扣减 | 1120 |
| body.aftersale_deduction | number | 是 | 售后扣款 | 1220 |

## 4. 收入明细列表

**接口名称：** 获取收入明细列表  
**功能描述：** 查询订单收入、退款扣减、售后扣款流水。  
**接口地址：** `/api/supplier/wallet/transactions`  
**请求方式：** `GET`

请求参数：

```json
{ "page": 1, "page_size": 10, "type": "all" }
```

响应参数：

```json
{ "error": 0, "body": { "list": [{ "id": 1, "title": "订单 SO20240518023", "amount": 8680, "direction": "income", "created_at": "2024-05-18" }], "total": 1 }, "message": "获取收入明细成功", "success": true }
```

## 5. 商品列表

**接口名称：** 获取商品列表  
**功能描述：** 查询家具商品档案。  
**接口地址：** `/api/supplier/products`  
**请求方式：** `GET`

请求参数：

```json
{ "page": 1, "page_size": 10, "status": "all", "keyword": "沙发" }
```

响应参数：

```json
{ "error": 0, "body": { "list": [{ "id": 101, "name": "北欧云朵模块沙发", "category": "沙发", "material": "棉麻", "color": "燕麦白", "size": "280×96×78cm", "model": "SF-ND-280", "price": 6899, "stock": 126, "status": "active", "support_installation": true }], "total": 1 }, "message": "获取商品列表成功", "success": true }
```

## 6. 创建商品档案

**接口名称：** 创建商品档案  
**功能描述：** 创建商品基础信息、库存周期、包装和安装服务；创建成功后商品直接生效并展示在商品列表中，不再进入平台审核流程。  
**接口地址：** `/api/supplier/products`  
**请求方式：** `POST`

请求参数：

```json
{ "name": "北欧云朵模块沙发", "images": [], "category_id": 12, "description": "高回弹海绵", "material": "棉麻", "color": "燕麦白", "size": "280×96×78cm", "model": "SF-ND-280", "style": "现代简约", "price": 6899, "stock": 126, "spot_stock": 126, "presale_cycle_days": 15, "custom_cycle_days": 45, "min_order_quantity": 2, "delivery_areas": ["华东"], "package_info": "三层纸箱", "support_installation": true }
```

响应参数：

```json
{ "error": 0, "body": { "product_id": 101, "status": "active", "review_status": "approved" }, "message": "商品创建成功", "success": true }
```

## 7. 提交报价或改价

**接口名称：** 提交报价或改价  
**功能描述：** 商品报价或价格调整，提交平台审核。  
**接口地址：** `/api/supplier/quotes`  
**请求方式：** `POST`

请求参数：

```json
{ "product_id": 101, "current_price": 4299, "new_price": 4099, "reason": "促销调价", "attachments": [] }
```

响应参数：

```json
{ "error": 0, "body": { "quote_id": 9001, "review_status": "pending" }, "message": "报价已提交审核", "success": true }
```

## 8. 配送单列表

**接口名称：** 获取配送单列表  
**功能描述：** 按日期、仓库、区域或订单查询发货配送任务。  
**接口地址：** `/api/supplier/fulfillments`  
**请求方式：** `GET`

请求参数：

```json
{ "date": "2024-05-18", "group_by": "area", "warehouse_id": 1, "status": "pending" }
```

响应参数：

```json
{ "error": 0, "body": { "summary": { "order_count": 18, "item_count": 42, "total_volume": 86.4, "total_weight": 2180 }, "list": [{ "delivery_id": 7001, "order_no": "SO20240518023", "area": "西湖区", "warehouse": "仓库 A", "customer_address": "杭州市西湖区", "appointment_time": "10:00-12:00", "quantity": 3, "volume": 8.6, "weight": 260, "status": "pending" }] }, "message": "获取配送单成功", "success": true }
```

## 9. 确认发货

**接口名称：** 确认发货  
**功能描述：** 确认配送单出库并提交配送安排。  
**接口地址：** `/api/supplier/fulfillments/{delivery_id}/ship`  
**请求方式：** `POST`

请求参数：

```json
{ "delivery_id": 7001, "driver_name": "张师傅", "driver_mobile": "13900000000", "remark": "已安排配送" }
```

响应参数：

```json
{ "error": 0, "body": { "delivery_id": 7001, "status": "shipped" }, "message": "确认发货成功", "success": true }
```

## 10. 安装订单列表

**接口名称：** 获取安装订单列表  
**功能描述：** 查询待安装订单、师傅、预约时间、状态和照片。  
**接口地址：** `/api/supplier/installations`  
**请求方式：** `GET`

请求参数：

```json
{ "date": "2024-05-18", "status": "pending" }
```

响应参数：

```json
{ "error": 0, "body": { "list": [{ "installation_id": 8001, "order_no": "SO20240518023", "installer_name": "周师傅", "appointment_time": "16:30-18:00", "status": "in_progress", "photos": [] }] }, "message": "获取安装订单成功", "success": true }
```

## 11. 提交安装完成

**接口名称：** 提交安装完成  
**功能描述：** 上传完工照片并更新安装状态。  
**接口地址：** `/api/supplier/installations/{installation_id}/complete`  
**请求方式：** `POST`

请求参数：

```json
{ "installation_id": 8001, "status": "completed", "photos": [], "remark": "客户已确认" }
```

响应参数：

```json
{ "error": 0, "body": { "installation_id": 8001, "status": "completed" }, "message": "安装完成已提交", "success": true }
```

## 12. 售后工单列表

**接口名称：** 获取售后工单列表  
**功能描述：** 查询破损、少件、错发、质量、安装异常、退换货工单。  
**接口地址：** `/api/supplier/aftersales`  
**请求方式：** `GET`

请求参数：

```json
{ "page": 1, "page_size": 10, "type": "all", "status": "pending" }
```

响应参数：

```json
{ "error": 0, "body": { "list": [{ "case_id": 6001, "case_no": "AS24051809", "type": "运输破损", "description": "岩板边角破损", "status": "pending", "remaining_hours": 2 }], "total": 1 }, "message": "获取售后工单成功", "success": true }
```

## 13. 处理售后工单

**接口名称：** 处理售后工单  
**功能描述：** 提交处理意见、方案和凭证。  
**接口地址：** `/api/supplier/aftersales/{case_id}/handle`  
**请求方式：** `POST`

请求参数：

```json
{ "case_id": 6001, "opinion": "确认属实", "solution": "redispatch", "attachments": [], "remark": "安排重新配送" }
```

响应参数：

```json
{ "error": 0, "body": { "case_id": 6001, "review_status": "pending" }, "message": "售后方案已提交审核", "success": true }
```

## 14. 审核列表

**接口名称：** 获取审核列表  
**功能描述：** 查询商品、报价、售后审核状态和驳回原因。  
**接口地址：** `/api/supplier/reviews`  
**请求方式：** `GET`

请求参数：

```json
{ "type": "all", "status": "all", "page": 1, "page_size": 10 }
```

响应参数：

```json
{ "error": 0, "body": { "list": [{ "review_id": 5001, "type": "product", "title": "商品审核", "status": "rejected", "reject_reason": "缺少包装尺寸" }], "stats": { "pending": 8, "rejected": 3, "approved": 42 } }, "message": "获取审核列表成功", "success": true }
```

## 15. 经营数据

**接口名称：** 获取经营数据  
**功能描述：** 查询浏览量、成交订单、销售金额、退货率、售后率和履约及时率。  
**接口地址：** `/api/supplier/analytics/overview`  
**请求方式：** `GET`

请求参数：

```json
{ "range": "30d" }
```

响应参数：

```json
{ "error": 0, "body": { "product_views": 48620, "order_count": 386, "sales_amount": 486920, "return_rate": 2.8, "aftersale_rate": 4.6, "fulfillment_rate": 96.8, "trend": [4.8, 6.6, 5.4], "top_products": [{ "product_id": 101, "name": "云朵模块沙发", "views": 9820, "orders": 86, "sales_amount": 592000 }] }, "message": "获取经营数据成功", "success": true }
```
