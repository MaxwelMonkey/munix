package com.munix

import grails.test.*

class ProductTests extends GrailsUnitTestCase {
	def sampleProduct
	def sampleProductNotForSale
	def sampleProductNotActive
	def sampleProductDiffDiscountType
	def sampleCategory
	def sampleSubcategory
	def sampleBrand
	def sampleModel
	def sampleModelNumber
	def sampleMaterial
	def sampleSize
	def sampleAddedDescription
	def sampleColor
	def samplePartNumber
	def sampleDiscountType
	def sampleDiscountType2
	def sampleUnit
	def sampleItemType
	
    protected void setUp() {
        super.setUp()
		setUpCategory()
		setUpSubCategory()
		setUpBrand()
		setUpModel()
		setUpModelNumber()
		setUpMaterial()
		setUpSize()
		setUpAddedDescription()
		setUpColor()
		setUpPartNumber()
		setUpDiscountTypes()
		setUpProduct()
    }
	
	private void setUpCategory() {
		sampleCategory = new ProductCategory(
			identifier: "category",
			description: "desc"
		)
		sampleCategory.save()
	}
	
	private void setUpSubCategory() {
		sampleSubcategory = new ProductSubcategory(
			identifier: "subcategory",
			description: "desc"
		)
		sampleSubcategory.save()
	}
	
	private void setUpBrand() {
		sampleBrand = new ProductBrand(
			identifier: "brand",
			description: "desc"
		)
		sampleBrand.save()
	}

	private void setUpModel() {
		sampleModel = new ProductModel(
			identifier: "model",
			description: "desc"
		)
		sampleModel.save()
	}
	
	private void setUpModelNumber() {
		sampleModelNumber = "modelNumber"
	}
	
	private void setUpMaterial() {
		sampleMaterial = new ProductMaterial(
			identifier: "material",
			description: "desc"
		)
		sampleMaterial.save()
	}
	
	private void setUpSize() {
		sampleSize = "size"
	}
	
	private void setUpAddedDescription() {
		sampleAddedDescription = "addedDescription"
	}
	
	private void setUpColor() {
		sampleColor = new ProductColor(
			identifier: "color",
			description: "desc"
		)
		sampleColor.save()
	}
	
	private void setUpPartNumber() {
		samplePartNumber = "partNumber"
	}
	
	private void setUpDiscountTypes() {
		sampleDiscountType = new DiscountType(
			identifier: "discountType",
			description: "desc",
			margin: BigDecimal.ONE
		)
		sampleDiscountType.save()
		
		sampleDiscountType2 = new DiscountType(
			identifier: "discountType2",
			description: "desc",
			margin: BigDecimal.ONE
		)
		sampleDiscountType2.save()
	}
	
	private void setUpUnit() {
		sampleUnit = new ProductUnit(
			identifier: "unit",
			description: "desc"
		)
		sampleUnit.save()
	}

	private void setUpItemType() {
		sampleItemType = new ItemType(
			identifier: "itemType",
			description: "desc"
		)
		sampleItemType.save()
	}


	private void setUpProduct() {
		sampleProduct = new Product(
			identifier: "product",
			brand: sampleBrand,
			category: sampleCategory,
			color: sampleColor,
			material: sampleMaterial,
			model: sampleModel,
			subcategory: sampleSubcategory,
			type: sampleDiscountType,
			unit: sampleUnit,
			itemType: sampleItemType,
			modelNumber: sampleModelNumber,
			partNumber: samplePartNumber,
			size: sampleSize,
			addedDescription: sampleAddedDescription,
			packageDetails: "packageDetails",
			wholeSalePrice: new BigDecimal("0"),
			retailPrice: new BigDecimal("0"),
			isNet: false,
			isComponent: true,
			isForSale: true,
			isForAssembly: false,
			reorderPoint: new BigDecimal("0"),
			eoq: new BigDecimal("0"),
			status: "Active"
		) 
		sampleProduct.save()
		
		sampleProductNotForSale = new Product(
			identifier: "product2",
			type: sampleDiscountType,
			isForSale: false,
		)
		sampleProductNotForSale.save()
		
		sampleProductNotActive = new Product(
			identifier: "product3",
			type: sampleDiscountType,
			status: "Inactive"
		)
		sampleProductNotActive.save()
		
		sampleProductDiffDiscountType = new Product(
			identifier: "product4",
			type: sampleDiscountType2,
			status: "Active"
		)
		sampleProductDiffDiscountType.save(flush: true)
	}

    protected void tearDown() {
        super.tearDown()
    }

	void testAvailableForSalesOrder() {
		def result = Product.availableForSalesOrder(sampleDiscountType).list()

		assertEquals "wrong number of results", 1, result.size() 
		assertTrue "should contain sample product", result.contains(sampleProduct)
	}
}
