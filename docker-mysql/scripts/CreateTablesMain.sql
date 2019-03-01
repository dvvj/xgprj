CREATE TABLE products (
  id INT NOT NULL,
  name VARCHAR(1023) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  price0 FLOAT NOT NULL,
  detailed_info VARCHAR(8191) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
  keywords VARCHAR(511) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
  PRIMARY KEY (id)
);

CREATE TABLE customers (
  uid VARCHAR(255) NOT NULL,
  name VARCHAR(31) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  pass_hash BINARY(64) NOT NULL,
  idcard_no VARCHAR(31) NOT NULL,
  mobile VARCHAR(31) NOT NULL,
  postal_addr VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  bday CHAR(10) NOT NULL,
  ref_uid VARCHAR(255) NOT NULL,
  PRIMARY KEY (uid)
);

CREATE TABLE orders (
  id BIGINT NOT NULL AUTO_INCREMENT,
  customer_id VARCHAR(255) NOT NULL,
  product_id INT NOT NULL,
  qty FLOAT NOT NULL,
  creation_time TIMESTAMP NOT NULL,
  proc_time1 TIMESTAMP,
  proc_time2 TIMESTAMP,
  proc_time3 TIMESTAMP,
  PRIMARY KEY (id)
);


CREATE TABLE order_history (
  id BIGINT NOT NULL AUTO_INCREMENT,
  order_id BIGINT NOT NULL,
  update_time TIMESTAMP NOT NULL,
  old_qty FLOAT NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE med_profs (
  prof_id VARCHAR(255) NOT NULL,
  name VARCHAR(31) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  pass_hash BINARY(64) NOT NULL,
  idcard_no VARCHAR(31) NOT NULL,
  mobile VARCHAR(31) NOT NULL,
  PRIMARY KEY (prof_id)
);

CREATE TABLE agency (
  agency_id VARCHAR(255) NOT NULL,
  name VARCHAR(31) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  pass_hash BINARY(64) NOT NULL,
  agency_no VARCHAR(31) NOT NULL,
  mobile VARCHAR(31) NOT NULL,
  PRIMARY KEY (agency_id)
);

CREATE TABLE price_plans (
  id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(31) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  info VARCHAR(4095) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  json VARCHAR(8191) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  json_ver VARCHAR(31) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (id)
);