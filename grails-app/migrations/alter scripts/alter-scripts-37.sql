update purchase_order_item set items_idx = 0;

update purchase_order po
join purchase_order_id poid
on po.id = poid.po_id
set po.counter_id = CAST(substring(purchase_order_id, length(purchase_order_id)-4, length(purchase_order_id)) AS UNSIGNED),
po.year = poid.year