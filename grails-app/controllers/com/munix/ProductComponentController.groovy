package com.munix

class ProductComponentController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.order = "component.identifier"
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [productComponentInstanceList: ProductComponent.list(params), productComponentInstanceTotal: ProductComponent.count()]
    }

}
