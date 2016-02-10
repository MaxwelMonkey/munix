package com.munix

class MaterialReleaseService {

    static transactional = true
	
	def jobOrderService

    def releaseStocksInWarehouse(MaterialRelease materialReleaseInstance) {
        def stock
        materialReleaseInstance.items.each{
            stock = it.materialRequisitionItem.component.getStock(materialReleaseInstance.warehouse)
            stock.qty-=it.qty
            stock.save()
        }
    }
    def returnStocksToWarehouse(MaterialRelease materialReleaseInstance) {
        def stock
        materialReleaseInstance.items.each{
            stock = it.materialRequisitionItem.component.getStock(materialReleaseInstance.warehouse)
            stock.qty+=it.qty
            stock.save()
        }
    }
    def isApprovable(MaterialRelease materialReleaseInstance){
        def jobOrder = materialReleaseInstance.jobOrder
        def approvable = true
        materialReleaseInstance.items.each {
            if(jobOrder.computeRequisitionItemRemainingBalance(it.materialRequisitionItem)<0){
                approvable = false
            }
        }
        return approvable
    }
    def isUnapprovable(MaterialRelease materialReleaseInstance){
        def jobOrder = materialReleaseInstance.jobOrder
        def unapprovable = true
        if(jobOrder.materialReleasesApprovedBy){
            unapprovable = false
        }
        return unapprovable
    }
	
	def getRemainingBalanceList(MaterialRelease materialReleaseInstance) {
		def jobOrderInstance = materialReleaseInstance.jobOrder
		
		def remainingBalanceList = []
		def remainingBalance = 0
		jobOrderInstance.requisition.items.each{
			remainingBalance = jobOrderInstance.computeProjectedRequisitionItemRemainingBalance(it)
            remainingBalanceList.add(remainingBalance)
		}
		return remainingBalanceList
		
	}
	
	def hasNoReleaseValues(MaterialRelease materialReleaseInstance) {
		def ifNoReleaseValues = true
		materialReleaseInstance.items.each{
			if(it.qty>0) {
				ifNoReleaseValues = false
			}
		}
		return ifNoReleaseValues
	}
	
	def checkIfHasValidReleaseValuesForEdit(MaterialRelease materialReleaseInstance, params) {
		def hasValidReleaseValues = false
		def itemSize = (materialReleaseInstance.items.size()-1)
		for (idx in 0..itemSize) {
			def releaseValue = params.("releaseItemList["+idx+"].qty")
				if(Double.parseDouble(releaseValue) > 0){
					hasValidReleaseValues = true
				}
			}
		return hasValidReleaseValues
	}
	
	def updateItemsCost(MaterialRelease materialReleaseInstance){
		materialReleaseInstance.items.each {
			it.cost = it.materialRequisitionItem?.component?.runningCost
			it.save()
		}
	}
	
}
