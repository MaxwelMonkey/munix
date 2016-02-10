package com.munix

import grails.test.*

class ProductTests extends GrailsUnitTestCase {
	def sampleProduct
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
		
		setUpProduct()
    }
	
	private void setUpCategory() {
		sampleCategory = new ProductCategory(
			identifier: "category",
			description: "desc"
		)
		mockDomain(ProductCategory, [sampleCategory])
	}
	
	private void setUpSubCategory() {
		sampleSubcategory = new ProductSubcategory(
			identifier: "subcategory",
			description: "desc"
		)
		mockDomain(ProductSubcategory, [sampleSubcategory])
	}
	
	private void setUpBrand() {
		sampleBrand = new ProductBrand(
			identifier: "brand",
			description: "desc"
		)
		mockDomain(ProductBrand, [sampleBrand])
	}

	private void setUpModel() {
		sampleModel = new ProductModel(
			identifier: "model",
			description: "desc"
		)
		mockDomain(ProductModel, [sampleModel])
	}
	
	private void setUpModelNumber() {
		sampleModelNumber = "modelNumber"
	}
	
	private void setUpMaterial() {
		sampleMaterial = new ProductMaterial(
			identifier: "material",
			description: "desc"
		)
		mockDomain(ProductMaterial, [sampleMaterial])
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
		mockDomain(ProductColor, [sampleColor])
	}
	
	private void setUpPartNumber() {
		samplePartNumber = "partNumber"
	}
	
	private void setUpDiscountType() {
		sampleDiscountType = new DiscountType(
			identifier: "discountType",
			description: "desc"
		)
	}
	
	private void setUpUnit() {
		sampleUnit = new ProductUnit(
			identifier: "unit",
			description: "desc"
		)
	}

	private void setUpItemType() {
		sampleItemType = new ItemType(
			identifier: "itemType",
			description: "desc"
		)
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
		mockDomain(Product, [sampleProduct])
	}

    protected void tearDown() {
        super.tearDown()
    }

    void testFormatDescription() {
		def description = sampleProduct.formatDescription()
		
		def expected = "category subcategory brand model modelNumber material size addedDescription color"

		assertEquals "Description should be correct", expected, description 
    }
}
