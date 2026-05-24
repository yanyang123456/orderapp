CREATE DATABASE IF NOT EXISTS orderapp_supplier DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE orderapp_supplier;
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS client_browse_history;
DROP TABLE IF EXISTS client_favorites;
DROP TABLE IF EXISTS client_aftersales;
DROP TABLE IF EXISTS client_payments;
DROP TABLE IF EXISTS client_order_items;
DROP TABLE IF EXISTS client_orders;
DROP TABLE IF EXISTS client_cart_items;
DROP TABLE IF EXISTS client_addresses;
DROP TABLE IF EXISTS client_auth_codes;
DROP TABLE IF EXISTS client_users;

CREATE TABLE client_users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_no VARCHAR(32) NOT NULL UNIQUE,
  mobile VARCHAR(20) NOT NULL UNIQUE,
  nickname VARCHAR(50) DEFAULT NULL,
  avatar VARCHAR(255) DEFAULT NULL,
  gender VARCHAR(20) DEFAULT NULL,
  level VARCHAR(30) NOT NULL DEFAULT 'normal',
  points INT NOT NULL DEFAULT 0,
  status VARCHAR(20) NOT NULL DEFAULT 'active',
  token VARCHAR(255) DEFAULT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Client users';

CREATE TABLE client_auth_codes (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  mobile VARCHAR(20) NOT NULL,
  code VARCHAR(10) NOT NULL,
  expired_at DATETIME NOT NULL,
  used TINYINT(1) NOT NULL DEFAULT 0,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  KEY idx_mobile (mobile)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Client login codes';

CREATE TABLE client_addresses (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  receiver_name VARCHAR(50) NOT NULL,
  receiver_mobile VARCHAR(20) NOT NULL,
  province VARCHAR(50) DEFAULT NULL,
  city VARCHAR(50) DEFAULT NULL,
  district VARCHAR(50) DEFAULT NULL,
  detail_address VARCHAR(255) NOT NULL,
  is_default TINYINT(1) NOT NULL DEFAULT 0,
  status VARCHAR(20) NOT NULL DEFAULT 'active',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  KEY idx_user_status (user_id, status),
  KEY idx_user_default (user_id, is_default)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Client addresses';

CREATE TABLE client_cart_items (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  supplier_id BIGINT NOT NULL,
  product_id BIGINT NOT NULL,
  quantity INT NOT NULL DEFAULT 1,
  support_installation TINYINT(1) NOT NULL DEFAULT 0,
  selected TINYINT(1) NOT NULL DEFAULT 1,
  status VARCHAR(20) NOT NULL DEFAULT 'active',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  UNIQUE KEY uk_user_product_active (user_id, product_id, status),
  KEY idx_user_status (user_id, status),
  KEY idx_supplier_product (supplier_id, product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Client cart items';

CREATE TABLE client_orders (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  order_no VARCHAR(50) NOT NULL UNIQUE,
  user_id BIGINT NOT NULL,
  supplier_id BIGINT NOT NULL,
  address_id BIGINT DEFAULT NULL,
  receiver_name VARCHAR(50) NOT NULL,
  receiver_mobile VARCHAR(20) NOT NULL,
  receiver_address VARCHAR(255) NOT NULL,
  delivery_method VARCHAR(40) NOT NULL DEFAULT 'home_delivery',
  appointment_date DATE DEFAULT NULL,
  appointment_time VARCHAR(50) DEFAULT NULL,
  product_amount DECIMAL(12,2) NOT NULL DEFAULT 0,
  delivery_fee DECIMAL(12,2) NOT NULL DEFAULT 0,
  installation_fee DECIMAL(12,2) NOT NULL DEFAULT 0,
  discount_amount DECIMAL(12,2) NOT NULL DEFAULT 0,
  pay_amount DECIMAL(12,2) NOT NULL DEFAULT 0,
  pay_status VARCHAR(20) NOT NULL DEFAULT 'unpaid',
  order_status VARCHAR(30) NOT NULL DEFAULT 'pending_payment',
  delivery_status VARCHAR(30) NOT NULL DEFAULT 'pending',
  installation_status VARCHAR(30) NOT NULL DEFAULT 'none',
  remark VARCHAR(255) DEFAULT NULL,
  paid_at DATETIME DEFAULT NULL,
  received_at DATETIME DEFAULT NULL,
  completed_at DATETIME DEFAULT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  KEY idx_user_status (user_id, order_status),
  KEY idx_supplier_status (supplier_id, order_status),
  KEY idx_order_no (order_no),
  KEY idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Client orders';

CREATE TABLE client_order_items (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  order_id BIGINT NOT NULL,
  order_no VARCHAR(50) NOT NULL,
  supplier_id BIGINT NOT NULL,
  product_id BIGINT NOT NULL,
  product_name VARCHAR(120) NOT NULL,
  product_image VARCHAR(255) DEFAULT NULL,
  category VARCHAR(50) DEFAULT NULL,
  material VARCHAR(100) DEFAULT NULL,
  color VARCHAR(50) DEFAULT NULL,
  size VARCHAR(80) DEFAULT NULL,
  model VARCHAR(80) DEFAULT NULL,
  price DECIMAL(12,2) NOT NULL DEFAULT 0,
  quantity INT NOT NULL DEFAULT 1,
  amount DECIMAL(12,2) NOT NULL DEFAULT 0,
  support_installation TINYINT(1) NOT NULL DEFAULT 0,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  KEY idx_order_id (order_id),
  KEY idx_order_no (order_no),
  KEY idx_supplier_product (supplier_id, product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Client order items';

CREATE TABLE client_payments (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  payment_no VARCHAR(50) NOT NULL UNIQUE,
  order_id BIGINT NOT NULL,
  order_no VARCHAR(50) NOT NULL,
  user_id BIGINT NOT NULL,
  pay_channel VARCHAR(30) NOT NULL,
  pay_amount DECIMAL(12,2) NOT NULL DEFAULT 0,
  status VARCHAR(20) NOT NULL DEFAULT 'pending',
  paid_at DATETIME DEFAULT NULL,
  transaction_no VARCHAR(100) DEFAULT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  KEY idx_order_id (order_id),
  KEY idx_order_no (order_no),
  KEY idx_user_status (user_id, status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Client payment records';

CREATE TABLE client_aftersales (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  case_no VARCHAR(50) NOT NULL UNIQUE,
  supplier_case_id BIGINT DEFAULT NULL,
  order_id BIGINT NOT NULL,
  order_no VARCHAR(50) NOT NULL,
  user_id BIGINT NOT NULL,
  supplier_id BIGINT NOT NULL,
  product_id BIGINT DEFAULT NULL,
  type VARCHAR(50) NOT NULL,
  description VARCHAR(255) NOT NULL,
  solution VARCHAR(50) DEFAULT NULL,
  photos JSON DEFAULT NULL,
  supplier_opinion VARCHAR(255) DEFAULT NULL,
  status VARCHAR(20) NOT NULL DEFAULT 'pending',
  review_status VARCHAR(20) DEFAULT NULL,
  completed_at DATETIME DEFAULT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  KEY idx_user_status (user_id, status),
  KEY idx_supplier_status (supplier_id, status),
  KEY idx_order_no (order_no),
  KEY idx_supplier_case_id (supplier_case_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Client aftersales';

CREATE TABLE client_favorites (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  supplier_id BIGINT NOT NULL,
  product_id BIGINT NOT NULL,
  status VARCHAR(20) NOT NULL DEFAULT 'active',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  UNIQUE KEY uk_user_product_status (user_id, product_id, status),
  KEY idx_user_status (user_id, status),
  KEY idx_supplier_product (supplier_id, product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Client favorites';

CREATE TABLE client_browse_history (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  supplier_id BIGINT NOT NULL,
  product_id BIGINT NOT NULL,
  product_name VARCHAR(120) DEFAULT NULL,
  product_image VARCHAR(255) DEFAULT NULL,
  viewed_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  KEY idx_user_viewed (user_id, viewed_at),
  KEY idx_supplier_product (supplier_id, product_id),
  KEY idx_product_viewed (product_id, viewed_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Client browse history';

SET FOREIGN_KEY_CHECKS = 1;
