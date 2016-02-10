import java.math.BigDecimal;

import com.munix.CustomerLedgerEntry;
import com.munix.CustomerLedger;

public class CustomerLedgerRobotKeywords {

	public BigDecimal runningBalance;
	public BigDecimal amount;
	
	public void GivenRunningBalanceOf(double runningBalance) {
		this.runningBalance = new BigDecimal(runningBalance);
	}
	
	public void WhenIApproveASalesDeliveryWithFinalAmountOf(double amount) {
		this.amount = new BigDecimal(amount);
	}
	
	public String ComputeFinalDebit() {
		CustomerLedgerEntry entry = new CustomerLedgerEntry();
		entry.setAmount(amount);
		entry.computeDebit();
		return entry.getDebitAmount().setScale(2, BigDecimal.ROUND_HALF_UP).toString();
	}
	
	public String ComputeFinalRunningBalance() {
		CustomerLedger ledger = new CustomerLedger();
		ledger.setBalance(runningBalance);
		ledger.updateBalanceAddDebit(amount);
		return ledger.getBalance().setScale(2, BigDecimal.ROUND_HALF_UP).toString();
	}
}
