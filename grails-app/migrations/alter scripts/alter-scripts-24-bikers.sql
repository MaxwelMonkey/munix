update munix.product_component as newpc, munix_backup.product_component as oldpc
set newpc.qty = oldpc.qty where newpc.id = oldpc.id;

update munix.direct_payment_item as dpi
set dpi.remark = (select remark from munix.credit_memo as cm where cm.direct_payment_item_id = dpi.id) where dpi.payment_type_id=13;

update munix.customer_ledger_entry as cle
set cle.remark = (select remark from munix.direct_payment_item as dpi where dpi.payment_type_id = 13 and dpi.direct_payment_id = cle.link_id and cle.details = 'CM' and cle.amount = dpi.amount) where cle.type='DIRECT_PAYMENT_ITEM' and cle.details = 'CM';
