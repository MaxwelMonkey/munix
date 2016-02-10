package com.munix


class SalesAgentService {

    static transactional = true

    def generateList(Map params) {
        def searchIdentifier = params.searchIdentifier
		
		params.max = 100
		params.offset = 0

        def query = {
            if(searchIdentifier){
                like('identifier', "%${searchIdentifier}%")
            }
        }
		
        return SalesAgent.createCriteria().list(params,query)
    }
}
