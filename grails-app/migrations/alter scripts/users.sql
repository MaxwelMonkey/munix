INSERT INTO user(id, version, enabled, passwd, user_real_name, username, email_show, email, description) 
VALUES 
(51, 1, true, "cdb0e76c1a69873cbdcdbe0a142d56c023dc9f22", "Role Test", "role.sales", false, "", "ROLE_SALES");

INSERT INTO user(id, version, enabled, passwd, user_real_name, username, email_show, email, description) 
VALUES 
(52, 1, true, "cdb0e76c1a69873cbdcdbe0a142d56c023dc9f22", "Role Purchasing", "role.purchasing", false, "", "ROLE_PURCHASING");

INSERT INTO user(id, version, enabled, passwd, user_real_name, username, email_show, email, description) 
VALUES 
(53, 1, true, "cdb0e76c1a69873cbdcdbe0a142d56c023dc9f22", "Role Production", "role.production", false, "", "ROLE_PRODUCTION");

INSERT INTO user(id, version, enabled, passwd, user_real_name, username, email_show, email, description) 
VALUES 
(54, 1, true, "cdb0e76c1a69873cbdcdbe0a142d56c023dc9f22", "Role Manager Sales", "role.manager.sales", false, "", "ROLE_MANAGER_SALES");

INSERT INTO user(id, version, enabled, passwd, user_real_name, username, email_show, email, description) 
VALUES 
(55, 1, true, "cdb0e76c1a69873cbdcdbe0a142d56c023dc9f22", "Role Manager Purchasing", "role.manager.purchasing", false, "", "ROLE_MANAGER_PURCHASING");

INSERT INTO user(id, version, enabled, passwd, user_real_name, username, email_show, email, description) 
VALUES 
(56, 1, true, "cdb0e76c1a69873cbdcdbe0a142d56c023dc9f22", "Role Manager Production", "role.manager.production", false, "", "ROLE_MANAGER_PRODUCTION");

INSERT INTO user(id, version, enabled, passwd, user_real_name, username, email_show, email, description) 
VALUES 
(57, , true, "cdb0e76c1a69873cbdcdbe0a142d56c023dc9f22", "Role Manager Delivery", "role.manager.delivery", false, "", "ROLE_MANAGER_DELIVERY");

INSERT INTO user(id, version, enabled, passwd, user_real_name, username, email_show, email, description) 
VALUES 
(58, 1, true, "cdb0e76c1a69873cbdcdbe0a142d56c023dc9f22", "Role Manager Accounting", "role.manager.accounting", false, "", "ROLE_MANAGER_ACCOUNTING");

INSERT INTO user(id, version, enabled, passwd, user_real_name, username, email_show, email, description) 
VALUES 
(59, 1, true, "cdb0e76c1a69873cbdcdbe0a142d56c023dc9f22", "Role Delivery", "role.delivery", false, "", "ROLE_DELIVERY");

INSERT INTO user(id, version, enabled, passwd, user_real_name, username, email_show, email, description) 
VALUES 
(60, 1, true, "cdb0e76c1a69873cbdcdbe0a142d56c023dc9f22", "Role Admin Purchasing", "role.admin.purchasing", false, "", "ROLE_ADMIN_PURCHASING");

INSERT INTO user(id, version, enabled, passwd, user_real_name, username, email_show, email, description) 
VALUES 
(61, 1, true, "cdb0e76c1a69873cbdcdbe0a142d56c023dc9f22", "Role Admin", "role.admin", false, "", "ROLE_ADMIN");

INSERT INTO user(id, version, enabled, passwd, user_real_name, username, email_show, email, description) 
VALUES 
(62, 1, true, "cdb0e76c1a69873cbdcdbe0a142d56c023dc9f22", "Role Accounting", "role.accounting", false, "", "ROLE_ACCOUNTING");

INSERT INTO role_people(role_id, user_id) VALUES (6, 51);
INSERT INTO role_people(role_id, user_id) VALUES (1, 52);
INSERT INTO role_people(role_id, user_id) VALUES (4, 53);
INSERT INTO role_people(role_id, user_id) VALUES (7, 54);
INSERT INTO role_people(role_id, user_id) VALUES (2, 55);
INSERT INTO role_people(role_id, user_id) VALUES (5, 56);
INSERT INTO role_people(role_id, user_id) VALUES (9, 57);
INSERT INTO role_people(role_id, user_id) VALUES (11, 58);
INSERT INTO role_people(role_id, user_id) VALUES (8, 59);
INSERT INTO role_people(role_id, user_id) VALUES (3, 60);
INSERT INTO role_people(role_id, user_id) VALUES (12, 61);
INSERT INTO role_people(role_id, user_id) VALUES (10, 62);
