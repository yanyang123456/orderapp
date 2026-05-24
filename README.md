# orderapp
此项目是一个家具/家居订单系统。基于客户端，供应端和后端数据库实现的系统


# orderapp 项目总说明

## 项目简介

`orderapp` 是一个家具/家居订单系统，包含三个核心部分：

- `backend`：Spring Boot + MyBatis 后端接口服务
- `client-h5`：客户端 H5 购物、下单、订单管理页面
- `src`：供应商端后台页面，负责商品、履约、钱包、售后、审核等管理

同时项目根目录下还保留了传统静态页面：

- `client/`：客户端旧版静态页面
- `supplier/`：供应商端旧版静态页面

---

## 目录结构

```text
orderapp/
├── backend/                  # 后端 Spring Boot 服务
│   └── src/main/
│       ├── java/com/orderapp/supplier/
│       │   ├── controller/   # 客户端、供应商接口控制器
│       │   ├── service/      # 业务服务层
│       │   ├── service/impl/ # 业务实现
│       │   ├── mapper/       # MyBatis Mapper
│       │   ├── entity/       # 数据实体
│       │   ├── dto/          # 请求对象
│       │   └── common/       # 通用配置、响应、异常处理
│       └── resources/
│           ├── mapper/       # MyBatis XML
│           └── application.yml
├── client-h5/                # 客户端 H5 前端
│   └── src/
│       ├── views/            # 首页、商品、购物车、结算、订单、售后、个人中心
│       ├── components/       # 通用组件
│       ├── router/           # 路由
│       └── api/              # 接口调用
├── src/                      # 供应商端前端
│   └── src/
│       ├── views/            # 工作台、商品、报价、履约、安装、售后、审核、数据、钱包
│       ├── router/           # 路由
│       └── api/              # 接口调用
├── client/                   # 客户端旧版静态页面
├── supplier/                 # 供应商端旧版静态页面
├── docs/                     # 接口文档
├── client_tables_mysql.sql   # 客户端相关数据库表结构
├── tables_mysql.sql          # 供应商相关数据库表结构
├── tables.md                 # 表结构说明
└── ui.md                     # UI 说明
```

---

## 功能说明

### 1. 客户端 H5

客户端主要完成下单和订单跟踪：

- 首页浏览商品
- 商品列表与商品详情
- 购物车管理
- 结算页填写联系人、手机号、地址、配送方式
- 创建订单
- 待付款订单支付
- 待发货 / 配送中 / 已完成状态查看
- 售后申请
- 个人中心

### 2. 供应商端

供应商端主要完成履约与经营管理：

- 工作台概览
- 商品建档
- 报价改价
- 发货配送
- 安装确认
- 售后工单处理
- 审核管理
- 数据分析
- 钱包与结算

### 3. 后端服务

后端负责统一提供客户端和供应商端接口：

- 用户登录和验证码登录
- 商品查询
- 购物车操作
- 创建订单、支付、确认收货
- 供应商履约单生成
- 发货后同步客户端订单状态
- 售后、安装、钱包、统计数据

---

## 当前订单流转

### 客户端流程

```text
购物车 -> 结算页 -> 创建订单(待付款) -> 支付 -> 待发货 -> 配送中 -> 确认收货 -> 已完成
```

### 供应商流程

```text
待发货履约单 -> 确认发货 -> 客户端订单变为配送中 -> 客户确认收货 -> 已完成
```

---

## 数据库

项目使用 MySQL，主要数据库名：

- `orderapp_supplier`

常见核心表：

- `client_users`
- `client_orders`
- `client_order_items`
- `client_payments`
- `client_cart_items`
- `client_aftersales`
- `supplier_fulfillments`
- `supplier_installations`
- `supplier_wallet_transactions`
- `supplier_wallet_settlements`

---

## 本地运行方式

### 1. 准备环境

建议环境：

- JDK 17
- Maven
- Node.js 18+ 或 20+
- MySQL 8+

### 2. 导入数据库

先执行数据库脚本，导入 `orderapp_supplier` 数据库。

例如：

```bash
mysql -uroot -p742500 < client_tables_mysql.sql
mysql -uroot -p742500 < tables_mysql.sql
```

如果你已经有库，只需要确保表存在即可。

### 3. 启动后端

进入后端目录：

```bash
cd backend
mvn spring-boot:run
```

后端默认端口：

```text
8080
```

后端配置文件：

- `backend/src/main/resources/application.yml`

### 4. 启动客户端 H5

进入客户端 H5 目录：

```bash
cd client-h5
npm install
npm run dev
```

默认开发地址：

```text
http://localhost:5173
```

### 5. 启动供应商端前端

项目根目录下的供应商前端：

```bash
cd .
npm install
npm run dev
```

如果你当前根目录已经安装过依赖，也可以直接启动。

---

## 接口与文档

项目接口文档位于：

- `docs/client-api.md`
- `docs/supplier-api.md`

表结构说明位于：

- `tables.md`
- `client_tables_mysql.sql`
- `tables_mysql.sql`

---

## 备注

- 客户端和供应商端都是独立的前端入口
- 后端统一提供数据和状态流转
- 如果修改接口参数、返回结构、URL 或请求方式，应同步更新接口文档
