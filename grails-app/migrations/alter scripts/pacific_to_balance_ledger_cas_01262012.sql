DELETE FROM check_payment_check_deposits where check_payment_id in (select id from check_payment where direct_payment_item_id in (select id from direct_payment_item where direct_payment_id is null));

Update customer set customer_account_id = null;
TRUNCATE customer_account;

Update customer set customer_ledger_id = null;
TRUNCATE customer_ledger_entry_customer_ledger_entry;
TRUNCATE customer_ledger_entry;
TRUNCATE customer_ledger;

update sales_order as so set so.discount = (select rate from discount_group dg where dg.id = so.discount_group_id) where so.discount_group_id is not null and so.discount=0 and customer_id IN (29,225);

UPDATE sales_delivery SET amount_paid = amount_paid - .10 WHERE id = 1521;
UPDATE sales_delivery SET amount_paid = amount_paid + .10 WHERE id = 1675;
UPDATE direct_payment_invoice SET amount=amount+0.10 WHERE id=922;
UPDATE direct_payment_invoice SET amount=amount-0.10 WHERE id=925;
DELETE FROM check_payment_check_deposits WHERE check_payment_id=285;
UPDATE check_payment SET status='PENDING' WHERE id=285;
DELETE FROM check_payment_check_deposits WHERE check_payment_id=918;
UPDATE check_payment SET status='PENDING' WHERE id=918;
