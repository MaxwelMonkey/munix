package com.munix

class ItemTypeController {

    def index = { redirect(action: "list", params: params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def list = {
        params.max = Math.min(params.max ? params.max.toInteger() : 10,  100)
        [itemTypeInstanceList: ItemType.list(params), itemTypeInstanceTotal: ItemType.count()]
    }

    def create = {
        def itemTypeInstance = new ItemType()
        itemTypeInstance.properties = params
        return [itemTypeInstance: itemTypeInstance]
    }

    def save = {
        def itemTypeInstance = new ItemType(params)
        if (!itemTypeInstance.hasErrors() && itemTypeInstance.save()) {
            flash.message = "itemType.created"
            flash.args = [itemTypeInstance.id]
            flash.defaultMessage = "ItemType ${itemTypeInstance.id} created"
            redirect(action: "show", id: itemTypeInstance.id)
        }
        else {
            render(view: "create", model: [itemTypeInstance: itemTypeInstance])
        }
    }

    def show = {
        def itemTypeInstance = ItemType.get(params.id)
        if (itemTypeInstance) {
            return [itemTypeInstance: itemTypeInstance]
        }
        else {
            flash.message = "itemType.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "ItemType not found with id ${params.id}"
            redirect(action: "list")
        }
    }

    def edit = {
        def itemTypeInstance = ItemType.get(params.id)
        if (itemTypeInstance) {
            return [itemTypeInstance: itemTypeInstance]
        }
        else {
            flash.message = "itemType.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "ItemType not found with id ${params.id}"
            redirect(action: "list")
        }
    }

    def update = {
        def itemTypeInstance = ItemType.get(params.id)
        if (itemTypeInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (itemTypeInstance.version > version) {
                    
                    itemTypeInstance.errors.rejectValue("version", "itemType.optimistic.locking.failure", "Another user has updated this ItemType while you were editing")
                    render(view: "edit", model: [itemTypeInstance: itemTypeInstance])
                    return
                }
            }
            itemTypeInstance.properties = params
            if (!itemTypeInstance.hasErrors() && itemTypeInstance.save()) {
                flash.message = "itemType.updated"
                flash.args = [params.id]
                flash.defaultMessage = "ItemType ${params.id} updated"
                redirect(action: "show", id: itemTypeInstance.id)
            }
            else {
                render(view: "edit", model: [itemTypeInstance: itemTypeInstance])
            }
        }
        else {
            flash.message = "itemType.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "ItemType not found with id ${params.id}"
            redirect(action: "edit", id: params.id)
        }
    }

}
