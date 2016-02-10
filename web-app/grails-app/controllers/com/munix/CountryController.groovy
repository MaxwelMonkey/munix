package com.munix

class CountryController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [countryInstanceList: Country.list(params), countryInstanceTotal: Country.count()]
    }

    def create = {
        def countryInstance = new Country()
        countryInstance.properties = params
        return [countryInstance: countryInstance]
    }

    def save = {
        def countryInstance = new Country(params)
        if (countryInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'country.label', default: 'Country'), countryInstance.id])}"
            redirect(action: "show", id: countryInstance.id)
        }
        else {
            render(view: "create", model: [countryInstance: countryInstance])
        }
    }

    def show = {
        def countryInstance = Country.get(params.id)
        if (countryInstance) {
            [countryInstance: countryInstance]
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'country.label', default: 'Country'), params.id])}"
            redirect(action: "list")
        }
    }

    def edit = {
        def countryInstance = Country.get(params.id)
        if (countryInstance) {
            return [countryInstance: countryInstance]
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'country.label', default: 'Country'), params.id])}"
            redirect(action: "list")
        }
    }

    def update = {
        def countryInstance = Country.get(params.id)
        if (countryInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (countryInstance.version > version) {
                    
                    countryInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'country.label', default: 'Country')] as Object[], "Another user has updated this Country while you were editing")
                    render(view: "edit", model: [countryInstance: countryInstance])
                    return
                }
            }
            countryInstance.properties = params
            if (!countryInstance.hasErrors() && countryInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'country.label', default: 'Country'), countryInstance.id])}"
                redirect(action: "show", id: countryInstance.id)
            }
            else {
                render(view: "edit", model: [countryInstance: countryInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'country.label', default: 'Country'), params.id])}"
            redirect(action: "list")
        }
    }

}
