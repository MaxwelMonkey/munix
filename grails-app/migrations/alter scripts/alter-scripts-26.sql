insert into check_payment_bounced_checks 
(check_payment_id, bounced_check_id) 
  (select id, bounced_check_id 
   from check_payment 
	where bounced_check_id is not null
);

insert into check_payment_check_deposits 
(check_payment_id, check_deposit_id) 
  (select id, check_deposit_id 
   from check_payment 
	where check_deposit_id is not null
);
