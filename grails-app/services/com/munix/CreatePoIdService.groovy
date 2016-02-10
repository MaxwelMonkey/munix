package com.munix

class CreatePoIdService {

    boolean transactional = true

    def createPoIds() {
		def pos=PurchaseOrder.list()
		pos.each{
			def poiInstance=new PurchaseOrderId()
			poiInstance.po=it
			poiInstance.supplier=it.supplier
			poiInstance.year=poiInstance.currentYear()
			poiInstance.purchaseOrderId=poiInstance.createNewPoId()
			poiInstance.save()
			it.poId=poiInstance
			it.save()
		}
    }
}
