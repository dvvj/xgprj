CREATE TABLE products (
  id INT NOT NULL,
  name VARCHAR(1023) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  price0 FLOAT NOT NULL,
  detailed_info VARCHAR(8191) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
  keywords VARCHAR(511) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
  categories VARCHAR(1023) NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE prod_categories (
  id BIGINT NOT NULL,
  name VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
  detailed_info VARCHAR(8191) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
  PRIMARY KEY (id)
);

CREATE TABLE customers (
  uid VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
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
  customer_id VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  product_id INT NOT NULL,
  qty FLOAT NOT NULL,
  actual_cost FLOAT NOT NULL,
  creation_time TIMESTAMP NOT NULL,
  pay_time TIMESTAMP,
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
  prof_id VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  name VARCHAR(31) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  pass_hash BINARY(64) NOT NULL,
  idcard_no VARCHAR(31) NOT NULL,
  mobile VARCHAR(31) NOT NULL,
  org_agent_id VARCHAR(255),
  PRIMARY KEY (prof_id)
);


CREATE TABLE customer_profiles (
  id BIGINT NOT NULL AUTO_INCREMENT,
  prof_id VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  customer_id VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  detailed_info VARCHAR(8191) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
  version VARCHAR(31) NOT NULL,
  PRIMARY KEY (id),
  INDEX(prof_id)
);

CREATE TABLE prof_org_agents (
  agent_id VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  org_id VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  name VARCHAR(31) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  pass_hash BINARY(64) NOT NULL,
  info VARCHAR(8191) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  phone VARCHAR(31) NOT NULL,
  join_date TIMESTAMP NOT NULL,
  PRIMARY KEY (agent_id)
);

CREATE TABLE med_prof_orgs (
  org_id VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  name VARCHAR(31) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  pass_hash BINARY(64) NOT NULL,
  info VARCHAR(8191) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  phone VARCHAR(31) NOT NULL,
  join_date TIMESTAMP NOT NULL,
  PRIMARY KEY (org_id)
);

CREATE TABLE agency (
  agency_id VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  name VARCHAR(31) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  pass_hash BINARY(64) NOT NULL,
  agency_no VARCHAR(31) NOT NULL,
  mobile VARCHAR(31) NOT NULL,
  PRIMARY KEY (agency_id)
);

CREATE TABLE price_plans (
  id VARCHAR(31) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  info VARCHAR(4095) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  defi VARCHAR(8191) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  vtag VARCHAR(31) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  creator VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (id)
);


CREATE TABLE price_plan_map (
  id BIGINT NOT NULL AUTO_INCREMENT,
  uid VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  plan_ids VARCHAR(127) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  start_time TIMESTAMP NOT NULL,
  expire_time TIMESTAMP,
  PRIMARY KEY (id)
);

CREATE TABLE reward_plans (
  id VARCHAR(31) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  info VARCHAR(4095) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  defi VARCHAR(8191) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  vtag VARCHAR(31) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  creator VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (id)
);


CREATE TABLE alipay_notify_raw (
  id BIGINT NOT NULL AUTO_INCREMENT,
  raw VARCHAR(4095) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  order_id BIGINT,
  trade_dt VARCHAR(31),
  PRIMARY KEY (id)
);

CREATE TABLE reward_plan_map (
  id BIGINT NOT NULL AUTO_INCREMENT,
  uid VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  plan_ids VARCHAR(127) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  start_time TIMESTAMP NOT NULL,
  expire_time TIMESTAMP,
  PRIMARY KEY (id)
);

CREATE TABLE org_agent_order_stats (
  org_agent_id VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  prof_id VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  order_id BIGINT NOT NULL,
  product_id INT NOT NULL,
  qty FLOAT NOT NULL,
  actual_cost FLOAT NOT NULL,
  creation_time TIMESTAMP NOT NULL,
  status INT NOT NULL,
  PRIMARY KEY (order_id),
  INDEX(org_agent_id)
);

CREATE VIEW org_order_stats AS
  SELECT
    orderstats.*, agents.org_id
  FROM
    org_agent_order_stats orderstats
      INNER JOIN
    prof_org_agents agents ON agents.agent_id = orderstats.org_agent_id