package com.munix

class SupplierPaymentController {

    static allowedMethods = [save: "POST", update: "POST", approve: "POST"]
    def authenticateService
    def constantService
    def supplierPaymentService
    def editSupplierPaymentService

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 100, 100)
        [supplierPaymentInstanceList: SupplierPayment.list(params), supplierPaymentInstanceTotal: SupplierPayment.count()]
    }

    def create = {
        def supplierPaymentInstance = new SupplierPayment()
        supplierPaymentInstance.properties = params
        def supplierList = supplierPaymentService.availableSuppliers()
        def purchaseInvoiceList
        if(params.id) {
            def supplierInstance = Supplier.get(params?.id)
            supplierPaymentInstance.supplier=supplierInstance
            purchaseInvoiceList = supplierPaymentService.availablePurchaseInvoices(supplierInstance)
        }

        return [supplierPaymentInstance: supplierPaymentInstance, sortedByIdentifier:supplierList.sortedByIdentifier, sortedByName:supplierList.sortedByName, purchaseInvoiceList:purchaseInvoiceList]
    }

    def save = {
		params.preparedBy = FormatUtil.createTimeStamp(authenticateService.userDomain())
        def supplierPaymentInstance = new SupplierPayment(params)
        def purchaseInvoiceList = request.getParameterValues('purchaseInvoice').collect { it.toLong() }
        if(supplierPaymentService.checkIfAPurchaseInvoiceHasAlreadyBeenTaken(supplierPaymentInstance.supplier,purchaseInvoiceList)){
            flash.error = "One or more of the Purchase Invoice selected has been taken by another Supplier Payment already."
            redirect(action: "create", model: [supplierPaymentInstance: supplierPaymentInstance])
        }else{
            editSupplierPaymentService.addPurchaseInvoiceToSupplierPayment(supplierPaymentInstance, purchaseInvoiceList)
            if(!supplierPaymentInstance.supplier){
                flash.error = "Please select a Supplier."
                redirect(action: "create", model: [supplierPaymentInstance: supplierPaymentInstance,purchaseInvoiceList:purchaseInvoiceList])
            }else{
	            if (supplierPaymentInstance.save(flush: true) && !purchaseInvoiceList.isEmpty()) {
	                 flash.message = "${message(code: 'default.created.message', args: [message(code: 'supplierPayment.label', default: 'SupplierPayment'), supplierPaymentInstance.id])}"
	                 redirect(action: "show", id: supplierPaymentInstance.id)
	            }
	            else {
	            	supplierPaymentInstance.errors.allErrors.each{
	            		println it
	            	}
	                flash.error = "Please select at least one Purchase Invoice."
	                redirect(action: "create", model: [supplierPaymentInstance: supplierPaymentInstance,purchaseInvoiceList:purchaseInvoiceList])
	            }
            }
        }

    }

    def show = {
        def supplierPaymentInstance = SupplierPayment.get(params.id)
        if (!supplierPaymentInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'supplierPayment.label', default: 'SupplierPayment'), params.id])}"
            redirect(action: "list")
        }
        else {
            [supplierPaymentInstance: supplierPaymentInstance]
        }
    }

    def edit = {
        def supplierPaymentInstance = SupplierPayment.get(params.id)

        if (!supplierPaymentInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'supplierPayment.label', default: 'SupplierPayment'), params.id])}"
            redirect(action: "list")
        }
        else {
            def purchaseInvoiceList = supplierPaymentService.availablePurchaseInvoices(supplierPaymentInstance.supplier)
			purchaseInvoiceList.addAll(0, supplierPaymentInstance.purchaseInvoices)
            return [supplierPaymentInstance: supplierPaymentInstance, purchaseInvoiceList:purchaseInvoiceList]
        }
    }

    def update = {
        def supplierPaymentInstance = SupplierPayment.get(params.id)
        if (supplierPaymentInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (supplierPaymentInstance.version > version) {
                    
                    supplierPaymentInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'supplierPayment.label', default: 'SupplierPayment')] as Object[], "Another user has updated this SupplierPayment while you were editing")
                    render(view: "edit", model: [supplierPaymentInstance: supplierPaymentInstance])
                    return
                }
            }
            supplierPaymentInstance.properties = params
            def purchaseInvoiceList = request.getParameterValues('purchaseInvoice').collect { it.toLong() }

			/*if (purchaseInvoiceList.isEmpty()) {
				flash.error = "Please select at least one Purchase Invoice."
				redirect(action: "show", id: supplierPaymentInstance.id)
			}
            else*/  // Removed restriction of at least one purchase invoice.
            if (!supplierPaymentInstance.hasErrors() && supplierPaymentInstance.save(flush: true)) {
				supplierPaymentService.updatePurchaseInvoiceFromSupplierPayment(supplierPaymentInstance, purchaseInvoiceList)
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'supplierPayment.label', default: 'SupplierPayment'), supplierPaymentInstance.id])}"
                redirect(action: "show", id: supplierPaymentInstance.id)
            }
            else {
                def invoicesAvailable = supplierPaymentService.availablePurchaseInvoices(supplierPaymentInstance.supplier)
                render(view: "edit", model: [supplierPaymentInstance: supplierPaymentInstance,purchaseInvoiceList:invoicesAvailable])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'supplierPayment.label', default: 'SupplierPayment'), params.id])}"
            redirect(action: "list")
        }
    }

    def approve = {
        def supplierPaymentInstance = SupplierPayment.get(params.id)
		if (supplierPaymentInstance.isFullyPaid()) {
	        supplierPaymentInstance?.approve()
			supplierPaymentInstance?.approvedBy = FormatUtil.createTimeStamp(authenticateService.userDomain())
			supplierPaymentInstance.purchaseInvoices.each {
				it.paid()
			}
			if (supplierPaymentInstance?.save()) {
				flash.message = "Supplier Payment has been successfully approved!"
				redirect(action: 'show', id: supplierPaymentInstance?.id)
			} else {
				flash.message = "Failed to approve supplier payment."
				redirect(action: "show", id: supplierPaymentInstance?.id)
			}
        } else {
			flash.message = "Can not approve until all invoices are paid."
			redirect(action: "show", id: supplierPaymentInstance?.id)
		}
    }
		
	def cancel = {
		def supplierPaymentInstance = SupplierPayment.get(params.id)
        if(supplierPaymentInstance.items){
            flash.message = "Supplier payment cannot be cancelled because there are still items in use!"
        }else{
    		supplierPaymentInstance.cancelledBy = FormatUtil.createTimeStamp(authenticateService.userDomain())
            supplierPaymentInstance?.cancel()
            supplierPaymentInstance?.save()
            flash.message = "Supplier payment has been successfully cancelled!"
        }
		redirect(action:'show',id:supplierPaymentInstance?.id)
	}
	
	def unapprove = {
		def supplierPaymentInstance = SupplierPayment.get(params.id)
		supplierPaymentInstance?.unapprove()
		supplierPaymentInstance.approvedBy = ""
		supplierPaymentInstance.purchaseInvoices.each {
			it.approved()
		}
		if (supplierPaymentInstance?.save()) {
			flash.message = "Supplier payment has been successfully unapproved!"
			redirect(action:'show', id: supplierPaymentInstance?.id)
		} else {
			flash.message = "Failed to unapprove supplier payment."
			redirect(action: "show", id: supplierPaymentInstance?.id)
		}
	}
	
	def print = {
		redirect(action:'show',id:params?.id)
	}
}
