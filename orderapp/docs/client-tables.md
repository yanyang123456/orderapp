# 客户端数据库表设计

## 设计目标

客户端数据库用于支撑家具商城 H5 的客户账户、地址、购物车、订单、订单明细、支付、售后、收藏和浏览历史等功能。

由于客户端和供应商端共用同一个后端服务，客户端表必须和现有供应商表保持业务一致性。核心一致性规则如下：

1. 客户端商品只读取供应商商品表 `supplier_products`。
2. 客户端订单明细必须关联 `supplier_products.id` 和 `supplier_products.supplier_id`。
3. 客户端订单支付成功后，需要生成供应商履约单 `supplier_fulfillments`。
4. 如果订单需要安装，需要生成供应商安装单 `supplier_installations`。
5. 客户端售后申请需要同时写入 `client_aftersales` 和供应商售后工单 `supplier_aftersale_cases`。
6. 订单完成或支付后，需要生成供应商钱包流水 `supplier_wallet_transactions`。
7. 订单、售后、浏览等行为需要更新供应商经营数据 `supplier_analytics_daily` 和 `supplier_product_metrics`。

## 表清单

| 表名 | 说明 | 是否关联供应商表 |
|---|---|---|
| client_users | 客户账户表 | 否 |
| client_auth_codes | 客户登录验证码 | 否 |
| client_addresses | 客户收货地址 | 否 |
| client_cart_items | 客户购物车 | 是，关联 supplier_products |
| client_orders | 客户订单主表 | 是，订单完成后联动供应商履约/钱包 |
| client_order_items | 客户订单明细 | 是，关联 supplier_products 和 suppliers |
| client_payments | 客户支付记录 | 是，关联 client_orders |
| client_aftersales | 客户售后申请 | 是，关联 client_orders、supplier_aftersale_cases |
| client_favorites | 客户收藏 | 是，关联 supplier_products |
| client_browse_history | 客户浏览历史 | 是，关联 supplier_products |

---

## 1. client_users 客户账户表

用于保存客户端用户基础信息。

| 字段 | 类型 | 必填 | 说明 |
|---|---|---|---|
| id | BIGINT | 是 | 主键 |
| user_no | VARCHAR(32) | 是 | 客户编号，唯一 |
| mobile | VARCHAR(20) | 是 | 手机号，唯一 |
| nickname | VARCHAR(50) | 否 | 昵称 |
| avatar | VARCHAR(255) | 否 | 头像 |
| gender | VARCHAR(20) | 否 | 性别 |
| level | VARCHAR(30) | 是 | 会员等级 |
| points | INT | 是 | 积分 |
| status | VARCHAR(20) | 是 | 状态：active、disabled、deleted |
| token | VARCHAR(255) | 否 | 登录 token |
| created_at | DATETIME | 是 | 创建时间 |
| updated_at | DATETIME | 是 | 更新时间 |

---

## 2. client_auth_codes 客户登录验证码表

用于客户端手机号验证码登录。

| 字段 | 类型 | 必填 | 说明 |
|---|---|---|---|
| id | BIGINT | 是 | 主键 |
| mobile | VARCHAR(20) | 是 | 手机号 |
| code | VARCHAR(10) | 是 | 验证码 |
| expired_at | DATETIME | 是 | 过期时间 |
| used | TINYINT | 是 | 是否已使用 |
| created_at | DATETIME | 是 | 创建时间 |

---

## 3. client_addresses 收货地址表

用于客户下单选择收货地址。

| 字段 | 类型 | 必填 | 说明 |
|---|---|---|---|
| id | BIGINT | 是 | 主键 |
| user_id | BIGINT | 是 | 客户 ID |
| receiver_name | VARCHAR(50) | 是 | 收货人 |
| receiver_mobile | VARCHAR(20) | 是 | 收货手机号 |
| province | VARCHAR(50) | 否 | 省 |
| city | VARCHAR(50) | 否 | 市 |
| district | VARCHAR(50) | 否 | 区 |
| detail_address | VARCHAR(255) | 是 | 详细地址 |
| is_default | TINYINT | 是 | 是否默认 |
| status | VARCHAR(20) | 是 | 状态 |
| created_at | DATETIME | 是 | 创建时间 |
| updated_at | DATETIME | 是 | 更新时间 |

---

## 4. client_cart_items 购物车表

用于保存客户加入购物车的商品。商品信息来源于 `supplier_products`。

| 字段 | 类型 | 必填 | 说明 |
|---|---|---|---|
| id | BIGINT | 是 | 主键 |
| user_id | BIGINT | 是 | 客户 ID |
| supplier_id | BIGINT | 是 | 供应商 ID，来自 supplier_products.supplier_id |
| product_id | BIGINT | 是 | 商品 ID，关联 supplier_products.id |
| quantity | INT | 是 | 数量 |
| support_installation | TINYINT | 是 | 是否选择安装 |
| selected | TINYINT | 是 | 是否选中 |
| status | VARCHAR(20) | 是 | 状态：active、deleted |
| created_at | DATETIME | 是 | 创建时间 |
| updated_at | DATETIME | 是 | 更新时间 |

一致性规则：

- 加入购物车时必须校验商品 `status = active`、`review_status = approved`、`stock > 0`。
- `supplier_id` 必须和商品当前 `supplier_id` 一致。
- 下单时再次校验库存，购物车库存不是最终库存依据。

---

## 5. client_orders 客户订单主表

用于保存客户订单主信息。

| 字段 | 类型 | 必填 | 说明 |
|---|---|---|---|
| id | BIGINT | 是 | 主键 |
| order_no | VARCHAR(50) | 是 | 客户订单号，唯一 |
| user_id | BIGINT | 是 | 客户 ID |
| supplier_id | BIGINT | 是 | 供应商 ID，单订单当前按单供应商设计 |
| address_id | BIGINT | 否 | 地址 ID |
| receiver_name | VARCHAR(50) | 是 | 收货人 |
| receiver_mobile | VARCHAR(20) | 是 | 收货手机号 |
| receiver_address | VARCHAR(255) | 是 | 收货地址快照 |
| delivery_method | VARCHAR(40) | 是 | 配送方式 |
| appointment_date | DATE | 否 | 预约配送日期 |
| appointment_time | VARCHAR(50) | 否 | 预约配送时间段 |
| product_amount | DECIMAL(12,2) | 是 | 商品金额 |
| delivery_fee | DECIMAL(12,2) | 是 | 配送费 |
| installation_fee | DECIMAL(12,2) | 是 | 安装费 |
| discount_amount | DECIMAL(12,2) | 是 | 优惠金额 |
| pay_amount | DECIMAL(12,2) | 是 | 实付金额 |
| pay_status | VARCHAR(20) | 是 | 支付状态：unpaid、paid、refunded |
| order_status | VARCHAR(30) | 是 | 订单状态 |
| delivery_status | VARCHAR(30) | 是 | 配送状态 |
| installation_status | VARCHAR(30) | 是 | 安装状态 |
| remark | VARCHAR(255) | 否 | 备注 |
| paid_at | DATETIME | 否 | 支付时间 |
| received_at | DATETIME | 否 | 收货时间 |
| completed_at | DATETIME | 否 | 完成时间 |
| created_at | DATETIME | 是 | 创建时间 |
| updated_at | DATETIME | 是 | 更新时间 |

订单状态建议：

| 状态 | 说明 |
|---|---|
| pending_payment | 待付款 |
| pending_delivery | 待发货 |
| shipping | 配送中 |
| pending_installation | 待安装 |
| completed | 已完成 |
| aftersale | 售后中 |
| cancelled | 已取消 |
| deleted | 已删除 |

一致性规则：

- 支付成功后，`client_orders.order_status` 从 `pending_payment` 变成 `pending_delivery`。
- 支付成功后必须生成 `supplier_fulfillments`。
- 如果订单任一商品选择安装，必须生成 `supplier_installations`。
- 订单完成后需要生成或确认供应商钱包流水。

---

## 6. client_order_items 客户订单明细表

用于保存订单中的商品快照。

| 字段 | 类型 | 必填 | 说明 |
|---|---|---|---|
| id | BIGINT | 是 | 主键 |
| order_id | BIGINT | 是 | 客户订单 ID |
| order_no | VARCHAR(50) | 是 | 客户订单号 |
| supplier_id | BIGINT | 是 | 供应商 ID |
| product_id | BIGINT | 是 | 商品 ID |
| product_name | VARCHAR(120) | 是 | 商品名称快照 |
| product_image | VARCHAR(255) | 否 | 商品主图快照 |
| category | VARCHAR(50) | 否 | 分类快照 |
| material | VARCHAR(100) | 否 | 材质快照 |
| color | VARCHAR(50) | 否 | 颜色快照 |
| size | VARCHAR(80) | 否 | 尺寸快照 |
| model | VARCHAR(80) | 否 | 型号快照 |
| price | DECIMAL(12,2) | 是 | 下单单价 |
| quantity | INT | 是 | 数量 |
| amount | DECIMAL(12,2) | 是 | 小计金额 |
| support_installation | TINYINT | 是 | 是否安装 |
| created_at | DATETIME | 是 | 创建时间 |

一致性规则：

- 创建订单时从 `supplier_products` 复制商品快照。
- 创建订单时扣减 `supplier_products.stock` 和 `spot_stock`。
- 订单明细 `supplier_id` 必须等于商品所属供应商。

---

## 7. client_payments 支付记录表

用于保存支付流水。

| 字段 | 类型 | 必填 | 说明 |
|---|---|---|---|
| id | BIGINT | 是 | 主键 |
| payment_no | VARCHAR(50) | 是 | 支付单号，唯一 |
| order_id | BIGINT | 是 | 订单 ID |
| order_no | VARCHAR(50) | 是 | 订单号 |
| user_id | BIGINT | 是 | 客户 ID |
| pay_channel | VARCHAR(30) | 是 | 支付渠道 |
| pay_amount | DECIMAL(12,2) | 是 | 支付金额 |
| status | VARCHAR(20) | 是 | 支付状态：pending、success、failed、refunded |
| paid_at | DATETIME | 否 | 支付成功时间 |
| transaction_no | VARCHAR(100) | 否 | 三方交易号 |
| created_at | DATETIME | 是 | 创建时间 |
| updated_at | DATETIME | 是 | 更新时间 |

一致性规则：

- 支付成功后更新 `client_orders.pay_status = paid`。
- 支付成功后生成 `supplier_fulfillments` 和供应商钱包待结算流水。

---

## 8. client_aftersales 客户售后表

用于保存客户售后申请，并和供应商售后工单保持一致。

| 字段 | 类型 | 必填 | 说明 |
|---|---|---|---|
| id | BIGINT | 是 | 主键 |
| case_no | VARCHAR(50) | 是 | 客户售后单号，唯一 |
| supplier_case_id | BIGINT | 否 | 供应商售后工单 ID，关联 supplier_aftersale_cases.id |
| order_id | BIGINT | 是 | 客户订单 ID |
| order_no | VARCHAR(50) | 是 | 客户订单号 |
| user_id | BIGINT | 是 | 客户 ID |
| supplier_id | BIGINT | 是 | 供应商 ID |
| product_id | BIGINT | 否 | 商品 ID |
| type | VARCHAR(50) | 是 | 售后类型 |
| description | VARCHAR(255) | 是 | 问题描述 |
| solution | VARCHAR(50) | 否 | 诉求 |
| photos | JSON | 否 | 客户上传图片 |
| supplier_opinion | VARCHAR(255) | 否 | 供应商处理意见 |
| status | VARCHAR(20) | 是 | 状态 |
| review_status | VARCHAR(20) | 否 | 审核状态 |
| completed_at | DATETIME | 否 | 完成时间 |
| created_at | DATETIME | 是 | 创建时间 |
| updated_at | DATETIME | 是 | 更新时间 |

一致性规则：

- 客户提交售后时，同时插入 `supplier_aftersale_cases`。
- `client_aftersales.supplier_case_id` 保存供应商售后工单 ID。
- 供应商处理售后后，同步更新客户端售后状态和方案。

---

## 9. client_favorites 客户收藏表

用于保存客户收藏商品。

| 字段 | 类型 | 必填 | 说明 |
|---|---|---|---|
| id | BIGINT | 是 | 主键 |
| user_id | BIGINT | 是 | 客户 ID |
| supplier_id | BIGINT | 是 | 供应商 ID |
| product_id | BIGINT | 是 | 商品 ID |
| status | VARCHAR(20) | 是 | 状态：active、deleted |
| created_at | DATETIME | 是 | 创建时间 |
| updated_at | DATETIME | 是 | 更新时间 |

---

## 10. client_browse_history 客户浏览历史表

用于记录客户商品浏览行为。

| 字段 | 类型 | 必填 | 说明 |
|---|---|---|---|
| id | BIGINT | 是 | 主键 |
| user_id | BIGINT | 是 | 客户 ID |
| supplier_id | BIGINT | 是 | 供应商 ID |
| product_id | BIGINT | 是 | 商品 ID |
| product_name | VARCHAR(120) | 否 | 商品名称快照 |
| product_image | VARCHAR(255) | 否 | 商品图片快照 |
| viewed_at | DATETIME | 是 | 浏览时间 |
| created_at | DATETIME | 是 | 创建时间 |

一致性规则：

- 浏览商品详情时写入浏览历史。
- 同步更新 `supplier_product_metrics.product_views`。
- 同步更新 `supplier_analytics_daily.product_views`。

---

## 客户端与供应商端一致性链路

### 1. 商品展示

```text
supplier_products.status = active
supplier_products.review_status = approved
supplier_products.stock > 0
↓
客户端首页 / 商品列表 / 商品详情可见
```

### 2. 客户端下单

```text
client_orders
client_order_items
↓
扣减 supplier_products.stock / spot_stock
↓
支付成功后生成 supplier_fulfillments
↓
如果需要安装，生成 supplier_installations
↓
更新 supplier_product_metrics
↓
更新 supplier_analytics_daily
```

### 3. 支付结算

```text
client_payments.status = success
↓
client_orders.pay_status = paid
↓
supplier_wallet_transactions 生成订单收入流水
↓
supplier_wallet_settlements 月度结算可统计
```

### 4. 售后

```text
client_aftersales
↓
supplier_aftersale_cases
↓
供应商处理售后
↓
同步 client_aftersales.status / supplier_opinion / review_status
```

## 推荐事务边界

### 创建订单事务

同一个事务内完成：

1. 查询并锁定商品库存。
2. 插入 `client_orders`。
3. 插入 `client_order_items`。
4. 扣减 `supplier_products.stock` 和 `spot_stock`。

### 支付成功事务

同一个事务内完成：

1. 插入或更新 `client_payments`。
2. 更新 `client_orders.pay_status` 和 `order_status`。
3. 插入 `supplier_fulfillments`。
4. 插入 `supplier_installations`。
5. 插入 `supplier_wallet_transactions`。
6. 更新 `supplier_product_metrics`。
7. 更新 `supplier_analytics_daily`。

### 售后申请事务

同一个事务内完成：

1. 插入 `client_aftersales`。
2. 插入 `supplier_aftersale_cases`。
3. 回写 `client_aftersales.supplier_case_id`。
4. 更新 `client_orders.order_status = aftersale`。
