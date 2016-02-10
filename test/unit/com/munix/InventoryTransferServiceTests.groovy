package com.munix

import grails.test.*

class InventoryTransferServiceTests extends GrailsUnitTestCase {
	def inventoryTransferService = new InventoryTransferService()
	def sampleInventoryTransfer
	def sampleInventoryTransferItem
	def sampleOriginWarehouse
	def sampleDestinationWarehouse
	def sampleProduct
	def sampleOriginStock
	def sampleDestinationStock
	
    protected void setUp() {
        super.setUp()
		setUpOriginWarehouse()
		setUpDestinationWarehouse()
		setUpProduct()
		setUpOriginStock()
		setUpDestinationStock()
		setUpProductStockRelationship()
		setUpInventoryTransfer()
		setUpInventoryTransferItem()
		setUpInventoryTransferRelationship()
    }

    protected void tearDown() {
        super.tearDown()
    }
	
	private void setUpInventoryTransferRelationship() {
		sampleInventoryTransfer.addToItems(sampleInventoryTransferItem)
		sampleInventoryTransfer.save()
		sampleInventoryTransferItem.transfer = sampleInventoryTransfer
		sampleInventoryTransferItem.save()
	}

	private void setUpProductStockRelationship() {
		sampleProduct.addToStocks(sampleOriginStock)
		sampleProduct.addToStocks(sampleDestinationStock)
		sampleProduct.save()
	}

	private void setUpOriginStock() {
		sampleOriginStock = new Stock(
				warehouse : sampleOriginWarehouse,
				product : sampleProduct,
				qty : new BigDecimal("5.00")
			)
		mockDomain(Stock,[sampleOriginStock])
	}

	private void setUpDestinationStock() {
		sampleDestinationStock = new Stock(
				warehouse : sampleDestinationWarehouse,
				product : sampleProduct,
				qty : new BigDecimal("10.00")
			)
		mockDomain(Stock,[sampleDestinationStock])
	}

	private void setUpProduct() {
		sampleProduct = new Product(id: 1,
			identifier : "testProduct",
			)
		mockDomain(Product, [sampleProduct])
	}

	private void setUpOriginWarehouse() {
		sampleOriginWarehouse = new Warehouse(
				identifier : "testOriginWarehouse",
				description : "testOriginWarehouseDescription"
			)
		mockDomain(Warehouse, [sampleOriginWarehouse])
	}

	private void setUpDestinationWarehouse() {
		sampleDestinationWarehouse = new Warehouse(
				identifier : "testDestinationWarehouse",
				description : "testDestinationWarehouseDescription"
			)
		mockDomain(Warehouse, [sampleDestinationWarehouse])
	}

	private setUpInventoryTransfer() {
		sampleInventoryTransfer = new InventoryTransfer(
				date : new Date(),
				originWarehouse : sampleOriginWarehouse,
				destinationWarehouse : sampleDestinationWarehouse,
				status : InventoryTransfer.Status.RECEIVED,
				preparedBy : "Jeje"
			)
		mockDomain(InventoryTransfer,[sampleInventoryTransfer])
	}
	
	private setUpInventoryTransferItem() {
		sampleInventoryTransferItem = new InventoryTransferItem(
				product : sampleProduct,
				qty : new BigDecimal("1"),
				originWarehouseQty : new BigDecimal("3.00"),
				destinationWarehouseQty : new BigDecimal("0.00")
			)
		mockDomain(InventoryTransferItem,[sampleInventoryTransferItem])
	}
	
    void testGenerateInventoryTransferItems() {
		def items = inventoryTransferService.generateInventoryTransferItems(sampleInventoryTransfer)
		assertEquals "Items must contain testProduct", sampleProduct.id, items.get(0)["productId"]
		assertEquals "Item must be equal to its origin stock", sampleInventoryTransferItem.originWarehouseQty, items.get(0)["originWarehouseStock"]
		assertEquals "Item must be equal to its destination stock", sampleInventoryTransferItem.destinationWarehouseQty, items.get(0)["destinationWarehouseStock"]
    }
	
}
