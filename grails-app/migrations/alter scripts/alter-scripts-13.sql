INSERT INTO `customer_charge_counter_receipts` (`counter_receipt_id`, `customer_charge_id`) SELECT counter_receipt_id, id from customer_charge where counter_receipt_id is not null;

update sales_delivery set is_taken_by_counter_receipt = false where is_taken_by_counter_receipt is null;

update customer_charge set is_taken_by_counter_receipt = false where is_taken_by_counter_receipt is null;

update bounced_check set is_taken_by_counter_receipt = false where is_taken_by_counter_receipt is null;

update credit_memo set is_taken_by_counter_receipt = false where is_taken_by_counter_receipt is null;

update customer_charge set is_taken_by_counter_receipt = true where id IN (Select customer_charge_id from customer_charge_counter_receipts where counter_receipt_id IN (Select id from counter_receipt where status != 'Unpaid'));

update customer_charge set is_taken_by_counter_receipt = false where id IN (Select customer_charge_id from customer_charge_counter_receipts where counter_receipt_id IN (Select id from counter_receipt where status = 'Unpaid'));

update sales_delivery set is_taken_by_counter_receipt = true where id IN (Select sales_delivery_id from sales_delivery_counter_receipts where counter_receipt_id IN (Select id from counter_receipt where status != 'Unpaid'));

update sales_delivery set is_taken_by_counter_receipt = false where id IN (Select sales_delivery_id from sales_delivery_counter_receipts where counter_receipt_id IN (Select id from counter_receipt where status = 'Unpaid'));

