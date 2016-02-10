insert into direct_payment_invoice (id, version, amount, direct_payment_id, type)
values (1644, 1, 85500.00, 511, "SALES_DELIVERY");

update direct_payment set status="Approved" where id=511;

update sales_delivery_item set items_idx = 10 where id = 852;
update sales_delivery_item set items_idx = 11 where id = 853;

update product set type_id = (select id from discount_type where description = "Parts & Accessories") where type_id = null;
