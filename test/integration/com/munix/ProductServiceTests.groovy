package com.munix

import grails.test.*

class ProductServiceTests extends GrailsUnitTestCase {
	def productService

	def sampleCity
	def sampleCustomerType
	def sampleTerm
	def sampleCustomerAccount
	def sampleCustomer
	def sampleSalesDelivery
	def sampleSalesDeliveryItem
	def sampleSalesOrder
	def sampleSalesOrderItem
	def sampleProductCategory
	def sampleProductSubcategory
	def sampleItemType
	def sampleDiscountType
	def sampleProductBrand
	def sampleProductModel
	def sampleProductMaterial
	def sampleProductColor
	def sampleProduct
	def sampleWarehouse
	def sampleItemLocation
	def samplePriceAdjustment
	def samplePriceAdjustmentItem
	
    protected void setUp() {
        super.setUp()
		setUpProductCategory()
		setUpProductSubcategory()
		setUpItemType()
		setUpDiscountType()
		setUpProductBrand()
		setUpProductModel()
		setUpProductMaterial()
		setUpProductColor()
		setUpProducts()
		setUpWarehouses()
		setUpItemLocations()
		setUpWarehouseAndItemLocationRelationship()
		setUpProductAndItemLocationRelationship()
		setUpPriceAdjustments()
		setUpPriceAdjustmentItems()
		setUpPriceAdjustmentAndPriceAdjustmentItemRelationship()
		
	}

    protected void tearDown() {
        super.tearDown()
    }
	
	void testGenerateList() {
		def product = new Product(
			identifier: "identifier",
			category: sampleProductCategory,
			subcategory: sampleProductSubcategory,
			itemType: sampleItemType,
			type: sampleDiscountType,
			brand: sampleProductBrand,
			model: sampleProductModel,
			modelNumber: "model number",
			material: sampleProductMaterial,
			size: "size",
			addedDescription: "added description",
			color: sampleProductColor,
			partNumber: "part number"
		)
		product.save(flush: true)

		setUpFilteredProducts()

		def params = [:]
		params.order = "asc"
		params.searchIdentifier = "identifier"
		params.searchCategory = "product category"
		params.searchSubcategory = "product subcategory"
		params.searchItemType = "item type"
		params.searchDiscountType = "discount type"
		params.searchBrand = "product brand"
		params.searchModel = "product model"
		params.searchModelNumber = "model number"
		params.searchMaterial = "product material"
		params.searchSize = "size"
		params.searchAdditional = "added description"
		params.searchColor = "product color"
		params.searchPartNumber = "part number"

		def result = productService.generateList(params)
		
		assertTrue "Result should contain product", result.contains(product)
		assertEquals "Result total should be 1", 1, result.size()
	}
	
	void testGenerateApprovedSalesDeliveriesForProduct(){
		setUpTerms()
		setUpCities()
		setUpCustomerTypes()
		setUpCustomerAccounts()
		setUpCustomers()
		setUpSalesOrders()
		setUpSalesOrderItem()
		setUpSalesDeliveryItems()
		setUpSalesDeliveries()
		
		def params = [:]
		params.id = sampleProduct.id
		
		def result = productService.generateApprovedSalesDeliveriesForProduct(params)
		 
		assertTrue "Result should contain sampleSalesDeliveryItem", result.contains(sampleSalesDeliveryItem)
		assertEquals "Result total should be 1", 1, result.size()
		
	}
	
    void testGetProductItemLocation() {
		def result = productService.getProductItemLocation(sampleProduct, sampleWarehouse)
		def expected = sampleItemLocation

		assertEquals "Product has item location" , expected, result
    }
	
	void testGenerateWarehouses() {
		def result = productService.generateWarehouses(sampleProduct).collect {it.itemLocationWarehouse}
		def expected = sampleWarehouse
		
		assertTrue "Result should have the sampleWarehouse", result.contains(expected)
	}
	
	void testGenerateWarehousesWithSelectedWarehouseItemLocation() {
		def result = productService.generateWarehouses(sampleProduct).collect {it.selectedItemLocation}
		def expected = sampleItemLocation
		
		assertTrue "Result should have sampleItemLocation", result.contains(expected)
	}
	
	void testGenerateAppliedPriceAdjustmentItemsForProduct() {
		def result = productService.generateAppliedPriceAdjustmentItemsForProduct(sampleProduct)
		
		assertTrue "The result should contain samplePriceAdjustmentItem", result.contains(samplePriceAdjustmentItem)
		assertEquals "The result should have 1 Price Adjustment Item", 1, result.size()
	}
	
	void testGetAllComponents() {
		def productComponent = new Product(identifier: "product", isComponent: false)
		productComponent.save(flush: true)
		
		def result = productService.getAllComponents("")
		
		assertEquals "should return 1 component", 1, result.size()
		assertTrue "should contain sample product", result.contains(sampleProduct)
	}
	void testGenerateAuditTrails(){
        def product = new Product(identifier: "sample", type: sampleDiscountType)
        product.save()
        assertEquals "Product Identifier must be sample","sample",product.identifier
        assertNull "Product audit trails must not have any value",product.auditTrails
        product.identifier = "edited"
        productService.generateAuditTrails(product)
        assertEquals "audit trails must contain 1 element", 1, product.auditTrails.size()
        product.auditTrails.each{
            assertEquals "auditTrails current value must be edited","edited",it.newEntry
            assertEquals "auditTrails previous value must be product","sample",it.previousEntry
            assertEquals "auditTrails fieldName value must be identifier","Identifier",it.fieldName
        }
    }
    void testGenerateAuditTrailsForSale(){
        def product = new Product(identifier: "sample", isForSale:true, type: sampleDiscountType)
        product.save()
        assertEquals "Product Identifier must be sample","sample",product.identifier
        assertNull "Product audit trails must not have any value",product.auditTrails
        assertTrue "Product isForSale must be true",product.isForSale
        product.isForSale = false
        productService.generateAuditTrails(product)
        assertEquals "audit trails must contain 1 element", 1, product.auditTrails.size()
        product.auditTrails.each{
            assertEquals "auditTrails current value must be false","false",it.newEntry
            assertEquals "auditTrails previous value must be true","true",it.previousEntry
            assertEquals "auditTrails fieldName value must be For Sale","For Sale",it.fieldName
        }
    }
	
	void testCreateStockCardForNewProduct() {
		productService.createStockCardForNewProduct(sampleProduct)
		
		assertNotNull "product should have a stock card", sampleProduct.stockCard
	}
	
	void testCreateStocksForNewProduct() {
		productService.createStocksForNewProduct(sampleProduct)
		
		sampleProduct.stocks.each {
			assertNotNull "product should have a stock with a warehouse", it.warehouse 
		}
	}
	
	void testUpdateRunningCostGivenProductAndCost() {
		def cost = new BigDecimal("100")
		productService.updateRunningCost(sampleProduct, cost)
		
		assertEquals "product should have correct running cost", cost, sampleProduct.runningCost
	}
	
	void testUpdateRunningCostGivenProduct() {
		def cost = new BigDecimal("100")
		def mockStockCostHistoryService = mockFor(StockCostHistoryService)
		mockStockCostHistoryService.demand.generateStockCostItems(1..1) {x ->
			def stockCostItems = []
			def stockCostItem = [:]
			stockCostItem.costLocal = new BigDecimal("50")
			stockCostItems.add(stockCostItem)
			stockCostItem.costLocal = cost
			stockCostItems.add(stockCostItem)
			return stockCostItems
		}
		productService.stockCostHistoryService = mockStockCostHistoryService.createMock()
		
		productService.updateRunningCost(sampleProduct)
		
		assertEquals "product should have correct running cost", cost, sampleProduct.runningCost
	}
	
	private void setUpProducts() {
		sampleProduct = new Product(
			identifier: "product",
			type: sampleDiscountType)
		sampleProduct.save(flush: true)
	}
	
	private void setUpItemLocations() {
		sampleItemLocation = new ItemLocation(description: "itemLocation")
		sampleItemLocation.warehouse = sampleWarehouse
		sampleItemLocation.save(flush: true)
	}
	
	private void setUpWarehouses() {
		sampleWarehouse = new Warehouse(identifier: "warehouse", description: "sample")
		sampleWarehouse.save(flush: true)
	}

	private setUpWarehouseAndItemLocationRelationship() {
		sampleWarehouse.itemLocations = [sampleItemLocation]
		sampleWarehouse.save(flush: true)
	}
	
	private setUpProductAndItemLocationRelationship() {
		sampleProduct.itemLocations = [sampleItemLocation]
		sampleProduct.save(flush: true)
	}
	
	private void setUpProductCategory() {
		sampleProductCategory = new ProductCategory(
			identifier: "product category",
			description: "desc"
		)
		sampleProductCategory.save(flush: true)
	}

	private void setUpProductSubcategory() {
		sampleProductSubcategory = new ProductSubcategory(
			identifier: "product subcategory",
			description: "desc"
		)
		sampleProductSubcategory.save(flush: true)
	}

	private void setUpItemType() {
		sampleItemType = new ItemType(
			identifier: "item type",
			description: "desc"
		)
		sampleItemType.save(flush: true)
	}

	private void setUpDiscountType() {
		sampleDiscountType = new DiscountType(
			identifier: "discount type",
			description: "desc",
			margin: BigDecimal.ONE
		)
		sampleDiscountType.save(flush: true)
	}

	private void setUpProductBrand() {
		sampleProductBrand = new ProductBrand(
			identifier: "product brand",
			description: "desc"
		)
		sampleProductBrand.save(flush: true)
	}

	private void setUpProductModel() {
		sampleProductModel = new ProductModel(
			identifier: "product model",
			description: "desc"
		)
		sampleProductModel.save(flush: true)
	}

	private void setUpProductMaterial() {
		sampleProductMaterial = new ProductMaterial(
			identifier: "product material",
			description: "desc"
		)
		sampleProductMaterial.save(flush: true)
	}

	private void setUpProductColor() {
		sampleProductColor = new ProductColor(
			identifier: "product color",
			description: "desc"
		)
		sampleProductColor.save(flush: true)
	}

	private void setUpPriceAdjustments() {
		samplePriceAdjustment = new PriceAdjustment(
			preparedBy: "me",
			priceType: PriceType.RETAIL,
			itemType: sampleItemType,
			effectiveDate: new Date(),
			status: PriceAdjustment.Status.APPLIED
		)
		samplePriceAdjustment.save(flush: true)
	}
	
	private void setUpPriceAdjustmentItems() {
		samplePriceAdjustmentItem = new PriceAdjustmentItem(
			product: sampleProduct,
			newPrice: new BigDecimal("110"),
			oldPrice: new BigDecimal("100")
		)
		samplePriceAdjustmentItem.save(flush: true)
	}
	
	private void setUpPriceAdjustmentAndPriceAdjustmentItemRelationship() {
		samplePriceAdjustment.items = [samplePriceAdjustmentItem]
		samplePriceAdjustment.save()
	}
	
	private void setUpFilteredProducts() {
		def productFilteredByIdentifier = new Product(
			identifier: "fail",
			category: sampleProductCategory,
			subcategory: sampleProductSubcategory,
			itemType: sampleItemType,
			type: sampleDiscountType,
			brand: sampleProductBrand,
			model: sampleProductModel,
			modelNumber: "model number",
			material: sampleProductMaterial,
			size: "size",
			addedDescription: "added description",
			color: sampleProductColor,
			partNumber: "part number"
		)
		productFilteredByIdentifier.save(flush: true)

		def productCategory = new ProductCategory(
			identifier: "fail",
			description: "desc"
		)
		productCategory.save(flush: true)

		def productFilteredByCategory = new Product(
			identifier: "identifier",
			category: productCategory,
			subcategory: sampleProductSubcategory,
			itemType: sampleItemType,
			type: sampleDiscountType,
			brand: sampleProductBrand,
			model: sampleProductModel,
			modelNumber: "model number",
			material: sampleProductMaterial,
			size: "size",
			addedDescription: "added description",
			color: sampleProductColor,
			partNumber: "part number"
		)
		productFilteredByCategory.save(flush: true)

		def productSubcategory = new ProductSubcategory(
			identifier: "fail",
			description: "desc"
		)
		productSubcategory.save(flush: true)

		def productFilteredBySubcategory = new Product(
			identifier: "identifier",
			category: sampleProductCategory,
			subcategory: productSubcategory,
			itemType: sampleItemType,
			type: sampleDiscountType,
			brand: sampleProductBrand,
			model: sampleProductModel,
			modelNumber: "model number",
			material: sampleProductMaterial,
			size: "size",
			addedDescription: "added description",
			color: sampleProductColor,
			partNumber: "part number"
		)
		productFilteredBySubcategory.save(flush: true)

		def itemType = new ItemType(
			identifier: "fail",
			description: "desc"
		)
		itemType.save(flush: true)

		def productFilteredByItemType = new Product(
			identifier: "identifier",
			category: sampleProductCategory,
			subcategory: sampleProductSubcategory,
			itemType: itemType,
			type: sampleDiscountType,
			brand: sampleProductBrand,
			model: sampleProductModel,
			modelNumber: "model number",
			material: sampleProductMaterial,
			size: "size",
			addedDescription: "added description",
			color: sampleProductColor,
			partNumber: "part number"
		)
		productFilteredByItemType.save(flush: true)

		def discountType= new DiscountType(
			identifier: "fail",
			description: "desc"
		)
		discountType.save(flush: true)

		def productFilteredByDiscountType = new Product(
			identifier: "identifier",
			category: sampleProductCategory,
			subcategory: sampleProductSubcategory,
			itemType: sampleItemType,
			type: discountType,
			brand: sampleProductBrand,
			model: sampleProductModel,
			modelNumber: "model number",
			material: sampleProductMaterial,
			size: "size",
			addedDescription: "added description",
			color: sampleProductColor,
			partNumber: "part number"
		)
		productFilteredByDiscountType.save(flush: true)

		def productBrand = new ProductBrand(
			identifier: "fail",
			description: "desc"
		)
		productBrand.save(flush: true)

		def productFilteredByBrand = new Product(
			identifier: "identifier",
			category: sampleProductCategory,
			subcategory: sampleProductSubcategory,
			itemType: sampleItemType,
			type: sampleDiscountType,
			brand: productBrand,
			model: sampleProductModel,
			modelNumber: "model number",
			material: sampleProductMaterial,
			size: "size",
			addedDescription: "added description",
			color: sampleProductColor,
			partNumber: "part number"
		)
		productFilteredByBrand.save(flush: true)

		def productModel = new ProductModel(
			identifier: "fail",
			description: "desc"
		)
		productModel.save(flush: true)

		def productFilteredByModel = new Product(
			identifier: "identifier",
			category: sampleProductCategory,
			subcategory: sampleProductSubcategory,
			itemType: sampleItemType,
			type: sampleDiscountType,
			brand: sampleProductBrand,
			model: productModel,
			modelNumber: "model number",
			material: sampleProductMaterial,
			size: "size",
			addedDescription: "added description",
			color: sampleProductColor,
			partNumber: "part number"
		)
		productFilteredByModel.save(flush: true)

		def productFilteredByModelNumber = new Product(
			identifier: "identifier",
			category: sampleProductCategory,
			subcategory: sampleProductSubcategory,
			itemType: sampleItemType,
			type: sampleDiscountType,
			brand: sampleProductBrand,
			model: sampleProductModel,
			modelNumber: "fail",
			material: sampleProductMaterial,
			size: "size",
			addedDescription: "added description",
			color: sampleProductColor,
			partNumber: "part number"
		)
		productFilteredByModelNumber.save(flush: true)

		def productMaterial = new ProductMaterial(
			identifier: "fail",
			description: "desc"
		)
		productMaterial.save(flush: true)

		def productFilteredByMaterial = new Product(
			identifier: "identifier",
			category: sampleProductCategory,
			subcategory: sampleProductSubcategory,
			itemType: sampleItemType,
			type: sampleDiscountType,
			brand: sampleProductBrand,
			model: sampleProductModel,
			modelNumber: "model number",
			material: productMaterial,
			size: "size",
			addedDescription: "added description",
			color: sampleProductColor,
			partNumber: "part number"
		)
		productFilteredByMaterial.save(flush: true)

		def productFilteredBySize = new Product(
			identifier: "identifier",
			category: sampleProductCategory,
			subcategory: sampleProductSubcategory,
			itemType: sampleItemType,
			type: sampleDiscountType,
			brand: sampleProductBrand,
			model: sampleProductModel,
			modelNumber: "model number",
			material: sampleProductMaterial,
			size: "fail",
			addedDescription: "added description",
			color: sampleProductColor,
			partNumber: "part number"
		)
		productFilteredBySize.save(flush: true)

		def productFilteredByAddedDescription = new Product(
			identifier: "identifier",
			category: sampleProductCategory,
			subcategory: sampleProductSubcategory,
			itemType: sampleItemType,
			type: sampleDiscountType,
			brand: sampleProductBrand,
			model: sampleProductModel,
			modelNumber: "model number",
			material: sampleProductMaterial,
			size: "size",
			addedDescription: "fail",
			color: sampleProductColor,
			partNumber: "part number"
		)
		productFilteredByAddedDescription.save(flush: true)

		def productColor = new ProductColor(
			identifier: "fail",
			description: "desc"
		)
		productColor.save(flush: true)

		def productFilteredByColor = new Product(
			identifier: "identifier",
			category: sampleProductCategory,
			subcategory: sampleProductSubcategory,
			itemType: sampleItemType,
			type: sampleDiscountType,
			brand: sampleProductBrand,
			model: sampleProductModel,
			modelNumber: "model number",
			material: sampleProductMaterial,
			size: "size",
			addedDescription: "added description",
			color: productColor,
			partNumber: "part number"
		)
		productFilteredByColor.save(flush: true)

		def productFilteredByPartNumber = new Product(
			identifier: "identifier",
			category: sampleProductCategory,
			subcategory: sampleProductSubcategory,
			itemType: sampleItemType,
			type: sampleDiscountType,
			brand: sampleProductBrand,
			model: sampleProductModel,
			modelNumber: "model number",
			material: sampleProductMaterial,
			size: "size",
			addedDescription: "added description",
			color: sampleProductColor,
			partNumber: "fail"
		)
		productFilteredByPartNumber.save(flush: true)
	}
	
	private setUpSalesOrders() {
		sampleSalesOrder = new SalesOrder(
			discountType: sampleDiscountType,
			priceType: PriceType.RETAIL,
			customer: sampleCustomer,
			items: [sampleSalesOrderItem]
		)
		sampleSalesOrder.save()
	}
	
	private setUpSalesOrderItem() {
		sampleSalesOrderItem = new SalesOrderItem(
				product: sampleProduct,
				qty: new BigDecimal("1"),
				price: new BigDecimal("0"),
				finalPrice: new BigDecimal("0"),
				invoice: sampleSalesOrder
			)
		sampleSalesOrder.addToItems(sampleSalesOrderItem)
		sampleSalesOrderItem.save()
	}
	
	private setUpSalesDeliveries() {
		sampleSalesDelivery = new SalesDelivery(sampleSalesOrder)
		sampleSalesDelivery.warehouse = sampleWarehouse
		sampleSalesDelivery.preparedBy = "me"
		sampleSalesDelivery.deliveryType= "nothing"
		sampleSalesDelivery.status = "Paid"
		sampleSalesDelivery.addToItems(sampleSalesDeliveryItem)
		sampleSalesDelivery.save()
	}
	
	private setUpSalesDeliveryItems() {
		sampleSalesDeliveryItem = new SalesDeliveryItem(product: sampleProduct, price: 1, qty: 1)
		sampleSalesDeliveryItem.save()
	}
	
	private setUpCustomers() {
		sampleCustomer = new Customer(identifier: "customer1",
				name:"cust",
				bilAddrCity:sampleCity,
				bilAddrStreet:"Accuier",
				status:Customer.Status.ACTIVE,
				type:sampleCustomerType,
				fax:"12345",
				term:sampleTerm,
				autoApprove:true,
				customerAccount: sampleCustomerAccount)
		sampleCustomer.save()
	}

	private setUpCustomerTypes() {
		sampleCustomerType = new CustomerType(identifier:"H",
				description:"happy")
		sampleCustomerType.save()
	}

	private setUpCities() {
		sampleCity = new City( description:"quezon city",
				identifier:"QC")
		sampleCity.save()
	}
	
	private setUpTerms() {
		sampleTerm = new Term(identifier:"term",
				description:"termDesc",
				dayValue:new BigDecimal("1234"))
		sampleTerm.save()
	}
	
	private void setUpCustomerAccounts() {
		sampleCustomerAccount = new CustomerAccount()
		sampleCustomerAccount.save()
	}
}
