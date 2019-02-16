INSERT INTO products (id, name, price0, detailed_info, keywords) VALUES (1, '产品1', 15.99, '产品1详细信息', 'kw1,kw2');
INSERT INTO products (id, name, price0, detailed_info, keywords) VALUES (2, '产品2', 139.99, '产品2详细信息', 'kw1,kw2,kw4');
INSERT INTO products (id, name, price0, detailed_info, keywords) VALUES (3, '产品3', 9.99, '产品3详细信息', 'kw2,kw4');
INSERT INTO products (id, name, price0, detailed_info, keywords) VALUES (4, '产品4', 1499.99, '产品4详细信息', 'kw1,kw2,kw5,kw6');

INSERT INTO customers (id, name, idcard_no, mobile, postal_addr, bday) VALUES ('uid=customer1', '张晓东', '3102030222313322', '13892929133', '邮寄地址1','1983-02-05');
INSERT INTO customers (id, name, idcard_no, mobile, postal_addr, bday) VALUES ('uid=customer2', '张晓', '31020555555555555', '1385555555', '邮寄地址2','1973-09-15');
INSERT INTO customers (id, name, idcard_no, mobile, postal_addr, bday) VALUES ('uid=customer3', '晓东', '3102036666666666', '1386666666', '邮寄地址1','1983-12-03');

INSERT INTO orders (customer_id, product_id, creation_time, proc_time1, proc_time2, proc_time3) VALUES ('customer3', 1, '2018-02-11 19:30:44', NULL, NULL, NULL);
INSERT INTO orders (customer_id, product_id, creation_time, proc_time1, proc_time2, proc_time3) VALUES ('customer3', 2, '2018-02-11 19:30:44', NULL, NULL, NULL);
INSERT INTO orders (customer_id, product_id, creation_time, proc_time1, proc_time2, proc_time3) VALUES ('customer2', 3, '2017-03-04 18:35:04', NULL, NULL, NULL);
INSERT INTO orders (customer_id, product_id, creation_time, proc_time1, proc_time2, proc_time3) VALUES ('customer2', 4, '2017-02-11 19:30:44', NULL, NULL, NULL);
INSERT INTO orders (customer_id, product_id, creation_time, proc_time1, proc_time2, proc_time3) VALUES ('customer2', 1, '2017-02-01 09:30:44', NULL, NULL, NULL);
