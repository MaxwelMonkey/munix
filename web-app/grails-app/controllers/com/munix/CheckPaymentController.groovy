package com.munix

class CheckPaymentController {
    static allowedMethods = [save: "POST", update: "POST"]
    def checkPaymentService
    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = 100
		if (!params.sort) params.sort = "date"
		if (!params.order) params.order = "asc"
        def checkPaymentInstanceList = checkPaymentService.getCheckPaymentListWithCriteria(params)
		def dateMap = checkPaymentService.generateDateStructList(params)
		[checkPaymentInstanceList: checkPaymentInstanceList, checkPaymentInstanceTotal: checkPaymentInstanceList.totalCount, dateMap: dateMap]
    }

    def search = {
		params.max = 100
		
		def query = {
			directPaymentItem {
				directPayment {
					eq("status", "Approved")
				}
			}
			
			if(params.id) {
				eq('id', Long.parseLong(params.id))
			}

			if(params.date) {
				ge('date', new Date(params.date))
			}

			if(params.bank) {
				bank{
					like('identifier', "%${params.bank}%")
				}
			}
		}
		
		def checkPaymentInstanceList = CheckPayment.createCriteria().list(params, query)
		render(view: 'list', model: [checkPaymentInstanceList: checkPaymentInstanceList, checkPaymentInstanceTotal: CheckPayment.createCriteria().count(query)])
	}
	
	def unpaidList = {
		if (!params.max) params.max = 100
		if (!params.offset) params.offset = 0
		if (!params.sort) params.sort = "id"
		if (!params.order) params.order = "asc"

		def customerInstance = Customer.get(params.customerId)
		def unpaidCheckPayments = checkPaymentService.getUnpaidCheckPaymentList(params, customerInstance)
		def totalUnpaidCheckPayments = checkPaymentService.computeTotalUnpaidCheckPayment(customerInstance)
		[customerInstance: customerInstance, totalUnpaidCheckPayments: totalUnpaidCheckPayments, unpaidCheckPaymentList: unpaidCheckPayments, unpaidCheckPaymentTotal: unpaidCheckPayments.totalCount]
	}
	
}
