package com.munix

class CustomerService {

    static transactional = true
	def authenticateService
	def generalMethodService

    def getListOfCustomersWithCriteria(params) {
        def searchIdentifier = params.searchIdentifier
        def searchName = params.searchName
        def searchSalesAgent = params.searchSalesAgent
        def searchStatus = params.searchStatus

        def query = {
            //Search
            if(searchIdentifier){
                like('identifier', "%${searchIdentifier}%")
            }

            if(searchName){
                like('name', "%${searchName}%")
            }

            if(searchSalesAgent){
                salesAgent{
                    like('identifier', "%${searchSalesAgent}%")
                }
            }

            if(searchStatus){
                eq('status', com.munix.Customer.Status.getStatusByName(searchStatus))
            }
        }
        return Customer.createCriteria().list(params,query)
    }
	
    def generateAuditTrails(Customer customer){
    	generateAuditTrails(customer, authenticateService.userDomain())
    }

    def generateAuditTrails(Customer customer, User user ){
        if(customer.isDirty()){
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
            def modifiedFieldNames = customer.getDirtyPropertyNames()
            modifiedFieldNames.each{fieldName->
                def field = map.get(fieldName)
                if(!field){
                    def display = generalMethodService.addWhiteSpaceAfterCapital(fieldName)
                    field = generalMethodService.capitalizeFirstLetter(display)
                }
                def currentValue = customer."$fieldName"==null?"":customer."$fieldName".toString()
                def originalValue = customer.getPersistentValue(fieldName)==null?"":customer.getPersistentValue(fieldName).toString()
                if (currentValue != originalValue) {
                    customer.addToAuditTrails(new CustomerAuditTrail(
                        user:user,
                        previousEntry:originalValue,
                        newEntry: currentValue,
                        fieldName: field,
                        customer: customer,
                        approvedBy: authenticateService.userDomain()
                    ))
                }
            }
        }
    }
}
