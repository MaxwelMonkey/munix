update sales_order set discount = 0 where discount_group_id is null and discount is not null;
update sales_order set net_discount = 0 where net_discount_group_id is null and net_discount is not null;
