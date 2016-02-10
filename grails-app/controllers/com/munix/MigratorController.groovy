package com.munix

class MigratorController {

	def migratorService
	
	def cleanCheckPaymentData = {
		migratorService.cleanCheckPaymentData()
	}
	
	def rePopulateStockTable = {
		migratorService.rePopulateStockTable()
	}
	
    def customerLedger = {
		migratorService.generateCustomerLedgers()
	}
	
	def stockCard = {
		migratorService.generateStockCards()
	}
	
    def oldPrices = {
		migratorService.generateOldPriceForPriceAdjustmentItem()
	}

	def setErroneousUnpaidSalesDeliveryToPaid = {
		migratorService.setErroneousUnpaidSalesDeliveryToPaid()
	}

    def fixCustomer339 ={
        migratorService.fixCustomerLedgerOfCustomer339()
    }
	
	def migrateProductComponents = {
		migratorService.migrateProductComponents()
	}
    def updateAmountForCSItems = {
        migratorService.updateAmountForCSItems()
    }
	
	def updateCounterReceiptItemAmount = {
		migratorService.updateCounterReceiptItemAmount()
	}
    def updatebouncedCheckAmountPaid = {
        migratorService.updatebouncedCheckAmountPaid()
    }
    def updateDirectPaymentInvoiceAmount = {
        migratorService.updateDirectPaymentInvoiceAmount()
    }
    def migrateSupplierItems = {
        migratorService.migrateSupplierItems()
    }
	
	def updateProductRunningCost = {
		migratorService.updateProductRunningCost()
	}
	
	def fixStockCardItemCosts = {
		migratorService.fixSalesDeliveryStockCardItemCosts()
		migratorService.fixCreditMemoStockCardItemCosts()
	}
	
	def updateCustomerAccountData = {
		migratorService.updateCustomerAccountData()
	}
	
	//to version 1.12
	def migrateToNewVersion = {
		fixStockCardItemCosts()
		println "Finished fixing stock card item cost"
		
		updateProductRunningCost()
		println "Finished updating product running cost"
		
		cleanCheckPaymentData()
		println "Finished cleaning check payment data"
		
		updateCustomerAccountData()
		println "Finished updating customer account data"
	}
	
	def updateBikersChoiceProducts = {
		migratorService.updateBikersChoiceProducts()
	}
	
	def updateTriplesProducts = {
		migratorService.updateTriplesProducts()
	}
	
	def updateTriplesProductStock = {
		migratorService.updateTriplesProductsBeginningBalance()
		migratorService.updateTriplesProductStock()
	}

	def updateStockCardRunningBalance = {
		migratorService.updateStockCardRunningBalance()
	}
	
	def fixDirectPaymentInvoiceAmountsForPaidCustomerPayments = {
		migratorService.fixDirectPaymentInvoiceAmountsForPaidCustomerPayments()
	}
	
	def migratePurchaseOrderItems = {
		migratorService.migratePurchaseOrderItems()
	}

	def createInitialProductStock = {
		migratorService.createInitialProductStock()
	}
	
	def updateStock = {
		migratorService.updateStock()
	}
}
