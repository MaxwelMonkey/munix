
insert into user(username, user_real_name, passwd, enabled) values ("super.admin", "super.admin", "cdb0e76c1a69873cbdcdbe0a142d56c023dc9f22",true);

INSERT INTO `role` VALUES (1,39,'ROLE_PURCHASING','Purchasing Personnel'),(2,38,'ROLE_MANAGER_PURCHASING','Purchasing Manager'),(3,27,'ROLE_ADMIN_PURCHASING','Super Purchasing Supervisor'),(4,34,'ROLE_PRODUCTION','Production Personnel'),(5,28,'ROLE_MANAGER_PRODUCTION','Production Supervisor'),(6,47,'ROLE_SALES','Sales Personnel'),(7,28,'ROLE_MANAGER_SALES','Sales Supervisor'),(8,26,'ROLE_DELIVERY','Delivery Personnel'),(9,26,'ROLE_MANAGER_DELIVERY','Delivery Supervisor'),(10,31,'ROLE_ACCOUNTING','Accounting Personnel'),(11,25,'ROLE_MANAGER_ACCOUNTING','Accounting Supervisor'),(12,5,'ROLE_ADMIN','Administrator'),(13,10,'ROLE_SUPER','Costing');

insert into role_people (role_id, user_id) select r.id role_id, u.id user_id from user as u, role as r where u.username = "super.admin"
