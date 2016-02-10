SELECT * FROM sales_order s WHERE s.status != 'Cancelled' AND (s.discount  = '0' AND s.discount_group_id != (SELECT id FROM discount_group dg WHERE dg.rate = '0'))
