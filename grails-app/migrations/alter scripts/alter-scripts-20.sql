update price_adjustment_item as bp set old_price = (select whole_sale_price from munix_test.product as lp where lp.id = bp.product_id)
where 'WHOLESALE' = (select price_type from price_adjustment where id = bp.price_adjustment_id) and old_price = 0;

update price_adjustment_item as bp set old_price = (select retail_price from munix_test.product as lp where lp.id = bp.product_id)
where 'RETAIL' = (select price_type from price_adjustment where id = bp.price_adjustment_id) and old_price = 0;

