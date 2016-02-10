package com.munix

class ProvinceController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [provinceInstanceList: Province.list(params), provinceInstanceTotal: Province.count()]
    }

    def create = {
        def provinceInstance = new Province()
        provinceInstance.properties = params
        return [provinceInstance: provinceInstance]
    }

    def save = {
        def provinceInstance = new Province(params)
        if (provinceInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'province.label', default: 'Province'), provinceInstance.id])}"
            redirect(action: "show", id: provinceInstance.id)
        }
        else {
            render(view: "create", model: [provinceInstance: provinceInstance])
        }
    }

    def show = {
        def provinceInstance = Province.get(params.id)
        if (provinceInstance) {
            [provinceInstance: provinceInstance]
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'province.label', default: 'Province'), params.id])}"
            redirect(action: "list")
        }
    }

    def edit = {
        def provinceInstance = Province.get(params.id)
        if (provinceInstance) {
            return [provinceInstance: provinceInstance]
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'province.label', default: 'Province'), params.id])}"
            redirect(action: "list")
        }
    }

    def update = {
        def provinceInstance = Province.get(params.id)
        if (provinceInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (provinceInstance.version > version) {
                    
                    provinceInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'province.label', default: 'Province')] as Object[], "Another user has updated this Province while you were editing")
                    render(view: "edit", model: [provinceInstance: provinceInstance])
                    return
                }
            }
            provinceInstance.properties = params
            if (!provinceInstance.hasErrors() && provinceInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'province.label', default: 'Province'), provinceInstance.id])}"
                redirect(action: "show", id: provinceInstance.id)
            }
            else {
                render(view: "edit", model: [provinceInstance: provinceInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'province.label', default: 'Province'), params.id])}"
            redirect(action: "list")
        }
    }

}
