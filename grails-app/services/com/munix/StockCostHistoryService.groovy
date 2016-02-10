package com.munix
import java.math.RoundingMode;


class StockCostHistoryService {
	def generalMethodService
	
    static transactional = true

    def generateStockCostItems(Product product) {
		def stockCostItems = []
		def purchaseInvoiceItems = getPurchaseInvoices(product)
		def jobOuts = getJobOuts(product)
		purchaseInvoiceItems.each {
			stockCostItems.add(convertPurchaseInvoiceItemToStockCostItem(it))
		}
		jobOuts.each {
			stockCostItems.add(convertJobOutToStockCostItem(it))
		}
		return stockCostItems.sort{it.date}
    }
	
	private List<PurchaseInvoiceItem> getPurchaseInvoices(Product product) {
		PurchaseInvoiceItem.withCriteria {
			and {
				purchaseInvoice {
					or {
						eq("status", PurchaseInvoice.Status.APPROVED)
						eq("status", PurchaseInvoice.Status.PAID)
					}
				}
				purchaseOrderItem {
					eq ("product", product)
				}
			}
		}
	}
	
	private List<JobOut> getJobOuts(Product product) {
		JobOut.withCriteria {
			and {
				eq("status", JobOut.Status.APPROVED)
				jobOrder {
					eq ("product", product)
				}
			}
		}
	}
	
	private Map convertPurchaseInvoiceItemToStockCostItem(PurchaseInvoiceItem purchaseInvoiceItem) {
		def stockCostItem = [:]
		stockCostItem.date = generalMethodService.getDateFromApprovedBy(purchaseInvoiceItem.purchaseInvoice.approvedBy)
		stockCostItem.supplier = purchaseInvoiceItem.purchaseInvoice.supplier
        stockCostItem.purchaseInvoice = purchaseInvoiceItem.purchaseInvoice
		stockCostItem.reference = purchaseInvoiceItem.purchaseInvoice.reference
		stockCostItem.supplierReference = purchaseInvoiceItem.purchaseInvoice.supplierReference?:""
		stockCostItem.currency = purchaseInvoiceItem.purchaseInvoice.supplier?.currency?.toString()
		stockCostItem.costForeign = purchaseInvoiceItem.purchaseInvoice.supplier?.currency?.toString() + " " + purchaseInvoiceItem?.discountedPrice.setScale(4,RoundingMode.DOWN)
		stockCostItem.exchangeRate = purchaseInvoiceItem.purchaseInvoice?.exchangeRate
		stockCostItem.costLocal = purchaseInvoiceItem.localDiscountedPrice
		stockCostItem.qty = purchaseInvoiceItem.qty
		stockCostItem.foreignCurrencyFinalPrice = purchaseInvoiceItem.finalPrice
		return stockCostItem
	}
	
	private Map convertJobOutToStockCostItem(JobOut jobOut) {
		def laborCost = jobOut.laborCost?.amount ?: BigDecimal.ZERO
		def componentsCost =  jobOut.jobOrder?.requisition?.computeTotalCostOfMaterialsPerAssembly() ?: BigDecimal.ZERO
		def totalCostPerAssembly = laborCost + componentsCost
		def stockCostItem = [:]
 		stockCostItem.id = jobOut.id
		stockCostItem.date = generalMethodService.getDateFromApprovedBy(jobOut.approvedBy)
		stockCostItem.reference = jobOut.toString()
		stockCostItem.currency = ""
		stockCostItem.costLocal = totalCostPerAssembly
		stockCostItem.qty = jobOut.qty
		return stockCostItem
	}
}
