INSERT INTO products (id, name, price0, detailed_info, keywords)  VALUES (1, 'Astaxin虾青素', 1499.99, '{"srcCountry":"SV","desc":"虾青素清除自由基的能力是维生素C的功效的6000倍；是维生素E的1000倍；是辅酶Q10的800倍；是一氧化氮的1800倍；是纳豆的3100倍；是花青素的700倍；是β-胡萝卜素功效的10倍；是lycopene（番茄红素）功效的1800倍；是carotol（叶黄素）功效的200倍；是teapolyphenols（茶多酚）功效的320倍。"}', '抗氧化剂,美白');
INSERT INTO products (id, name, price0, detailed_info, keywords)  VALUES (2, '瑞典ACO Gravid孕妇产妇复合维生素', 139.99, '{"srcCountry":"SV","desc":"含有12种维生素和10种矿物质。针对孕期和哺乳期女性对于营养的额外需求添加了如叶酸、铁、钙和维生素D在内的多种维生素和矿物质。含有重要的抗氧化功能的维生素C和E，能够防止DNA，蛋白质和脂肪的氧化变化。"}', '孕产妇,维生素,矿物质');
INSERT INTO products (id, name, price0, detailed_info, keywords)  VALUES (3, 'Pharbio Omega-3 Forte 70%高纯度深海鱼油', 9.99, '{"srcCountry":"SV","desc":"瑞典销量最好，品质最高的成人鱼油，也是世界上纯度最高的鱼油，是瑞典国家药房鱼油销量冠军。70%的Omega3 含量（DHA/EPA), 瑞典医生唯一推荐孕妇哺乳期间补充DHA/EPA的鱼油，含DHA有助于宝宝大脑、眼睛等器官的发育。Pharbio产品完全按照欧洲严格的药品标准生产，高纯度源自先进的生产工艺，去除了鱼油本身含有的大部分饱和脂肪酸。在普通天然鱼油中，大约含有20-25%的不需要的饱和脂肪酸，而Pharbio Omega-3 Forte胶囊只含有5%不需要的饱和脂肪酸。将对人体有害的饱和脂肪酸尽量剔除并提纯（世界上没有任何一款鱼油是可以完全剔除饱和脂肪酸的），从营养学角度认为非重要的脂肪酸性物质以及可能存在的环境毒素和重金属也被尽量剔除，确保是健康有效的鱼油，纯度世界第一高！"}', '鱼油,Omega-3');
INSERT INTO products (id, name, price0, detailed_info, keywords)  VALUES (4, 'LIFE Q10 100mg 辅酶', 149.99, '{"srcCountry":"US","desc":"每粒Life Q10包含30毫克辅酶Q10，也叫作辅酶q，它是一种脂溶性的类似维生素的物质。Q10的主要功能是在细胞线粒体能量（三磷酸腺苷）合成时充当辅酶。所有的细胞都是靠三磷酸腺苷才能进行各种活动流程，因此辅酶对身体所有的细胞而言都是至关重要的，如此一来，辅酶对所有组织和器官的重要性也就不言而喻了。"}', '辅酶Q10');

INSERT INTO med_profs (prof_id, name, idcard_no, mobile, pass_hash)  VALUES ('prof1','李卫东','3302030222313322','13792929133',X'3c9909afec25354d551dae21590bb26e38d53f2173b8d3dc3eee4c047e7ab1c1eb8b85103e3be7ba613b31bb5c9c36214dc9f14a42fd7a2fdb84856bca5c44c2');
INSERT INTO med_profs (prof_id, name, idcard_no, mobile, pass_hash)  VALUES ('prof2','张鑫','33020555555555555','1375555555',X'f6b07b6c1340e947b861def5f8b092d8ee710826dc56bd175bdc8f3a16b0b8acf853c64786a710dedf9d1524d61e32504e27d60de159af110bc3941490731578');
INSERT INTO med_profs (prof_id, name, idcard_no, mobile, pass_hash)  VALUES ('prof3','陈伟达','3302036666666666','1376666666',X'e32ef19623e8ed9d267f657a81944b3d07adbb768518068e88435745564e8d4150a0a703be2a7d88b61e3d390c2bb97e2d4c311fdc69d6b1267f05f59aa920e7');

INSERT INTO customers (uid, name, idcard_no, mobile, postal_addr, bday, ref_uid, pass_hash)  VALUES ('customer1','张晓东','3102030222313322','13892929133','邮寄地址1','1983-02-05','prof1',X'3c9909afec25354d551dae21590bb26e38d53f2173b8d3dc3eee4c047e7ab1c1eb8b85103e3be7ba613b31bb5c9c36214dc9f14a42fd7a2fdb84856bca5c44c2');
INSERT INTO customers (uid, name, idcard_no, mobile, postal_addr, bday, ref_uid, pass_hash)  VALUES ('customer2','张晓','31020555555555555','1385555555','邮寄地址2','1983-02-05','prof1',X'f6b07b6c1340e947b861def5f8b092d8ee710826dc56bd175bdc8f3a16b0b8acf853c64786a710dedf9d1524d61e32504e27d60de159af110bc3941490731578');
INSERT INTO customers (uid, name, idcard_no, mobile, postal_addr, bday, ref_uid, pass_hash)  VALUES ('customer3','晓东','3102036666666666','1386666666','邮寄地址3','1983-12-03','prof1',X'e32ef19623e8ed9d267f657a81944b3d07adbb768518068e88435745564e8d4150a0a703be2a7d88b61e3d390c2bb97e2d4c311fdc69d6b1267f05f59aa920e7');
INSERT INTO customers (uid, name, idcard_no, mobile, postal_addr, bday, ref_uid, pass_hash)  VALUES ('customer4','王丽','3102033333333333','13833333333','邮寄地址4','1983-12-03','prof2',X'79681fe463f8f34fda5d8bebb59e61b6099c031cf2aa251116cd4bedbec59430f82a02868807315f66a75e408f4f106f65394dfa792413620f9f79ce3a2baef6');

INSERT INTO orders (customer_id, product_id, qty, creation_time, proc_time1, proc_time2, proc_time3) VALUES ('customer3', 1, 1.0, '2018-02-11 19:30:44', NULL, NULL, NULL);
INSERT INTO orders (customer_id, product_id, qty, creation_time, proc_time1, proc_time2, proc_time3) VALUES ('customer1', 2, 3.0, '2018-02-11 19:30:44', NULL, NULL, NULL);
INSERT INTO orders (customer_id, product_id, qty, creation_time, proc_time1, proc_time2, proc_time3) VALUES ('customer2', 3, 5.0, '2017-03-04 18:35:04', NULL, NULL, NULL);
INSERT INTO orders (customer_id, product_id, qty, creation_time, proc_time1, proc_time2, proc_time3) VALUES ('customer1', 4, 2.0, '2017-02-11 19:30:44', NULL, NULL, NULL);
INSERT INTO orders (customer_id, product_id, qty, creation_time, proc_time1, proc_time2, proc_time3) VALUES ('customer2', 1, 4.0, '2017-02-01 09:30:44', NULL, NULL, NULL);
INSERT INTO orders (customer_id, product_id, qty, creation_time, proc_time1, proc_time2, proc_time3) VALUES ('customer1', 2, 10.0, '2018-12-01 09:30:44', '2018-12-01 09:32:44', NULL, NULL);
INSERT INTO orders (customer_id, product_id, qty, creation_time, proc_time1, proc_time2, proc_time3) VALUES ('customer1', 3, 2.0, '2018-09-01 09:30:44', '2018-09-02 09:30:44', NULL, NULL);
INSERT INTO orders (customer_id, product_id, qty, creation_time, proc_time1, proc_time2, proc_time3) VALUES ('customer1', 3, 3.0, '2019-03-17 19:30:44', NULL, NULL, NULL);

INSERT INTO price_plans (id, info, defi, vtag)  VALUES ('Fixed-0.8','Fixed Rate 80%','{"globalRate":0.8}','FixedRate');
INSERT INTO price_plans (id, info, defi, vtag)  VALUES ('Fixed-0.9','Fixed Rate 90%','{"globalRate":0.9}','FixedRate');
INSERT INTO price_plans (id, info, defi, vtag)  VALUES ('ProdBased-Basic','Producted Based Basic, range: 80% - 90%','{"globalRate":0.9,"prodRates":{"1":0.8,"2":0.85}}','ProductBasedRates');
INSERT INTO price_plans (id, info, defi, vtag)  VALUES ('ProdBased-Advanced','Producted Based Advanced, range: 75% - 85%','{"globalRate":0.85,"prodRates":{"1":0.8,"2":0.75}}','ProductBasedRates');

INSERT INTO price_plan_map (uid, plan_ids, start_time)  VALUES ('prof1','Fixed-0.9','2019-03-04T16:00:28.179');
INSERT INTO price_plan_map (uid, plan_ids, start_time)  VALUES ('prof1','Fixed-0.8','2019-03-11T16:00:28.239');
INSERT INTO price_plan_map (uid, plan_ids, start_time)  VALUES ('prof2','ProdBased-Basic,Fixed-0.9','2019-03-11T16:00:28.239');
INSERT INTO price_plan_map (uid, plan_ids, start_time)  VALUES ('customer1','ProdBased-Advanced','2019-03-11T16:00:28.24');

INSERT INTO reward_plans (id, info, defi, vtag)  VALUES ('Fixed-0.01','Fixed Rate 1%','{"globalRate":0.01}','FixedRate');
INSERT INTO reward_plans (id, info, defi, vtag)  VALUES ('Fixed-0.02','Fixed Rate 2%','{"globalRate":0.02}','FixedRate');
INSERT INTO reward_plans (id, info, defi, vtag)  VALUES ('Fixed-1.5','Fixed Rate x 1.5, used only in combination with other plans','{"globalRate":1.5}','FixedRate');
INSERT INTO reward_plans (id, info, defi, vtag)  VALUES ('ProdBased-Basic','Producted Based Basic, range: 1% - 3%','{"globalRate":0.01,"prodRates":{"1":0.02,"2":0.03}}','ProductBasedRates');
INSERT INTO reward_plans (id, info, defi, vtag)  VALUES ('ProdBased-Advanced','Producted Based Advanced, range: 2% - 5%','{"globalRate":0.02,"prodRates":{"1":0.03,"2":0.05}}','ProductBasedRates');

INSERT INTO reward_plan_map (uid, plan_ids, start_time)  VALUES ('prof1','Fixed-0.01','2019-03-05T08:18:27.597');
INSERT INTO reward_plan_map (uid, plan_ids, start_time)  VALUES ('prof1','Fixed-0.02','2019-03-12T08:18:27.655');
INSERT INTO reward_plan_map (uid, plan_ids, start_time)  VALUES ('prof2','ProdBased-Basic,Fixed-1.5','2019-03-12T08:18:27.655');
INSERT INTO reward_plan_map (uid, plan_ids, start_time)  VALUES ('customer1','ProdBased-Advanced','2019-03-12T08:18:27.655');

INSERT INTO prof_orgs (org_id, name, info, org_no, phone, join_date, pass_hash)  VALUES ('prof_org1','医药公司1','医药公司1 info','org_no1','13792929133','2018-03-04T16:00:28.179',X'3c9909afec25354d551dae21590bb26e38d53f2173b8d3dc3eee4c047e7ab1c1eb8b85103e3be7ba613b31bb5c9c36214dc9f14a42fd7a2fdb84856bca5c44c2');
INSERT INTO prof_orgs (org_id, name, info, org_no, phone, join_date, pass_hash)  VALUES ('prof_org2','医药公司2','医药公司2 info','org_no2','1375555555','2019-08-04T16:00:28.179',X'f6b07b6c1340e947b861def5f8b092d8ee710826dc56bd175bdc8f3a16b0b8acf853c64786a710dedf9d1524d61e32504e27d60de159af110bc3941490731578');
