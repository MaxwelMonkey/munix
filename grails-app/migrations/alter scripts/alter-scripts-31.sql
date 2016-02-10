update stock_card_item s, purchase_invoice p
set s.cost_foreign = (s.cost_foreign * (1 - (p.discount_rate / 100))), 
s.cost_local = (s.cost_local * (1 - (p.discount_rate / 100)))
where p.id = s.link_id
and (
  s.type = "APPROVED_PURCHASE_INVOICE"
  or s.type = "UNAPPROVED_PURCHASE_INVOICE"
);

update sales_order so, discount_group d
set net_discount = d.rate
where so.net_discount_group_id = d.id;