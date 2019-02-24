CREATE TABLE products (
  id INT NOT NULL,
  name VARCHAR(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  price0 FLOAT NOT NULL,
  detailed_info VARCHAR(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
  keywords VARCHAR(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
  PRIMARY KEY (id)
);

CREATE TABLE customers (
  uid VARCHAR(256) NOT NULL,
  name VARCHAR(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  pass_hash BINARY(64) NOT NULL,
  idcard_no VARCHAR(32) NOT NULL,
  mobile VARCHAR(32) NOT NULL,
  postal_addr VARCHAR(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  bday CHAR(10) NOT NULL,
  ref_uid VARCHAR(256) NOT NULL,
  PRIMARY KEY (uid)
);

CREATE TABLE orders (
  id BIGINT NOT NULL AUTO_INCREMENT,
  customer_id VARCHAR(256) NOT NULL,
  product_id INT NOT NULL,
  qty FLOAT NOT NULL,
  creation_time TIMESTAMP NOT NULL,
  proc_time1 TIMESTAMP,
  proc_time2 TIMESTAMP,
  proc_time3 TIMESTAMP,
  PRIMARY KEY (id)
);

