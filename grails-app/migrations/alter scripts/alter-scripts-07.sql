update direct_payment_invoice set type = "CREDIT_MEMO" where type = "Credit Memo";
update direct_payment_invoice set type = "SALES_DELIVERY" where type = "Delivery";
update direct_payment_invoice set type = "CUSTOMER_CHARGE" where type = "Charge";
update direct_payment_invoice set type = "BOUNCED_CHECK" where type = "Bounced Check";
