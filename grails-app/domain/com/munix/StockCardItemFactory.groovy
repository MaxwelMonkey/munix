package com.munix

abstract class StockCardItemFactory {

    public static StockCardItem createInitialBalanceEntry() {
		return new StockCardItem(
			type : StockCardItem.Type.INITIAL_ENTRY,
			balance: BigDecimal.ZERO
		)
	}

    public static StockCardItem createUnapprovedSalesDeliveryItem(SalesDeliveryItem salesDeliveryItem) {
		StockCardItem item = createSalesDeliveryItem(salesDeliveryItem)
		item.type = StockCardItem.Type.UNAPPROVED_SALES_DELIVERY
        item.warehouseIn = salesDeliveryItem.delivery.warehouse.identifier
        item.qtyIn = salesDeliveryItem.qty
		return item
	}
	
    public static StockCardItem createApprovedSalesDeliveryItem(SalesDeliveryItem salesDeliveryItem) {
		StockCardItem item = createSalesDeliveryItem(salesDeliveryItem)
		item.type = StockCardItem.Type.APPROVED_SALES_DELIVERY
        item.warehouseOut = salesDeliveryItem.delivery.warehouse.identifier
        item.qtyOut = salesDeliveryItem.qty
		return item
	}

    private static StockCardItem createSalesDeliveryItem(SalesDeliveryItem salesDeliveryItem) {
		new StockCardItem(
			dateOpened: salesDeliveryItem.delivery.date,
			datePosted: new Date(),
			referenceId: salesDeliveryItem.delivery.salesDeliveryId,
			supplierCustomer: salesDeliveryItem.delivery.customer.name,
			sellingAmount: salesDeliveryItem.discountedPrice,
			linkId: salesDeliveryItem.delivery.id,
            balance: salesDeliveryItem.product.getTotalStock()
		)
	}

    public static StockCardItem createUnapprovedMaterialReleaseItem(MaterialReleaseItem materialReleaseItem) {
		StockCardItem item = createMaterialReleaseItem(materialReleaseItem)
		item.type = StockCardItem.Type.UNAPPROVED_MATERIAL_RELEASE
        item.warehouseIn = materialReleaseItem.materialRelease.warehouse.identifier
        item.qtyIn = materialReleaseItem.qty
		return item
	}
    public static StockCardItem createApprovedMaterialReleaseItem(MaterialReleaseItem materialReleaseItem) {
		StockCardItem item = createMaterialReleaseItem(materialReleaseItem)
		item.type = StockCardItem.Type.APPROVED_MATERIAL_RELEASE
        item.warehouseOut = materialReleaseItem.materialRelease.warehouse.identifier
        item.qtyOut = materialReleaseItem.qty
		return item
	}

    private static StockCardItem createMaterialReleaseItem(MaterialReleaseItem materialReleaseItem) {
		def materialRequisitionItem = materialReleaseItem.materialRequisitionItem
        new StockCardItem(
			dateOpened: materialReleaseItem.materialRelease.date,
			datePosted: new Date(),
			referenceId: materialReleaseItem.materialRelease.toString(),
			costLocal: materialRequisitionItem.computeCostPerUnit(),
			linkId:  materialReleaseItem.materialRelease.id,
            balance: materialRequisitionItem.component.getTotalStock()
		)
	}
	
	public static StockCardItem createApprovedPurchaseInvoiceItem(PurchaseInvoiceItem purchaseInvoiceItem) {
		def stockCardItem = createPurchaseInvoiceItem(purchaseInvoiceItem)
		stockCardItem.type = StockCardItem.Type.APPROVED_PURCHASE_INVOICE
		stockCardItem.warehouseIn = purchaseInvoiceItem.purchaseInvoice.warehouse.identifier
		stockCardItem.qtyIn = purchaseInvoiceItem.qty
		return stockCardItem
	}
	
	public static StockCardItem createUnapprovedPurchaseInvoiceItem(PurchaseInvoiceItem purchaseInvoiceItem) {
		def stockCardItem = createPurchaseInvoiceItem(purchaseInvoiceItem)
		stockCardItem.type = StockCardItem.Type.UNAPPROVED_PURCHASE_INVOICE
		stockCardItem.warehouseOut = purchaseInvoiceItem.purchaseInvoice.warehouse.identifier
		stockCardItem.qtyOut = purchaseInvoiceItem.qty
		return stockCardItem
	}
	
	private static StockCardItem createPurchaseInvoiceItem(PurchaseInvoiceItem purchaseInvoiceItem) {
		new StockCardItem(
			dateOpened: purchaseInvoiceItem.purchaseInvoice.date,
			datePosted: new Date(),
			referenceId: purchaseInvoiceItem.purchaseInvoice.reference +" / "+purchaseInvoiceItem.purchaseInvoice.supplierReference?:"",
			supplierCustomer: purchaseInvoiceItem.purchaseInvoice.supplier.name,
			costForeign: purchaseInvoiceItem?.discountedPrice,
			currencyForeign: purchaseInvoiceItem?.purchaseInvoice?.supplier?.currency,
			costLocal: purchaseInvoiceItem?.localDiscountedPrice,
			linkId: purchaseInvoiceItem.purchaseInvoice.id,
			balance: purchaseInvoiceItem.purchaseOrderItem.product.getTotalStock()
		)
	}
	
	public static StockCardItem createApprovedCreditMemoItem(CreditMemoItem creditMemoItem) {
		StockCardItem item = createCreditMemoItem(creditMemoItem)
		if (creditMemoItem.creditMemo.isADebitMemo()) {
			item.type = StockCardItem.Type.APPROVED_DEBIT_MEMO
		} else {
			item.type = StockCardItem.Type.APPROVED_CREDIT_MEMO
		}
		item.warehouseIn = creditMemoItem.creditMemo?.warehouse?creditMemoItem.creditMemo?.warehouse?.identifier:creditMemoItem.deliveryItem.delivery.warehouse.identifier
		item.qtyIn = creditMemoItem.oldQty - creditMemoItem.newQty
		return item
	}
	
	public static StockCardItem createUnapprovedCreditMemoItem(CreditMemoItem creditMemoItem) {
		StockCardItem item = createCreditMemoItem(creditMemoItem)
		if (creditMemoItem.creditMemo.isADebitMemo()) {
			item.type = StockCardItem.Type.UNAPPROVED_DEBIT_MEMO
		} else {
			item.type = StockCardItem.Type.UNAPPROVED_CREDIT_MEMO
		}
		item.warehouseOut = creditMemoItem.creditMemo?.warehouse?creditMemoItem.creditMemo?.warehouse?.identifier:creditMemoItem.deliveryItem.delivery.warehouse.identifier
		item.qtyOut = creditMemoItem.oldQty - creditMemoItem.newQty
		return item
	}

	private static StockCardItem createCreditMemoItem(CreditMemoItem creditMemoItem) {
		new StockCardItem(
			dateOpened: creditMemoItem.date,
			datePosted: new Date(),
			referenceId: creditMemoItem.creditMemo.toString(),
			supplierCustomer: creditMemoItem.creditMemo.customer.name,
			sellingAmount: creditMemoItem.computeDiscountedAmount(),
			linkId: creditMemoItem.creditMemo.id,
			balance: creditMemoItem.deliveryItem.product.getTotalStock()
		)
	}
	
	public static StockCardItem createApprovedJobOutItem(JobOut jobOut) {
		StockCardItem item = createJobOutItem(jobOut)
		item.type = StockCardItem.Type.APPROVED_JOB_OUT
		item.warehouseIn = jobOut.warehouse.identifier
		item.qtyIn = jobOut.qty
		return item
	}
	
	public static StockCardItem createUnapprovedJobOutItem(JobOut jobOut) {
		StockCardItem item = createJobOutItem(jobOut)
		item.type = StockCardItem.Type.UNAPPROVED_JOB_OUT
		item.warehouseOut = jobOut.warehouse.identifier
		item.qtyOut = jobOut.qty
		return item
	}

	private static StockCardItem createJobOutItem(JobOut jobOut) {
		new StockCardItem(
			dateOpened: jobOut.date,
			datePosted: new Date(),
			referenceId: jobOut.toString(),
			linkId: jobOut.id,
			balance: jobOut.jobOrder.product.getTotalStock()
		)
	}
	
	public static StockCardItem createApprovedInventoryTransferItem(InventoryTransferItem inventoryTransferItem) {
		def stockCardItem = createInventoryTransferItem(inventoryTransferItem)
		stockCardItem.type = StockCardItem.Type.APPROVED_INVENTORY_TRANSFER
		stockCardItem.warehouseIn = inventoryTransferItem.transfer.destinationWarehouse.identifier
		stockCardItem.qtyIn = inventoryTransferItem.qty
		stockCardItem.warehouseOut = inventoryTransferItem.transfer.originWarehouse.identifier
		stockCardItem.qtyOut = inventoryTransferItem.qty
		return stockCardItem
	}	
	
	private static StockCardItem createInventoryTransferItem(InventoryTransferItem inventoryTransferItem) {
		new StockCardItem(
			dateOpened: inventoryTransferItem.transfer.date,
			datePosted: new Date(),
			referenceId: inventoryTransferItem.transfer.toString(),
			linkId: inventoryTransferItem.id,
			balance: inventoryTransferItem.product.getTotalStock()
		)
	}
	
	public static StockCardItem createApprovedInventoryAdjustmentItem(InventoryAdjustmentItem inventoryAdjustmentItem) {
		def stockCardItem = createInventoryAdjustmentItem(inventoryAdjustmentItem)
		stockCardItem.type = StockCardItem.Type.APPROVED_INVENTORY_ADJUSTMENT

		if((inventoryAdjustmentItem.newStock - inventoryAdjustmentItem.oldStock) < 0) {
			stockCardItem.warehouseOut = inventoryAdjustmentItem.adjustment.warehouse.identifier
			stockCardItem.qtyOut = inventoryAdjustmentItem.oldStock - inventoryAdjustmentItem.newStock
		} else {
			stockCardItem.warehouseIn = inventoryAdjustmentItem.adjustment.warehouse.identifier
			stockCardItem.qtyIn = inventoryAdjustmentItem.newStock - inventoryAdjustmentItem.oldStock
		}

		return stockCardItem
	}
	
	private static StockCardItem createInventoryAdjustmentItem(InventoryAdjustmentItem inventoryAdjustmentItem) {
		new StockCardItem(
			dateOpened: inventoryAdjustmentItem.adjustment.dateGenerated,
			datePosted: new Date(),
			referenceId: inventoryAdjustmentItem.adjustment.toString(),
			linkId: inventoryAdjustmentItem.id,
			balance: inventoryAdjustmentItem.product.getTotalStock()
		)
	}
}
