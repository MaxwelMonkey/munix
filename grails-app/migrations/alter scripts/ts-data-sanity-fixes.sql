update product set type_id = (select id from discount_type where description = "Parts & Accessories") where type_id = null;
