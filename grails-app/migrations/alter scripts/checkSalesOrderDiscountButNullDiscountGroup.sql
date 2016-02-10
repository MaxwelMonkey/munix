SELECT * FROM sales_order s WHERE s.status != 'Cancelled' AND (s.discount > 0 AND s.discount_group_id IS NULL);
