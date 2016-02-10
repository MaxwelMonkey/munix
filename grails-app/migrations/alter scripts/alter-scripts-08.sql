update bago.product as bp set bp.item_type_id = (select type_id from luma.product lp where bp.identifier = lp.identifier limit 1);

update munix.sales_delivery as SD set SD.direct_payment_invoice_id = (select id from munix.direct_payment_invoice_delivery as DPSD where SD.id = DPSD.sales_delivery_id limit 1);


