update sales_delivery_item set items_idx = 4 where id = 4515;
update material_release_item set material_requisition_item_id = 1 where id = 6;
update material_release_item set material_requisition_item_id = 1 where id = 295;
update material_release_item set material_requisition_item_id = 1 where id = 379;
update material_release_item set material_requisition_item_id = 1 where id = 431;

delete from direct_payment_invoice where direct_payment_id = 511;

update product set type_id = (select id from discount_type where description = "Parts") where type_id = null;
