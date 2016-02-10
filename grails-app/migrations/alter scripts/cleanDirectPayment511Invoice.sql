DELETE FROM direct_payment_invoice where direct_payment_id = 511;
UPDATE direct_payment SET status="Cancelled" where id=511;
