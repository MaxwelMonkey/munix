package com.munix

class DirectPayment {
    Date date = new Date()
    String status = "Unapproved"
    Customer customer
    String preparedBy
    String approvedBy
	String cancelledBy
    String remark
	Overpayment overpayment

    static hasMany = [invoices:DirectPaymentInvoice, items:DirectPaymentItem, printLogs:PrintLogDirectPayment]
	
    static constraints = {
        date(nullable:false)
        status(nullable:false)
        customer(nullable:false)
        preparedBy(nullable:false)
        approvedBy(nullable:true)
		cancelledBy(nullable:true)
        remark(nullable:true)
		overpayment(nullable:true)
    }

	String isNotUnapprovable() {
		def result
		items.each {
			if (it.isNotUnapprovable()) {
				result = it.isNotUnapprovable()
			}
		}
		return result
	}
	
    String toString(){
        "DP-${formatId()}"
    }

    String formatId(){
         "${id}".padLeft(8,'0')
    }

    String formatInvoiceTotal(){
        "PHP ${String.format( '%,.2f',computeInvoiceTotal() )}"
    }
    String formatPaymentTotal(){
        "PHP ${String.format( '%,.2f',computePaymentTotal() )}"
    }
    String formatBalance(){
        "PHP ${String.format( '%,.2f',computeBalance() )}"
    }

    BigDecimal computeBalance() {
		return computePaymentTotal() - computeInvoiceTotal()
	}

	BigDecimal computePaymentTotal() {
		def total = 0
		items.each {
			total += it.amount
		}
		return total
	}

    BigDecimal computeInvoiceTotal(){
        def total = 0
        invoices.each{
            total += it.amount
        }
        return total
    }

    void approved(){
        status = "Approved"
    }

    void unapproved(){
        status = "Unapproved"
    }

    void cancelled(){
        status = "Cancelled"
    }

    def isApproved(){
        return status == "Approved"
    }
	
    def isUnapproved(){
        return status == "Unapproved"
    }
    def isCancelled(){
        return status == "Cancelled"
    }
	
    void addDirectPaymentInvoice(DirectPaymentInvoice directPaymentInvoice){
        this.addToInvoices(directPaymentInvoice)
    }

    void removeDirectPaymentItem(DirectPaymentItem directPaymentItemInstance){
		if (directPaymentItemInstance != null) {
			this.removeFromItems(directPaymentItemInstance)
			directPaymentItemInstance.delete()
		}
    }
	
    List<DirectPaymentItem> directPaymentItemsWithCreditMemo() {
        return DirectPaymentItem.withCriteria {
            paymentType {
                eq('description', 'Credit Memo')
            }
            eq('directPayment',this)
        }
    }
	
    void approveTransaction(){
        approved()
        invoices.each {
            it.getRelatedCustomerPayment().finishTransaction(it.amount)
        }
        def itemList = directPaymentItemsWithCreditMemo()
        itemList.each {
            it.relatedCreditMemo().finishTransaction(it.amount)
        }
    }
	
    void unapproveTransaction(){
        unapproved()
        invoices.each{
            it.getRelatedCustomerPayment().rollbackTransaction(it.amount)
        }
        def itemList = directPaymentItemsWithCreditMemo()
        itemList.each{
            it.relatedCreditMemo().rollbackTransaction(it.amount)
        }
    }
	
    void unassociateItems(){
        invoices.each{
            def customerPayment = it.getRelatedCustomerPayment()
            customerPayment.notTakenByDirectPayment()
        }

        def itemList = directPaymentItemsWithCreditMemo()
        itemList.each{
            def creditMemo = it.relatedCreditMemo()
            creditMemo.notTakenByDirectPayment()
        }
    }
}
