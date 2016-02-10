DELETE FROM check_payment_check_deposits where check_payment_id in (select id from check_payment where direct_payment_item_id in (select id from direct_payment_item where direct_payment_id is null));
