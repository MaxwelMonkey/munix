package com.munix

class ItemLocationController {

    def index = { redirect(action: "list", params: params) }

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def list = {
        params.max = Math.min(params.max ? params.max.toInteger() : 10,  100)
        [itemLocationInstanceList: ItemLocation.list(params), itemLocationInstanceTotal: ItemLocation.count()]
    }

    def create = {
        def itemLocationInstance = new ItemLocation()
        itemLocationInstance.properties = params
        return [itemLocationInstance: itemLocationInstance]
    }

    def save = {
        def itemLocationInstance = new ItemLocation(params)
        if (!itemLocationInstance.hasErrors() && itemLocationInstance.save()) {
            flash.message = "itemLocation.created"
            flash.args = [itemLocationInstance.id]
            flash.defaultMessage = "Item location created"
            redirect(action: "show", id: itemLocationInstance.id)
        }
        else {
            render(view: "create", model: [itemLocationInstance: itemLocationInstance])
        }
    }

    def show = {
        def itemLocationInstance = ItemLocation.get(params.id)
        if (itemLocationInstance) {
            return [itemLocationInstance: itemLocationInstance]
        }
        else {
            flash.message = "itemLocation.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "Item location not found"
            redirect(action: "list")
        }
    }

    def edit = {
        def itemLocationInstance = ItemLocation.get(params.id)
        if (itemLocationInstance) {
            return [itemLocationInstance: itemLocationInstance]
        }
        else {
            flash.message = "itemLocation.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "Item location not found"
            redirect(action: "list")
        }
    }

    def update = {
        def itemLocationInstance = ItemLocation.get(params.id)
        if (itemLocationInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (itemLocationInstance.version > version) {
                    
                    itemLocationInstance.errors.rejectValue("version", "itemLocation.optimistic.locking.failure", "Another user has updated this Item location while you were editing")
                    render(view: "edit", model: [itemLocationInstance: itemLocationInstance])
                    return
                }
            }
            itemLocationInstance.properties = params
            if (!itemLocationInstance.hasErrors() && itemLocationInstance.save()) {
                flash.message = "itemLocation.updated"
                flash.args = [params.id]
                flash.defaultMessage = "Item location updated"
                redirect(action: "show", id: itemLocationInstance.id)
            }
            else {
                render(view: "edit", model: [itemLocationInstance: itemLocationInstance])
            }
        }
        else {
            flash.message = "itemLocation.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "Item location not found"
            redirect(action: "edit", id: params.id)
        }
    }

}
