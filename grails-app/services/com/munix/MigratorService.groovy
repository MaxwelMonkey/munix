package com.munix

import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator

class MigratorService implements org.springframework.context.ApplicationContextAware{
	boolean transactional = true

	def applicationContext
	def customerLedgerService
	def generalMethodService
	def customerLedgerEntryFactory
	def productService
	def constantService

	public void setApplicationContext(org.springframework.context.ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}
	def getString(cellNumber,row){
		def cell = row.getCell(cellNumber)
		def text=cell?.getStringCellValue()
		if(!text){
			text=""
		}
		return text
	}

	def getValue(cellNumber,row, HSSFFormulaEvaluator evaluator){
		def value;
		def cell = row.getCell(cellNumber)
		def cellValue = evaluator.evaluate(cell)
		if(cellValue) {
			if(cellValue.getCellType() == 0) {
				value = cellValue.getNumberValue()
			} else if (cellValue.getCellType() == 1) {
				value = cellValue.getStringValue()
			}
		} else {
			value = ""
		}
		
		return value
	}

	def updateCounterReceiptItemAmount() {
		def items = CounterReceiptItem.list()
		items.sort{it.id}.each {
			if(it.invoiceType == CustomerPaymentType.SALES_DELIVERY) {
				def sd = SalesDelivery.get(it.invoiceId)
				it.amount = sd.computeProjectedDue()
				it.save(flush:true)
			} else if (it.invoiceType == CustomerPaymentType.CUSTOMER_CHARGE) {
				def charge = CustomerCharge.get(it.invoiceId)
				it.amount = charge.computeProjectedDue()
				it.save(flush:true)
			} else if (it.invoiceType == CustomerPaymentType.CREDIT_MEMO) {
				def cm = CreditMemo.get(it.invoiceId)
				it.amount = cm.computeCreditMemoTotalAmount()
				it.save(flush:true)
			} else if (it.invoiceType == CustomerPaymentType.BOUNCED_CHECK) {
				def bc = BouncedCheck.get(it.invoiceId)
				it.amount = bc.computeTotalAmount()
				it.save(flush:true)
			}
		}
	}

	def updateSupplier() {
		def nameOfWorkbook = "Supplier_List.xls"
		// define the location where to store the workbook
		def locationToStoreWorkbook = "/WEB-INF/resource/"
		//create a POIFSFileSystem object to read the data
		def template = applicationContext.getResource(locationToStoreWorkbook + nameOfWorkbook)

		def wb = new HSSFWorkbook(template.getInputStream())
		def sheet = wb.getSheetAt(0);
		for(a in 2..sheet.getPhysicalNumberOfRows()-1){
			def row = sheet.getRow(a)
			def identifier=getString(0,row)
			def name=getString(1,row)
			def supplier=Supplier.findByName(name)
			if(!supplier){
				supplier=new Supplier()
				supplier.name=name
			}
			supplier.identifier=identifier
			supplier.country=Country.findByIdentifier(getString(2,row))
			supplier.address=getString(3,row)
			supplier.tin=getString(4,row)
			supplier.term=Term.findByIdentifier(getString(5,row))
			supplier.currency=CurrencyType.findByIdentifier(getString(6,row))
			supplier.contact=getString(7,row)
			supplier.landline=getString(8,row)
			supplier.fax=getString(9,row)
			supplier.website=getString(10,row)
			supplier.email=getString(11,row)
			supplier.skype=getString(12,row)
			supplier.yahoo=getString(13,row)
			supplier.save()
		}

	}

	def updateBikersChoiceProducts() {
		def nameOfWorkbook = "bikerschoice_product_to_IKA.xls"
		updateProducts(nameOfWorkbook, "BikersChoice")
	}
	
	def updateTriplesProducts() {
		def nameOfWorkbook = "triple_s_product_to_IKA.xls"
		updateProducts(nameOfWorkbook, "Triple S")
	}
	
	def updateTriplesProductsBeginningBalance() {
		def nameOfWorkbook = "triple_s_product_to_IKA.xls"
		updateProductsBeginningBalance(nameOfWorkbook, "Triple S")
	}

	def updateProductsBeginningBalance(nameOfWorkbook, name){
		// define the location where to get the workbook
		def locationToStoreWorkbook = "/WEB-INF/resource/"
		//create a POIFSFileSystem object to read the data
		def template = applicationContext.getResource(locationToStoreWorkbook + nameOfWorkbook)
		
		def wb = new HSSFWorkbook(template.getInputStream())
		def sheet = wb.getSheetAt(0);
		HSSFFormulaEvaluator evaluator = new HSSFFormulaEvaluator(wb)
		int cnt = 0

		for(a in 1..sheet.getPhysicalNumberOfRows()-1){
			def row = sheet.getRow(a)
			def identifier=getValue(1,row, evaluator)
			def beginningBalance=getValue(5,row, evaluator)
			def product=Product.findByIdentifier(identifier)
			if(product){
				if(beginningBalance) {
					def item = StockCardItem.findByStockCardAndType(product.stockCard, StockCardItem.Type.INITIAL_ENTRY)
					if(item) {
						item.balance = new BigDecimal(beginningBalance)
						item.save()
					}
				}
				product.save()
				cnt++
			} else {
				println identifier + " not found!"
			}
		}
		println cnt + " product(s) for " + name + " modified!"
	}
	
    def updateProducts(nameOfWorkbook, name){
		// define the location where to get the workbook
		def locationToStoreWorkbook = "/WEB-INF/resource/"
		//create a POIFSFileSystem object to read the data
		def template = applicationContext.getResource(locationToStoreWorkbook + nameOfWorkbook)
		
		def wb = new HSSFWorkbook(template.getInputStream())
		def sheet = wb.getSheetAt(0);
		HSSFFormulaEvaluator evaluator = new HSSFFormulaEvaluator(wb)
		int cnt = 0

		for(a in 1..sheet.getPhysicalNumberOfRows()-1){
			def row = sheet.getRow(a)
			def identifier=getValue(1,row, evaluator)
			def newStatus=getValue(3,row, evaluator)
			def runningCost=getValue(4,row, evaluator)
			def product=Product.findByIdentifier(identifier)
            if(product){
                if(newStatus){
                    product.status= newStatus
                }
                if(runningCost){
                    product.runningCost = new BigDecimal(runningCost)
                }
                product.save()
				cnt++
            } else {
				println identifier + " not found!"
			}
		}
		
		println cnt + " product(s) for " + name + " modified!"
    }

	def addWholeSaleAndRetailToSalesOrder(){
		def ctr=0
		def salesOrderList=SalesOrder.list()
		salesOrderList.each{
			if(ctr%2==0){
				it.setRetail()
			}else{
				it.setWholesale()
			}
			it.save()
			ctr++
		}
	}

	def generateCustomerLedgers() {
		def ctr = 0
		def ledgers = []
		if (CustomerLedger.list().size() == 0) {
			def ledger
			Customer.list().each {
				ledger = new CustomerLedger(customer: it)
				generateInitialBalanceEntry(ledger)

				if (ledger.save()) {
					ctr++
					ledgers.add(ledger)
				}
			}
		}
		println ctr + " customer ledgers generated"

		generateLedgerEntries()

		println "Finished generating entries"

		generateLedgerAmount()

		println "DONE!"
	}

	def fixCustomerLedgerOfCustomer339(){
		def customer339 = Customer.get(339)
		def oldCustomerLedger = customer339.customerLedger
		oldCustomerLedger.delete()

		def ledger = new CustomerLedger(customer: customer339)
		generateInitialBalanceEntry(ledger)

		customer339.customerLedger = ledger
		customer339.save()

		generateSalesDeliveryEntry(ledger)
		generateCustomerChargeEntry(ledger)
		generateDebitMemoEntry(ledger)
		generateDirectPaymentEntry(ledger)

		def bouncedChecks = BouncedCheck.findAllByCustomer(customer339)
		bouncedChecks.each{
			generateBouncedCheckEntry(ledger, it)
		}

		def checkPayments = CheckPayment.findAllByCustomerAndStatus(customer339, CheckPayment.Status.DEPOSITED)
		checkPayments.each {
			def checkPayment = it
			def checkDeposit = checkPayment.checkDeposit
			def entry = CustomerLedgerEntryFactory.createApprovedCheckDepositItem(checkDeposit, checkPayment)
			entry.datePosted = generalMethodService.getDateFromApprovedBy(checkDeposit.approvedBy)
			entry.runningBalance = new BigDecimal("0.00")

			ledger.addToEntries(entry)
			ledger.save(flush: true)
		}
		computeLedgerAmounts(ledger)
	}

	def generateLedgerEntries() {
		def ledgers = CustomerLedger.list()
		println "Starting to generate sales delivery, customer charge, debit memo and direct payment entries"
		ledgers.each {
			generateSalesDeliveryEntry(it)
			generateCustomerChargeEntry(it)
			generateDebitMemoEntry(it)
			generateDirectPaymentEntry(it)
		}
		println "Finished generating entries"
		println "Starting to generate bounced check entries"
		generateBouncedChecksEntry()
		println "Finished generating entries"
		println "Starting to generate check deposit entries"
		generateCheckDepositsEntry()
	}

	def generateLedgerAmount() {
		def ledgers = CustomerLedger.list()
		ledgers.each {
			computeLedgerAmounts(it)
		}
	}

	def computeLedgerAmounts(CustomerLedger customerLedger){
		BigDecimal balance = BigDecimal.ZERO
		customerLedger.entries.sort {it.datePosted}.each {
			if(!it.isChild){
				if(it.debitAmount) {
					it.runningBalance = balance + it.debitAmount
					customerLedger.updateBalanceAddDebit(it.amount)
					balance = it.runningBalance
				} else if (it.creditAmount) {
					it.runningBalance = balance - it.creditAmount
					customerLedger.updateBalanceAddCredit(it.amount)
					balance = it.runningBalance
				} else {

				}
			}

			if(it.type.equals(CustomerLedgerEntry.Type.APPROVED_DIRECT_PAYMENT)) {
				def entry = it
				BigDecimal sum = BigDecimal.ZERO
				entry.paymentBreakdown.each {
					if (it.creditAmount) {
						sum += it.creditAmount
					} else if (it.debitAmount) {
						sum -= it.debitAmount
					}
				}

				entry.runningBalance = balance - sum
				customerLedger.updateBalanceAddCredit(sum)
				balance = entry.runningBalance
			}

		}
	}

	def generateSalesDeliveryEntry(CustomerLedger ledger) {
		def ctr = 0
		def sd = SalesDelivery.findAllByCustomer(ledger.customer)
		sd.each {
			if (it.isApproved()||it.isPaid()) {
				def entry = CustomerLedgerEntryFactory.createApprovedSalesDelivery(it)
				entry.datePosted = generalMethodService.getDateFromApprovedBy(it.approvedBy)
				entry.runningBalance = new BigDecimal("0.00")
				ledger.addToEntries(entry)
				if(ledger.save()) {
					ctr++
				}
			}

		}
	}

	def generateCustomerChargeEntry(CustomerLedger ledger) {
		def ctr = 0
		def cc = CustomerCharge.findAllByCustomer(ledger.customer)
		cc.each {
			if (it.isApproved()||it.isPaid()) {
				def entry = CustomerLedgerEntryFactory.createApprovedCustomerCharge(it)
				entry.datePosted = generalMethodService.getDateFromApprovedBy(it.approvedBy)
				entry.runningBalance = new BigDecimal("0.00")
				ledger.addToEntries(entry)
				if(ledger.save()) {
					ctr++
				}
			}
		}
	}

	def generateCheckDepositsEntry() {
		def ctr = 0
		def cd = CheckDeposit.list()
		cd.each {
			def checkDeposit = it
			if (checkDeposit.status == "Cleared") {
				checkDeposit.checks.each {
					def checkPayment = it
					def entry = CustomerLedgerEntryFactory.createApprovedCheckDepositItem(checkDeposit, checkPayment)
					entry.datePosted = generalMethodService.getDateFromApprovedBy(checkDeposit.approvedBy)
					entry.runningBalance = new BigDecimal("0.00")

					def ledger = checkPayment.customer.customerLedger
					ledger.addToEntries(entry)
					if(ledger.save()) {
						ctr++
					}
				}
			}
		}
	}
	def generateCheckDepositEntry(CustomerLedger customerLedger, CheckDeposit checkDeposit) {
		if (checkDeposit.status == "Cleared") {
			checkDeposit.checks.each {
				def checkPayment = it
				def entry = CustomerLedgerEntryFactory.createApprovedCheckDepositItem(checkDeposit, checkPayment)
				entry.datePosted = generalMethodService.getDateFromApprovedBy(checkDeposit.approvedBy)
				entry.runningBalance = new BigDecimal("0.00")

				def ledger = checkPayment.customer.customerLedger
				ledger.addToEntries(entry)
				if(ledger.save(flush: true)) {
					ctr++
				}
			}
		}
	}

	def generateBouncedChecksEntry() {
		def ctr = 0
		def bc = BouncedCheck.list()

		bc.each {
			def bouncedCheck = it
			def ledger = bouncedCheck.customer.customerLedger
			generateBouncedCheckEntry(ledger,bouncedCheck)
		}
	}
	def generateBouncedCheckEntry(CustomerLedger ledger, BouncedCheck bouncedCheck){
		if (bouncedCheck.isApproved()||bouncedCheck.isPaid()) {
			def customerLedgerEntry = CustomerLedgerEntryFactory.createApprovedBouncedCheck(bouncedCheck)
			customerLedgerEntry.datePosted = generalMethodService.getDateFromApprovedBy(bouncedCheck.approvedBy)
			customerLedgerEntry.runningBalance = new BigDecimal("0.00")
			ledger.addToEntries(customerLedgerEntry)
			ledger.save()

			bouncedCheck.checks.each {
				def checkPayment = it
				def entry = CustomerLedgerEntryFactory.createApprovedBouncedCheckItem(bouncedCheck, checkPayment)
				entry.datePosted = generalMethodService.getDateFromApprovedBy(bouncedCheck.approvedBy)
				entry.runningBalance = new BigDecimal("0.00")

				customerLedgerEntry.addPaymentBreakdown(entry)
				ledger.addToEntries(entry)
				ledger.save()
			}
		}
	}
	def generateDebitMemoEntry(CustomerLedger ledger) {
		def ctr = 0
		def dm = CreditMemo.findAllByCustomer(ledger.customer)
		dm.each {
			if (it.isApproved()||it.isPaid()) {
                if(it.isADebitMemo()){
                    def entry = CustomerLedgerEntryFactory.createApprovedDebitMemo(it)
                    entry.datePosted = generalMethodService.getDateFromApprovedBy(it.approvedBy)
                    entry.runningBalance = new BigDecimal("0.00")
                    ledger.addToEntries(entry)
                    if(ledger.save()) {
                        ctr++
                    }
                }

			}
		}
	}

	def generateDirectPaymentEntry(CustomerLedger ledger) {
		def ctr = 0
		def dp = DirectPayment.findAllByCustomer(ledger.customer)

		dp.each {
			def directPayment = it
			if(it.status == "Approved") {
				def directPaymentLedgerEntry = CustomerLedgerEntryFactory.createApprovedDirectPayment(it)
				directPaymentLedgerEntry.datePosted = generalMethodService.getDateFromApprovedBy(directPayment.approvedBy)
				directPaymentLedgerEntry.runningBalance = new BigDecimal("0.00")
				ledger.addToEntries(directPaymentLedgerEntry)
				ledger.save()
				it.items.each {
					def entry = CustomerLedgerEntryFactory.createDirectPaymentItem(it)
					if(!it.paymentType.isCheck) {
						entry.creditAmount= entry.amount
					}

					entry.datePosted = generalMethodService.getDateFromApprovedBy(directPayment.approvedBy)
					entry.runningBalance = new BigDecimal("0.00")
					ledger.addToEntries(entry)
					directPaymentLedgerEntry.addPaymentBreakdown(entry)
					if(ledger.save()) {
						ctr++
					}
				}
				def balance = it.computeBalance()
				if(balance > 0){
					def overpayment = new Overpayment(
							amount:balance,
							directPayment: it)
					overpayment.save()
					it.overpayment = overpayment
					it.save()
					def ledgerEntry = CustomerLedgerEntryFactory.createApprovedOverpayment(overpayment)
					ledgerEntry.datePosted = generalMethodService.getDateFromApprovedBy(directPayment.approvedBy)
					ledgerEntry.runningBalance = new BigDecimal("0.00")
					ledger.addToEntries(ledgerEntry)
					directPaymentLedgerEntry.addPaymentBreakdown(ledgerEntry)
					ledger.save()
				}
				directPaymentLedgerEntry.save()
			}
		}
	}

	def generateInitialBalanceEntry(CustomerLedger ledger) {
		def entry = CustomerLedgerEntryFactory.createInitialBalanceEntry(ledger.customer)
		entry.dateOpened = generalMethodService.createDate("01/01/2011")
		entry.datePosted = generalMethodService.createDate("01/01/2011")
		ledger.addToEntries(entry)
		ledger.save(flush: true)
	}
	def generateOldPriceForPriceAdjustmentItem(){
		PriceAdjustmentItem.list().product.unique().each{
			def priceAdjustmentItems = PriceAdjustmentItem.findAllByProduct(it)
			def retailOldPrice = BigDecimal.ZERO
			def whoseSaleOldPrice = BigDecimal.ZERO
			priceAdjustmentItems.sort{it.priceAdjustment.effectiveDate}.each{
				if(it.priceAdjustment.priceType== PriceType.RETAIL){
					it.oldPrice = retailOldPrice

					if(it.priceAdjustment.status==PriceAdjustment.Status.APPLIED){
						retailOldPrice = it.newPrice
					}
				} else {
					it.oldPrice = whoseSaleOldPrice

					if(it.priceAdjustment.status==PriceAdjustment.Status.APPLIED){
						whoseSaleOldPrice = it.newPrice
					}
				}
				it.save()
			}
		}
	}

	def setErroneousUnpaidSalesDeliveryToPaid() {
		SalesDelivery.list().each {
			try {
				if (it.computeTotalAmount() > 0 && it.amountPaid >= it.computeTotalAmount() && "Unpaid".equals(it.status)) {
					it.status = "Paid"
					it.save(flush: true)
				}
			} catch (org.hibernate.ObjectNotFoundException e) {
				// do nothing
			}
		}
	}

	def migrateProductComponents() {
		def products = Product.list()
		def productComponents = ProductComponent.list()
		products.sort{it.id}.each { product ->
			if(!product.components.isEmpty()) {
				def components = ProductComponent.findAllByProduct(product)
				int idx = 0
				components.sort{it.id}.each { component ->
					product.componentList[idx] = component
					idx++
				}
				product.save(flush:true)
			}
		}
	}
	def migrateSupplierItems() {
		def suppliers = Supplier.list()
		def supplierItems = SupplierItem.list()
		suppliers.sort{it.id}.each { supplier ->
			if(!supplier.items.isEmpty()) {
				def supplierItem = SupplierItem.findAllBySupplier(supplier)
				int idx = 0
				supplierItem.sort{it.id}.each { item ->
					supplier.supplierItemList[idx] = item
					idx++
				}
				supplier.save(flush:true)
			}
		}
	}
	def updateAmountForCSItems(){
		def collectionScheds = CollectionSchedule.list()
		collectionScheds.each{collectionSchedule ->
			collectionSchedule.items.each{
				it.amount = it.counterReceipt.computeInvoicesTotal()
			}
		}
	}
	def updatebouncedCheckAmountPaid(){
		BouncedCheck.list().each{
			if(it.isPaid()){
				it.amountPaid=it.computeTotalAmount()
				it.save()
			}
		}
	}
	def updateDirectPaymentInvoiceAmount(){
		BouncedCheck.list().each{ bouncedCheck->
			bouncedCheck.invoices.each{invoice->
				invoice.amount = invoice.getRelatedCustomerPayment().computeTotalAmount()
				invoice.save()
			}
		}
	}

	def generateStockCards() {
		def ctr = 0
		def stockCards = []
		if (StockCard.list().size() == 0) {
			println "Generating stock cards..."
			def stockCard
			Product.list().each {
				stockCard = new StockCard(product: it)

				if (generateInitialBalanceStockCardItem(stockCard)) {
					stockCards.add(stockCard)
					ctr++
					if (ctr % 100 == 0) {
						println ctr + " stock cards generated"
					}
				}
			}
		}
		println ctr + " stock cards generated"

		generatePurchaseInvoices()
		generateSalesDeliveries()
		generateMaterialReleases()
		generateInventoryAdjustment()
		generateInventoryTransfer()
		generateJobOuts()
		generateCreditMemos()

		println "Finished generating stock card items"
	}

	private boolean generateInitialBalanceStockCardItem(StockCard stockCard) {
		def stockCarditem = StockCardItemFactory.createInitialBalanceEntry()
		stockCard.addToItems(stockCarditem)
		return stockCard.save()
	}

	private void generateInventoryAdjustment() {
		println "generating inventory adjustment stock card items"
		def ctr = 0
		InventoryAdjustmentItem.list().findAll{it.adjustment && it.adjustment.status == InventoryAdjustment.Status.APPROVED}.each {
			def inventoryAdjustmentItem = StockCardItemFactory.createApprovedInventoryAdjustmentItem(it)
			inventoryAdjustmentItem.datePosted = generalMethodService.getDateFromApprovedBy(it.adjustment.approvedBy)
			def stockCard = it.product.stockCard
			stockCard.addToItems(inventoryAdjustmentItem)
			stockCard.save()
			ctr++
			if (ctr % 100 == 0) {
				println ctr + " inventory adjustment stock card items generated"
			}
		}
		println " inventory adjustment stock card items generation complete"
	}
	
	private void generateInventoryTransfer() {
		println "generating inventory adjustment stock card items"
		def ctr = 0
		InventoryTransferItem.list().findAll{it.transfer && it.transfer.status == InventoryTransfer.Status.RECEIVED}.each {
			def inventoryTransferItem = StockCardItemFactory.createApprovedInventoryTransferItem(it)
			inventoryTransferItem.datePosted = generalMethodService.getDateFromApprovedBy(it.transfer.receivedBy)
			def stockCard = it.product.stockCard
			stockCard.addToItems(inventoryTransferItem)
			stockCard.save()
			ctr++
			if (ctr % 100 == 0) {
				println ctr + " inventory transfer stock card items generated"
			}
		}
		println " inventory transfer stock card items generation complete"
	}
	
	private void generatePurchaseInvoices() {
		def ctr = 0
		PurchaseInvoiceItem.list().findAll{it.purchaseInvoice.status == PurchaseInvoice.Status.APPROVED || it.purchaseInvoice.status == PurchaseInvoice.Status.PAID}.each {
			def stockCarditem = StockCardItemFactory.createApprovedPurchaseInvoiceItem(it)
			stockCarditem.datePosted = generalMethodService.getDateFromApprovedBy(it.purchaseInvoice.approvedBy)
			def stockCard = it.purchaseOrderItem.product.stockCard
			stockCard.addToItems(stockCarditem)
			stockCard.save()
			ctr++
			if (ctr % 100 == 0) {
				println ctr + " pi stock card items generated"
			}
		}
		println " pi stock card items generation complete" 
	}
	
	private void generateSalesDeliveries() {
		def ctr = 0
		SalesDeliveryItem.list().findAll{it.delivery.status == constantService.SALES_DELIVERY_APPROVED || it.delivery.status == constantService.SALES_DELIVERY_PAID}.each {
			if(it.qty>0){
                def stockCarditem = StockCardItemFactory.createApprovedSalesDeliveryItem(it)
                stockCarditem.datePosted = generalMethodService.getDateFromApprovedBy(it.delivery.approvedBy)
                def stockCard = it.product.stockCard
                stockCard.addToItems(stockCarditem)
                stockCard.save()
                ctr++
                if (ctr % 100 == 0) {
                    println ctr + " sd stock card items generated"
                }
            }
		}
		println " sd stock card items generation complete" 
	}

	private void generateMaterialReleases() {
		MaterialReleaseItem.list().findAll{it.materialRelease.status == MaterialRelease.Status.APPROVED || it.materialRelease.status == MaterialRelease.Status.SECOND_APPROVED}.each {
			def stockCarditem = StockCardItemFactory.createApprovedMaterialReleaseItem(it)
			stockCarditem.datePosted = generalMethodService.getDateFromApprovedBy(it.materialRelease.approvedBy)
			def stockCard = it.materialRelease.jobOrder.product.stockCard
			stockCard.addToItems(stockCarditem)
			stockCard.save()
		}
	}
	
	private void generateCreditMemos() {
		CreditMemoItem.list().findAll{it.creditMemo.status == "Approved" || it.creditMemo.status == "Paid"}.each {
			def stockCarditem = StockCardItemFactory.createApprovedCreditMemoItem(it)
			stockCarditem.datePosted = generalMethodService.getDateFromApprovedBy(it.creditMemo.approvedTwoBy)
			def stockCard = it.deliveryItem.product.stockCard
			stockCard.addToItems(stockCarditem)
			stockCard.save()
		}
	}
	
	private void generateJobOuts() {
		JobOut.list().findAll{it.status == JobOut.Status.APPROVED}.each {
			def stockCarditem = StockCardItemFactory.createApprovedJobOutItem(it)
			stockCarditem.datePosted = generalMethodService.getDateFromApprovedBy(it.approvedBy)
			def stockCard = it.jobOrder.product.stockCard
			stockCard.addToItems(stockCarditem)
			stockCard.save()
		}
	}
	
	private void rePopulateStockTable() {
		def ctr = 0
		Product.list().sort{it.id}.each { product ->
			Warehouse.list().sort{it.id}.each { warehouse ->
				def stockEntry = new Stock(product: product, warehouse: warehouse, qty : BigDecimal.ZERO)
				stockEntry.save(flush:true)
			}
			ctr++
			if(ctr % 100 == 0) {
				println ctr + " products generated!"
			}
		}
	}
	
	def updateProductRunningCost() {
		def ctr = 0
		println "Starting to update products"
		Product.list().each {
			productService.updateRunningCost(it)
			ctr++
			if (ctr % 100 == 0) {
				println ctr + " products have been updated"
			}
		}
		println "DONE"
	}
	

	def cleanCheckPaymentData() {
		CheckPayment.list().sort{it.id}.each { cp -> 
			def item = DirectPaymentItemCheck.findByCheckPayment(cp)
			if(!item) {
				cp.directPaymentItem = null
				cp.save(flush:true)
			}
		}
	}

	def fixSalesDeliveryStockCardItemCosts() {
		println "fixing SD's"
		def ctr = 0
		SalesDeliveryItem.withCriteria {
			and {
				delivery {
					or {
						eq ("status", "Paid")
						eq ("status", "Unpaid")
					}
				}
				product {
					eq ("isNet", true)
				}
			}
		}.each {salesDeliveryItem ->
			def stockCardItem = salesDeliveryItem.product.stockCard.items.find {it.linkId == salesDeliveryItem.delivery.id }
			stockCardItem.sellingAmount = salesDeliveryItem.discountedPrice
			stockCardItem.save()
			ctr ++
			if (ctr % 1000 == 0) {
				println ctr + " stock cards have been updated"
			}
		}
		println "DONE"
	}
	
	def fixCreditMemoStockCardItemCosts() {
		println "fixing CM's"
		def ctr = 0
		CreditMemoItem.withCriteria {
			and {
				creditMemo {
					or {
						eq ("status", "Approved")
						eq ("status", "Paid")
					}
				}
			}
		}.each {creditMemoItem ->
			def stockCardItem = creditMemoItem.deliveryItem.product.stockCard.items.find {it.linkId == creditMemoItem.creditMemo.id }
			if (stockCardItem) {
				stockCardItem.sellingAmount = creditMemoItem.computeDiscountedAmount()
				stockCardItem.save()
				ctr ++
				if (ctr % 100 == 0) {
					println ctr + " stock cards have been updated"
				}
			}
		}
		println "DONE"
	}

	def updateCustomerAccountData() {
		Customer.list().each { customer ->
			customer.customerAccount = new CustomerAccount(
				totalUnpaidSalesDeliveries : computeTotalUnpaidSalesDeliveries(customer),
				totalUnpaidCustomerCharges : computeTotalUnpaidCustomerCharges(customer),
				totalUnpaidDebitMemos : computeTotalUnpaidDebitMemos(customer),
				totalUnpaidBouncedChecks : computeTotalUnpaidBouncedCheck(customer),
				totalUnpaidCheckPayments : computeTotalUnpaidCheckPayment(customer)
			)
			
			customer.customerAccount.save()
			customer.save()
		}
	}
	
	private def getAllUnpaidBouncedCheckList(Customer customer){
		return BouncedCheck.findAllByCustomerAndStatus(customer, "Approved")
	}

	def computeTotalUnpaidBouncedCheck(Customer customer){
		def bouncedChecks = getAllUnpaidBouncedCheckList(customer)
		BigDecimal totalUnpaidBouncedChecks = BigDecimal.ZERO
		bouncedChecks.each{
			totalUnpaidBouncedChecks += it.computeProjectedDue()
		}
		return totalUnpaidBouncedChecks
	}
	
	private def getAllUnpaidSalesDeliveryList(Customer customer){
		return SalesDelivery.findAllByCustomerAndStatus(customer,"Unpaid")
	}
	
	def computeTotalUnpaidSalesDeliveries(Customer customer){
		def salesDeliveries = getAllUnpaidSalesDeliveryList(customer)
		BigDecimal totalUnpaidSalesDeliveries = BigDecimal.ZERO
		salesDeliveries.each{
			totalUnpaidSalesDeliveries += it.computeAmountDue()
		}
		return totalUnpaidSalesDeliveries
	}
	
	private def getAllUnpaidCheckPayment(Customer customer){
		return CheckPayment.withCriteria {
			and {
				directPaymentItem {
					directPayment {
						eq('status',"Approved")
					}
				}
				eq('customer', customer)
				or {
					eq("status", CheckPayment.Status.PENDING)
					eq("status", CheckPayment.Status.FOR_REDEPOSIT)
				}
			}
		}
	}

	def computeTotalUnpaidCheckPayment(Customer customer){
		def checkPayments = getAllUnpaidCheckPayment(customer)
		BigDecimal totalUnpaidCheckPayment = BigDecimal.ZERO
		checkPayments.each{ cp ->
			if(cp.directPaymentItem) {
				totalUnpaidCheckPayment += cp.amount
			}
		}
		return totalUnpaidCheckPayment
	}
	
	List<CreditMemo> getAllUnpaidDebitMemoList(Customer customer){
		def unpaidCreditMemo = CreditMemo.findAllByCustomerAndStatus(customer,"Approved")
		def debitMemos = []
		unpaidCreditMemo.each{
			if(it.isADebitMemo()){
				debitMemos.add(it)
			}
		}
		return debitMemos
	}

	def computeTotalUnpaidDebitMemos(Customer customer){
		def debitMemos = getAllUnpaidDebitMemoList(customer)
		BigDecimal totalUnpaidDebitMemo = BigDecimal.ZERO
		debitMemos.each{
			totalUnpaidDebitMemo += it.computeCreditMemoTotalAmount()
		}
		return totalUnpaidDebitMemo
	}
	
	private def getAllUnpaidCustomerChargeList(Customer customer){
		return CustomerCharge.findAllByCustomerAndStatus(customer,"Unpaid")
	}
	
	def computeTotalUnpaidCustomerCharges(Customer customer){
		def customerCharges = getAllUnpaidCustomerChargeList(customer)
		BigDecimal totalUnpaidCustomerCharges = BigDecimal.ZERO
		customerCharges.each {
			totalUnpaidCustomerCharges += it.computeActualDue()
		}
		return totalUnpaidCustomerCharges
	}
	
	def updateStockCardRunningBalance() {
		println "Updating stock running balance"
		StockCard.list().sort{it.id}.each { sc ->
			if(sc.items) {
				def recentBalance = BigDecimal.ZERO
				def currentIndex = 0;
				def sciSize
				if(sc.items) {
					sciSize = sc.items.size()
				}
				for(def sci : sc.items.sort{map1, map2 -> map1.datePosted <=> map2.datePosted ?: map1.id <=> map2.id}) {
					if(currentIndex != 0) {
						if(sci.type == StockCardItem.Type.APPROVED_INVENTORY_TRANSFER) {
							sci.balance = recentBalance
						} else {
							if(sci.qtyIn || sci.qtyIn == 0) {
								sci.balance = recentBalance + sci.qtyIn
							} else if(sci.qtyOut || sci.qtyOut == 0) {
								sci.balance = recentBalance - sci.qtyOut
							}
							sci.save()
							recentBalance = sci.balance
						}
					} else {
						recentBalance = sci.balance
					}
					currentIndex++
				}
			}	
		}
		println "Finished updating stock running balance"
	}
	
	def updateStock() {
		println "Started updating stock"
		StockCard.list().each {stockCard ->
			def stockMap = [:]
			stockMap.warehouses = [] as Set
			stockCard.items.sort{map1, map2 -> map1.datePosted <=> map2.datePosted ?: map1.id <=> map2.id}.each {sci ->
				stockMap.warehouses.add(sci.warehouseIn?:sci.warehouseOut)
				if (sci.type == StockCardItem.Type.APPROVED_INVENTORY_ADJUSTMENT ) {
					updateStockForInventoryAdjustment(sci, stockMap)
				} else {
					if (sci.warehouseIn) {
						def qty = stockMap.get(sci.warehouseIn) ?: 0
						stockMap.put(sci.warehouseIn, qty + sci.qtyIn)
					} 
					if (sci.warehouseOut) {
						def qty = stockMap.get(sci.warehouseOut) ?: 0
						stockMap.put(sci.warehouseOut, qty - sci.qtyOut)
					}
				}
			}
			stockMap.warehouses.each {warehouse ->
				if (warehouse) {
					def balance = stockMap.get(warehouse)
					def stock = stockCard.product.getStock(Warehouse.findByIdentifier(warehouse))
					stock.qty = balance
					stock.save()
				}
			}
		}
		println "Finished updating stock"
	}
	
	private void updateStockForInventoryAdjustment(StockCardItem sci, Map stockMap) {
		def inventoryAdjustmentItem = InventoryAdjustmentItem.get(sci.linkId)
		fixStockCardItem(inventoryAdjustmentItem, sci, stockMap)
		fixOldStockOfInventoryAdjustmentItem(inventoryAdjustmentItem, sci, stockMap)
		adjustCurrentWarehouseStock(inventoryAdjustmentItem, sci, stockMap)
	}
	
	private void fixStockCardItem(InventoryAdjustmentItem inventoryAdjustmentItem, StockCardItem sci, Map stockMap) {
		def oldStock = stockMap.get(sci.warehouseIn?:sci.warehouseOut) ?: 0
		def newStock = inventoryAdjustmentItem.newStock
		def difference = newStock - oldStock
		if (difference > 0) {
			sci.warehouseIn = inventoryAdjustmentItem.adjustment.warehouse
			sci.qtyIn = difference
			sci.warehouseOut = null
			sci.qtyOut = null
		} else {
			sci.warehouseOut = inventoryAdjustmentItem.adjustment.warehouse
			sci.qtyOut = -1 * difference
			sci.warehouseIn = null
			sci.qtyIn = null
		}
		sci.save()
	}
	
	private void fixOldStockOfInventoryAdjustmentItem(InventoryAdjustmentItem inventoryAdjustmentItem, StockCardItem sci, Map stockMap) {
		def oldStock = stockMap.get(sci.warehouseIn?:sci.warehouseOut) ?: 0
		inventoryAdjustmentItem.oldStock = oldStock
		inventoryAdjustmentItem.save()
	} 
	
	private void adjustCurrentWarehouseStock(InventoryAdjustmentItem inventoryAdjustmentItem, StockCardItem sci, Map stockMap) {
		stockMap.put(sci.warehouseIn?:sci.warehouseOut, inventoryAdjustmentItem.newStock)
	}
	
	private def getAllDirectPaymentInvoicesWithZeroAmount(){
		return DirectPaymentInvoice.findAllByAmount(0)
	}
	
	def fixDirectPaymentInvoiceAmountsForPaidCustomerPayments() {
		int ctr = 0
		getAllDirectPaymentInvoicesWithZeroAmount().each { dpi ->
			if(dpi.type == CustomerPaymentType.BOUNCED_CHECK || dpi.type == CustomerPaymentType.CREDIT_MEMO) {
				def customerPayment = dpi.getRelatedCustomerPayment()
				if(customerPayment.isPaid()) {
					dpi.amount = customerPayment.computeTotalAmount().abs()
					dpi.save()
					ctr++
				}
			}
		}
		println ctr + " dpi amount fixed"
	}
	
	def migratePurchaseOrderItems() {
		def purchaseOrders = PurchaseOrder.list()
		def purchaseOrderItems = PurchaseOrderItem.list()
		purchaseOrders.sort{it.id}.each { purchaseOrder ->
			if(!purchaseOrder.items.isEmpty()) {
				def purchaseOrderItem = PurchaseOrderItem.findAllByPo(purchaseOrder)
				int idx = 0
				purchaseOrderItem.sort{it.id}.each { item ->
					purchaseOrder.itemList[idx] = item
					idx++
				}
				purchaseOrder.save(flush:true)
			}
		}
	}	

	def updateTriplesProductStock() {
		def warehouse = Warehouse.findByIdentifier("CAL")
		int ctr = 0
		Product.list().sort{it.id}.each { product ->
			def stock = Stock.findByProductAndWarehouse(product, warehouse)
			def stockCard = StockCard.findByProduct(product)
			if(stock && stockCard) {
				if(stockCard.items) {
					def lastItem = stockCard.items.asList().sort{it.datePosted}.get(stockCard.items.size() - 1)
					stock.qty = lastItem.balance
					stock.save()
				}
			}
			ctr++
			if(ctr%500 == 0) {
				println ctr + " products have been modified!"
			}
		}
	}
	
	def createInitialProductStock() {
		Product.list().sort{it.id}.each{ product ->
			productService.createStocksForNewProduct(product)
		}
	}
}