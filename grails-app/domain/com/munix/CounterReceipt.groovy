package com.munix

class CounterReceipt {
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

    Date date = new Date()
    Date counterDate
    Date collectionDate
    Date dueDate
    Status status = Status.UNAPPROVED
    Customer customer
    String preparedBy
    String approvedBy
	String cancelledBy
	String remark

    static hasMany = [
		items: CounterReceiptItem,
		printLogs: PrintLogCounterReceipt
	]
	
    static belongsTo = [SalesDelivery, CustomerCharge, CreditMemo, BouncedCheck]
	static transients = ['deliveries', 'charges', 'creditMemos', 'bouncedChecks']

    static constraints = {
        date(nullable: false)
        counterDate(nullable: true)
        collectionDate(nullable: true)
        status(nullable: false)
        customer(nullable: false)
        preparedBy(nullable: false)
        approvedBy(nullable: true)
		cancelledBy(nullable:true)
		remark(nullable:true)
        dueDate(nullable: true )
		items(cascade:"all-delete-orphan,delete")
    }

    void cancel() {
        status = Status.CANCELLED
		freeUpCustomerPayments()
    }

    void approve() {
        status = Status.APPROVED
		freeUpCustomerPayments()
    }
	
    void unapprove() {
        status = Status.UNAPPROVED
		takeUpCustomerPayments()
    }

    def isCancelled() {
        return status == Status.CANCELLED
    }

    def isApproved() {
        return status == Status.APPROVED
    }

    def isUnapproved() {
        return status == Status.UNAPPROVED
    }

    String toString() {
        "CR-${formatId()}"
    }

    String formatId() {
         "${id}".padLeft(8,'0')
    }

    BigDecimal computeDeliveryTotal() {
        def total = 0
        deliveries.each {
            total += it.computeTotalAmount()
        }
        return total
    }

    BigDecimal computeChargeTotal() {
        def total = 0
        charges.each {
            total += it.computeTotalAmount()
        }
        return total
    }
	
	BigDecimal computeCreditMemoTotal() {
		def total = 0
		creditMemos.each {
			total += it.computeCreditMemoTotalAmount().abs()
		}
		return total
	}
	
	BigDecimal computeBouncedCheckTotal() {
		def total = 0
		bouncedChecks.each {
			total += it.computeTotalAmount()
		}
		return total
	}

    BigDecimal computeDeliveryAmountPaidTotal() {
        def total = 0
        deliveries.each {
            total += it.amountPaid
        }
        return total
    }

    BigDecimal computeChargeAmountPaidTotal() {
        def total = 0
        charges.each {
            total += it.amountPaid
        }
        return total
    }
	
	BigDecimal computeCreditMemoAmountPaidTotal() {
		def total = 0
		creditMemos.each {
			total += it.amountPaid
		}
		return total
	}
	
	BigDecimal computeBouncedCheckAmountPaidTotal() {
		def total = 0
		bouncedChecks.each {
			total += it.amountPaid
		}
		return total
	}

    BigDecimal computeDeliveryAmountDueTotal() {
        def total = 0
        deliveries.each {
            total += it.computeProjectedDue()
        }
        return total
    }
	
	BigDecimal computeSalesDeliveryAmountDueTotal() {
		def total = 0
		deliveries.each {
			total += it.computeAmountDue()
		}
		return total
	}

    BigDecimal computeChargeAmountDueTotal() {
        def total = 0
        charges.each {
            total += it.computeProjectedDue()
        }
        return total
    }
	
	BigDecimal computeCreditMemoAmountDueTotal() {
		def total = 0
		creditMemos.each {
			total += it.computeProjectedDue()
		}
		return total
	}
	
	BigDecimal computeBouncedCheckAmountDueTotal() {
		def total = 0
		bouncedChecks.each {
			total += it.computeProjectedDue()
		}
		return total
	}
	
    BigDecimal computeTotal() {
        return computeDeliveryTotal() + computeChargeTotal()
    }
	
	BigDecimal computeInvoicesTotal() {
		def total = 0
		items.each { 
			total += it.amount
		}
		return total
	}

    BigDecimal computeAmountDueTotal() {
        return computeDeliveryAmountDueTotal() + computeChargeAmountDueTotal()
    }
	
	BigDecimal computeInvoicesAmountDueTotal() {
		return computeSalesDeliveryAmountDueTotal() + computeChargeAmountDueTotal() + computeCreditMemoAmountDueTotal() + computeBouncedCheckAmountDueTotal()
	}
	
    BigDecimal computeAmountPaidTotal() {
        return computeDeliveryAmountPaidTotal() + computeChargeAmountPaidTotal() + computeCreditMemoAmountPaidTotal() + computeBouncedCheckAmountPaidTotal()
    }
	
	BigDecimal computeOriginalAmountTotal() {
		def total = 0
		items.each {
			total += it.invoice.computeTotalAmount().abs()
		}
		return total
	}

	private void freeUpCustomerPayments() {
		deliveries.each {
			it.notTakenByCounterReceipt()
		}
		charges.each {
			it.notTakenByCounterReceipt()
		}
		creditMemos.each {
			it.notTakenByCounterReceipt()
		}
		bouncedChecks.each {
			it.notTakenByCounterReceipt()
		}
	}
	
	private void takeUpCustomerPayments() {
		deliveries.each {
			it.takenByCounterReceipt()
		}
		charges.each {
			it.takenByCounterReceipt()
		}
		creditMemos.each {
			it.takenByCounterReceipt()
		}
		bouncedChecks.each {
			it.takenByCounterReceipt()
		}
	}
	
	List<CounterReceiptItem> getDeliveries() {
		retrieveCustomerPaymentInItemsWithClass(SalesDelivery.class)
	}
	
	List<CounterReceiptItem> getCharges() {
		retrieveCustomerPaymentInItemsWithClass(CustomerCharge.class)
	}
	
	List<CounterReceiptItem> getCreditMemos() {
		retrieveCustomerPaymentInItemsWithClass(CreditMemo.class)
	}
	
	List<CounterReceiptItem> getBouncedChecks() {
		retrieveCustomerPaymentInItemsWithClass(BouncedCheck.class)
	}

	private List retrieveCustomerPaymentInItemsWithClass(Class className) {
		def customerPayments = []
		def invoicesWithGivenClass = retrieveItemInvoicesWithClass(className)
		invoicesWithGivenClass.each {
			customerPayments.add(it.invoice)
		}
		return customerPayments
	}	
	
	private Set retrieveItemInvoicesWithClass(Class className) {
		this.items.findAll{it.invoice.class == className}
	}
}
