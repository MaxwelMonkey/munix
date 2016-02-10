package com.munix

class ForwarderService {

    static transactional = true

    def generateList(Map params) {
		def searchIdentifier = params.searchIdentifier

		def query = {
			and{
				if(searchIdentifier){
					or {
						like('identifier', "%${searchIdentifier}%")
						like('description', "%${searchIdentifier}%")
					}
				}
			}
		}

		return Forwarder.createCriteria().list(params,query)
    }
}
