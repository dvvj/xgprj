INSERT INTO products (id, name, price0, detailed_info, keywords) VALUES (1, '产品1', 15.99, '产品1详细信息', 'kw1,kw2');
INSERT INTO products (id, name, price0, detailed_info, keywords) VALUES (2, '产品2', 139.99, '产品2详细信息', 'kw1,kw2,kw4');
INSERT INTO products (id, name, price0, detailed_info, keywords) VALUES (3, '产品3', 9.99, '产品3详细信息', 'kw2,kw4');
INSERT INTO products (id, name, price0, detailed_info, keywords) VALUES (4, '产品4', 1499.99, '产品4详细信息', 'kw1,kw2,kw5,kw6');

INSERT INTO customers (uid, name, idcard_no, mobile, postal_addr, bday, ref_uid, pass_hash)  VALUES ('customer1','张晓东','3102030222313322','13892929133','邮寄地址1','1983-02-05','TestRef-4ae8f129bf1c',X'3c9909afec25354d551dae21590bb26e38d53f2173b8d3dc3eee4c047e7ab1c1eb8b85103e3be7ba613b31bb5c9c36214dc9f14a42fd7a2fdb84856bca5c44c2');
INSERT INTO customers (uid, name, idcard_no, mobile, postal_addr, bday, ref_uid, pass_hash)  VALUES ('customer2','张晓','31020555555555555','1385555555','邮寄地址2','1983-02-05','TestRef-4ae8f129bf1c',X'f6b07b6c1340e947b861def5f8b092d8ee710826dc56bd175bdc8f3a16b0b8acf853c64786a710dedf9d1524d61e32504e27d60de159af110bc3941490731578');
INSERT INTO customers (uid, name, idcard_no, mobile, postal_addr, bday, ref_uid, pass_hash)  VALUES ('customer3','晓东','3102036666666666','1386666666','邮寄地址3','1983-12-03','TestRef-4ae8f129bf1c',X'e32ef19623e8ed9d267f657a81944b3d07adbb768518068e88435745564e8d4150a0a703be2a7d88b61e3d390c2bb97e2d4c311fdc69d6b1267f05f59aa920e7');
INSERT INTO customers (uid, name, idcard_no, mobile, postal_addr, bday, ref_uid, pass_hash)  VALUES ('customer4','王丽','3102033333333333','13833333333','邮寄地址4','1983-12-03','TestRef-4ae8f129bf1c',X'79681fe463f8f34fda5d8bebb59e61b6099c031cf2aa251116cd4bedbec59430f82a02868807315f66a75e408f4f106f65394dfa792413620f9f79ce3a2baef6');

INSERT INTO orders (customer_id, product_id, qty, creation_time, proc_time1, proc_time2, proc_time3) VALUES ('customer3', 1, 1.0, '2018-02-11 19:30:44', NULL, NULL, NULL);
INSERT INTO orders (customer_id, product_id, qty, creation_time, proc_time1, proc_time2, proc_time3) VALUES ('customer1', 2, 3.0, '2018-02-11 19:30:44', NULL, NULL, NULL);
INSERT INTO orders (customer_id, product_id, qty, creation_time, proc_time1, proc_time2, proc_time3) VALUES ('customer2', 3, 5.0, '2017-03-04 18:35:04', NULL, NULL, NULL);
INSERT INTO orders (customer_id, product_id, qty, creation_time, proc_time1, proc_time2, proc_time3) VALUES ('customer1', 4, 2.0, '2017-02-11 19:30:44', NULL, NULL, NULL);
INSERT INTO orders (customer_id, product_id, qty, creation_time, proc_time1, proc_time2, proc_time3) VALUES ('customer2', 1, 4.0, '2017-02-01 09:30:44', NULL, NULL, NULL);
