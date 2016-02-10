package com.munix

class PriceAdjustmentService {

    static transactional = true
	def generalMethodService
    private final static String APPROVABLE = "Approvable"
    private final static String DATEERROR = "DateError"
    private final static String PRICETYPEANDITEMTYPEERROR = "PriceTypeAndItemTypeError"

    def checkIfUnapprovable(PriceAdjustment priceAdjustmentInstance) {
		def isUnapprovable = false
		def today = generalMethodService.dateToday()

		if(today < priceAdjustmentInstance.effectiveDate) {
			isUnapprovable = true
		}

		return isUnapprovable
	}
    def checkIfApprovable(PriceAdjustment priceAdjustmentInstance) {
        def message
		def today = generalMethodService.dateToday()

		if(today >= priceAdjustmentInstance.effectiveDate) {
            message = DATEERROR
		}else if(obtainPriceAdjustmentWithDatePriceTypeAndItemType(priceAdjustmentInstance)){
            message = PRICETYPEANDITEMTYPEERROR
        }
        else{
            message = APPROVABLE
        }
		return message
	}
	
    private obtainPriceAdjustmentWithDatePriceTypeAndItemType(PriceAdjustment priceAdjustmentInstance){

        return PriceAdjustment.withCriteria {
            and{
                eq('effectiveDate',priceAdjustmentInstance.effectiveDate)
                eq('priceType',priceAdjustmentInstance.priceType)
				eq('itemType',priceAdjustmentInstance.itemType)
                eq('status',PriceAdjustment.Status.APPROVED)
            }
        }
    }
	
	def generateList(Map params) {
		def searchPriceAdjustmentId = params.searchPriceAdjustmentId
		def searchPriceType = params.searchPriceType
		def searchItemType = params.searchItemType
		def searchStatus = params.searchStatus
		def searchDateGeneratedFrom
		if(params?.searchDateGeneratedFrom){
			searchDateGeneratedFrom = generalMethodService.createDate(params?.searchDateGeneratedFrom)
		}
		def searchDateGeneratedTo
		if(params?.searchDateGeneratedTo){
			searchDateGeneratedTo = generalMethodService.createDate(params?.searchDateGeneratedTo)
		}
		def searchEffectiveDateFrom
		if(params?.searchEffectiveDateFrom){
			searchEffectiveDateFrom = generalMethodService.createDate(params?.searchEffectiveDateFrom)
		}
		def searchEffectiveDateTo
		if(params?.searchEffectiveDateTo){
			searchEffectiveDateTo = generalMethodService.createDate(params?.searchEffectiveDateTo)
		}
		
		def query = {
				and{
					if(searchPriceAdjustmentId){
						like('priceAdjustmentId', "%${searchPriceAdjustmentId}%")
					}
					if(searchPriceType){
						eq('priceType', com.munix.PriceType.getTypeByName(searchPriceType))
					}
					
					if (searchItemType){
						itemType{
							like('identifier', "%${searchItemType}%")
						}
						
					}
					
					if(searchStatus){
					eq("status", searchStatus == "Approved" ? PriceAdjustment.Status.APPROVED : searchStatus == "Unapproved" ? PriceAdjustment.Status.UNAPPROVED : searchStatus == "Applied" ? PriceAdjustment.Status.APPLIED : PriceAdjustment.Status.CANCELLED)
					}
					
					if(searchDateGeneratedFrom&&searchDateGeneratedTo){
						ge('dateGenerated',searchDateGeneratedFrom)
						le('dateGenerated',searchDateGeneratedTo)
					}else if(searchDateGeneratedFrom){
						ge('dateGenerated',searchDateGeneratedFrom)
					}else if(searchDateGeneratedTo){
						le('dateGenerated',searchDateGeneratedTo)
					}
					
					if(searchEffectiveDateFrom&&searchEffectiveDateTo){
						ge('effectiveDate',searchEffectiveDateFrom)
						le('effectiveDate',searchEffectiveDateTo)
					}else if(searchEffectiveDateFrom){
						ge('effectiveDate',searchEffectiveDateFrom)
					}else if(searchEffectiveDateTo){
						le('effectiveDate',searchEffectiveDateTo)
					}
				}
		}
		def priceAdjustmentInstanceList = PriceAdjustment.createCriteria().list(params, query)
	}
	
	public Set queryAvailableProducts(ItemType itemType, String productSearchCriterion) {
		def productList = Product.withCriteria{
			eq('itemType', itemType)
			eq('isForSale', true)
		}
		productList = productList?.findAll{it.description.toLowerCase() =~ productSearchCriterion.toLowerCase() || it.identifier.toLowerCase() =~ productSearchCriterion.toLowerCase()}
		return new HashSet(productList)
	}
    def isCancelable(PriceAdjustment priceAdjustment){
        def isCancelable = false
        if(priceAdjustment.isUnapproved()){
            isCancelable = true
        }
        return isCancelable
    }
}