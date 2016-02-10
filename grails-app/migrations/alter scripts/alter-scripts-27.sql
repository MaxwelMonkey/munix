insert into counter_receipt_item(version, amount, counter_receipt_id, invoice_id, invoice_type) (SELECT 0, "0.00", counter_receipt_id, bounced_check_id, "BOUNCED_CHECK" from bounced_check_counter_receipts);
insert into counter_receipt_item(version, amount, counter_receipt_id, invoice_id, invoice_type) (SELECT 0, "0.00", counter_receipt_id, credit_memo_id, "CREDIT_MEMO" from credit_memo_counter_receipts);
insert into counter_receipt_item(version, amount, counter_receipt_id, invoice_id, invoice_type) (SELECT 0, "0.00", counter_receipt_id, customer_charge_id, "CUSTOMER_CHARGE" from customer_charge_counter_receipts);
insert into counter_receipt_item(version, amount, counter_receipt_id, invoice_id, invoice_type) (SELECT 0, "0.00", counter_receipt_id, sales_delivery_id, "SALES_DELIVERY" from sales_delivery_counter_receipts);

