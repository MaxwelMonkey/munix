package com.munix

class PriceAdjustmentUpdateService {

	static transactional = true
	def generalMethodService
	def updatePrices() {
		def today = generalMethodService.dateToday()
		def priceAdjustments = PriceAdjustment.findAllByEffectiveDateLessThanEqualsAndStatus(today,PriceAdjustment.Status.APPROVED)
		
		if(!priceAdjustments.isEmpty()){
			priceAdjustments?.sort{it.effectiveDate}.each {
				updateProductPrices(it)
				it.status = PriceAdjustment.Status.APPLIED
				it.save()
			}
		}
	}

	private updateProductPrices(PriceAdjustment priceAdjustmentInstance){
		def priceType = priceAdjustmentInstance.priceType

		priceAdjustmentInstance.items.each{
			def product = it.product
			product.setProductPrice(priceType.toString(), it.newPrice)
		}
	}
}
