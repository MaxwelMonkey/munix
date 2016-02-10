package com.munix


import grails.converters.JSON

class PriceAdjustmentController {
	def authenticateService
	def priceAdjustmentService
	def generalMethodService
    private final static String APPROVABLE = "Approvable"
    private final static String DATEERROR = "DateError"
    private final static String PRICETYPEANDITEMTYPEERROR = "PriceTypeAndItemTypeError"
    def index = { redirect(action: "list", params: params) }

    // the save and update actions only accept POST requests
    static allowedMethods = [save: "POST", update: "POST"]

    def list = {
		if (!params.max) params.max = 100
		if (!params.offset) params.offset = 0
		if (!params.sort) params.sort = "id"
		if (!params.order) params.order = "asc"
		
		def generatedlist = priceAdjustmentService.generateList(params)
		[priceAdjustmentInstanceList: generatedlist, priceAdjustmentInstanceTotal: generatedlist.totalCount]
    }

    def create = {
        def priceAdjustmentInstance = new PriceAdjustment(params)
        return [priceAdjustmentInstance: priceAdjustmentInstance, warehouses: Warehouse.list(sort:"identifier")]
    }

    def save = {
		params.preparedBy = FormatUtil.createTimeStamp(authenticateService.userDomain())
		params.priceType = PriceType.getTypeByName(params.priceType)
		
        def priceAdjustmentInstance = new PriceAdjustment(params)
		
		def toBeDeleted = priceAdjustmentInstance.itemList?.findAll { it.isDeleted }
		priceAdjustmentInstance.items.removeAll(toBeDeleted)
		
        if (!priceAdjustmentInstance.hasErrors() && priceAdjustmentInstance.save()) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'priceAdjustment.label', default: 'Price Adjustment'), priceAdjustmentInstance.id])}"
            redirect(action: "show", id: priceAdjustmentInstance.id)
        } else {
            render(view: "create", model: [priceAdjustmentInstance: priceAdjustmentInstance, warehouses: Warehouse.list(sort:"identifier")])
        }
    }

    def show = {
        def priceAdjustmentInstance = PriceAdjustment.get(params.id)
        if (priceAdjustmentInstance) {
            return [priceAdjustmentInstance: priceAdjustmentInstance]
        }
        else {
            flash.message = "Price Adjustment not found."
            flash.args = [params.id]
            flash.defaultMessage = "Price Adjustment not found with id ${params.id}."
            redirect(action: "list")
        }
    }

    def edit = {
        def priceAdjustmentInstance = PriceAdjustment.get(params.id)
        if (priceAdjustmentInstance) {
            return [priceAdjustmentInstance: priceAdjustmentInstance, warehouses: Warehouse.list(sort:"identifier")]
        }
        else {
            flash.message = "Price Adjustment not found."
            flash.args = [params.id]
            flash.defaultMessage = "Price Adjustment not found with id ${params.id}."
            redirect(action: "list")
        }
    }

    def update = {
        def priceAdjustmentInstance = PriceAdjustment.get(params.id)
        if (priceAdjustmentInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (priceAdjustmentInstance.version > version) {
                    
                    priceAdjustmentInstance.errors.rejectValue("version", "priceAdjustment.optimistic.locking.failure", "Another user has updated this Price Adjustment while you were editing.")
                    render(view: "edit", model: [priceAdjustmentInstance: priceAdjustmentInstance, warehouses: Warehouse.list(sort:"identifier")])
                    return
                }
            }
			params.priceType = PriceType.getTypeByName(params.priceType)
			
            priceAdjustmentInstance.properties = params
			
			def toBeDeleted = priceAdjustmentInstance.itemList?.findAll { it.isDeleted }
			priceAdjustmentInstance.items.removeAll(toBeDeleted)
	
            if (!priceAdjustmentInstance.hasErrors() && priceAdjustmentInstance.save()) {
                flash.message = "Price Adjustment updated."
                flash.args = [params.id]
                flash.defaultMessage = "Price Adjustment ${params.id} updated."
                redirect(action: "show", id: priceAdjustmentInstance.id)
            } else {
                render(view: "edit", model: [priceAdjustmentInstance: priceAdjustmentInstance, warehouses: Warehouse.list(sort:"identifier")])
            }
        } else {
            flash.message = "Price Adjustment not found"
            flash.args = [params.id]
            flash.defaultMessage = "Price Adjustment not found with id ${params.id}."
            redirect(action: "edit", id: params.id)
        }
    }
	
    def retrieveProductsForSale = {
        def itemType = ItemType.findById(params.itemType)
		if (itemType) {
		    def productList = priceAdjustmentService.queryAvailableProducts(itemType, params.sSearch)
		    def data = generateDataMap(productList, params)
		
		    def jsonResponse = [iTotalRecords : data.size(), iTotalDisplayRecords : data.size(), aaData : data]
		    render jsonResponse as JSON
		} else {
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'itemType.label', default: 'Item Type'), params.itemType])}"
			redirect(action: "create")
		}
    }

	private List generateDataMap(Set productList, Map params) {
		def warehouseList = Warehouse.list(sort: "identifier")
		def data = []
		productList?.each{product->
			def dataMap = []
			dataMap.addAll(
				product.id,
				product.identifier,
				product.description,
				product.isNet,
				product.getProductPrice(params.priceType), // for display only
				product.getProductPrice("Wholesale"),
				product.getProductPrice("Retail")
			)
			warehouseList.each {
				dataMap.add(product.formatSOH(it))
			}
			data.add(dataMap)
		}
		return data
	}
	
    def approve = {
        def priceAdjustmentInstance = PriceAdjustment.get(params.id)
        def message = priceAdjustmentService.checkIfApprovable(priceAdjustmentInstance)
        if(APPROVABLE.equals(message)) {
            priceAdjustmentInstance.approvedBy = FormatUtil.createTimeStamp(authenticateService.userDomain())
		    priceAdjustmentInstance.approved()
            priceAdjustmentInstance?.save()
            flash.message = "Price Adjustment has been successfully Approved."
        }else if(DATEERROR.equals(message)){
            flash.error = "Effective Date cannot be today or earlier. Price Adjustment can't be Approved."
        }else if(PRICETYPEANDITEMTYPEERROR.equals(message)){
            flash.error = "A Price Adjustment with the same Price Type and same Item Type for the same Effective Date already exists."
        }
        redirect(action:'show',id:priceAdjustmentInstance?.id)
    }

	def unapprove = {
		def priceAdjustmentInstance = PriceAdjustment.get(params.id)
		
		if(priceAdjustmentService.checkIfUnapprovable(priceAdjustmentInstance)) {
			priceAdjustmentInstance?.unapproved()
			priceAdjustmentInstance.approvedBy = ""
			priceAdjustmentInstance?.save()
			flash.message = "Price Adjustment has been successfully Unapproved."
		} else {
			flash.message = "Price Adjustment can no longer be Unapproved."
		}
			redirect(action:'show',id:priceAdjustmentInstance?.id)
	}

	def cancel = {
		def priceAdjustmentInstance = PriceAdjustment.get(params.id)
        if(priceAdjustmentService.isCancelable(priceAdjustmentInstance)){
    		priceAdjustmentInstance.cancelledBy = FormatUtil.createTimeStamp(authenticateService.userDomain())
            priceAdjustmentInstance?.cancelled()
            priceAdjustmentInstance?.save()
            flash.message = "Price Adjustment has now been Cancelled."
        }else{
            flash.error = "Price Adjustment can't be cancelled because it is currently approved."
        }
		redirect(action:'show',id:priceAdjustmentInstance?.id)
	}
	
	def print = {
		redirect(action:'show',id:params?.id)
	}
}
