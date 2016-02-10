SELECT * FROM sales_order s WHERE s.status != 'Cancelled' AND (s.discount  != (SELECT rate FROM discount_group dg WHERE dg.id = s.discount_group_id))
