update purchase_order set discount_rate = 0;
update product set retail_price = 0 where retail_price is null;
update product set running_cost = 0 where running_cost is null;
update sales_order_item so, product p set so.cost = p.running_cost where so.product_id = p.id;
update sales_delivery_item sd, product p set sd.cost = p.running_cost where sd.product_id = p.id;