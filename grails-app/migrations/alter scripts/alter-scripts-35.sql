DELETE FROM sales_delivery_item where order_item_id is null AND (select status from sales_delivery as sd where sd.id = delivery_id) = "Cancelled";

update customer_discount_log_item i,
customer_discount_log_customer_discount_log_item m
set i.customer_discount_log_id = m.customer_discount_log_items_id
where i.id = m.customer_discount_log_item_id;

update customer_discount c,
customer_discount_log l
set c.log_id = l.id
where l.discount_id = c.id
and l.discount_id is not null;
