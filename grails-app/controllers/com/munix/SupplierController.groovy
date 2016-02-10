package com.munix

import grails.converters.JSON

class SupplierController {
    def supplierService
    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        def searchIdentifier = params.searchIdentifier
        def searchName = params.searchName
        def searchCountry = params.searchCountry
        def searchContact = params.searchContact

        if (!params.max) params.max = 100
        if (!params.offset) params.offset = 0
        if (!params.sort) params.sort = "identifier"
        if (!params.order) params.order = "asc"

        def query = {
            //Search
            if(searchIdentifier){
                like('identifier', "%${searchIdentifier}%")
            }

            if(searchName){
                like('name', "%${searchName}%")
            }

            if(searchCountry){
                country{
                    like('identifier', "%${searchCountry}%")
                }
            }

            if(searchContact){
                like('contact', "%${searchContact}%")
            }

            if (params.sort == 'country') {
                country {
                    order('identifier', params.order)
                }
            }
        }

        def supplierInstanceList = Supplier.createCriteria().list(params,query)
        [supplierInstanceList: supplierInstanceList, supplierInstanceTotal: Supplier.createCriteria().count(query)]
    }

    def create = {
        def supplierInstance = new Supplier()
        supplierInstance.properties = params
        return [supplierInstance: supplierInstance]
    }

    def save = {
        def supplierInstance = new Supplier(params)
        if (supplierInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'supplier.label', default: 'Supplier'), supplierInstance.id])}"
            redirect(action: "show", id: supplierInstance.id)
        }
        else {
            render(view: "create", model: [supplierInstance: supplierInstance])
        }
    }

    def show = {
        def supplierInstance = Supplier.get(params.id)
        if (supplierInstance) {
            def showEditButton = supplierService.rolePurchasingIsLoggedIn()
            [supplierInstance: supplierInstance, showEditButton: showEditButton]
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'supplier.label', default: 'Supplier'), params.id])}"
            redirect(action: "list")

        }
    }

    def edit = {
        if(supplierService.rolePurchasingIsLoggedIn()){
            def supplierInstance = Supplier.get(params.id)
            if (supplierInstance) {
                return [supplierInstance: supplierInstance]
            }
            else {
                flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'supplier.label', default: 'Supplier'), params.id])}"
                redirect(action: "list")
            }
        }else{
            flash.message = "Sorry you are not allowed to access this feature"
            redirect(action: "list")
        }

    }

    def update = {
        def supplierInstance = Supplier.get(params.id)
        if (supplierInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (supplierInstance.version > version) {
                    
                    supplierInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'supplier.label', default: 'Supplier')] as Object[], "Another user has updated this Supplier while you were editing")
                    render(view: "edit", model: [supplierInstance: supplierInstance])
                    return
                }
            }
            supplierInstance.properties = params
            if (!supplierInstance.hasErrors() && supplierInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'supplier.label', default: 'Supplier'), supplierInstance.id])}"
                redirect(action: "show", id: supplierInstance.id)
            }
            else {
                render(view: "edit", model: [supplierInstance: supplierInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'supplier.label', default: 'Supplier'), params.id])}"
            redirect(action: "list")
        }
    }
    def editSupplierItems = {
        if(supplierService.rolePurchasingIsLoggedIn()){
            def supplierInstance = Supplier.get(params.id)
            if (supplierInstance) {
                return [supplierInstance: supplierInstance]
            }
            else {
                flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'supplier.label', default: 'Supplier'), params.id])}"
                redirect(action: "list")
            }
        }else{
            flash.message = "Sorry you are not allowed to access this feature"
            redirect(action: "list")
        }
    }
    def updateSupplierItems = {
		def supplierInstance = Supplier.get(params.id)
		supplierInstance?.properties = params

		def isComponentsEmpty = false
		def toBeDeleted = supplierInstance?.items.findAll {it.isDeleted}
		if (toBeDeleted) {
			if(toBeDeleted.size() >= supplierInstance?.items.size()) {
				isComponentsEmpty = true
			}
			else {
				toBeDeleted.each { deleted->
					supplierInstance.removeFromItems(deleted)
					deleted.delete()
				}
				supplierInstance?.items?.removeAll(toBeDeleted)
			}
		}

		if(isComponentsEmpty) {
			flash.error = "Supplier item not updated! Must have at least one component."
		} else {
			if (supplierInstance.save(flush: true, cascade : true)) {
				flash.message = "${message(code: 'default.updated.message', args: [message(code: 'supplierItem.label', default: 'Supplier item'), supplierInstance.id])}"
				redirect(action: "show", id: supplierInstance.id)
				return
			} else {
				flash.error = "Supplier item not updated!"
			}
		}
		redirect(action: "show", id: supplierInstance.id)
		return
	}

    def generateAvailableProductsForSupplierItem = {
		def productList = new ArrayList(supplierService.queryAvailableProducts(params.sSearch))
		def data = createSupplierItemProductDataMap(productList, params.iSortCol_0)
		def jsonResponse = [iTotalRecords : data.size(), iTotalDisplayRecords : data.size(), aaData : data]
		render jsonResponse as JSON
	}
    private List createSupplierItemProductDataMap(List productList, sortColumn) {
		def data = []
		if(!sortColumn) sortColumn = "1"
    	if(sortColumn=="1"){
    		productList = productList.sort{ it.identifier}
    	}else if(sortColumn=="2"){
    		productList = productList.sort{ it.formatDescription() }
    	}
		productList?.each {
			def dataMap = []
			dataMap.addAll(
                it.id,
                it.identifier,
                it.description,
            )
			data.add(dataMap)
		}
		return data
	}
}
