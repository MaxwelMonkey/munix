package com.munix

class JobOutService {
	
	def stockCardService
	def productService
	def authenticateService

    static transactional = true

    def updateStocksInJobOut(JobOut jobOutInstance) {
        def stock = jobOutInstance.jobOrder.product.getStock(jobOutInstance.warehouse)
        if(stock){
            stock.qty += jobOutInstance.qty
        }
        stock.save()
    }
	
    def removeStocksInJobOut(JobOut jobOutInstance) {
        def stock = jobOutInstance.jobOrder.product.getStock(jobOutInstance.warehouse)
        if(stock){
            stock.qty -= jobOutInstance.qty
        }
        stock.save()
    }
	
    def isUnapprovable(JobOut jobOutInstance){
        def jobOrder = jobOutInstance.jobOrder
        def unapprovable = true
        if(jobOrder.isCompleted()){
            unapprovable = false
        }
        return unapprovable
    }
	
	Boolean validateQuantity(BigDecimal qty, BigDecimal remaining) {
		qty <= remaining
	}
	
	def approve(JobOut jobOutInstance) {
		def result = false
		
		jobOutInstance.approvedBy = FormatUtil.createTimeStamp(authenticateService.userDomain())
		jobOutInstance.approve()
		
		if (jobOutInstance?.save()) {
			updateStocksInJobOut(jobOutInstance)
			stockCardService.createApprovedJobOut(jobOutInstance)
			
			def laborCost = jobOutInstance?.laborCost?.amount ?: BigDecimal.ZERO
			def componentsCost =  jobOutInstance?.jobOrder?.requisition?.computeTotalCostOfMaterialsPerAssembly() ?: BigDecimal.ZERO
			def totalCostPerAssembly = laborCost + componentsCost
			productService.updateRunningCost(jobOutInstance.jobOrder.product, totalCostPerAssembly)
			productService.updateRunningCostInForeignCurrency(jobOutInstance.jobOrder.product, BigDecimal.ZERO, CurrencyType.get(1), 1)
			
			result = true
		}
		return result
			
	}
}
