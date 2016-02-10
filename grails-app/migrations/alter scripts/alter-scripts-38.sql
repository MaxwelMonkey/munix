update supplier_payment_item spi set spi.check_branch = (
select identifier from bank_branch where spi.check_bank_id = id
), spi.check_bank_id = (
select bank_id from bank_branch where spi.check_bank_id = id
);
