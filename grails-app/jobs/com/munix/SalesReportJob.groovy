package com.munix

import java.util.Calendar;


class SalesReportJob {

	def cronExpression = "0 0 0 ? * MON-SUN"
		
	def mailService
			
	def execute() {
		try{
			println "RUNNING SALES REPORT JOB"
			def cal = Calendar.getInstance()
			cal.add(Calendar.DATE, -1)
			def dateFrom = cal.getTime()
			def dateTo = new Date()
			def dailyList = generateData(dateFrom, dateTo)
			def period  = dateFrom.format('MM/dd/yyyy')
			sendReportEmail(period, dailyList)
			if(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)==1){
				cal.set(Calendar.DAY_OF_MONTH, 1)
				dateFrom = cal.getTime()
				period = "the period " & dateFrom.format('MMM dd, yyyy')+" - "+dateTo.format('MMM dd, yyyy')
				def monthlyList = generateData(dateFrom, dateTo)
				sendReportEmail(period, monthlyList)
			}
		}catch(Exception e){
			e.printStackTrace()
		}
	}
		
    def sendReportEmail(period, map){	
    	def customerTypes = CustomerType.list().sort{it.identifier}
    	def discountTypes = DiscountType.list().sort{it.identifier}
    	
    	def company = com.munix.Company.get(1)?.name
    	def subj = ""+ company +" " + period 
    	def email = "<style>table.result{ border-collapse:collapse; border:solid 1px black;} table.result tr td{ padding:7px; }</style>"
    	email += "<strong>${company}</strong>"
    	email += "<br/><br/>" + period
    	email += "<br/><br/>"
    	email += "<table class='result' border='1' style='border-collapse:collapse; border:solid 1px black;'>"
    	email += "<tr><td></td>"
    	
    	def total = 0.0

    	discountTypes.each{
    		email += "<td style='padding:7px;' style='text-align:center;'><strong>${it}</strong></td>"
    	}
    	email += "<td style='padding:7px;' style='text-align:center;'><strong>Total</strong></td></tr>"

    	customerTypes.each{
    		def ct = map[it.id]
			def ctName = it?.identifier
			email += "<tr><td style='padding:7px;'><strong>${ctName}</strong></td>"
			def ctTotal = 0.00
			discountTypes.each{
        		if(ct){
					def dt = ct[it.id]
					if(dt){
						ctTotal += dt
						email += "<td align='right' style='padding:7px;'>${String.format('%,.2f',dt)}</td>"
					}else{
						email += "<td align='right' style='padding:7px;'>0.00</td>"
					}
        		}else{
					email += "<td align='right' style='padding:7px;'>0.00</td>"
        		}
			}
			email += "<td align='right' style='padding:7px;'><strong>${String.format('%,.2f',ctTotal)}</strong></td></tr>"
			total += ctTotal
    	}
		email += "<tr><td style='padding:7px;'><strong>Total</strong></td>"
		discountTypes.each{
			def dtTotal = map["dtTotals"][it.id]
			if(!dtTotal) dtTotal = 0.00
			email += "<td align='right' style='padding:7px;'><strong>${String.format('%,.2f',dtTotal)}</strong></td>"
		}
		email += "<td align='right' style='padding:7px;'><strong>${String.format('%,.2f',total)}</strong></td>"
    	email += "</tr></table>"
    	
    	def users = User.findAllByEmailSalesReport(true)
    	println "Recipients: "+ users
    	users.each{
			def emailAdd = it.email
			mailService.sendMail {
			   to emailAdd
			   from "admin@zigma-sales.com"
			   subject subj
			   html email
			}
		}
	}
	
	def generateData(dateFrom, dateTo) {
		def where = "where 1=1"
    	def paramMap = [:]
		where += " and date>=:dateFrom"
		paramMap.put("dateFrom",dateFrom)
		
		where += " and date<:dateTo"
    	paramMap.put("dateTo",dateTo)
    	def list = SalesDelivery.executeQuery("from SalesDelivery ${where}",paramMap)
    	
    	def dtTotals = [:]
    	def map = [:]
    	list.each{
    		def sd = it
    		def customerType = sd.customer?.type
    		def discountType = sd.invoice?.discountType
    		def ctData = map[customerType.id]
    		if(!ctData){
    			ctData = [:]
    		}
    		def dtData = ctData[discountType.id]
    		if(!dtData){
    			dtData = 0
    		}
    		def dtTotalsData = dtTotals[discountType.id]
    		if(!dtTotalsData) dtTotalsData = 0
    		
    		dtData += it.computeTotalAmount()
    		dtTotalsData += it.computeTotalAmount()
    		
    		dtTotals[discountType.id] = dtTotalsData
    		
    		ctData[discountType.id] = dtData
    		map[customerType.id] = ctData
    	}
		map["dtTotals"] = dtTotals
		map
	}
}
