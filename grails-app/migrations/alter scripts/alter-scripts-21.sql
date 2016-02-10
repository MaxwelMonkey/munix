update customer set status = replace(status, ' ', '');
UPDATE customer SET status = UPPER(status);
UPDATE direct_delivery SET status = UPPER(status);
UPDATE collection_schedule SET status = UPPER(status);
UPDATE check_payment SET status = UPPER(status);
update counter_receipt set status = UPPER(status);
