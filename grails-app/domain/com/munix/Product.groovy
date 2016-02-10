package com.munix

import org.apache.commons.collections.list.LazyList;
import org.apache.commons.collections.FactoryUtils;

class Product {

    String identifier
    ProductBrand brand
    ProductCategory category
    ProductColor color
    ProductMaterial material
    ProductModel model
    ProductSubcategory subcategory
    DiscountType type
    ProductUnit unit
    ItemType itemType
    String modelNumber
    String partNumber
    String size
    String addedDescription
	String packageDetails
	BigDecimal wholeSalePrice = 0
	BigDecimal retailPrice = 0
    byte[] photo
    Boolean isNet //use the net price
    Boolean isComponent = true//used a component for the final product
    Boolean isForSale = true //the product is for sale
    Boolean isForAssembly //the product can be assembled
    BigDecimal reorderPoint = 0
    BigDecimal eoq = 0
    String status = "Active"
	BigDecimal runningCost = 0
	BigDecimal runningCostInForeignCurrency = 0
	StockCard stockCard
	String remark
	CurrencyType currency
    BigDecimal exchangeRate = 0
	List components = new ArrayList()

    static hasMany = [stocks:Stock, components:ProductComponent, jobOrders:JobOrder, itemLocations:ItemLocation, laborCosts: LaborCost, auditTrails:ProductAuditTrail]
	static transients = ['description', 'wholeSalePriceMargin', 'retailPriceMargin']
	static mappedBy = [components: 'product']
	
    static namedQueries = {
         availableForSalesOrder{discountType ->
           and{
			  eq("status", "Active")
              eq("isForSale", true)
              eq("type", discountType)
           }
         }

    }
    static constraints = {
        identifier(nullable: false, unique: true)
        brand(nullable: true)
        category(nullable: true)
        color(nullable: true)
        material(nullable: true)
        model(nullable: true)
        subcategory(nullable: true)
        type(nullable: false)
        unit(nullable: true)
        itemType(nullable: true)
        size(nullable: true)
        addedDescription(nullable: true)
		packageDetails(nullable: true, maxSize: 250)
        modelNumber(nullable: true)
        partNumber(nullable: true)
		retailPrice(nullable: true)
        photo(nullable: true, maxSize: 60000)
        isNet(nullable: true)
        isComponent(nullable: true)
        isForSale(nullable: true)
        isForAssembly(nullable: true)
        reorderPoint(nullable: true)
        eoq(nullable: true)
        status(nullable: false)
		stockCard(nullable: true)
		runningCost(nullable: true, scale:4)
		remark(nullable: true, blank:true)
		currency(nullable: true, blank:true)
		exchangeRate(nullable: true)
    }

    String toString(){
        identifier
    }

    String formatDescription(){
		def delimiter = " "
		def sb = new StringBuilder()  
		
		if (category) {
			sb.append(this.category.toString())
			sb.append(delimiter)
		}

        if(subcategory){
            sb.append(this.subcategory.toString())
            sb.append(delimiter)
        }

        if(brand){
            sb.append(this.brand.toString())
            sb.append(delimiter)
        }

        if(model){
            sb.append(this.model.toString())
            sb.append(delimiter)
        }

		if(modelNumber){
			sb.append(this.modelNumber)
			sb.append(delimiter)
		}

        if(material){
            sb.append(this.material.toString())
            sb.append(delimiter)
        }

		if (size) {
			sb.append(this.size)
			sb.append(delimiter)
		}
		
		if (addedDescription) {
			sb.append(this.addedDescription)
			sb.append(delimiter)
		}
        
        if(color){
            sb.append(this.color.toString())
            sb.append(delimiter)
        }

        return sb.toString().trim()
    }
	
	String getDescription(){
		return formatDescription()
	}
	
	BigDecimal getProductPrice(type){
		if("Retail".equalsIgnoreCase(type)){
			return retailPrice
		}
		else{
			return wholeSalePrice
		}
	}
	
	BigDecimal getProductPrice(PriceType priceType){
		if(priceType == PriceType.RETAIL) {
			return retailPrice
		}
		else{
			return wholeSalePrice
		}
	}

	BigDecimal setProductPrice(String type, BigDecimal newPrice){
		if("Retail".equalsIgnoreCase(type)){
			retailPrice = newPrice
		}
		else{
			wholeSalePrice = newPrice
		}
	}
	
    String formatSOH(Warehouse warehouse){
        getStock(warehouse)?.formatQty()
    }

    String formatRetailPrice(){
        if(retailPrice){
            return "PHP ${String.format('%,.4f',retailPrice)}"
        }else{
            return ""
        }
    }

    String formatWholeSalePrice(){
        if(wholeSalePrice){
            "PHP ${String.format('%,.4f',wholeSalePrice)}"
        }else{
            return ""
        }
    }

	String formatPackageDetails(){
		if(packageDetails != null) {
			return "${packageDetails}"
		} else {
			return ""
		}
	}
	
    String formatReorderPoint(){
        "${String.format('%,.4f',reorderPoint)}"
    }

    String formatEoq(){
        "${String.format('%,.4f',eoq)}"
    }

    def isRetail(){
        if(retailPrice != null){
            return true
        }else{
            return false
        }
    }

    def isWholesale(){
        if(wholeSalePrice != null){
            return true
        }else{
            return false
        }
    }

    Stock getStock(Warehouse warehouse){
        def stock = null
        stocks.each{
            if(it.warehouse == warehouse){
                stock = it
            }
        }
        return stock
    }

    def getTotalStock(){
        def total = 0
        stocks.each {
          total += it?.qty
        }
        return total
    }
	
	String getPackageDetails(){
		return packageDetails;
	}

	String getWholeSalePriceMargin() {
		wholeSalePrice - runningCost
	}

	String getRetailPriceMargin() {
		retailPrice - runningCost
	}

	def getComponentList(){
		return LazyList.decorate(components, FactoryUtils.instantiateFactory(ProductComponent.class))
	}
	
	def formatWholeSaleSellingPriceMargin() {
		if(wholeSalePrice) {
			return "${String.format('%,.2f',computeWholeSaleSellingPrice())}%"
		} else {
			return ""
		}
	}
	def formatRetailSellingPriceMargin() {
		if(retailPrice) {
			return "${String.format('%,.2f',computeRetailSellingPrice())}%"
		} else {
			return ""
		}
	}
	
	def computeWholeSaleSellingPrice() {
		def sellingPrice = BigDecimal.ZERO
		def margin = isNet ? type.netItemMargin : type.discountedItemMargin
		if(runningCost) {
			sellingPrice = computeSellingPriceMargin(wholeSalePrice, margin)
		}
		return sellingPrice
	}
	
	def computeRetailSellingPrice() {
		def sellingPrice = BigDecimal.ZERO
		def margin = isNet ? type.netItemMargin : type.discountedItemMargin
		if(runningCost) {
			sellingPrice = computeSellingPriceMargin(retailPrice, margin)
		}
		return sellingPrice
	}
	
	def computeSellingPriceMargin(sellingPrice, margin) {
		((((sellingPrice * (1 - margin)) / runningCost) - 1) * 100)
	}
	
	def formatSellingPriceMargin() {
		if(isNet) {
			"${String.format('%,.2f',netItemMargin())}"
		} else {
			"${String.format('%,.2f',discountedItemMargin())}"
		}
	}
	
	def discountedItemMargin() {
		type.discountedItemMargin * 100
	}
	
	def netItemMargin() {
		type.netItemMargin * 100
	}
	
	def formatRunningCostInForeignCurrency(){
		def result = "${String.format('%,.4f',runningCostInForeignCurrency)}"
		if(currency)
			result = currency.identifier + " " + result
		result
	}
}
