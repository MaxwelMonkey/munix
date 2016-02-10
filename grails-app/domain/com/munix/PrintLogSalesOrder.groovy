package com.munix

class PrintLogSalesOrder extends Log{
	SalesOrder salesOrder
	PrintType printType
    static constraints = {
		salesOrder(nullable:false)
    }
	def setPrintTypePrice(){
		printType=PrintType.PRICE
	}
	def setPrintTypeNoPrice(){
		printType=PrintType.NO_PRICE
	}
	PrintLogSalesOrder(){}
	PrintLogSalesOrder(salesOrder,user){
		this.user=user
		this.salesOrder= salesOrder
	}
	enum PrintType{
		PRICE("Price"),
		NO_PRICE("No Price")
		
		String description
		
		PrintType(String description){
			this.description = description
		}
		
		@Override
		public String toString() {
			return description
		}
	}
}
