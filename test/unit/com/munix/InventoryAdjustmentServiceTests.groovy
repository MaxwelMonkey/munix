package com.munix

import grails.test.*

class InventoryAdjustmentServiceTests extends GrailsUnitTestCase {
    
	def inventoryAdjustmentService = new InventoryAdjustmentService()
	def sampleProduct
	def sampleInventoryAdjustment
	def sampleInventoryAdjustmentItem
	def sampleItemType
	def sampleWarehouse
	def sampleStock
	
	protected void setUp() {
        super.setUp()
		setUpItemType()
		setUpWarehouse()
		setUpProduct()
		setUpStock()
		setUpProductStockRelationship()
		setUpInventoryAdjustment()
		setUpInventoryAdjustmentItem()
		setUpInventoryAdjustmentRelationship()
    }

	private void setUpProductStockRelationship() {
		sampleProduct.addToStocks(sampleStock)
		sampleProduct.save()
	}
	
	private void setUpStock() {
		sampleStock = new Stock(
				warehouse : sampleWarehouse,
				product : sampleProduct,
				qty : new BigDecimal("10.00")
			)
		mockDomain(Stock,[sampleStock])
	}
	
	private void setUpItemType() {
		sampleItemType = new ItemType(
				identifier : "testItemType",
				description : "testItemTypeDescription"
			)
		mockDomain(ItemType, [sampleItemType])
	}
	
	private void setUpWarehouse() {
		sampleWarehouse = new Warehouse(
				identifier : "testWarehouse",
				description : "testWarehouseDescription"
			)
		mockDomain(Warehouse, [sampleWarehouse])
	}
	
	private void setUpProduct() {
		sampleProduct = new Product(id: 1, 
			identifier : "testProduct",
			)
		mockDomain(Product, [sampleProduct])
	}
	
	private void setUpInventoryAdjustmentRelationship() {
		sampleInventoryAdjustment.addToItems(sampleInventoryAdjustmentItem)
		sampleInventoryAdjustment.save()
		sampleInventoryAdjustmentItem.adjustment = sampleInventoryAdjustment
		sampleInventoryAdjustmentItem.save()
	}
	
	private void setUpInventoryAdjustment() {
		sampleInventoryAdjustment = new InventoryAdjustment(
				status : InventoryAdjustment.Status.UNAPPROVED,
				preparedBy : "Jeje",
				itemType : sampleItemType,
				warehouse : sampleWarehouse,
				dateGenerated : new Date()
			)
		mockDomain(InventoryAdjustment, [sampleInventoryAdjustment])
	}

	private void setUpInventoryAdjustmentItem() {
		sampleInventoryAdjustmentItem = new InventoryAdjustmentItem(
				product : sampleProduct,
				newStock : new BigDecimal("2.00"),
				oldStock : new BigDecimal("1.00"),
				isDeleted : false
			)
		mockDomain(InventoryAdjustmentItem, [sampleInventoryAdjustmentItem])
	}
	
    void testGenerateInventoryAdjustmentItems() {
		def items = inventoryAdjustmentService.generateInventoryAdjustmentItems(sampleInventoryAdjustment)
		assertEquals "Items must contain testProduct", sampleProduct.id, items.get(0)["productId"]
		assertEquals "Items must get the old stock of the inventory adjustment item", sampleInventoryAdjustmentItem.product.getStock(sampleWarehouse).qty, items.get(0)["oldStock"]
		assertEquals "Stock difference must be 1", new BigDecimal("1.00"), items.get(0)["stockDifference"]
    }
	
	void testUpdateProductStock() {
		inventoryAdjustmentService.updateProductStock(sampleInventoryAdjustment)
		assertEquals "Product stock must now be equal to inventory adjustment item new stock.", sampleInventoryAdjustmentItem.newStock, sampleProduct.getStock(sampleWarehouse).qty 
	}
	
	protected void tearDown() {
		super.tearDown()
	}
}
