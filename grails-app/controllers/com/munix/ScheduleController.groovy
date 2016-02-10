package com.munix

class ScheduleController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
    def authenticateService
    def index = {
        redirect(action: "list", params: params)
    }

    def search = {
        def scheduleList
        def counter
        def scheduleInstance
        try{
            if( params.id != ""){
                scheduleList = Schedule.findById(Long.parseLong(params.id))
            }

            if(params.status != ""){
                def scheduleCriteria = Schedule.createCriteria()
                scheduleList = scheduleCriteria {
                    ilike("status","%${params.status}%")
                    if(params.order == 'asc'){
                        order("status","des")
                    }else{
                        order("status". "asc")
                    }
                }
            }

            if( params.date != ""){
                def scheduleCriteria = PurchaseOrder.createCriteria()
                scheduleList = scheduleCriteria {
                    or{
                        eq("date", new Date(params.date))
                        ge("date", new Date(params.date))
                    }
                }
            }

            scheduleList.collect{
                scheduleInstance = Schedule.get(it.id)
                counter++
            }

            params.max = Math.min(params.max ? params.int('max') : 10, 100)
            render(view:'list',model: [scheduleInstanceList: ScheduleList, scheduleInstanceTotal: counter,
                    params:scheduleInstance])
        }catch(Exception){
            redirect(action:"index")
        }
        
    }


    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [scheduleInstanceList: Schedule.list(params), scheduleInstanceTotal: Schedule.count()]
    }

    def create = {
        def scheduleInstance = new Schedule()
        scheduleInstance.properties = params
        return [scheduleInstance: scheduleInstance]
    }

    def save = {
        def scheduleInstance = new Schedule(params)
        def principal = authenticateService.principal()
        scheduleInstance.preparedBy = "${principal.getUsername()}"
        if (scheduleInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'schedule.label', default: 'Schedule'), scheduleInstance.id])}"
            redirect(action: "show", id: scheduleInstance.id)
        }
        else {
            render(view: "create", model: [scheduleInstance: scheduleInstance])
        }
    }

    def show = {
        def scheduleInstance = Schedule.get(params.id)
        if (!scheduleInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'schedule.label', default: 'Schedule'), params.id])}"
            redirect(action: "list")
        }
        else {
            [scheduleInstance: scheduleInstance]
        }
    }

    def edit = {
        def scheduleInstance = Schedule.get(params.id)
        if (!scheduleInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'schedule.label', default: 'Schedule'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [scheduleInstance: scheduleInstance]
        }
    }

    def update = {
        def scheduleInstance = Schedule.get(params.id)
        if (scheduleInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (scheduleInstance.version > version) {
                    
                    scheduleInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'schedule.label', default: 'Schedule')] as Object[], "Another user has updated this Schedule while you were editing")
                    render(view: "edit", model: [scheduleInstance: scheduleInstance])
                    return
                }
            }
            scheduleInstance.properties = params
            if (!scheduleInstance.hasErrors() && scheduleInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'schedule.label', default: 'Schedule'), scheduleInstance.id])}"
                redirect(action: "show", id: scheduleInstance.id)
            }
            else {
                render(view: "edit", model: [scheduleInstance: scheduleInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'schedule.label', default: 'Schedule'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def scheduleInstance = Schedule.get(params.id)
        if (scheduleInstance) {
            try {
                scheduleInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'schedule.label', default: 'Schedule'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'schedule.label', default: 'Schedule'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'schedule.label', default: 'Schedule'), params.id])}"
            redirect(action: "list")
        }
    }

    def chart = {
        render(view:"chart")
    }
}
