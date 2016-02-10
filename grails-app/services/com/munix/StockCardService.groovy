package com.munix

class StockCardService {

    static transactional = true

    def createUnapprovedSalesDeliveryStockCards(SalesDelivery salesDelivery) {
        def product
        def stockCardItem
        salesDelivery.items.each{
            if(it.qty>0){
                product = it.product
                stockCardItem = StockCardItemFactory.createUnapprovedSalesDeliveryItem(it)
                addStockCardItemToProduct(product, stockCardItem)
            }
        }
    }
    def createApprovedSalesDeliveryStockCards(SalesDelivery salesDelivery) {
        def product
        def stockCardItem
        salesDelivery.items.each{
            if(it.qty>0){
                product = it.product
                stockCardItem = StockCardItemFactory.createApprovedSalesDeliveryItem(it)
                addStockCardItemToProduct(product, stockCardItem)
            }

        }
    }
    def createUnapprovedMaterialReleaseStockCards(MaterialRelease materialRelease) {
        def product
        def stockCardItem
        materialRelease.items.each{
            product = it.materialRequisitionItem.component
            stockCardItem = StockCardItemFactory.createUnapprovedMaterialReleaseItem(it)
           addStockCardItemToProduct(product, stockCardItem)
        }
    }
    def createApprovedMaterialReleaseStockCards(MaterialRelease materialRelease) {
        def product
        def stockCardItem
        materialRelease.items.each{
            product = it.materialRequisitionItem.component
            stockCardItem = StockCardItemFactory.createApprovedMaterialReleaseItem(it)
            addStockCardItemToProduct(product, stockCardItem)
        }
    }
	
	def createApprovedPurchaseInvoice(PurchaseInvoice purchaseInvoice) {
		purchaseInvoice.items.each{
			def product = it.purchaseOrderItem.product
			def stockCardItem = StockCardItemFactory.createApprovedPurchaseInvoiceItem(it)
			addStockCardItemToProduct(product, stockCardItem)
		}
	}
	
	def createApprovedInventoryTransfer(InventoryTransfer inventoryTransfer) {
		inventoryTransfer.items.each{ item ->
			def product = item.product
			def stockCardItem = StockCardItemFactory.createApprovedInventoryTransferItem(item)
			addStockCardItemToProduct(product, stockCardItem)
		}
	}
	
	def createApprovedInventoryAdjustment(InventoryAdjustment inventoryAdjustment){
		inventoryAdjustment.items.each{ item ->
			def product = item.product
			def stockCardItem = StockCardItemFactory.createApprovedInventoryAdjustmentItem(item)
			addStockCardItemToProduct(product, stockCardItem)
		}
	}
	
	def createUnapprovedPurchaseInvoice(PurchaseInvoice purchaseInvoice) {
		purchaseInvoice.items.each {
			def product = it.purchaseOrderItem.product
			def stockCardItem = StockCardItemFactory.createUnapprovedPurchaseInvoiceItem(it)
			addStockCardItemToProduct(product, stockCardItem)
		}
	}
	
	def createApprovedCreditMemo(CreditMemo creditMemo) {
		creditMemo.items.each{
			if(it.hasQtyChanged()){
				def product = it.deliveryItem.product
				def stockCardItem = StockCardItemFactory.createApprovedCreditMemoItem(it)
				addStockCardItemToProduct(product, stockCardItem)
			}
		}
	}
	
	def createUnapprovedCreditMemo(CreditMemo creditMemo) {
		creditMemo.items.each {
			if(it.hasQtyChanged()){
				def product = it.deliveryItem.product
				def stockCardItem = StockCardItemFactory.createUnapprovedCreditMemoItem(it)
				addStockCardItemToProduct(product, stockCardItem)
			}
		}
	}
	
	def createApprovedJobOut(JobOut jobOut) {
		def product = jobOut.jobOrder.product
		def stockCardItem = StockCardItemFactory.createApprovedJobOutItem(jobOut)
		addStockCardItemToProduct(product, stockCardItem)
	}
	
	def createUnapprovedJobOut(JobOut jobOut) {
		def product = jobOut.jobOrder.product
		def stockCardItem = StockCardItemFactory.createUnapprovedJobOutItem(jobOut)
		addStockCardItemToProduct(product, stockCardItem)
	}
	
	private void addStockCardItemToProduct(Product product, StockCardItem stockCardItem) {
		stockCardItem.stockCard = product.stockCard
		stockCardItem.save()
			//product.stockCard.addToItems(stockCardItem)
			//product.save()
	}
}
