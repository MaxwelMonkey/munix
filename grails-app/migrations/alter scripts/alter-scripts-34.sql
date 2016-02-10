update direct_payment_invoice as dpi set amount=
(
	select SUM(amount) 
	from check_payment where id in 
	(select check_payment_id from check_payment_bounced_checks where bounced_check_id in 
		(select bounced_check_invoices_id from bounced_check_direct_payment_invoice where direct_payment_invoice_id=dpi.id)
	)
) 
where 
(
((select status from bounced_check where id =
(select bounced_check_invoices_id from bounced_check_direct_payment_invoice where direct_payment_invoice_id=dpi.id) limit 1)

='Paid') and dpi.amount = 0);
