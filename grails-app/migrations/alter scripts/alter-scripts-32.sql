update stock_card_item set date_posted = null;
CREATE TEMPORARY TABLE `temporary_table_for_post_date` (`id` BIGINT  NOT NULL,`date` DATETIME ,PRIMARY KEY (`id`))
ENGINE = MyISAM;

insert into temporary_table_for_post_date (id,date) select id,STR_TO_DATE(substring(approved_by,-24), '%b. %d, %Y - %h:%i %p') AS date from sales_delivery where status = "Unpaid"; 
update stock_card_item as sci set date_posted = (select date from temporary_table_for_post_date where id = sci.link_id and reference_id REGEXP "^SD" limit 1) where date_posted is null;
truncate table temporary_table_for_post_date;

insert into temporary_table_for_post_date (id,date) select id,STR_TO_DATE(substring(approved_by,-24), '%b. %d, %Y - %h:%i %p') AS date from purchase_invoice where status = "Approved"; 
update stock_card_item as sci set date_posted = (select date from temporary_table_for_post_date where id = sci.link_id and type like "%PURCHASE_INVOICE%" limit 1) where date_posted is null;
truncate table temporary_table_for_post_date;

insert into temporary_table_for_post_date (id,date) select id,STR_TO_DATE(substring(approved_by,-24), '%b. %d, %Y - %h:%i %p') AS date from material_release; 
update stock_card_item as sci set date_posted = (select date from temporary_table_for_post_date where id = sci.link_id and type like "%MATERIAL_RELEASE%" limit 1) where date_posted is null;
truncate table temporary_table_for_post_date;

insert into temporary_table_for_post_date (id,date) select id,STR_TO_DATE(substring(approved_by,-24), '%b. %d, %Y - %h:%i %p') AS date from credit_memo; 
update stock_card_item as sci set date_posted = (select date from temporary_table_for_post_date where id = sci.link_id and (type like "%CREDIT_MEMO%" or type like "%DEBIT_MEMO%" ) limit 1) where date_posted is null;
truncate table temporary_table_for_post_date;

insert into temporary_table_for_post_date (id,date) select id,STR_TO_DATE(substring(approved_by,-24), '%b. %d, %Y - %h:%i %p') AS date from job_out; 
update stock_card_item as sci set date_posted = (select date from temporary_table_for_post_date where id = sci.link_id and type like "%JOB_OUT%" limit 1) where date_posted is null;
truncate table temporary_table_for_post_date;

insert into temporary_table_for_post_date (id,date) select id,STR_TO_DATE(substring(received_by,-24), '%b. %d, %Y - %h:%i %p') AS date from inventory_transfer; 
update stock_card_item as sci set date_posted = (select date from temporary_table_for_post_date where id = sci.link_id and type like "%INVENTORY_TRANSFER%" limit 1) where date_posted is null;
truncate table temporary_table_for_post_date;
