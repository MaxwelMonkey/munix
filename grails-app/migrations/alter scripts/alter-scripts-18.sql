INSERT INTO purchase_invoice_item(id, version, final_price, purchase_invoice_id, purchase_order_item_id, qty, received_items_idx, items_idx) SELECT id, version, final_price, invoice_id, po_item_id, qty, received_items_idx, items_idx FROM purchase_delivery_item;

UPDATE purchase_invoice SET status = UPPER(status);
UPDATE supplier_payment SET status = UPPER(status);
