package com.munix

public enum CustomerPaymentType {
	CREDIT_MEMO("Credit Memo"), SALES_DELIVERY("Delivery"), BOUNCED_CHECK("Bounced Check"),
	CUSTOMER_CHARGE("Charge")

	String name

	CustomerPaymentType(String name) {
		this.name = name
	}

	@Override
	public String toString() {
		return name
	}
}