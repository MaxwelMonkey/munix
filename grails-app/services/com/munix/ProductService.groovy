package com.munix

class ProductService {

    static transactional = true
	private final static CHECKED_BOX = "on"
	def authenticateService
    def generalMethodService
	def stockCostHistoryService

	def constructItemLocationList(List<String> itemLocationIDs) {
		def itemLocations = []
		itemLocationIDs.removeAll("no selection")
		if (itemLocationIDs) {
			itemLocationIDs.each {
				def itemLocation = ItemLocation.findById(it.toLong())
				itemLocations.add(itemLocation)
			}
		}
		
		return itemLocations
	}
	
	def generateWarehouses(final Product productInstance) {
		def warehouses = []
		
		Warehouse?.withCriteria{
			isNotEmpty("itemLocations")
			order("id")
		}?.each {
			def warehouse = [:]
			warehouse.itemLocationWarehouse = it
			warehouse.selectedItemLocation = getProductItemLocation(productInstance, it)
			warehouses.add(warehouse)
		}
		
		return warehouses
	}
	
	def getProductItemLocation(final Product productInstance, final Warehouse warehouse) {
		def itemLocation
		
		productInstance?.itemLocations.each { 
			if (!itemLocation && it?.warehouse?.id == warehouse.id) {
				itemLocation = ItemLocation.get(it.id)
			}
		}
		
		return itemLocation
	}
	
    def checkIfProductComponentExistForProduct(final String checkBoxStatus, final Product product) {
		boolean componentExist = false;

		boolean checkStatus = isChecked(checkBoxStatus)
		if(checkStatus == false && product.isComponent != checkStatus){
			def productComponent = ProductComponent.findByComponent(product)
			if(productComponent) {
				componentExist = true
			}

		}

		return componentExist
    }
	
	boolean isChecked(final String checkBoxStatus) {
		return  checkBoxStatus == CHECKED_BOX ? true : false
	}
	
	def generateAppliedPriceAdjustmentItemsForProduct(Product productInstance) {
		return PriceAdjustmentItem.withCriteria {
			and {
				priceAdjustment {
					eq('status', PriceAdjustment.Status.APPLIED)
				}
				eq('product',productInstance)
			}
		}
	}
	
	def generateList(Map params) {
		def searchIdentifier = params.searchIdentifier
		def searchCategory = params.searchCategory
		def searchSubcategory = params.searchSubcategory
		def searchBrand = params.searchBrand
		def searchModel = params.searchModel
		def searchModelNumber = params.searchModelNumber
		def searchMaterial = params.searchMaterial
		def searchSize = params.searchSize
		def searchAdditional = params.searchAdditional
		def searchColor = params.searchColor
		def searchPartNumber = params.searchPartNumber
		def searchItemType = params.searchItemType
		def searchDiscountType = params.searchDiscountType
		def searchStatus = params.searchStatus
		def searchIsNet = params.searchIsNet
		def searchIsForSale = params.searchIsForSale
		def searchIsComponent = params.searchIsComponent
		def searchIsAssembly = params.searchIsAssembly
		def searchKeyword = params.searchKeyword
		
		def searchInventoryBalance = params.inventoryBalance

		def query = {
			//Search
			if(searchIdentifier){
				like('identifier', "%${searchIdentifier}%")
			}

			if(searchCategory){
				category{
					like('identifier', "%${searchCategory}%")
				}
			}

			if(searchSubcategory){
				subcategory{
					like('identifier', "%${searchSubcategory}%")
				}
			}

			if(searchBrand){
				brand{
					like('identifier', "%${searchBrand}%")
				}
			}
			
			if(searchItemType){
				itemType{
					like('identifier', "%${searchItemType}%")
				}
			}

			if(searchDiscountType){
				type{
					like('identifier', "%${searchDiscountType}%")
				}
			}

			if(searchModel){
				model{
					like('identifier', "%${searchModel}%")
				}
			}

			if(searchModelNumber){
				like('modelNumber', "%${searchModelNumber}%")
			}

			if(searchMaterial){
				material{
					like('identifier', "%${searchMaterial}%")
				}
			}

			if(searchSize){
				like('size', "%${searchSize}%")
			}

			if(searchAdditional){
				like('addedDescription', "%${searchAdditional}%")
			}

			if(searchColor){
				color{
					like('identifier', "%${searchColor}%")
				}
			}

			if(searchPartNumber){
				like('partNumber', "%${searchPartNumber}%")
			}

            if(searchStatus){
				eq('status',searchStatus)
			}
            if(searchIsNet){
				eq('isNet',searchIsNet.equals("True")?true:false)
			}
            if(searchIsForSale){
				eq('isForSale',searchIsForSale.equals("True")?true:false)
			}
            if(searchIsComponent){
				eq('isComponent',searchIsComponent.equals("True")?true:false)
			}
            if(searchIsAssembly){
				eq('isForAssembly',searchIsAssembly.equals("True")?true:false)
			}
            
            if(searchInventoryBalance){
            	stocks {
        			if(searchInventoryBalance== ">0"){
        				gt('qty',BigDecimal.valueOf(0))
        			}else if(searchInventoryBalance== "0"){
        				eq('qty',BigDecimal.valueOf(0))
        			}else if(searchInventoryBalance== "<0"){
        				lt('qty',BigDecimal.valueOf(0))
        			}
            	}
            }

			if(searchKeyword){
				searchKeyword.split(" ")?.each{
					def keyword = it
					or{
						category{
							like('identifier', "%${keyword}%")
						}
						subcategory{
							like('identifier', "%${keyword}%")
						}
						brand{
							like('identifier', "%${keyword}%")
						}
						model {
							like('identifier', "%${keyword}%")
						}
						like('modelNumber', "%${keyword}%")
						material {
							like('identifier', "%${keyword}%")
						}
						like('size', "%${keyword}%")
						like('addedDescription', "%${keyword}%")
					}
				}
			}

			//Sort
			if(params.sort=="identifier"){
				order('identifier', params.order)
			}else if(params.sort=="unit"){
				unit {
					order('identifier', params.order)
				}
			}else if(params.sort=="description"){
				category {
					order('identifier', params.order)
				}
				subcategory {
					order('identifier', params.order)
				}
				brand {
					order('identifier', params.order)
				}
				model {
					order('identifier', params.order)
				}

				order('modelNumber', params.order)
				
				material {
					order('identifier', params.order)
				}

				order('size', params.order)
				order('addedDescription', params.order)
				params.sort = "addedDescription"

			}else if(params.sort=="partNumber"){
				order('partNumber', params.order)
			}else if(params.sort=="itemType"){
				itemType {
					order('identifier', params.order)
				}
			}else if(params.sort=="wholeSalePrice"){
				order('wholeSalePrice', params.order)
			}else if(params.sort=="retailPrice"){
				order('retailPrice', params.order)
			}else if(params.sort=="type"){
				type {
					order('identifier', params.order)
				}
			}else if(params.sort=="isNet"){
				order('isNet', params.order)
			}else if(params.sort=="isComponent"){
				order('isComponent', params.order)
			}else if(params.sort=="isForSale"){
				order('isForSale', params.order)
			}else if(params.sort=="isForAssembly"){
				order('isForAssembly', params.order)
			}else if(params.sort=="reorderPoint"){
				order('reorderPoint', params.order)
			}else if(params.sort=="eoq"){
				order('eoq', params.order)
			}else if(params.sort=="status"){
				order('status', params.order)
			}else if(params.sort=="runningCostInForeignCurrency"){
				order('runningCostInForeignCurrency', params.order)
			}else if(params.sort=="runningCost"){
				order('runningCost', params.order)
			}

			
			

			resultTransformer org.hibernate.Criteria.DISTINCT_ROOT_ENTITY
		}
		
		return Product.createCriteria().list(params,query)
	}
	
	def getAllComponents(String productSearchCriterion) {
		def products = Product.withCriteria{
			eq('isComponent', true)
		}
		products = products?.findAll{it.description.toLowerCase() =~ productSearchCriterion.toLowerCase() || it.identifier.toLowerCase() =~ productSearchCriterion.toLowerCase()}
		return products
	}
	
    def generateAuditTrails(Product product){
        if(product.isDirty()){
            def map =[isNet:"Use Net Price",isComponent:"Use as Component",isForSale:"For Sale", isForAssembly:"For Assembly", eoq:"EOQ", remark: "Remarks"]
            def modifiedFieldNames = product.getDirtyPropertyNames()
            modifiedFieldNames.each{fieldName->
                def field = map.get(fieldName)
                if(!field){
                    def display = generalMethodService.addWhiteSpaceAfterCapital(fieldName)
                    field = generalMethodService.capitalizeFirstLetter(display)
                }
                def currentValue = product."$fieldName"==null?"":product."$fieldName".toString()
                def originalValue = product.getPersistentValue(fieldName)==null?"":product.getPersistentValue(fieldName).toString()
                if (currentValue != originalValue) {
                    if(field == "Photo"){
                        currentValue = ""
                        originalValue = ""
                    }
                    product.addToAuditTrails(new ProductAuditTrail(
                        user:authenticateService.userDomain(),
                        previousEntry:originalValue,
                        newEntry: currentValue,
                        fieldName: field,
                        product: product
                    ))
                }
            }
        }
    }
	
	def generateApprovedSalesDeliveriesForProduct(params) {
		def productInstance = Product.get(params.id)
		
		def query = {
			and {
				delivery {
					or {
						eq('status', "Paid")
						eq('status', "Unpaid")
					}
				}
				eq('product', productInstance)
				ne('qty', BigDecimal.ZERO)
			}
		}
		return SalesDeliveryItem.createCriteria().list(params, query)
	}
	
	def createStockCardForNewProduct(Product productInstance) {
		productInstance.stockCard = new StockCard(product: productInstance)
		productInstance.stockCard.addToItems(StockCardItemFactory.createInitialBalanceEntry())
		productInstance.stockCard.save(flush: true)
	}
	
	def createStocksForNewProduct(Product productInstance) {
        Warehouse.list().each{ warehouse ->
            def stockInstance = new Stock()
            stockInstance.product = productInstance
            stockInstance.warehouse = warehouse
            productInstance.addToStocks(stockInstance)
			productInstance.save(flush: true)
        }
	}
	
	def updateRunningCost(Product product, BigDecimal cost) {
		product.runningCost = cost
		product.save()
	}
	def updateRunningCostInForeignCurrency(Product product, BigDecimal cost, CurrencyType currency, BigDecimal exchangeRate){
        product.runningCostInForeignCurrency = cost
        product.currency = currency
        product.exchangeRate = exchangeRate
		product.save()
    }
	def updateRunningCost(Product product) {
		def stockCostItems = stockCostHistoryService.generateStockCostItems(product)
		if (stockCostItems && stockCostItems.size() > 0) {
			def stockCostItemsLastEntry = stockCostItems.get(stockCostItems.size()-1)
			product.runningCost = stockCostItemsLastEntry.costLocal
			product.runningCostInForeignCurrency = stockCostItemsLastEntry.foreignCurrencyFinalPrice?:0
		} else {
			product.runningCost = BigDecimal.ZERO
			product.runningCostInForeignCurrency = BigDecimal.ZERO
		}
		product.save(flush:true)
	}
}
