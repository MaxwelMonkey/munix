alter table `customer` drop column branch_id;

UPDATE customer SET bil_addr_city_id=1 where bil_addr_city_id IS NULL;
TRUNCATE bounced_check;

alter table `customer` MODIFY COLUMN `bil_addr_city_id` BIGINT(20)  NOT NULL;

