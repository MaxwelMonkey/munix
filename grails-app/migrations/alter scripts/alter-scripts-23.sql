UPDATE payment_type SET deductible_from_sales = false;
UPDATE price_adjustment SET date_generated = STR_TO_DATE(substring(prepared_by, length(prepared_by) - 23, length(prepared_by)), '%b. %e, %Y - %h:%i %p');
UPDATE purchase_invoice SET status = UPPER(status);
UPDATE product_component SET components_idx = 0;