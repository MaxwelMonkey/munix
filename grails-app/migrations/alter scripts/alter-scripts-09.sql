update sales_order_item as soi set is_net = (select is_net from product p where soi.product_id = p.id limit 1);

