import java.math.BigDecimal;

import com.munix.CreditMemoItem;
import com.munix.Product;
import com.munix.SalesDeliveryItem;

public class CreditMemoRobotKeywords {
	public BigDecimal oldQty;
	public BigDecimal oldPrice;
	public BigDecimal newQty;
	public BigDecimal newPrice;
	
	public CreditMemoItem creditMemoItem = new CreditMemoItem();
	public SalesDeliveryItem deliveryItem = new SalesDeliveryItem();
	
	public void CreditMemoItemWithValues (int oldQty, int oldPrice, int newQty, int newPrice){
		this.oldQty = new BigDecimal(oldQty);
		this.oldPrice = new BigDecimal(oldPrice);
		this.newQty = new BigDecimal(newQty);
		this.newPrice = new BigDecimal(newPrice);
	}
	
	public String ComputeCredit (){
		Product product = new Product();
		product.setIdentifier("hi");
		deliveryItem.setProduct(product);
		deliveryItem.setQty(oldQty);
		deliveryItem.setPrice(oldPrice);
		creditMemoItem.setOldQty(oldQty);
		creditMemoItem.setOldPrice(oldPrice);
		creditMemoItem.setNewQty(newQty);
		creditMemoItem.setNewPrice(newPrice);
		creditMemoItem.setDeliveryItem(deliveryItem);
		BigDecimal finalCreditAmount = creditMemoItem.computeFinalAmount();
		return finalCreditAmount.toString();
	}
}