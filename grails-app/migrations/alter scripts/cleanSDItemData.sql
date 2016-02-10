update sales_delivery_item as sdi set order_item_id = null where(select COUNT(id) from sales_order_item where id=sdi.order_item_id)=0;
