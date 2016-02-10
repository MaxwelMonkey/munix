update check_payment cp, check_deposit_check_payment cdcp
set cp.check_deposit_id = cdcp.check_deposit_checks_id 
where cp.id = cdcp.check_payment_id