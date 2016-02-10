package com.munix

class LaborCostController {
	def list = {
		redirect(controller: "product", action: "list")
	}
	
	def show = {
		def laborCostInstance = LaborCost.get(params.id)
		if (laborCostInstance) {
			redirect(controller: "product", action: "show", id: params.id)
		} else {
			redirect(controller: "product", action: "list")
		}
	}
	
    def create = {
        def laborCostInstance = new LaborCost(params)
        return [laborCostInstance: laborCostInstance]
    }
	
	def save = {
		def laborCostInstance = new LaborCost(params)
		laborCostInstance.product = Product.get(params.id)
		if (laborCostInstance.save(flush: true)) {
			flash.message = "${message(code: 'default.created.message', args: [message(code: 'laborCost.label', default: 'Labor Cost'), laborCostInstance.id])}"
			redirect(action: "show", id: params.id, controller:"product")
		} else {
			render(view: "create", model: [laborCostInstance: laborCostInstance])
		}
	}
	
	def edit = {
		def laborCostInstance = LaborCost.get(params.id)
		if (laborCostInstance) {
			return [laborCostInstance: laborCostInstance]
		}
		else {
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'laborCost.label', default: 'Labor Cost'), params.id])}"
			redirect(action: "list")
		}
	}

	def update = {
		def laborCostInstance = LaborCost.get(params.id)
		if (laborCostInstance) {
			if (params.version) {
				def version = params.version.toLong()
				if (laborCostInstance.version > version) {
					
					laborCostInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'laborCost.label', default: 'Labor Cost')] as Object[], "Another user has updated this Labor Cost while you were editing")
					render(view: "edit", model: [laborCostInstance: laborCostInstance])
					return
				}
			}
			laborCostInstance.properties = params
			if (!laborCostInstance.hasErrors() && laborCostInstance.save(flush: true)) {
				flash.message = "${message(code: 'default.updated.message', args: [message(code: 'laborCost.label', default: 'Labor Cost'), laborCostInstance.id])}"
				redirect(action: "show", controller:"product", id: laborCostInstance.product.id)
			}
			else {
				render(view: "edit", model: [laborCostInstance: laborCostInstance])
			}
		}
		else {
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'laborCost.label', default: 'Labor Cost'), params.id])}"
			redirect(action: "list")
		}
	}
	
	def delete = {
		def laborCostInstance = LaborCost.get(params.id)
		if (laborCostInstance) {
			def productId = laborCostInstance.product.id
			try {
				laborCostInstance.delete(flush: true)
				flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'laborCost.label', default: 'Labor Cost'), params.id])}"
				redirect(action: "show", controller:"product", id:productId )
			}
			catch (org.springframework.dao.DataIntegrityViolationException e) {
				flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'laborCost.label', default: 'Labor Cost'), params.id])}"
				redirect(action: "show", id: params.id)
			}
		}
		else {
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'laborCost.label', default: 'Labor Cost'), params.id])}"
			redirect(action: "list")
		}
	}
}