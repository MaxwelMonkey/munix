package com.munix
import grails.converters.*
import java.math.RoundingMode;

class CreditMemoItemController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
    def salesDeliveryService
    def creditMemoService
    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [creditMemoItemInstanceList: CreditMemoItem.list(params), creditMemoItemInstanceTotal: CreditMemoItem.count()]
    }

    def create = {
        def creditMemoItemInstance = new CreditMemoItem()
        creditMemoItemInstance.properties = params
        def creditMemoInstance = CreditMemo.get(params.id)
        def salesDeliveryList = salesDeliveryService.listOfSalesDeliveryForCreditMemo(creditMemoInstance.customer,creditMemoInstance.discountType, creditMemoInstance)

        return [creditMemoItemInstance: creditMemoItemInstance, creditMemoInstance : creditMemoInstance, salesDeliveryList : salesDeliveryList]
    }
    def save = {
        def salesDeliveryList
        def creditMemoItemInstance = new CreditMemoItem(params)
        creditMemoItemInstance?.creditMemo = CreditMemo.get(params.id)
        if(creditMemoItemInstance?.creditMemo){
            salesDeliveryList = salesDeliveryService.listOfSalesDeliveryForCreditMemo(creditMemoItemInstance?.creditMemo?.customer,creditMemoItemInstance?.creditMemo?.discountType, creditMemoItemInstance?.creditMemo)
        }
        else{
            flash.message = "Please select a credit memo before creating an item"
            render(view: "create", model: [creditMemoItemInstance: creditMemoItemInstance,creditMemoInstance : creditMemoItemInstance.creditMemo, salesDeliveryList:salesDeliveryList])
            return
        }
        if(!params.salesDelivery){
            flash.message =  "${message(code: 'default.blank.message', args: ['Delivery'])}"
            render(view: "create", model: [creditMemoInstance : CreditMemo.get(params.id), salesDeliveryList:salesDeliveryList])
            return
        }
        else if(!params.deliveryItem.id){
            flash.message =  "${message(code: 'default.blank.message', args: ["Product"])}"
            render(view: "create", model: [creditMemoInstance : CreditMemo.get(params.id), salesDeliveryList:salesDeliveryList])
            return
        }else if(params.oldQty == '0' && params.newQty == '0'){
            flash.message = "Cannot create a new item because the old qty and new qty are 0"
            render(view: "create", model: [creditMemoInstance : CreditMemo.get(params.id), salesDeliveryList:salesDeliveryList])
            return
        }else if(creditMemoService.checkIfUnapprovedCreditMemoExistForProduct(creditMemoItemInstance.deliveryItem)){
            flash.message = "Cannot save record, there is already an existing unapproved credit memo for this item."
            render(view: "create", model: [creditMemoInstance : CreditMemo.get(params.id), salesDeliveryList:salesDeliveryList])
            return
        }else{
            if (creditMemoItemInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.created.message', args: [message(code: 'creditMemoItem.label', default: 'CreditMemoItem'), creditMemoItemInstance.id])}"
                redirect(controller: "creditMemo", action: "show", id: creditMemoItemInstance?.creditMemo?.id)
            }
            else {
                render(view: "create", model: [creditMemoItemInstance: creditMemoItemInstance,creditMemoInstance : creditMemoItemInstance.creditMemo, salesDeliveryList:salesDeliveryList])
            }
        }
    }
    def show = {
        def creditMemoItemInstance = CreditMemoItem.get(params.id)
        if (creditMemoItemInstance) {
            [creditMemoItemInstance: creditMemoItemInstance]
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'creditMemoItem.label', default: 'CreditMemoItem'), params.id])}"
            redirect(action: "list")
        }
    }

    def edit = {
        def creditMemoItemInstance = CreditMemoItem.get(params.id)
        if (creditMemoItemInstance) {
            if(creditMemoService.checkIfCreditMemoExistForProduct(creditMemoItemInstance.deliveryItem, creditMemoItemInstance.date)){
                flash.message = "A credit memo item has already been created. To edit this please cancel or delete the item before"
                redirect(controller:"creditMemo", action: "show" , id: creditMemoItemInstance.creditMemo?.id)
            }else{
                def salesDeliveryList = salesDeliveryService.listOfSalesDeliveryForCreditMemo(creditMemoItemInstance.creditMemo.customer,creditMemoItemInstance.creditMemo.discountType, creditMemoItemInstance?.creditMemo)
                def deliveryItemList = creditMemoItemInstance.creditMemo.items?.deliveryItem - creditMemoItemInstance?.deliveryItem
                def salesDeliveryItemList = salesDeliveryService.listSalesDeliveryItemForCreditMemo(creditMemoItemInstance.deliveryItem.delivery, deliveryItemList?.id)
				def discount = (creditMemoItemInstance.obtainDiscountRate()/100).setScale(2,RoundingMode.DOWN)
				def discountedNewPrice = creditMemoItemInstance.deliveryItem?.price - (creditMemoItemInstance.deliveryItem?.price * discount)
                return [creditMemoItemInstance: creditMemoItemInstance,salesDeliveryList : salesDeliveryList, salesDeliveryItemList:salesDeliveryItemList, discount: discount, discountedNewPrice: discountedNewPrice]
            }

        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'creditMemoItem.label', default: 'CreditMemoItem'), params.id])}"
            redirect(action: "list")
        }
    }

    def update = {
        def salesDeliveryList
        def creditMemoItemInstance = CreditMemoItem.get(params.id)
        def salesDeliveryItemList = []
        if (creditMemoItemInstance) {
             salesDeliveryList = salesDeliveryService.listOfSalesDeliveryForCreditMemo(creditMemoItemInstance?.creditMemo?.customer,creditMemoItemInstance?.creditMemo?.discountType, creditMemoItemInstance?.creditMemo)
             def deliveryItemList = creditMemoItemInstance.creditMemo.items?.deliveryItem - creditMemoItemInstance?.deliveryItem
             salesDeliveryItemList = salesDeliveryService.listSalesDeliveryItemForCreditMemo(creditMemoItemInstance.deliveryItem.delivery, deliveryItemList?.id)
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'creditMemoItem.label', default: 'CreditMemoItem'), params.id])}"
            redirect(action: "list")
            return
        }
        if(!params.salesDelivery){
            flash.message =  "${message(code: 'default.blank.message', args: ["Delivery"])}"
            render(view: "edit", model: [creditMemoItemInstance: creditMemoItemInstance, salesDeliveryList:salesDeliveryList, salesDeliveryItemList:salesDeliveryItemList])
            return
        }
        else if(!params.deliveryItem.id){
            flash.message =  "${message(code: 'default.blank.message', args: ["Product"])}"
            render(view: "edit", model: [creditMemoItemInstance:creditMemoItemInstance, salesDeliveryList:salesDeliveryList,salesDeliveryItemList:salesDeliveryItemList])
            return
        }else if(params.oldQty=='0'){
            flash.message = "Cannot create a new item because the old qty is 0"
            render(view: "edit", model: [creditMemoItemInstance : creditMemoItemInstance, salesDeliveryList:salesDeliveryList, salesDeliveryItemList:salesDeliveryItemList])
            return
        }
        else if(params.oldQty<params.newQty){
            flash.message = "The old qty cannot be less than the new qty."
            render(view: "edit", model: [creditMemoItemInstance : creditMemoItemInstance, salesDeliveryList:salesDeliveryList, salesDeliveryItemList:salesDeliveryItemList])
            return
        }
        else{
            if (params.version) {
                def version = params.version.toLong()
                if (creditMemoItemInstance.version > version) {
                    creditMemoItemInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'creditMemoItem.label', default: 'CreditMemoItem')] as Object[], "Another user has updated this CreditMemoItem while you were editing")
                    render(view: "edit", model: [creditMemoItemInstance: creditMemoItemInstance])
                    return
                }
            }
            creditMemoItemInstance.properties = params
            if (!creditMemoItemInstance.hasErrors() && creditMemoItemInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'creditMemoItem.label', default: 'CreditMemoItem'), creditMemoItemInstance.id])}"
                redirect(controller:"creditMemo", action: "show", id: creditMemoItemInstance.creditMemo?.id)
            }
            else {
                render(view: "edit", model: [creditMemoItemInstance: creditMemoItemInstance, salesDeliveryList:salesDeliveryList, salesDeliveryItemList:salesDeliveryItemList])
            }
        }
    }
    def delete = {
        def creditMemoItemInstance = CreditMemoItem.get(params.id)
        if (creditMemoItemInstance) {
            try {
                def creditMemoId=creditMemoItemInstance.creditMemo.id
                creditMemoItemInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'creditMemoItem.label', default: 'CreditMemoItem'), params.id])}"
                redirect(action: "show",controller:"creditMemo",id:creditMemoId)
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'creditMemoItem.label', default: 'CreditMemoItem'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'creditMemoItem.label', default: 'CreditMemoItem'), params.id])}"
            redirect(action: "list")
        }
    }
    def updateSelect = {
        def tempList=[]
        if(params.selectedValue && params.creditMemoId){
            def salesDeliveryInstance = SalesDelivery.findById(params.selectedValue)
            def creditMemoInstance = CreditMemo.findById(params.creditMemoId)
            def salesDeliveryItemList = []
            def creditMemoItemInstance = []
            if(params.creditMemoItemId){
                creditMemoItemInstance = CreditMemoItem.findById(params?.creditMemoItemId)
            }
            def deliveryItemList = creditMemoInstance.items?.deliveryItem - creditMemoItemInstance?.deliveryItem
            if(deliveryItemList.isEmpty()){
                salesDeliveryItemList = salesDeliveryInstance.items
            }
            else{
                salesDeliveryItemList = salesDeliveryService.listSalesDeliveryItemForCreditMemo(salesDeliveryInstance, deliveryItemList?.id)
            }
            def usedCreditMemoItemList = CreditMemoItem.executeQuery("from CreditMemoItem c where (c.creditMemo.status = 'Unapproved' or c.creditMemo.status = 'Paid') and c.creditMemo.customer.id = 57")
            salesDeliveryItemList.each{
            	def sdi = it
            	def add=true
            	usedCreditMemoItemList.each{
            		if(sdi.id == it.deliveryItem?.id){
            			add = false
            		}
            	}
    			if (add && sdi.qty != 0) {
    				tempList.add([id:sdi.id,product:sdi.product.toString()])
    			}
            }
        }

        def jsonMap=[items:tempList]
        render jsonMap as JSON
    }
    def updateOldPriceAndQtyFields = {
        def salesDeliveryItemInstance
        def creditMemoItemInstance
		def discount = 0
		def discountedNewPrice
        if(params.creditMemoItemId){
            creditMemoItemInstance = CreditMemoItem.findById(params?.creditMemoItemId)
        }
        if(params.selectedValue){
            salesDeliveryItemInstance = SalesDeliveryItem.findById(params?.selectedValue)
            discount = ((salesDeliveryItemInstance.getDiscount()?:0)/100).setScale(2,RoundingMode.DOWN)
            discountedNewPrice = salesDeliveryItemInstance?.price - (salesDeliveryItemInstance?.price * discount)
        }
		
        def jsonMap = [qty:salesDeliveryItemInstance?.computeRemainingQtyExcludingCreditMemo(creditMemoItemInstance)?:"0", price:salesDeliveryItemInstance?.price?:"0",discount:discount, discountedNewPrice: discountedNewPrice?:"0"]
        render jsonMap as JSON
    }
}
