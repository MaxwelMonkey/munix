update sales_order as so set so.discount = (select rate from discount_group dg where dg.id = so.discount_group_id) where so.discount_group_id is not null and so.discount=0
