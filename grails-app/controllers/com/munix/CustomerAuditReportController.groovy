package com.munix

import groovy.sql.Sql

class CustomerAuditReportController {
	
	def authenticateService
	def dataSource
    
    def search = {
        def map =[busAddrStreet:"Street (Business)",
  				busAddrCity:"City (Business)",
  				busAddrZip:"Zip (Business)", 
  				bilAddrStreet:"Street (Billing)", 
  				bilAddrCity:"City (Billing)",
  				bilAddrZip:"Zip (Billing)",
  				bank1:"Bank 1",
  				branch1:"Branch 1",
  				accountName1:"Account Name 1",
  				accountNumber1:"Account Number 1",
  				bank2:"Bank 2",
  				branch2:"Branch 2",
  				accountName2:"Account Name 2",
  				accountNumber2:"Account Number 2",
  				bank3:"Bank 3",
  				branch3:"Branch 3",
  				accountName3:"Account Name 3",
  				accountNumber3:"Account Number 3",
  				contact:"Main Contact",
  				contactPosition:"Main Contact Position",
  				]
  		["fields":map]
	}

    def list = {
    	def result = [:]
    	if(params.customer?.id != null ){
    		def customers = []
            if(params.customer?.id instanceof String)
            	customers.add(Customer.get(Long.parseLong(params.customer.id)))
            else{
            	params.customer?.id.each{
                	customers.add(Customer.get(Long.parseLong(it)))
            	}
            }
    	
            customers.each{
            	def customerLogs = retrieveCustomerAuditLogs(params, it)
            	if(customerLogs?.size()>0){
            		result[it.name] = customerLogs
            	}
            }
            model:["result":result]
    	}
    	def user = User.findByUsername(authenticateService.principal().username)
    	model:[username:user.userRealName, "result":result]
	}
    	
    def retrieveCustomerAuditLogs(params, customer){
    	def where = "where customer.id = :customerId"
    	def map = ["customerId":customer.id]
    	if(params.dateFrom){
    		where += " and date>=:dateFrom"
    		map.put("dateFrom",params.dateFrom)
    	}
    	if(params.dateTo){
    		where += " and date<:dateTo"
        	map.put("dateTo",params.dateTo + 1)
    	}
    	if(params.field != null ){
    		where += " and fieldName in (:fields)"
            map.put("fields",params.field)
    	}
    	if(params.user?.id != null ){
    		where += " and user.id in (:userIds)"

            if(params.user?.id instanceof String)
                map.put("userIds",Long.parseLong(params.user?.id))
            else
                map.put("userIds",params.user?.id.collect{value -> Long.parseLong(value)})
    	}
    	CustomerAuditTrail.executeQuery("from CustomerAuditTrail ${where}",map)
    }
    
	    	
}
