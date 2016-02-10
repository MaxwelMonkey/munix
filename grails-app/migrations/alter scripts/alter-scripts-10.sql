insert into customer_charge_counter_receipts 
(counter_receipt_id, customer_charge_id) 
  (select counter_receipt_id, id 
   from customer_charge 
   where customer_charge.counter_receipt_id is not null
);

alter table customer_charge drop column counter_receipt_id;
