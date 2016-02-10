package com.munix

class SupplierService {

    static transactional = true
    def authenticateService

    def rolePurchasingIsLoggedIn(){
        def result = false
        if(authenticateService.ifAnyGranted("ROLE_PURCHASING")){
            result = true
        }
        return result
    }
	
	public Set queryAvailableProducts(String productSearchCriterion) {
		def productList = Product.list()?.findAll{it.description.toLowerCase() =~ productSearchCriterion.toLowerCase() || it.identifier.toLowerCase() =~ productSearchCriterion.toLowerCase()}
		return new HashSet(productList)
	}
}
