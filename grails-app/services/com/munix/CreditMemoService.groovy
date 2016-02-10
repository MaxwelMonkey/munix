package com.munix

class CreditMemoService {

    static transactional = true
    private static final String CANCELLED= "Cancelled"
    private static final String APPROVED= "Approved"
    private static final String PAID= "Paid"
    private static final String UNAPPROVED= "Unapproved"
	def customerLedgerService
    def stockCardService
    def customerAccountsSummaryService
    def authenticateService

    def getCreditMemoListForSalesDelivery(SalesDelivery salesDelivery) {
        def creditMemoItems = []
        if(salesDelivery){
            creditMemoItems = CreditMemoItem.withCriteria {
                order ("date","asc")
                and{
                    deliveryItem {
                        'in'("id", salesDelivery.items?.id)
                    }
                }
            }
        }

        return creditMemoItems
    }
	
    //check if a credit memo item exist that is not yet cancelled
    def checkIfCreditMemoExistForProduct(SalesDeliveryItem deliveryItemInstance, Date dateInstance){
        def creditMemoItem = CreditMemoItem.withCriteria{
            maxResults 1
            eq("deliveryItem", deliveryItemInstance)
            gt("date",dateInstance)
            creditMemo{
            	and{
            		ne("status",CANCELLED)
            		ne("status",APPROVED)
            		ne("status",PAID)
            	}
            }
        }
        return creditMemoItem? true:false
    }
	
    //check if a credit memo item exist that is not yet cancelled
    def checkIfUnapprovedCreditMemoExistForProduct(SalesDeliveryItem deliveryItemInstance){
        def creditMemoItem = CreditMemoItem.withCriteria{
            maxResults 1
            eq("deliveryItem", deliveryItemInstance)
            creditMemo{
            	or{
            		eq("status",UNAPPROVED)
            		eq("status","Second Approval Pending")
            	}
            }
        }
        return creditMemoItem? true:false
    }
	
    def checkIfAllCreditMemoItemsAreTheLatest(CreditMemo creditMemo){
        def checker = true
        creditMemo.items.each {
            if(it.deliveryItem.obtainLatestCreditMemoItem().id!=it.id){
                checker = false
            }
        }
        return checker
    }
    def getApprovedDebitMemoList(Map params, Customer customer) {
        if(!params.offset) params.offset = 0
        if(!params.max) params.max = 100

        def debitMemoList = getAllUnpaidDebitMemoList(customer)
        int total = debitMemoList.size()
        int upperLimit = findUpperIndex(params.offset, params.max, total)
        def filteredDebitMemoList = []
        if(total>0){
            filteredDebitMemoList = debitMemoList.getAt(params.offset..upperLimit)
        }
        return [debitMemoList:filteredDebitMemoList,totalCount : debitMemoList.size()]
    }

    private static int findUpperIndex(int offset, int max, int total) {
        max = offset + max - 1
        if (max >= total) {
            max -= max - total + 1
        }
        return max
    }
	def generateList(Map params) {
		def searchIdentifier = params.searchIdentifier
		def searchCustomerName = params.searchCustomerName
		def searchCustomerId = params.searchCustomerId
		def searchDiscountType = params.searchDiscountType
		def searchReason = params.searchReason
		def searchStatus = params.searchStatus
		
		def query = {
			and{
				if(searchIdentifier){
					like('id', Long.parseLong(searchIdentifier))
				}
				if(searchCustomerName){
					customer{
						like('name', "%${searchCustomerName}%")
					}
				}
				if(searchCustomerId){
					customer{
						like('identifier', "%${searchCustomerId}%")
					}
				}
				if(searchStatus){
					eq('status', searchStatus)
				}
				if(searchDiscountType){
					discountType{
						like('identifier', "%${searchDiscountType}%")
					}
				}
				if(searchReason){
					reason{
						like('identifier', "%${searchReason}%")
					}
				}
			}
		}
		return CreditMemo.createCriteria().list(params,query)
	}
    List<CreditMemo> getAllUnpaidDebitMemoList(Customer customer){
        def unpaidCreditMemo = CreditMemo.findAllByCustomerAndStatus(customer,"Approved")
        def debitMemos = []
        unpaidCreditMemo.each{
            if(it.isADebitMemo()){
                debitMemos.add(it)
            }
        }
		return debitMemos
	}

	def computeTotalUnpaidDebitMemos(Customer customer){
		def debitMemos = getAllUnpaidDebitMemoList(customer)
		BigDecimal totalUnpaidDebitMemo = BigDecimal.ZERO
		debitMemos.each{
			totalUnpaidDebitMemo += it.computeCreditMemoTotalAmount()
		}
		return totalUnpaidDebitMemo
	}
    def secondApproveCreditMemo(CreditMemo creditMemo){
        creditMemo.approvedTwoBy = FormatUtil.createTimeStamp(authenticateService.userDomain())
        creditMemo?.approve()
        if (creditMemo.isADebitMemo()) {
            customerLedgerService.createApprovedDebitMemo(creditMemo)
            customerAccountsSummaryService.addCustomerAccountDebitMemo(creditMemo)
        } else {
            customerLedgerService.createApprovedCreditMemo(creditMemo)
        }
        creditMemo?.items.each {
            def qty = it.oldQty - it.newQty
            def warehouse = creditMemo?.warehouse?creditMemo.warehouse:it.deliveryItem.delivery.warehouse
            it.deliveryItem.product.getStock(warehouse).qty += qty
        }

        stockCardService.createApprovedCreditMemo(creditMemo)
        if(creditMemo.save(flush: true)){
            return true
        } else {
            return false
        }
    }
    def unapproveCreditMemo(CreditMemo creditMemo){
        creditMemo.unapprove()
        if (creditMemo.isADebitMemo()) {
            customerLedgerService.createUnapprovedDebitMemo(creditMemo)
            if(creditMemo.approvedTwoBy){
                customerAccountsSummaryService.removeCustomerAccountDebitMemo(creditMemo)
            }
        } else {
            customerLedgerService.createUnapprovedCreditMemo(creditMemo)
        }

        creditMemo.setApprovedTwoBy("")
        creditMemo.setApprovedBy("")

        creditMemo?.items.each {
            def qty = it.oldQty - it.newQty
            def warehouse = creditMemo?.warehouse?creditMemo.warehouse:it.deliveryItem.delivery.warehouse
            it.deliveryItem.product.getStock(warehouse).qty -= qty
        }

        stockCardService.createUnapprovedCreditMemo(creditMemo)

        if(creditMemo.save(flush: true)){
            return true
        } else {
            return false
        }
    }
    def cancelCreditMemo(CreditMemo creditMemo){
        def isCancellable = false
        if(creditMemo.isUnapproved()){
            isCancellable = true
        }
        if(isCancellable){
            creditMemo?.cancelled()
            creditMemo?.save(flush: true)
        }
		return isCancellable
    }
}
