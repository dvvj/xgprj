CREATE TABLE products (
  id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  detailed_info VARCHAR(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
  PRIMARY KEY (id)
);
