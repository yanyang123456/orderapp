CREATE DATABASE IF NOT EXISTS orderapp_supplier DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE orderapp_supplier;
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS supplier_product_metrics;
DROP TABLE IF EXISTS supplier_analytics_daily;
DROP TABLE IF EXISTS supplier_reviews;
DROP TABLE IF EXISTS supplier_aftersale_cases;
DROP TABLE IF EXISTS supplier_installations;
DROP TABLE IF EXISTS supplier_fulfillments;
DROP TABLE IF EXISTS supplier_quotes;
DROP TABLE IF EXISTS supplier_products;
DROP TABLE IF EXISTS supplier_wallet_transactions;
DROP TABLE IF EXISTS supplier_wallet_settlements;
DROP TABLE IF EXISTS supplier_dashboard_alerts;
DROP TABLE IF EXISTS supplier_auth_codes;
DROP TABLE IF EXISTS suppliers;

CREATE TABLE suppliers (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  supplier_no VARCHAR(32) NOT NULL UNIQUE,
  supplier_name VARCHAR(100) NOT NULL,
  contact_mobile VARCHAR(20) NOT NULL UNIQUE,
  contact_name VARCHAR(50) DEFAULT NULL,
  status VARCHAR(20) NOT NULL DEFAULT 'active',
  token VARCHAR(255) DEFAULT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Supplier base info';

CREATE TABLE supplier_auth_codes (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  mobile VARCHAR(20) NOT NULL,
  code VARCHAR(10) NOT NULL,
  expired_at DATETIME NOT NULL,
  used TINYINT(1) NOT NULL DEFAULT 0,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  KEY idx_mobile (mobile)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Login codes';

CREATE TABLE supplier_dashboard_alerts (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  supplier_id BIGINT NOT NULL,
  alert_type VARCHAR(40) NOT NULL,
  title VARCHAR(100) NOT NULL,
  content VARCHAR(255) NOT NULL,
  priority VARCHAR(20) NOT NULL DEFAULT 'normal',
  status VARCHAR(20) NOT NULL DEFAULT 'pending',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  KEY idx_supplier_status (supplier_id, status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Dashboard alerts';

CREATE TABLE supplier_wallet_settlements (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  supplier_id BIGINT NOT NULL,
  settlement_month CHAR(7) NOT NULL,
  total_income DECIMAL(12,2) NOT NULL DEFAULT 0,
  withdrawable_amount DECIMAL(12,2) NOT NULL DEFAULT 0,
  frozen_amount DECIMAL(12,2) NOT NULL DEFAULT 0,
  order_amount DECIMAL(12,2) NOT NULL DEFAULT 0,
  delivery_fee DECIMAL(12,2) NOT NULL DEFAULT 0,
  installation_fee DECIMAL(12,2) NOT NULL DEFAULT 0,
  refund_deduction DECIMAL(12,2) NOT NULL DEFAULT 0,
  aftersale_deduction DECIMAL(12,2) NOT NULL DEFAULT 0,
  status VARCHAR(20) NOT NULL DEFAULT 'settled',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY uk_supplier_month (supplier_id, settlement_month)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Wallet settlements';

CREATE TABLE supplier_wallet_transactions (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  supplier_id BIGINT NOT NULL,
  settlement_id BIGINT DEFAULT NULL,
  title VARCHAR(100) NOT NULL,
  description VARCHAR(255) DEFAULT NULL,
  amount DECIMAL(12,2) NOT NULL,
  direction VARCHAR(20) NOT NULL,
  biz_type VARCHAR(40) NOT NULL,
  biz_no VARCHAR(50) DEFAULT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  KEY idx_supplier_direction (supplier_id, direction),
  KEY idx_biz_no (biz_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Wallet transactions';
CREATE TABLE supplier_products (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  supplier_id BIGINT NOT NULL,
  name VARCHAR(120) NOT NULL,
  images JSON DEFAULT NULL,
  category_id BIGINT DEFAULT NULL,
  category VARCHAR(50) NOT NULL,
  description TEXT DEFAULT NULL,
  material VARCHAR(100) DEFAULT NULL,
  color VARCHAR(50) DEFAULT NULL,
  size VARCHAR(80) DEFAULT NULL,
  model VARCHAR(80) DEFAULT NULL,
  style VARCHAR(50) DEFAULT NULL,
  price DECIMAL(12,2) NOT NULL DEFAULT 0,
  stock INT NOT NULL DEFAULT 0,
  spot_stock INT NOT NULL DEFAULT 0,
  presale_cycle_days INT DEFAULT 0,
  custom_cycle_days INT DEFAULT 0,
  min_order_quantity INT DEFAULT 1,
  delivery_areas JSON DEFAULT NULL,
  package_info VARCHAR(255) DEFAULT NULL,
  support_installation TINYINT(1) NOT NULL DEFAULT 0,
  status VARCHAR(20) NOT NULL DEFAULT 'draft',
  review_status VARCHAR(20) NOT NULL DEFAULT 'pending',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  KEY idx_supplier_status (supplier_id, status),
  KEY idx_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Products';

CREATE TABLE supplier_quotes (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  supplier_id BIGINT NOT NULL,
  product_id BIGINT NOT NULL,
  current_price DECIMAL(12,2) NOT NULL,
  new_price DECIMAL(12,2) NOT NULL,
  reason VARCHAR(255) NOT NULL,
  attachments JSON DEFAULT NULL,
  review_status VARCHAR(20) NOT NULL DEFAULT 'pending',
  reject_reason VARCHAR(255) DEFAULT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  KEY idx_supplier_status (supplier_id, review_status),
  KEY idx_product (product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Quote requests';

CREATE TABLE supplier_fulfillments (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  supplier_id BIGINT NOT NULL,
  delivery_no VARCHAR(50) NOT NULL UNIQUE,
  order_no VARCHAR(50) NOT NULL,
  area VARCHAR(50) NOT NULL,
  warehouse_id BIGINT DEFAULT NULL,
  warehouse VARCHAR(50) NOT NULL,
  customer_name VARCHAR(50) DEFAULT NULL,
  customer_address VARCHAR(255) NOT NULL,
  appointment_date DATE NOT NULL,
  appointment_time VARCHAR(50) NOT NULL,
  item_summary VARCHAR(255) DEFAULT NULL,
  quantity INT NOT NULL DEFAULT 0,
  volume DECIMAL(10,2) NOT NULL DEFAULT 0,
  weight DECIMAL(10,2) NOT NULL DEFAULT 0,
  driver_name VARCHAR(50) DEFAULT NULL,
  driver_mobile VARCHAR(20) DEFAULT NULL,
  status VARCHAR(20) NOT NULL DEFAULT 'pending',
  shipped_at DATETIME DEFAULT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  KEY idx_supplier_date_status (supplier_id, appointment_date, status),
  KEY idx_order_no (order_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Fulfillments';

CREATE TABLE supplier_installations (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  supplier_id BIGINT NOT NULL,
  order_no VARCHAR(50) NOT NULL,
  product_summary VARCHAR(255) DEFAULT NULL,
  customer_name VARCHAR(50) DEFAULT NULL,
  customer_address VARCHAR(255) DEFAULT NULL,
  installer_name VARCHAR(50) DEFAULT NULL,
  installer_mobile VARCHAR(20) DEFAULT NULL,
  appointment_date DATE NOT NULL,
  appointment_time VARCHAR(50) NOT NULL,
  status VARCHAR(20) NOT NULL DEFAULT 'pending',
  photos JSON DEFAULT NULL,
  remark VARCHAR(255) DEFAULT NULL,
  completed_at DATETIME DEFAULT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  KEY idx_supplier_date_status (supplier_id, appointment_date, status),
  KEY idx_order_no (order_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Installations';
CREATE TABLE supplier_aftersale_cases (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  supplier_id BIGINT NOT NULL,
  case_no VARCHAR(50) NOT NULL UNIQUE,
  order_no VARCHAR(50) DEFAULT NULL,
  type VARCHAR(50) NOT NULL,
  description VARCHAR(255) NOT NULL,
  status VARCHAR(20) NOT NULL DEFAULT 'pending',
  remaining_hours INT DEFAULT 0,
  opinion VARCHAR(255) DEFAULT NULL,
  solution VARCHAR(50) DEFAULT NULL,
  attachments JSON DEFAULT NULL,
  review_status VARCHAR(20) DEFAULT NULL,
  remark VARCHAR(255) DEFAULT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  KEY idx_supplier_status (supplier_id, status),
  KEY idx_order_no (order_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='After-sales cases';

CREATE TABLE supplier_reviews (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  supplier_id BIGINT NOT NULL,
  review_type VARCHAR(30) NOT NULL,
  biz_id BIGINT NOT NULL,
  title VARCHAR(120) NOT NULL,
  status VARCHAR(20) NOT NULL DEFAULT 'pending',
  reject_reason VARCHAR(255) DEFAULT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  KEY idx_supplier_status (supplier_id, status),
  KEY idx_biz (review_type, biz_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Review records';

CREATE TABLE supplier_analytics_daily (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  supplier_id BIGINT NOT NULL,
  stat_date DATE NOT NULL,
  product_views INT NOT NULL DEFAULT 0,
  order_count INT NOT NULL DEFAULT 0,
  sales_amount DECIMAL(12,2) NOT NULL DEFAULT 0,
  return_rate DECIMAL(5,2) NOT NULL DEFAULT 0,
  aftersale_rate DECIMAL(5,2) NOT NULL DEFAULT 0,
  fulfillment_rate DECIMAL(5,2) NOT NULL DEFAULT 0,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY uk_supplier_date (supplier_id, stat_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Daily analytics';

CREATE TABLE supplier_product_metrics (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  supplier_id BIGINT NOT NULL,
  product_id BIGINT NOT NULL,
  stat_date DATE NOT NULL,
  views INT NOT NULL DEFAULT 0,
  orders INT NOT NULL DEFAULT 0,
  sales_amount DECIMAL(12,2) NOT NULL DEFAULT 0,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY uk_product_date (product_id, stat_date),
  KEY idx_supplier_date (supplier_id, stat_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Product analytics';
INSERT INTO suppliers (id, supplier_no, supplier_name, contact_mobile, contact_name, status, token) VALUES
(10001, 'SUP202405180001', '林木家具', '13800000000', '林先生', 'active', 'mock-token');

INSERT INTO supplier_auth_codes (mobile, code, expired_at, used) VALUES
('13800000000', '123456', DATE_ADD(NOW(), INTERVAL 10 MINUTE), 0);

INSERT INTO supplier_dashboard_alerts (supplier_id, alert_type, title, content, priority, status) VALUES
(10001, 'quote_rejected', '报价审核被驳回', '北欧橡木餐桌需补充材质证明', 'high', 'pending'),
(10001, 'installation_photo', '安装完成照片待上传', '订单 SO20240518023 · 预约 16:30', 'normal', 'pending');

INSERT INTO supplier_wallet_settlements (id, supplier_id, settlement_month, total_income, withdrawable_amount, frozen_amount, order_amount, delivery_fee, installation_fee, refund_deduction, aftersale_deduction, status) VALUES
(2001, 10001, '2024-05', 128560.80, 92340.00, 8216.00, 119800.00, 6240.00, 4860.00, 1120.00, 1220.00, 'settled');

INSERT INTO supplier_wallet_transactions (supplier_id, settlement_id, title, description, amount, direction, biz_type, biz_no, created_at) VALUES
(10001, 2001, '订单 SO20240518023', '三人位真皮沙发 · 已完成安装', 8680.00, 'income', 'order', 'SO20240518023', '2024-05-18 10:20:00'),
(10001, 2001, '售后扣减 AS24051809', '运输破损 · 重新配送费用', 360.00, 'deduction', 'aftersale', 'AS24051809', '2024-05-17 15:30:00');

INSERT INTO supplier_products (id, supplier_id, name, images, category_id, category, description, material, color, size, model, style, price, stock, spot_stock, presale_cycle_days, custom_cycle_days, min_order_quantity, delivery_areas, package_info, support_installation, status, review_status) VALUES
(101, 10001, '北欧云朵模块沙发', JSON_ARRAY('https://images.unsplash.com/photo-1618220179428-22790b461013'), 12, '沙发', '高回弹海绵坐垫', '棉麻', '燕麦白', '280×96×78cm', 'SF-ND-280', '现代简约', 6899.00, 126, 126, 15, 45, 2, JSON_ARRAY('华东'), '三层纸箱', 1, 'draft', 'pending'),
(102, 10001, '意式岩板餐桌', JSON_ARRAY('https://images.unsplash.com/photo-1604578762246-41134e37f9cc'), 13, '餐桌', '耐磨岩板桌面', '岩板', '鱼肚白', '180×90×76cm', 'DT-LUX-180', '意式轻奢', 4299.00, 48, 48, 7, 30, 1, JSON_ARRAY('华东', '华南'), '木架加固包装', 1, 'reviewing', 'pending');

INSERT INTO supplier_quotes (id, supplier_id, product_id, current_price, new_price, reason, attachments, review_status) VALUES
(9001, 10001, 102, 4299.00, 4099.00, '促销调价', JSON_ARRAY(), 'pending');

INSERT INTO supplier_fulfillments (id, supplier_id, delivery_no, order_no, area, warehouse_id, warehouse, customer_name, customer_address, appointment_date, appointment_time, item_summary, quantity, volume, weight, status) VALUES
(7001, 10001, 'D24051801', 'SO20240518023', '西湖区', 1, '仓库 A', '陈女士', '杭州市西湖区文三路', '2024-05-18', '10:00-12:00', '沙发 2 / 茶几 1', 3, 8.60, 260.00, 'pending'),
(7002, 10001, 'D24051805', 'SO20240518025', '滨江区', 1, '仓库 A', '李先生', '杭州市滨江区江南大道', '2024-05-18', '14:00-18:00', '床架 1 / 床垫 1', 2, 6.20, 210.00, 'pending');

INSERT INTO supplier_installations (id, supplier_id, order_no, product_summary, customer_name, customer_address, installer_name, installer_mobile, appointment_date, appointment_time, status, photos) VALUES
(8001, 10001, 'SO20240518023', '全屋柜体 3 件', '王先生', '杭州市拱墅区', '周师傅', '13800005621', '2024-05-18', '16:30-18:00', 'in_progress', JSON_ARRAY());

INSERT INTO supplier_aftersale_cases (id, supplier_id, case_no, order_no, type, description, status, remaining_hours) VALUES
(6001, 10001, 'AS24051809', 'SO20240518023', '运输破损', '岩板餐桌边角破损', 'pending', 2),
(6002, 10001, 'AS24051812', 'SO20240518025', '少件', '柜体五金包缺失', 'pending', 6);

INSERT INTO supplier_reviews (id, supplier_id, review_type, biz_id, title, status, reject_reason) VALUES
(5001, 10001, 'product', 101, '商品审核 · 北欧云朵沙发', 'rejected', '缺少包装尺寸与安装服务说明'),
(5002, 10001, 'quote', 9001, '报价审核 · 岩板餐桌', 'pending', NULL),
(5003, 10001, 'aftersale', 6001, '售后方案审核 · AS24051809', 'approved', NULL);

INSERT INTO supplier_analytics_daily (supplier_id, stat_date, product_views, order_count, sales_amount, return_rate, aftersale_rate, fulfillment_rate) VALUES
(10001, '2024-05-12', 4820, 38, 48000.00, 2.80, 4.60, 96.80),
(10001, '2024-05-13', 5660, 45, 66000.00, 2.70, 4.50, 96.90),
(10001, '2024-05-14', 5240, 41, 54000.00, 2.90, 4.70, 96.50),
(10001, '2024-05-15', 6780, 59, 78000.00, 2.60, 4.40, 97.00),
(10001, '2024-05-16', 6120, 48, 62000.00, 2.80, 4.60, 96.80),
(10001, '2024-05-17', 7380, 72, 88000.00, 2.50, 4.20, 97.20),
(10001, '2024-05-18', 8620, 83, 90920.00, 2.80, 4.60, 96.80);

INSERT INTO supplier_product_metrics (supplier_id, product_id, stat_date, views, orders, sales_amount) VALUES
(10001, 101, '2024-05-18', 9820, 86, 592000.00),
(10001, 102, '2024-05-18', 6420, 51, 317000.00);

SET FOREIGN_KEY_CHECKS = 1;
