package com.munix

class SupplierPayment {
	
	static enum Status {
		UNAPPROVED("Unapproved"), APPROVED("Approved"), CANCELLED("Cancelled")
		
		String name
		
		Status(String name) {
			this.name = name
		}
		
		@Override
		public String toString() {
			return name
		}
	}
	
    Supplier supplier
    Date date = new Date()
    Status status = Status.UNAPPROVED
    String approvedBy
    String preparedBy
	String cancelledBy
	String remark

    static hasMany = [items:SupplierPaymentItem, purchaseInvoices:PurchaseInvoice]
    
    static constraints = {
        supplier(nullable:false)
        date(nullable:false)
        status(nullable:false)
        approvedBy(nullable:true)
        preparedBy(nullable:false)
		cancelledBy(nullable:true)
		remark(nullable:true)
    }

    String toString(){
        "SP-${formatId()}"
    }

    String formatId(){
        "${id}".padLeft(8,'0')
    }

    String formatPaymentTotal(){
        "${String.format( '%,.2f',computePaymentTotal() )}"
    }

    String formatInvoiceTotal(){
        "${String.format( '%,.2f',computeInvoiceTotal() )}"
    }

    String formatRemainingBalance(){
        "${String.format( '%,.2f',computeRemainingBalance() )}"
    }

    BigDecimal computePaymentTotal(){
        def total = 0
        items?.each{
            total += it.amount
        }
        return total
    }

    BigDecimal computeInvoiceTotal(){
        def total = 0
        purchaseInvoices?.each{
            total += it.computePurchaseInvoiceDiscountedForeignTotal()
        }
        return total
    }

    BigDecimal computeRemainingBalance(){
        computeInvoiceTotal() - computePaymentTotal()
    }
	
	void approve(){
		status = Status.APPROVED
	}
	
	void cancel(){
		status = Status.CANCELLED
	}

	void unapprove(){
		status = Status.UNAPPROVED
	}
	
	def isApproved(){
		return status == Status.APPROVED
	}
	
	def isCancelled(){
		return status == Status.CANCELLED
	}
	def isUnapproved(){
		return status == Status.UNAPPROVED
	}

	def isFullyPaid() {
		return this.computeRemainingBalance().compareTo(BigDecimal.ZERO) < 1
	}
}
