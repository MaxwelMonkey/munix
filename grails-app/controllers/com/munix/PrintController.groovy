package com.munix

class PrintController {
	
	def authenticateService
	def directPaymentService
	def inventoryAdjustmentService
	def inventoryTransferService
	def customerLedgerService
	
	def creditMemo = {
        def creditMemoInstance = CreditMemo.get(params.id)
        creditMemoInstance.addToPrintLogs(new PrintLogCreditMemo(creditMemo:creditMemoInstance,user:authenticateService.userDomain()))
        creditMemoInstance.save()
        
        def customer = creditMemoInstance?.customer
        def address = ""
        if(customer?.busAddrCity){
        	address += customer?.busAddrCity
        	address += " "
        }
        address += customer?.busAddrCity?.province?.region 
        
        def model = [:]
        model["type"] = "creditMemo"
        model["title"] = "Credit / Debit Memo"
        model["header"] = [["Customer Name",creditMemoInstance?.customer?.encodeAsHTML(),"Date", formatDate(date:creditMemoInstance?.date, format:"MMM. dd, yyyy")],
                           ["Customer Address",address?.encodeAsHTML(),"Credit Memo #",creditMemoInstance],
                           ["Sales Agent",customer?.salesAgent,"Discount Type",creditMemoInstance?.discountType?.encodeAsHTML()],
                           ["Customer Terms",customer?.term,"CM Reason",creditMemoInstance?.reason],
                           ]
        
        model["columns"] = ["Sales Delivery #","Item Code - Description","Old Qty","Old Price","New Qty","New Price", "Amount", "Discount", "Final Amount"]
		model["list"] = []
        creditMemoInstance.items.sort{it.deliveryItem?.delivery?.toString()}.each{
        	def newQty = String.format( '%,.0f',it.newQty )
        	def newPrice = String.format( '%,.2f',it.newPrice )
        	if(it.oldQty!=it.newQty){
        		newQty = "<b>"+newQty+"</b>"
        	}
        	if(it.oldPrice!=it.newPrice){
        		newPrice = "<b>"+newPrice+"</b>"
        	}
        	model["list"] << [it?.deliveryItem?.delivery, "<b>"+it?.deliveryItem?.product+"</b><br>"+it?.deliveryItem?.product?.description + "<br><i>"+(it?.remark?it?.remark:"")+"</i>",String.format( '%,.0f',it.oldQty ),String.format( '%,.2f',it.oldPrice ),newQty,newPrice,String.format( '%,.2f',it.computeFinalAmount()),String.format( '%,.2f',it.computeDiscountAmount()),String.format( '%,.2f',it.computeDiscountedAmount())]
        }
        
        model["totals"] = [["","","","","","Total",String.format( '%,.2f',creditMemoInstance.computeTotal()), String.format( '%,.2f',creditMemoInstance.computeDiscountTotal()), String.format( '%,.2f',creditMemoInstance.computeTotalAmount())]]
        model["footer"] = [["Created By", creditMemoInstance?.preparedBy,"",""],
                           ["Checked By","________________________","",""],
                           ["Approved By","________________________","",""],
                           ["Approved By","________________________","",""]]
        render view: "form", model:model
	}
	
	def jobOrder = {
		def jobOrderInstance = JobOrder.get(params.id)
		def model = [:]
		model["type"] = "jobOrder"
		model["title"] = "Job Order"
		model["header"] = [["Product",jobOrderInstance?.product?.identifier +" - "+jobOrderInstance?.product?.description,"Date:", "${formatDate(date:jobOrderInstance?.startDate, format:'MMM. dd, yyyy')} - ${formatDate(date:jobOrderInstance?.targetDate, format:'MMM. dd, yyyy')}"],
		                   ["Order Quantity",jobOrderInstance?.formatQty(),"Job Order #",jobOrderInstance],
		                   ["Assigned Group",jobOrderInstance?.assignedTo?.encodeAsHTML(),"",""]
		                   ]
		                   
		model["columns"] = ["Quantity", "Units Required","Unit", "Item Code", "Description"]
		model["list"] = []
		jobOrderInstance?.requisition?.items?.sort{it.component.description}.each{
			model["list"] << [formatNumber(number:it?.computeQuantity(), format:'###,##0.00'), it?.formatUnitsRequired(), it?.component.unit, it?.component?.identifier, it?.component?.description]
		}
		model["footer"] = [["Remarks",jobOrderInstance?.remark?.encodeAsHTML(),"Prepared By","________________________"],
		                   ["Created By",jobOrderInstance?.preparedBy,"Date & Time Received","________________________"],
		                   ["Approved By",jobOrderInstance?.approvedBy,"Checked By","________________________"],
				           ["","","Date & Time Checked","________________________"]
		                   ]
		render view:"form", model:model
	}
	
    def jobOrderAccounting = {
        def jobOrderInstance = JobOrder.get(params.id)
        def model = [:]
        model["type"] = "jobOrder"
        model["title"] = "Job Order"
        model["header"] = [["Assigned Group",jobOrderInstance?.assignedTo?.encodeAsHTML(),"Start Date:", "${formatDate(date:jobOrderInstance?.startDate, format:'MMM. dd, yyyy')}"],
                ["","","Job Order #",jobOrderInstance]
        ]

        model["columns"] = ["Quantity", "Product Code", "Product Description"]
        model["list"] = []
//	        jobOrderInstance?.requisition?.items?.sort{it.component.description}.each{
//	            model["list"] << [formatNumber(number:it?.computeQuantity(), format:'###,##0'), jobOrderInstance.product.toString(), jobOrderInstance.product.getDescription()]
//	        }
        model["list"] << [formatNumber(number:jobOrderInstance?.formatQty(), format:'###,##0'), jobOrderInstance?.product?.toString(), jobOrderInstance?.product?.getDescription()]
        model["footer"] = [["Remarks",jobOrderInstance?.remark?.encodeAsHTML(),"Prepared By","________________________"],
                ["Created By",jobOrderInstance?.preparedBy,"Date & Time Received","________________________"],
                ["Approved By",jobOrderInstance?.approvedBy,"Checked By","________________________"],
                ["","","Date & Time Checked","________________________"]
        ]
        render view:"form", model:model
    }

	def jobOut = {
		def jobOutInstance = JobOut.get(params.id)
		def model = [:]
		model["type"] = "jobOut"
		model["title"] = "Job Out"
		model["header"] = [["Job Order #",jobOutInstance?.jobOrder,"Job Out #",jobOutInstance],
		                   ["Assigned to",jobOutInstance?.jobOrder?.assignedTo?.encodeAsHTML(),"Date",formatDate(date:jobOutInstance?.date, format:"MMM. dd, yyyy")],
		                   ["Warehouse",jobOutInstance?.warehouse?.identifier]
						   ]
		model["columns"] = ["Unit", "Item Code - Item Description", "Quantity", "Labor Cost", "Total"]
		model["list"] = []
		jobOutInstance?.jobOrder?.product?.each{
			def qty = jobOutInstance?.qty?jobOutInstance?.qty:""
			def laborCost = jobOutInstance?.laborCost?.amount?jobOutInstance?.laborCost?.amount+"<br>"+jobOutInstance?.laborCost:""
			def total = jobOutInstance?.qty?jobOutInstance?.laborCost?.amount?jobOutInstance?.qty * jobOutInstance?.laborCost?.amount:"":""
			model["list"] << [it.unit, it?.identifier + " - " + it?.description, "<b>"+qty+"</b>", laborCost, "<b>"+(total?String.format( '%,.2f', total):"")+"</b>"]
		}
		model["footer"] = [["Remarks",jobOutInstance?.jobOrder?.remark?.encodeAsHTML(),"Received By","________________________"],
			["Created By",jobOutInstance?.preparedBy,"Date & Time Received","________________________"]
			]
		render view:"form", model:model
		/*
		def jobOutInstance = JobOut.get(params.id)
		def model = [:]
		model["type"] = "jobOut"
		model["title"] = "Job Out"
		model["header"] = [["Job Order #",jobOutInstance?.jobOrder,"Job Out #",jobOutInstance],
		                   ["Assigned Group",jobOutInstance?.jobOrder?.assignedTo?.encodeAsHTML(),"Date",formatDate(date:jobOutInstance?.date, format:"MMM. dd, yyyy")]
		                   ]
		                   
		model["columns"] = ["Quantity", "Item Code - Item Description"]
		model["list"] = []
		jobOutInstance?.materials?.items?.each{
			model["list"] << [it?.qty?.intValue(), it?.component?.identifier + " - " + it?.component?.description]
		}
		model["footer"] = [["Remarks",jobOutInstance?.jobOrder?.remark?.encodeAsHTML(),"Received By","________________________"],
		                   ["Created By",jobOutInstance?.preparedBy,"Date & Time Received","________________________"]
		                   ]
		render view:"form", model:model
		*/
	}
	
	def materialRelease = {
		def materialReleaseInstance = MaterialRelease.get(params.id)
		def model = [:]
 		model["type"] = "materialRelease"
		model["title"] = "Material Release"
		model["header"] = [["Product",materialReleaseInstance?.jobOrder?.product?.identifier +" - "+materialReleaseInstance?.jobOrder?.product?.description,"Job Order #",materialReleaseInstance?.jobOrder],
		                   ["Order Quantity",materialReleaseInstance?.jobOrder?.qty,"Material Release #",materialReleaseInstance],
		                   ["Assigned Group",materialReleaseInstance?.jobOrder?.assignedTo?.encodeAsHTML(),"Date",formatDate(date:materialReleaseInstance?.date, format:"MMM. dd, yyyy")]
		                   ]
		                   
		model["columns"] = ["Quantity","Unit", "Code","Description"]
		model["list"] = []
		materialReleaseInstance?.items?.sort{it.materialRequisitionItem?.component?.description}.each{
			model["list"] << [formatNumber(number:it?.qty, format:'###,##0.00'), it?.materialRequisitionItem?.component?.unit, it?.materialRequisitionItem?.component?.identifier, it?.materialRequisitionItem?.component?.description]
		}
		model["footer"] = [["Remarks",materialReleaseInstance?.jobOrder?.remark?.encodeAsHTML(),"Released By","________________________"],
		                   ["Created By",materialReleaseInstance?.preparedBy,"Date & Time Released","________________________"],
		                   ["Checked By","________________________","Received By","________________________"],
		                   ["Date & Time Checked","________________________","Date & Time Received","________________________"]
		                   ]
		render view:"form", model:model
	}
	
	def directPayment = {
		def directPaymentInstance = DirectPayment.get(params.id)
		def model = [:]
 		model["type"] = "directPayment"
		model["title"] = "Direct Payment"
		model["header"] = [["Customer Name",directPaymentInstance?.customer,"Date",formatDate(date:directPaymentInstance?.date, format:"MMM. dd, yyyy")],
		                   ["Customer Address",directPaymentInstance?.customer?.formatBusinessAddress(),"Direct Payment #",directPaymentInstance],
		                   ["Customer Terms",directPaymentInstance?.customer?.term,"Sales Agent",directPaymentInstance?.customer?.salesAgent]
		                   ]
		                   
		model["columns"] = ["Payment Type", "Reference #", "Date", "Check #", "Check Date", "Bank", "Remarks", "Amount"]
        model["tableTitle"] = "Payments"
		model["list"] = []
		def items = directPaymentService?.retrieveDetailsOfItems(directPaymentInstance)
		items?.sort{it.id}.each{
			def checkNo, checkDate, bank, type = ""
			checkNo = it?.checkNumber
			checkDate = formatDate(date:it?.checkDate, format:"MMM. dd, yyyy")
			bank = it.checkBank
			model["list"] << [it?.paymentType, it?.referenceNumber, formatDate(date:it?.date, format:"MMM. dd, yyyy"), checkNo, checkDate, bank, it.remark, "${String.format('%,.2f',it?.amount)}"]
		}
        model["totals"] = [["","","","","","", "Total", "${String.format('%,.2f',directPaymentInstance?.computePaymentTotal())}"]]

		model["columns2"] = ["Date", "Type", "Reference #", "Amount","Due","Amount Applied","Net Due"]
        model["tableTitle2"] = "Applied Transactions"
		model["list2"] = []
		BigDecimal paymentTotal = directPaymentInstance?.computePaymentTotal() 
		def invoices = directPaymentService?.retrieveDetailsOfInvoices(directPaymentInstance)
		Map invoiceTotals = directPaymentService?.accumulateTotals(invoices)
		BigDecimal balance = paymentTotal - (invoiceTotals?.applied ? invoiceTotals?.applied : 0 ) 
				
		invoices?.sort{it.id}.each{
			model["list2"] << [formatDate(date:it?.date, format:"MMM. dd, yyyy"), it.type, it?.customerPayment,
					 String.format( '%,.2f',it.amount ), String.format( '%,.2f',it.due ), String.format( '%,.2f',it.applied ), String.format( '%,.2f',it.net )]
		}
        model["totals2"] = [["","","Total", "${String.format('%,.2f',invoiceTotals?.amount)}", "${String.format('%,.2f',invoiceTotals?.due)}", "${String.format('%,.2f',invoiceTotals?.applied)}", "${String.format('%,.2f',invoiceTotals?.net)}"],
                            ["&nbsp;"],["Balance","${String.format('%,.2f',balance)}"],["Remarks",directPaymentInstance?.remark?.encodeAsHTML(),"",""]]

		model["footer"] = [
		                   ]
		// TODO: Compute Balance
		render view:"form2", model:model
	}
	
	def directPaymentAccount = {
		render view:"bankAccount", model:["bankAccounts":BankAccount.list(),"submit":"directPaymentBackOfCheck","controller":"directPayment","action":"show",id:params.id]
	}
	
	def directPaymentBackOfCheck = {
		def directPaymentInstance = DirectPayment.get(params.id)
		def bankAccount = BankAccount.get(params["bankAccount.id"])
		def model = [:]
 		model["type"] = "directPayment"
		model["title"] = "Direct Payment"
 		model["customerName"] = directPaymentInstance?.customer?.name
 		model["directPayment"] = directPaymentInstance?.toString()
 		model["date"] = formatDate(date:directPaymentInstance?.date, format:"MMM. dd, yyyy")
 		model["accountNumber"] =  bankAccount?.accountNumber
 		model["accountName"] =  bankAccount?.accountName
		render view:"backOfCheck", model:model
	}
	
	def purchaseOrder = {
		def purchaseOrderInstance = PurchaseOrder.get(params.id)
		def model = [:]
 		model["type"] = "purchaseOrder"
		model["title"] = "Purchase Order"
		model["header"] = [["From:",Company.get(1)?.name,"",""],
		                   ["Supplier Name",purchaseOrderInstance?.supplier?.name,"Date",formatDate(date:purchaseOrderInstance?.date, format:"MMM. dd, yyyy")],
		                   ["Supplier Address",purchaseOrderInstance?.supplier?.address?.encodeAsHTML(),"P.O. #",purchaseOrderInstance?.formatId()]
		                   ]
		                   
		model["columns"] = ["Qty", "Unit", "Code", "Part Number", "Supplier Code", "Picture", "Description", "New Price", "Amount"]
		model["list"] = []
		purchaseOrderInstance?.items?.sort{it.product.description}.each{
			def supplierCode = SupplierItem.findBySupplierAndProduct(purchaseOrderInstance?.supplier,it?.product).productCode
			def imgPath = g.resource(dir:'product/viewImage',file:it.product.id?.toString())
			model["list"] << [it?.formatQty(), it?.product?.unit, it?.product, it?.product?.partNumber, supplierCode, "<img src='"+imgPath+"' border='0'>", it?.product?.description, (it?.price!=it?.finalPrice?"<b>":"")+purchaseOrderInstance?.supplier?.currency?.toString() + " " +String.format('%,.4f',it?.finalPrice)+(it?.price!=it?.finalPrice?"</b>":""), purchaseOrderInstance?.supplier?.currency?.toString() + " " + String.format('%,.4f',it?.computeAmount())]
		}
        model["totals"] = [["","","","","","","Total",purchaseOrderInstance?.currency, String.format('%,.4f',purchaseOrderInstance?.computeTotal())]]
		
       model["footer"] = [["Remarks",purchaseOrderInstance?.remark,"",""],
                          ["Created By",purchaseOrderInstance?.preparedBy,"Approved By",purchaseOrderInstance?.approvedBy]
       ]
		// TODO: Compute Balance
		render view:"form", model:model
	}

	def purchaseOrderNoPrice = {
		def purchaseOrderInstance = PurchaseOrder.get(params.id)
		def model = [:]
 		model["type"] = "purchaseOrderNoPrice"
		model["title"] = "Purchase Order"
		model["header"] = [["From:",Company.get(1)?.name,"",""],
		                   ["Supplier Name",purchaseOrderInstance?.supplier?.name,"Date",formatDate(date:purchaseOrderInstance?.date, format:"MMM. dd, yyyy")],
		                   ["Supplier Address",purchaseOrderInstance?.supplier?.address?.encodeAsHTML(),"P.O. #",purchaseOrderInstance?.formatId()]
		                   ]
		                   
		model["columns"] = ["Qty", "Unit", "Code", "Part Number", "Supplier Code", "Picture", "Description","Price"]
		model["list"] = []
		purchaseOrderInstance?.items?.sort{it.product.description}.each{
			def supplierCode = SupplierItem.findBySupplierAndProduct(purchaseOrderInstance?.supplier,it?.product).productCode
			def imgPath = g.resource(dir:'product/viewImage',file:it.product.id?.toString())
			model["list"] << [it?.formatQty(), it?.product?.unit, it?.product, it?.product?.partNumber, supplierCode, "<img src='"+imgPath+"' border='0'>", it?.product?.description, "______________"]
		}
		
	       model["footer"] = [["Remarks",purchaseOrderInstance?.remark,"",""],
	                          ["Created By",purchaseOrderInstance?.preparedBy,"Approved By",purchaseOrderInstance?.approvedBy]
	       ]
		// TODO: Compute Balance
		render view:"form", model:model
	}

	def purchaseOrderNoPicture = {
		def purchaseOrderInstance = PurchaseOrder.get(params.id)
		def model = [:]
 		model["type"] = "purchaseOrder"
		model["title"] = "Purchase Order"
		model["header"] = [["From:",Company.get(1)?.name,"",""],
		                   ["Supplier Name",purchaseOrderInstance?.supplier?.name,"Date",formatDate(date:purchaseOrderInstance?.date, format:"MMM. dd, yyyy")],
		                   ["Supplier Address",purchaseOrderInstance?.supplier?.address?.encodeAsHTML(),"P.O. #",purchaseOrderInstance?.formatId()]
		                   ]
		                   
		model["columns"] = ["Qty", "Unit", "Code", "Part Number", "Supplier Code", "Description", "New Price", "Amount"]
		model["list"] = []
		purchaseOrderInstance?.items?.sort{it.product.description}.each{
			def supplierCode = SupplierItem.findBySupplierAndProduct(purchaseOrderInstance?.supplier,it?.product).productCode
			model["list"] << [it?.formatQty(), it?.product?.unit, it?.product, it?.product?.partNumber, supplierCode, it?.product?.description, (it?.price!=it?.finalPrice?"<b>":"")+purchaseOrderInstance?.supplier?.currency?.toString() + " " +String.format('%,.4f',it?.finalPrice)+(it?.price!=it?.finalPrice?"</b>":""), purchaseOrderInstance?.supplier?.currency?.toString() + " " + String.format('%,.4f',it?.computeAmount())]
		}
        model["totals"] = [["","","","","","Total",purchaseOrderInstance?.currency, String.format('%,.4f',purchaseOrderInstance?.computeTotal())]]
		
       model["footer"] = [["Remarks",purchaseOrderInstance?.remark,"",""],
                          ["Created By",purchaseOrderInstance?.preparedBy,"Approved By",purchaseOrderInstance?.approvedBy]
       ]
		// TODO: Compute Balance
		render view:"form", model:model
	}

	def purchaseOrderNoPriceNoPicture = {
		def purchaseOrderInstance = PurchaseOrder.get(params.id)
		def model = [:]
 		model["type"] = "purchaseOrderNoPrice"
		model["title"] = "Purchase Order"
		model["header"] = [["From:",Company.get(1)?.name,"",""],
		                   ["Supplier Name",purchaseOrderInstance?.supplier?.name,"Date",formatDate(date:purchaseOrderInstance?.date, format:"MMM. dd, yyyy")],
		                   ["Supplier Address",purchaseOrderInstance?.supplier?.address?.encodeAsHTML(),"P.O. #",purchaseOrderInstance?.formatId()]
		                   ]
		                   
		model["columns"] = ["Qty", "Unit", "Code", "Part Number", "Supplier Code", "Description","Price"]
		model["list"] = []
		purchaseOrderInstance?.items?.sort{it.product.description}.each{
			def supplierCode = SupplierItem.findBySupplierAndProduct(purchaseOrderInstance?.supplier,it?.product).productCode
			model["list"] << [it?.formatQty(), it?.product?.unit, it?.product, it?.product?.partNumber, supplierCode, it?.product?.description, "______________"]
		}
		
	       model["footer"] = [["Remarks",purchaseOrderInstance?.remark,"",""],
	                          ["Created By",purchaseOrderInstance?.preparedBy,"Approved By",purchaseOrderInstance?.approvedBy]
	       ]
		// TODO: Compute Balance
		render view:"form", model:model
	}


	def purchaseInvoice = {
			def purchaseInvoiceInstance = PurchaseInvoice.get(params.id)
			def model = [:]
	 		model["type"] = "purchaseInvoice"
			model["title"] = "Purchase Invoice"
			model["header"] = [["Supplier Name",purchaseInvoiceInstance?.supplier?.name,"Reference #",purchaseInvoiceInstance],
			                   ["Supplier Address",purchaseInvoiceInstance?.supplier?.address?.encodeAsHTML(),"Supplier Reference #",purchaseInvoiceInstance?.supplierReference],
			                   ["Ex. Rate",purchaseInvoiceInstance?.exchangeRate,"Invoice Date",formatDate(date:purchaseInvoiceInstance?.invoiceDate, format:"MMM. dd, yyyy")],
			                   ["Currency",purchaseInvoiceInstance?.supplier?.currency,"Delivery Date",formatDate(date:purchaseInvoiceInstance?.deliveryDate, format:"MMM. dd, yyyy")]
			                   ]
			                   
			model["columns"] = ["PO", "Qty", "Unit", "Code", "Description", "Unit Price (${purchaseInvoiceInstance?.supplier?.currency})", "Unit Price (PHP)", "Amount (${purchaseInvoiceInstance?.supplier?.currency})","Amount (PHP)"]
			model["list"] = []
			purchaseInvoiceInstance?.items?.sort{it.purchaseOrderItem?.product?.description}.each{
				model["list"] << [it?.purchaseOrderItem?.po.formatId(), String.format("%,.0f",it.qty), it?.purchaseOrderItem?.product?.unit, it?.purchaseOrderItem?.product, it?.purchaseOrderItem?.product?.description,String.format("%,.4f",it?.finalPrice.setScale(4,BigDecimal.ROUND_HALF_UP)),String.format("%,.4f",it?.finalPrice.multiply((purchaseInvoiceInstance?.exchangeRate?:0)).setScale(4,BigDecimal.ROUND_HALF_UP)), String.format("%,.4f",it?.finalPrice?.multiply(it.qty?.intValue())?.setScale(4,BigDecimal.ROUND_HALF_UP)),String.format("%,.4f",it?.finalPrice?.multiply(it.qty?.intValue())?.multiply((purchaseInvoiceInstance?.exchangeRate?:0))?.setScale(4,BigDecimal.ROUND_HALF_UP))]
			}
	        model["totals"] = [["","","","","Total",'','',String.format("%,.4f",purchaseInvoiceInstance.computeForeignAmountTotal()?.setScale(4,BigDecimal.ROUND_HALF_UP)),String.format("%,.4f",purchaseInvoiceInstance.computePurchaseInvoicePhpTotal()?.setScale(4,BigDecimal.ROUND_HALF_UP))],
	                           ["","","","","Discount","${purchaseInvoiceInstance?.discountRate}%","",String.format("%,.4f",purchaseInvoiceInstance.computePurchaseInvoiceDiscountForeignAmount()?.setScale(4,BigDecimal.ROUND_HALF_UP)),String.format("%,.4f",purchaseInvoiceInstance.computePurchaseInvoiceDiscountPhpAmount()?.setScale(4,BigDecimal.ROUND_HALF_UP))],
	                           ["","","","","Grand Total","","",String.format("%,.4f",purchaseInvoiceInstance.computePurchaseInvoiceDiscountedForeignTotal()?.setScale(4,BigDecimal.ROUND_HALF_UP)),String.format("%,.4f",purchaseInvoiceInstance.computePurchaseInvoiceDiscountedPhpTotal()?.setScale(4,BigDecimal.ROUND_HALF_UP))]
	                           ]
			
	       model["footer"] = [["Remarks",purchaseInvoiceInstance?.remark,"",""],
	                          ["Created By",purchaseInvoiceInstance?.preparedBy,"",""]
	       ]
			// TODO: Compute Balance
			render view:"form", model:model
		}

	def purchaseInvoiceNoPrice = {
			def purchaseInvoiceInstance = PurchaseInvoice.get(params.id)
			def model = [:]
	 		model["type"] = "purchaseInvoiceNoPrice"
			model["title"] = "Purchase Invoice"
			model["header"] = [["Supplier Name",purchaseInvoiceInstance?.supplier?.name,"Invoice Date",formatDate(date:purchaseInvoiceInstance?.invoiceDate, format:"MMM. dd, yyyy")],
					           ["Reference #",purchaseInvoiceInstance,"Delivery Date",formatDate(date:purchaseInvoiceInstance?.deliveryDate, format:"MMM. dd, yyyy")],
			                   ["Supplier Reference #",purchaseInvoiceInstance?.supplierReference,"",""]
			                   ]
			                   
			model["columns"] = ["PO", "Qty", "Unit", "Code", "Supplier Code", "Description", "Package Details"]
			model["list"] = []
			purchaseInvoiceInstance?.items?.sort{it.purchaseOrderItem?.product?.description}.each{
				model["list"] << [it?.purchaseOrderItem?.po.formatId(), String.format("%,.0f",it.qty), it?.purchaseOrderItem?.product?.unit, it?.purchaseOrderItem?.product, it?.purchaseOrderItem?.productCode, it?.purchaseOrderItem?.product?.description, it?.purchaseOrderItem?.product?.packageDetails]
			}
			
	       model["footer"] = [["Remarks",purchaseInvoiceInstance?.remark,"",""],
	                          ["Created By",purchaseInvoiceInstance?.preparedBy,"",""]
	       ]
			// TODO: Compute Balance
			render view:"form", model:model
		}

	def purchaseInvoiceComparison = {
		def purchaseInvoiceInstance = PurchaseInvoice.get(params.id)
		def model = [:]
 		model["type"] = "purchaseInvoiceComparison"
		model["title"] = "Purchase Invoice"
		model["header"] = [["Supplier Name",purchaseInvoiceInstance?.supplier?.name,"Reference #",purchaseInvoiceInstance],
		                   ["Supplier Address",purchaseInvoiceInstance?.supplier?.address?.encodeAsHTML(),"Supplier Reference #",purchaseInvoiceInstance?.supplierReference],
		                   ["","","Invoice Date",formatDate(date:purchaseInvoiceInstance?.date, format:"MMM. dd, yyyy")],
		                   ["","","Delivery Date",formatDate(date:purchaseInvoiceInstance?.deliveryDate, format:"MMM. dd, yyyy")]
		                   ]
		                   
		model["columns"] = ["PO", "Qty", "Code", "Description", "PO Price", "Price", "Amount (Foreign Currency)"]
		model["list"] = []
		purchaseInvoiceInstance?.items?.sort{it.product?.description}.each{
			model["list"] << [it?.poItem?.po, it.poItem.formatQty(), it?.product, it?.product?.description,String.format("%,.4f",it?.poItem?.finalPrice.setScale(4,BigDecimal.ROUND_HALF_UP)),String.format("%,.4f",it?.finalPrice.setScale(4,BigDecimal.ROUND_HALF_UP)),String.format("%,.4f",it?.finalPrice?.multiply(it.qty?.intValue())?.setScale(4,BigDecimal.ROUND_HALF_UP))]
		}
        model["totals"] = [["","","","","Total",String.format("%,.4f",purchaseInvoiceInstance.computeFinalPriceTotal()?.setScale(4,BigDecimal.ROUND_HALF_UP)),String.format("%,.4f",purchaseInvoiceInstance.computeForeignAmountTotal()?.setScale(4,BigDecimal.ROUND_HALF_UP))],
                           ["","","","","Discount","${purchaseInvoiceInstance?.discountRate}%",String.format("%,.4f",purchaseInvoiceInstance.computePurchaseInvoiceDiscountForeignAmount()?.setScale(4,BigDecimal.ROUND_HALF_UP)),],
                           ["","","","","Grand Total","",String.format("%,.4f",purchaseInvoiceInstance.computePurchaseInvoiceDiscountedForeignTotal()?.setScale(4,BigDecimal.ROUND_HALF_UP))]
                           ]
		
       model["footer"] = [["Remarks",purchaseInvoiceInstance?.remark,"",""],
                          ["Created By",purchaseInvoiceInstance?.preparedBy,"",""]
       ]
		// TODO: Compute Balance
		render view:"form", model:model
	}

	def counterReceipt = {
		def counterReceiptInstance = CounterReceipt.get(params.id)
		def printLog = new PrintLogCounterReceipt(counterReceipt: counterReceiptInstance, user:authenticateService.userDomain())
		counterReceiptInstance.addToPrintLogs(printLog)
		counterReceiptInstance.save(flush:true)
		def model = [:]
 		model["type"] = "counterReceipt"
		model["title"] = params.receiptType=="SOA"?"Statement of Account":"Counter Receipt";
		model["header"] = [["Customer Name",counterReceiptInstance?.customer?.name,"Date",formatDate(date:counterReceiptInstance?.date, format:"MMM. dd, yyyy")],
		                   ["Customer Address",counterReceiptInstance?.customer?.formatBusinessAddress(),"Counter Receipt #",counterReceiptInstance],
		                   ["Terms",counterReceiptInstance?.customer?.term,"Sales Agent",counterReceiptInstance?.customer?.salesAgent]
		                   ]
		                   
		model["columns"] = ["Date", "Reference #", "DR #", "SI #", "Original Amount", "Amount"]
		model["list"] = []
		/*
		def deliveries = counterReceiptInstance.items.findAll { it.invoiceType == CustomerPaymentType.SALES_DELIVERY }
		def charges = counterReceiptInstance.items.findAll { it.invoiceType == CustomerPaymentType.CUSTOMER_CHARGE }
		def creditMemo = counterReceiptInstance.items.findAll { it.invoiceType == CustomerPaymentType.CREDIT_MEMO }
		*/
		def counterReceipts = counterReceiptInstance.items.findAll {it.invoiceType == CustomerPaymentType.SALES_DELIVERY || it.invoiceType == CustomerPaymentType.CREDIT_MEMO}
		def origAmountTotal = 0
		def amountTotal = 0
		/*
		charges?.sort{it.toString()}.each{
			origAmountTotal += it.invoice?.computeTotalAmount()
			amountTotal += it.amount
			model["list"] << [formatDate(date:it?.invoice?.date, format:"MMM. dd, yyyy"), it.invoice, "", "", String.format("%,.2f",it.invoice?.computeTotalAmount()), String.format("%,.2f",it.amount)]
		}
		deliveries?.sort{it.toString()}.each{
			origAmountTotal += it.invoice?.computeTotalAmount()
			amountTotal += it.amount
			model["list"] << [formatDate(date:it?.invoice?.date, format:"MMM. dd, yyyy"), it.invoice, it.invoice?.deliveryReceiptNumber, it.invoice?.salesDeliveryNumber, String.format("%,.2f",it.invoice?.computeTotalAmount()), String.format("%,.2f",it.amount)]
		}
		creditMemo?.sort{it.toString()}.each{
			origAmountTotal += it.invoice?.computeTotalAmount()
			amountTotal += it.amount
			model["list"] << [formatDate(date:it?.invoice?.date, format:"MMM. dd, yyyy"), it.invoice, "", "", String.format("%,.2f",it.invoice?.computeTotalAmount()), String.format("%,.2f",it.amount)]
		}
		*/
		counterReceipts.sort{it.invoice.toString()}.each{
			origAmountTotal += it.invoice?.computeTotalAmount()
			amountTotal += it.amount
			if(it.invoiceType == CustomerPaymentType.SALES_DELIVERY)
				model["list"] << [formatDate(date:it?.invoice?.date, format:"MMM. dd, yyyy"), it.invoice, it.invoice?.deliveryReceiptNumber, it.invoice?.salesDeliveryNumber, String.format("%,.2f",it.invoice?.computeTotalAmount()), String.format("%,.2f",it.amount)];
			else
				model["list"] << [formatDate(date:it?.invoice?.date, format:"MMM. dd, yyyy"), it.invoice, "", "", String.format("%,.2f",it.invoice?.computeTotalAmount()), String.format("%,.2f",it.amount)]
		}
		def charges = counterReceiptInstance.items.findAll { it.invoiceType == CustomerPaymentType.CUSTOMER_CHARGE }
		charges?.sort{it.toString()}.each{
			def charge = it.invoice
			charge?.items.each{
				origAmountTotal += it.amount
				amountTotal += it.amount
				model["list"] << [formatDate(date:charge?.date, format:"MMM. dd, yyyy"), charge, it.reference, "", String.format("%,.2f",it.amount), String.format("%,.2f",it.amount)]
			}
		}
		
        model["totals"] = [["","","","Total",String.format("%,.2f",origAmountTotal),String.format("%,.2f",amountTotal)]
                           ]
		
       model["footer"] = [["Payment is due on",formatDate(date:counterReceiptInstance?.dueDate, format:"MMM. dd, yyyy"),"",""],
                          ["Remarks",counterReceiptInstance?.remark,"Received By","________________________"],
                          ["Created By",counterReceiptInstance?.preparedBy,"Date & Time Received","________________________"]
                          
       ]
		// TODO: Compute Balance
		render view:"form", model:model
	}

	def collectionSchedule = {
		def collectionScheduleInstance = CollectionSchedule.get(params.id)
		def model = [:]
 		model["type"] = "collectionSchedule"
		model["title"] = "Collection Schedule"
		model["header"] = [["Date",formatDate(date:collectionScheduleInstance?.startDate, format:"MMM. dd, yyyy") + " - " + formatDate(date:collectionScheduleInstance?.endDate, format:"MMM. dd, yyyy"),"Ref. #",collectionScheduleInstance],
		                   ["Collector Ref. #",collectionScheduleInstance?.identifier,"Route",collectionScheduleInstance?.description],
		                   ["Collector",collectionScheduleInstance?.collector,"",""]
		                   ]
		                   
		model["tableTitle"] = "Counter"
		model["columns"] = ["Date", "Customer", "Counter Receipt #", "Due Date","Remarks", "Time In", "Time Out"]
		model["list"] = []
		collectionScheduleInstance?.items?.findAll{it.type=="Counter"}.sort{it.counterReceipt?.customer?.toString()}.each{
			model["list"] << [formatDate(date:it?.counterReceipt?.date, format:"MMM. dd, yyyy"), it.counterReceipt?.customer, it?.counterReceipt, formatDate(date:it?.counterReceipt?.dueDate, format:"MMM. dd, yyyy"), it.remark, "",""]
		}

		model["tableTitle2"] = "Collection"
		model["columns2"] = ["Date", "Customer", "Counter Receipt #", "Due Date", "Remarks", "Time In", "Time Out"]
		model["list2"] = []
		collectionScheduleInstance?.items?.findAll{it.type=="Collection"}.sort{it.counterReceipt?.customer?.toString()}.each{
			model["list2"] << [formatDate(date:it?.counterReceipt?.date, format:"MMM. dd, yyyy"), it.counterReceipt?.customer, it?.counterReceipt, formatDate(date:it?.counterReceipt?.dueDate, format:"MMM. dd, yyyy"), it.remark, "",""]
		}

		model["tableTitle3"] = "Both"
		model["columns3"] = ["Date", "Customer", "Counter Receipt #", "Due Date", "Remarks", "Time In", "Time Out"]
 		model["list3"] = []
 		collectionScheduleInstance?.items?.findAll{it.type=="Both"}.sort{it.counterReceipt?.customer?.toString()}.each{
 			model["list3"] << [formatDate(date:it?.counterReceipt?.date, format:"MMM. dd, yyyy"), it.counterReceipt?.customer, it?.counterReceipt, formatDate(date:it?.counterReceipt?.dueDate, format:"MMM. dd, yyyy"), it.remark, "",""]
 		}

		model["footer"] = [["Remarks",collectionScheduleInstance?.remarks,"Received By","________________________"],
                          ["Created By",collectionScheduleInstance?.preparedBy,"Date & Time Received","________________________"]
                          
       ]
		// TODO: Compute Balance
		render view:"collectionSchedule", model:model
	}

	def checkDeposit = {
		def checkDepositInstance = CheckDeposit.get(params.id)
		def model = [:]
		model["bean"] = checkDepositInstance             
 		model["type"] = params.view
		model["title"] = "Check Deposit"
		model["header"] = [["Deposit Date",formatDate(date:checkDepositInstance?.depositDate, format:"MMM. dd, yyyy"),"Deposit Slip #",checkDepositInstance],
		                   ["Account Name",checkDepositInstance?.account?.accountName,"Account #",checkDepositInstance?.account?.accountNumber],
		                   ["Bills Purchase",checkDepositInstance?.billsPurchase?"Yes":"No","",""]
		                   ]
		                   
		model["columns"] = ["Bank - Branch", "Check #", "Check Amount"]
		model["list"] = []
		checkDepositInstance?.checks?.sort{it.toString()}.each{
			model["list"] << [it.bank.identifier + " - " + it.branch, it, String.format('%,.2f',it.amount)]
		}
		
        model["totals"] = [["","Total",checkDepositInstance.formatTotal()]
                            ]
        model["footer"] = [["Created By",checkDepositInstance?.preparedBy,"",""]]

        render view:params.view, model:model
	}
	
	def bouncedCheck = {
		def bouncedCheckInstance = BouncedCheck.get(params.id)
		def model = [:]
 		model["type"] = "bouncedCheck"
		model["title"] = "Bounced Check"
		model["header"] = [["Customer Name",bouncedCheckInstance.customer?.name,"Date",formatDate(date:bouncedCheckInstance?.date, format:"MMM. dd, yyyy")],
		                   ["Customer Address",bouncedCheckInstance.customer?.formatBusinessAddress(),"Bounced Check #",bouncedCheckInstance,"",""],
		                   ["Check Status",bouncedCheckInstance.status,"For Redeposit",bouncedCheckInstance.forRedeposit?"True":"False"]
		                   ]
		                   
		model["columns"] = ["Bank - Branch", "Check #", "Check Date", "Check Amount"]
		model["list"] = []
		bouncedCheckInstance?.checks?.each{
			model["list"] << [it.bank.identifier + " - " + it.branch, it,formatDate(date:it?.date, format:"MMM. dd, yyyy"), it.formatAmount()]
		}
		
        model["totals"] = [["","","Total",bouncedCheckInstance.formatTotal()]
                            ]
        model["footer"] = [["Remarks",bouncedCheckInstance?.remark,"",""],
                           ["Created By",bouncedCheckInstance?.preparedBy,"",""]]
                           

        render view:"form", model:model
	}
	
	def directDelivery = {
		def model = [:]
 		model["type"] = "directDelivery"
		model["title"] = "Direct Delivery"

        render view:"form", model:model
	}

	
	def customerCharge = {
		def customerChargeInstance = CustomerCharge.get(params.id)
		def model = [:]
 		model["type"] = "customerCharge"
		model["title"] = "Customer Charge"
		model["header"] = [["Customer Name",customerChargeInstance.customer?.name,"Date",formatDate(date:customerChargeInstance?.date, format:"MMM. dd, yyyy")],
		                   ["Customer Address",customerChargeInstance.customer?.formatBusinessAddress(),"Customer Charge #",customerChargeInstance]
		                   ]
		                   
		model["columns"] = ["Date","Type", "Reference #","Description","Amount"]
		model["list"] = []
		customerChargeInstance?.items?.each{
			model["list"] << [formatDate(date:it.date, format:"MMM. dd, yyyy"),it.charge,it.reference, it.remark, String.format( '%,.2f',it.amount )]
		}
		
        model["totals"] = [["","","","Total",String.format( '%,.2f',customerChargeInstance.computeTotalAmount())]
                            ]
        model["footer"] = [["Remarks",customerChargeInstance?.remark,"",""],
                           ["Created By",customerChargeInstance?.preparedBy,"",""]]
                           

        render view:"form", model:model
	}

	
	def supplierPayment = {
		def supplierPaymentInstance = SupplierPayment.get(params.id)
		def model = [:]
 		model["type"] = "supplierPayment"
		model["title"] = "Supplier Payment"
		model["header"] = [["Supplier Name",supplierPaymentInstance?.supplier?.name,"Date",formatDate(date:supplierPaymentInstance?.date, format:"MMM. dd, yyyy")],
		                   ["Supplier Address",supplierPaymentInstance?.supplier?.address,"Reference #",supplierPaymentInstance],
		                   ["Supplier Terms",supplierPaymentInstance?.supplier?.term,"",""]
		                   ]
		                   
   		model["columns"] = ["Payment Type", "Bank - Branch", "Check #", "Check Date", "Amount"]
        model["tableTitle"] = "Payments"
 		model["list"] = []
 		supplierPaymentInstance?.items?.sort{it.id}.each{
 			def checkNo, checkDate, bank, type = ""
 			if(it?.type?.isCheck){
 				checkNo = it.checkNumber
 				checkDate = formatDate(date:it?.date, format:"MMM. dd, yyyy")
 				bank = it.formatCheckBankAndBranch()
 				type = it.type?.description
 			}
 			model["list"] << [it?.type, bank, checkNo, checkDate, String.format('%,.2f',it?.amount)]
 		}
        model["totals"] = [["","","","Total", "${String.format('%,.2f',supplierPaymentInstance?.computePaymentTotal())}"]]

 		model["columns2"] = ["Invoice Date", "Delivery Date", "Reference", "Supplier Reference","Amount"]
        model["tableTitle2"] = "Invoices"
 		model["list2"] = []
 		supplierPaymentInstance?.purchaseInvoices?.sort{it.id}.each{
 			model["list2"] << [formatDate(date:it?.invoiceDate, format:"MMM. dd, yyyy"), formatDate(date:it?.deliveryDate, format:"MMM. dd, yyyy"), it, it?.supplierReference,
 					 String.format( '%,.2f',it.computePurchaseInvoiceDiscountedForeignTotal() )]
 		}
        model["totals2"] = [["","","","Total", "${String.format('%,.2f',supplierPaymentInstance?.computeInvoiceTotal())}"]]
        model["footer"] = [["Received By","________________________","",""],
                           ["Date and Time","________________________","",""]]
                           

        render view:"form2", model:model
	}
	
	
	def inventoryAdjustment = {
		def inventoryAdjustmentInstance = InventoryAdjustment.get(params.id)
		def model = [:]
		 model["type"] = "inventoryAdjustment"
		model["title"] = "Inventory Adjustment"
		model["header"] = [["Warehouse",inventoryAdjustmentInstance?.warehouse?.identifier,"ID",inventoryAdjustmentInstance.toString()],
						   ["Item Type",inventoryAdjustmentInstance?.itemType,"Date:",formatDate(date:inventoryAdjustmentInstance?.dateGenerated, format:"MMM. dd, yyyy")]
						   ]
						   
		model["columns"] = ["Identifier","Description", "Old Stock","New Stock","Difference"]
		model["list"] = []
		inventoryAdjustmentService.generateInventoryAdjustmentItems(inventoryAdjustmentInstance)?.each{
			model["list"] << [it.identifier, it.description, it.oldStock, it.newStock, it.stockDifference]
		}
		
		/*
		 * model["totals"] = [["","","","Total",String.format( '%,.2f',inventoryAdjustmentInstance.computeTotalAmount())]
		 *
		 *					]
		*/
		model["footer"] = [["Remarks",inventoryAdjustmentInstance?.remark,"",""],
						   ["Created By",inventoryAdjustmentInstance?.preparedBy,"",""]]
						   

		render view:"form", model:model
	}
	
	def inventoryTransfer = {
		def inventoryTransferInstance = InventoryTransfer.get(params.id)
		def model = [:]
		 model["type"] = "inventoryTransfer"
		model["title"] = "Inventory Transfer"
		model["header"] = [["Origin",inventoryTransferInstance?.originWarehouse?.encodeAsHTML(),"ID",inventoryTransferInstance],
						   ["Destination",inventoryTransferInstance?.destinationWarehouse?.encodeAsHTML(),"Date:",formatDate(date:inventoryTransferInstance?.date, format:"MMM. dd, yyyy")]
						   ]
						   
		model["columns"] = ["Identifier","Description", "Quantity"]
		model["list"] = []
		inventoryTransferService.generateInventoryTransferItems(inventoryTransferInstance)?.each{
			model["list"] << [it.identifier, it.description, formatNumber(number:it.qty, format:'###,##0.00')]
		}
		
		/*
		 * model["totals"] = [["","","","Total",String.format( '%,.2f',inventoryAdjustmentInstance.computeTotalAmount())]
		 *
		 *					]
		*/
		model["footer"] = [["Remarks",inventoryTransferInstance?.remark,"",""],
						   ["Created By",inventoryTransferInstance?.preparedBy,"",""]]
						   

		render view:"form", model:model
	}
	
	def priceAdjustment = {
		def priceAdjustmentInstance = PriceAdjustment.get(params.id)
		def model = [:]
		 model["type"] = "priceAdjustment"
		model["title"] = "Price Adjustment"
		model["header"] = [["Item Type",fieldValue(bean: priceAdjustmentInstance, field: "itemType"),"PA #",priceAdjustmentInstance],
						   ["Price Type",priceAdjustmentInstance?.priceType?.encodeAsHTML(),"Effective Date:",formatDate(date:priceAdjustmentInstance?.effectiveDate, format:"MMM. dd, yyyy")]
						   ]
						   
		model["columns"] = ["Identifier","Description", "Old Price", "New Price", "Net", "Margin"]
		model["list"] = []
		priceAdjustmentInstance?.items?.each{
			model["list"] << [it?.product?.identifier, it?.product?.description, "PHP ${formatNumber(number:it?.oldPrice, format:'###,##0.00')}", "PHP ${formatNumber(number:it?.newPrice, format:'###,##0.00')}", it?.product?.isNet?"NET":"", formatNumber(number:it?.margin, format:'###,##0.00')+"%" ]
		}
		
		/*
		 * model["totals"] = [["","","","Total",String.format( '%,.2f',inventoryAdjustmentInstance.computeTotalAmount())]
		 *
		 *					]
		*/
		model["footer"] = [["Remarks",priceAdjustmentInstance?.remark,"",""],
						   ["Created By",priceAdjustmentInstance?.preparedBy,"",""]]
						   

		render view:"form", model:model
	}
	
	def customerLedger = {
		def customerLedgerInstance = CustomerLedger.get(params.id)
		def model = [:]
		model["type"] = "customerLedger"
		model["title"] = "Customer Ledger"
		model["header"] = [["Customer",customerLedgerInstance.customer,"Date Range","${formatDate(params.postDateBeforeText, format:'MMM. dd, yyyy')} - ${formatDate(params.postDateAfterText, format:'MMM. dd, yyyy')}"]]
						   
		model["columns"] = ["Date Created","Date Posted", "Type", "Remarks", "Reference #", "Amount", "Debit", "Credit", "Balance"]
		model["list"] = []
		customerLedgerService.getCustomerLedgerEntries(customerLedgerInstance, params).entries.each{
			def entry = it
			if(!entry.isChild){
				model["list"] << [formatDate(date:entry?.dateOpened, format:'MMM. dd, yyyy'), formatDate(date:entry?.datePosted, format:"MMM. dd, yyyy"), "${it.type} ${it.details?:''}", "", it.referenceId, entry.amount?"${formatNumber(number:entry.amount, format:'###,##0.00')}":'', entry.debitAmount?"${number:formatNumber(number:entry.debitAmount, format:'###,##0.00')}":'', entry.creditAmount?"${formatNumber(number:entry.creditAmount, format:'###,##0.00')}":'', entry.runningBalance?"${formatNumber(number:entry.runningBalance, format:'###,##0.00')}":'' ]
				if(entry.paymentBreakdown){
					entry.paymentBreakdown.sort{it.details}.each{
						def payment=it;
						model["list"] << ['', '', "${payment.details?:''}", "${payment.remark?:''}", '', payment.amount?"${formatNumber(number:payment.amount, format:'###,##0.00')}":'', payment.debitAmount?"${number:formatNumber(number:payment.debitAmount, format:'###,##0.00')}":'', payment.creditAmount?"${formatNumber(number:payment.creditAmount, format:'###,##0.00')}":'', '' ]
					}
				}
			}
		}
		
		/*
		 * model["totals"] = [["","","","Total",String.format( '%,.2f',inventoryAdjustmentInstance.computeTotalAmount())]
		 *
		 *					]
		*/
		/*
		 * model["footer"] = [["Remarks",customerLedgerInstance?.remark,"",""],
						   ["Created By",customerLedgerInstance?.preparedBy,"",""]]
		*/
						   

		render view:"form", model:model
	}
	
	def checkWarehousing = {
		def checkWarehousingInstance = CheckWarehousing.get(params.id);
		def model = [:]
		model["type"] = "checkWarehousing"
		model["title"] = "Check Warehousing"
		model["header"] = [["Origin: ",checkWarehousingInstance?.originWarehouse?.encodeAsHTML(),"Ref. #",checkWarehousingInstance],
						  	["Destination",checkWarehousingInstance?.destinationWarehouse?.encodeAsHTML(),"Date:",formatDate(date:checkWarehousingInstance?.date, format:"MMM. dd, yyyy")]
						  ]
						  
		model["columns"] = ["Check Date","Check Bank - Branch", "Check #", "Amount"]
		model["list"] = []
		checkWarehousingInstance?.checks?.sort{it.date}.each{
			model["list"] << [formatDate(date:it.date, format:'MMM. dd, yyyy'),it.bank,it.checkNumber,it.formatAmount()]
		}
		model["totals"] = [["","","Total",checkWarehousingInstance?.formatTotal()]]
		
		model["footer"] = [["Remarks",checkWarehousingInstance?.remark,"",""],
						  	["Created By",checkWarehousingInstance?.preparedBy,"",""]]
		
		render view:"form", model:model

	}
}