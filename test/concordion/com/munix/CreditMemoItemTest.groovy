package com.munix

import com.munix.CreditMemoItem;
import com.munix.Product;
import com.munix.SalesDeliveryItem;

class CreditMemoItemTest {
	
	public CreditMemoItem creditMemoItem = new CreditMemoItem();

    public Result computeFinalCredit(BigDecimal oldQty, BigDecimal oldPrice, BigDecimal newQty, BigDecimal newPrice) {
		
		creditMemoItem.setOldQty(oldQty);
		creditMemoItem.setOldPrice(oldPrice);
		creditMemoItem.setNewQty(newQty);
		creditMemoItem.setNewPrice(newPrice);
		
		Result result = new Result();
		result.finalCredit = creditMemoItem.computeFinalAmount().toString();
		return result;
    }
	
	class Result {
		public String finalCredit;
	}

}