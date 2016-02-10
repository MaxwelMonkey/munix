import java.math.BigDecimal;
import com.munix.CreditMemoItem;
import com.munix.Product;
import com.munix.SalesDeliveryItem;

public class CreditMemoFixture {
	public BigDecimal oldQty;
	public BigDecimal oldPrice;
	public BigDecimal newQty;
	public BigDecimal newPrice;

	public CreditMemoFixture() {
		super();
	}

	public CreditMemoItem creditMemoItem = new CreditMemoItem();
	public SalesDeliveryItem deliveryItem = new SalesDeliveryItem();
	
	public void setOldQty(int oldQty){
		this.oldQty = new BigDecimal(oldQty);
	}
	
	public void setOldPrice(int oldPrice){
		this.oldPrice = new BigDecimal(oldPrice);
	}
	
	public void setNewQty(int newQty){
		this.newQty = new BigDecimal(newQty);
	}
	
	public void setNewPrice(int newPrice){
		this.newPrice = new BigDecimal(newPrice);
	}
	
	public BigDecimal finalCredit(){
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
		return finalCreditAmount;
	}
}

