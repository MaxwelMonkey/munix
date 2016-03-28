package com.munix

import grails.converters.*
import org.apache.poi.POIXMLDocument
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator
import org.apache.poi.ss.usermodel.CellValue
import org.springframework.web.multipart.MultipartHttpServletRequest
import org.springframework.web.multipart.commons.CommonsMultipartFile


class SalesOrderController  implements org.springframework.context.ApplicationContextAware{

    static allowedMethods = [save: "POST", update: "POST", markAsComplete: "POST", cancel: "POST"]
    def constantService
    def authenticateService
    def salesOrderService
    def generalMethodService
    
	def applicationContext
    def productService
  
    def index = {
        redirect(action: "list", params: params)
    }
    def filter = {
        redirect(action : "list", params: params)
    }

    def list = {
        if (!params.max) params.max = 100
        if (!params.offset) params.offset = 0
        if (!params.sort) params.sort = "id"
        if (!params.order) params.order = "asc"
        
        if(params.searchStatus){
        	if(params?.searchStatus=="All statuses"){
	            params.searchStatuses=["Unapproved","Second Approval Pending","Approved", "Complete", "Cancelled"]
        	}else{
        		params.searchStatuses=[params.searchStatus]
        	}
        }else{
            params.searchStatuses=["Unapproved","Second Approval Pending","Approved"]
        }
        def salesOrderInstanceList = SalesOrder.filter(params).list()
        def count = SalesOrder.countFilter(params).count()
        [salesOrderInstanceList: salesOrderInstanceList, salesOrderInstanceTotal: count, params : params]
    }

    def create = {
        def salesOrderInstance = new SalesOrder()
        def customerList= Customer.findAllByStatusInList([Customer.Status.ACTIVE,Customer.Status.BADACCOUNT]).sort{it.toString()}

        return [salesOrderInstance: salesOrderInstance, customerList:customerList, warehouseList:Warehouse.list(sort:"identifier")]
    }

    def save = {
		def priceType = PriceType.getTypeByName(params.priceType)
		params.priceType = null
		params.priceType = priceType
		def salesOrderInstance = new SalesOrder(params)
		
		def dg = salesOrderInstance?.customer?.discounts.find{it.discountType?.id==salesOrderInstance.discountType?.id && it.type==CustomerDiscount.Type.DISCOUNT}?.discountGroup
		salesOrderInstance.discountGroup = dg
		salesOrderInstance.discount = dg?.rate
		def ndg = salesOrderInstance?.customer?.discounts.find{it.discountType?.id==salesOrderInstance.discountType?.id && it.type==CustomerDiscount.Type.NET}?.discountGroup
		salesOrderInstance.netDiscountGroup = ndg
		salesOrderInstance.netDiscount = ndg?.rate
        salesOrderInstance.salesAgent = salesOrderInstance?.customer?.salesAgent
        salesOrderInstance.preparedBy = FormatUtil.createTimeStamp(authenticateService.userDomain())
        def toBeDeleted = salesOrderInstance.orderItemList?.findAll { it.isDeleted }
        salesOrderInstance.items.removeAll(toBeDeleted)
		
        if(salesOrderInstance.customer?.autoApprove){
			approveSalesOrder(salesOrderInstance)
        }
		
        if (salesOrderInstance.save(flush: true, cascade : true) && params.customer.id != "") {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'salesOrder.label', default: 'SalesOrder'), salesOrderInstance.id])}"
            redirect(action: "show", id: salesOrderInstance.id)
        }
        else {
            render(view: "create", model: [salesOrderInstance: salesOrderInstance, customerList:Customer.list().sort{it.toString()},warehouseList:Warehouse.list(sort:"identifier")])
        }
    }

    def show = {
        def salesOrderInstance = SalesOrder.get(params.id)
        if (!salesOrderInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'salesOrder.label', default: 'SalesOrder'), params.id])}"
            redirect(action: "list")
        }
        else {
            def boldDiscountGroup = isDiscountChanged(salesOrderInstance)
			def boldNetDiscountGroup = isNetDiscountChanged(salesOrderInstance)
            [salesOrderInstance: salesOrderInstance,printLogList:salesOrderInstance.printLogs, boldDiscountGroup : boldDiscountGroup, boldNetDiscountGroup: boldNetDiscountGroup]
        }
    }

    private boolean isDiscountChanged(SalesOrder salesOrderInstance) {
        def checker = CustomerDiscount.withCriteria {
            and{
                eq("discountType",salesOrderInstance.discountType)
                eq("customer",salesOrderInstance.customer)
				eq("type",CustomerDiscount.Type.DISCOUNT)
                eq("discountGroup",salesOrderInstance.discountGroup)
            }
        }
        return checker?false:true
    }

	private boolean isNetDiscountChanged(SalesOrder salesOrderInstance) {
		def checker = CustomerDiscount.withCriteria {
			and{
				eq("discountType",salesOrderInstance.discountType)
				eq("customer",salesOrderInstance.customer)
				eq("type",CustomerDiscount.Type.NET)
				eq("discountGroup",salesOrderInstance.netDiscountGroup)
			}
		}
		return checker?false:true
	}
	
    def edit = {
        def salesOrderInstance = SalesOrder.get(params.id)
        if (!salesOrderInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'salesOrder.label', default: 'SalesOrder'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [salesOrderInstance: salesOrderInstance,warehouseList:Warehouse.list(sort:"identifier"), salesOrderItems:salesOrderService.generateSalesOrderItems(salesOrderInstance) ]
        }
    }

    def update = {
        def salesOrderInstance = SalesOrder.get(params.id)

        if (salesOrderInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (salesOrderInstance.version > version) {
                    salesOrderInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'salesOrder.label', default: 'SalesOrder')] as Object[], "Another user has updated this SalesOrder while you were editing")
                    render(view: "edit", model: [salesOrderInstance: salesOrderInstance])
                    return
                }
            }
			def dg = salesOrderInstance?.discountGroup
			def dr = salesOrderInstance?.discount
			def ndg = salesOrderInstance?.netDiscountGroup
			def ndr = salesOrderInstance?.netDiscount
            salesOrderInstance.properties = params
            def toBeDeleted = salesOrderInstance.orderItemList?.findAll { it.isDeleted == true }
			salesOrderInstance.items.removeAll(toBeDeleted)
			toBeDeleted.each{
				it.delete()
			}
			if (authenticateService.ifNotGranted("ROLE_MANAGER_SALES")) {
				salesOrderInstance.discountGroup = dg
				salesOrderInstance.discount = dr
				salesOrderInstance.netDiscountGroup = ndg
				salesOrderInstance.netDiscount = ndr
			}
            if (!salesOrderInstance.hasErrors() && salesOrderInstance.save(flush: true, cascade : true)) {
				flash.message = "${message(code: 'default.updated.message', args: [message(code: 'salesOrder.label', default: 'SalesOrder'), salesOrderInstance.id])}"
                redirect(action: "show", id: salesOrderInstance.id)
            }
            else {
                render(view: "edit", model: [salesOrderInstance: salesOrderInstance,warehouseList:Warehouse.list(sort:"identifier"),salesOrderItems:salesOrderService.generateSalesOrderItems(salesOrderInstance)])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'salesOrder.label', default: 'SalesOrder'), params.id])}"
            redirect(action: "list")
        }
    }

    def approve = {
        def salesOrderInstance = SalesOrder.get(params.id)
		if (authenticateService.ifNotGranted("ROLE_MANAGER_SALES")) {
			def defaultQuantityHasBeenChanged = salesOrderService.checkIfDefaultQuantityHasBeenChanged(salesOrderInstance)
			if (defaultQuantityHasBeenChanged) {
				flash.message = "Order can only be approved by sales manager!"
				redirect(action:'show', id: salesOrderInstance?.id)
				return
			}
		}

        approveSalesOrder(salesOrderInstance)
        salesOrderInstance.save()

        flash.message = "Order has been successfully approved!"
        redirect(action:'show',id:salesOrderInstance?.id)
    }

    def retrieveProductsForSale = {
      if(request.post){
          def discountType = DiscountType.findById(params.discountType)
          def searchFields = [identifier:params.identifier,
                  category:params.category,
                  subcategory:params.subcategory,
                  brand:params.brand,
                  model:params.model,
                  size:params.size,
                  color:params.color]
          def products = salesOrderService.getAvailableProducts(discountType,searchFields, params.priceType?:"")
          products = products?.sort{ it?.description}
          def data = []
          def warehouseList = Warehouse.list(sort:"identifier")
          def dataMap = []
          products?.each{product->
            dataMap = []
            dataMap.addAll(product.id,
                product.identifier,
                product.description,
				product.partNumber,
                product.isNet,
                String.format('%,.2f',product.getProductPrice(params.priceType)),
				product?.getPackageDetails())
            warehouseList.each{
            	try{
            	if(product.getStock(it).qty>0)
                	dataMap.add("<span class='positive'>"+product.formatSOH(it)+"</span>")
                else if(product.getStock(it).qty<0)
                	dataMap.add("<span class='negative'>"+product.formatSOH(it)+"</span>")
                else
                	dataMap.add(product.formatSOH(it))
                }catch(e){
                e.printStackTrace()
                }
            }
            data.add(dataMap)
          }
          def jsonResponse = [iTotalRecords : data.size(), iTotalDisplayRecords : data.size(), aaData : data]
          render jsonResponse as JSON
      }else{
          redirect(controller:"logout")
      }
    }

    def retrieveCustomerDiscounts = {
      if(request.post){
         def customer = Customer.findById(params?.customer)
         def jsonResponse = []
         customer?.discounts?.each {
           jsonResponse.add([
               id : it.id,
               discountGroupId : it.discountGroup.id,
               discountTypeId : it.discountType.id,
               identifier : it.discountType.identifier,
               desc : it.discountGroup.description,
               rate : it.discountGroup.rate,
               rateFormat : it.discountGroup.formatRate(),
			   type: it.type.name
           ])
         }
        render jsonResponse as JSON
      } else {
          redirect(controller:"logout")
      }
    }

    def retrieveDiscountGroups = {
      if(request.post){
         def jsonResponse = []
         DiscountGroup.list()?.each {
           jsonResponse.add([
               id : it.id,
               rate : it.rate,
               rateFormat : it.formatRate()
           ])
         }
        render jsonResponse as JSON
      }else{
          redirect(controller:"logout")
      }
    }


    def approveTwo = {
        def salesOrderInstance = SalesOrder.get(params.id)
        def canSecondApprove = true
        if(salesOrderInstance?.customer?.managerSecondApprove && authenticateService.ifNotGranted("ROLE_MANAGER_ACCOUNTING")){
            canSecondApprove = false
        }
        if(canSecondApprove){
        	secondApproveSalesOrder(salesOrderInstance)
        	salesOrderInstance.save()
        	flash.message = "Order has been successfully approved!"
        }else{
        	flash.error = "Sales order can only be approved by ROLE_MANAGER_ACCOUNTING!"
        }
        redirect(action:'show',id:salesOrderInstance?.id)
    }

    def unapprove = {
        def salesOrderInstance = SalesOrder.get(params.id)
		if (salesOrderInstance.isUnapprovable) {
			salesOrderInstance?.unapproved()
			salesOrderInstance.approvedBy = ""
			salesOrderInstance.approvedTwoBy = ""
			salesOrderInstance?.removeItemCosts()
			salesOrderInstance?.save()
			flash.message = "Order has been successfully unapproved!"
		} else {		
			flash.error = "Sales Order can't be unapproved because it has at least one Sales Delivery!"
		}
        redirect(action:'show',id:salesOrderInstance?.id)
    }

    def print = {
        def salesOrderInstance = SalesOrder.get(params.id)
        def user = authenticateService.userDomain()

        def canPrint = true
        
		if (authenticateService.ifNotGranted("ROLE_MANAGER_SALES")) {
			def printable = false
			def printType= PrintLogSalesOrder.PrintType.PRICE
			def printlogs = PrintLogSalesOrder.executeQuery("from PrintLogSalesOrder p where p.salesOrder=:so and p.printType=:t",[so:salesOrderInstance, t:printType])
			if(printlogs.size()==0) printable = true
	        def approved = ApprovalProcess.executeQuery("from ApprovalProcess a where a.status=:status and a.referenceNumber=:id and a.type=:type and a.requestedBy=:u and a.remarks=:r",[status:"Approved", id:salesOrderInstance.id?.intValue(), type:"Sales Order", u:user, r:printType.toString()])
	        if(!printable && approved.size()==0){
	        	def msg = "${user.userRealName} has requested to print Sales Order # ${salesOrderInstance.toString()} for ${printType.toString()}."
	        	new ApprovalProcess(description:msg, requestedBy:user, date:new Date(), type:"Sales Order", referenceNumber:salesOrderInstance.id, remarks: printType.toString()).save(flush:true)
		        redirect action:"show", id: salesOrderInstance.id, params:["a":"print"]
		        canPrint = false
		        return
	        }else{
	        	def printLog
	        	if(approved.size()>0){
	        		printLog = new PrintLogSalesOrder(salesOrderInstance,authenticateService.userDomain())
	        		approved[0].delete()
	        	}else{
	        		printLog = new PrintLogSalesOrder(salesOrderInstance,authenticateService.userDomain())
	        	}
				printLog.setPrintTypePrice()
				printLog.save()
				salesOrderInstance.addToPrintLogs(printLog)
	        }
		}else{
			def printLog=new PrintLogSalesOrder(salesOrderInstance,authenticateService.userDomain())
			printLog.setPrintTypePrice()
			printLog.save()
			salesOrderInstance.addToPrintLogs(printLog)
		}
        
        if(canPrint){
	        salesOrderInstance.save(flush:true)
	        redirect action:"show", id: salesOrderInstance.id, params:["a":"printAllowed"]
	        return
        }
    }

    def printApproved = {
        def salesOrderInstance = SalesOrder.get(params.id)
        def printLog=new PrintLogSalesOrder(salesOrderInstance,authenticateService.userDomain())
		printLog.setPrintTypePrice()
		printLog.save()
		salesOrderInstance.addToPrintLogs(printLog)
        salesOrderInstance.save(flush:true)
        render view:"print", model:[salesOrderInstance: salesOrderInstance]
    }

    def printNoPrice = {
        def salesOrderInstance = SalesOrder.get(params.id)
        def printLog=new PrintLogSalesOrder(salesOrderInstance,authenticateService.userDomain())
		printLog.setPrintTypeNoPrice()
		printLog.save()
		salesOrderInstance.addToPrintLogs(printLog)
        salesOrderInstance.save(flush:true)
        [salesOrderInstance: salesOrderInstance]
    }

    def markAsComplete = {
        def salesOrderInstance = SalesOrder.get(params.id)
        def deliveryUnapprovedList = salesOrderInstance?.deliveries?.findAll{
            it.status == "Unapproved"
        }
        
        if(deliveryUnapprovedList.size() > 0){
            flash.message = "Unable to Mark as Complete due to unapproved deliveries"
            redirect(action: "show", id: salesOrderInstance.id)
        }
        else{
            if(salesOrderInstance?.deliveries?.size() > 0){
                salesOrderInstance?.complete()
                salesOrderInstance.closedBy = FormatUtil.createTimeStamp(authenticateService.userDomain())
                salesOrderInstance?.save()
                flash.message = "Order has been successfully completed!"
            }
            else{
                flash.message = "Order cannot be completed because there are no deliveries!"
            }

            redirect(action:'show',id:salesOrderInstance?.id)
        }
    }

    def cancel = {
        def salesOrderInstance = SalesOrder.get(params.id)
		if(salesOrderInstance.isCancelable()) {
			salesOrderInstance.cancelledBy = FormatUtil.createTimeStamp(authenticateService.userDomain())
			salesOrderInstance?.cancelled()
			if (salesOrderInstance?.save()) {
				flash.message = "Order has now been cancelled!"
				redirect(action:'show',id:salesOrderInstance?.id)
			} else {
				flash.error = "Unable to cancel Order!"
			}
		}else {
			flash.error = "Sales order cannot be cancelled because it is already approved!"
		}
		redirect(action:'show',id:salesOrderInstance?.id)
    }
	
	def retrieveCustomerRemainingCredit = {
		def customerInstance = Customer.get(params.customerId)
		def jsonMap = [remainingCredit: customerInstance?.remainingCredit,
                isNegative:customerInstance.remainingCreditIsNegative()]
		render jsonMap as JSON
	}

	def retrieveProductHistory = {
		def productInstance = Product.get(params.productId)
        def customerInstance = Customer.get(params.customerId)
        def salesDeliveryItems = salesOrderService.getThreeLatestSalesDeliveryItemsOfProductAndCustomer(productInstance, customerInstance)
        def jsonMap = []
        salesDeliveryItems.each{
            jsonMap.add([productAmount: it.price,
                    salesDeliveryId:it.delivery.toString(),
                    dateApproved:generalMethodService.getDateFromApprovedBy(it.delivery.preparedBy).format("MMM. dd, yyyy - hh:mm a"),
                    discountRate:it.getDiscount()+"%",
                    isNet:it.orderItem.isNet?"true":"false",
                    discountedPrice:it.getDiscountedPrice()])
        }
		render jsonMap as JSON
	}

	def viewPriceMargin = {
		def salesOrderInstance = SalesOrder.get(params.id)
		[salesOrderInstance: salesOrderInstance]
	}
    private def approveSalesOrder(SalesOrder salesOrderInstance){
        salesOrderInstance.approvedBy = FormatUtil.createTimeStamp(authenticateService.userDomain())
        def canSecondApprove = true
        if(salesOrderInstance?.customer?.managerSecondApprove && authenticateService.ifNotGranted("ROLE_MANAGER_ACCOUNTING")){
            canSecondApprove = false
        }
		if(salesOrderInstance.customer?.autoSecondApprove && canSecondApprove){
			secondApproveSalesOrder(salesOrderInstance)
		} else {
			salesOrderInstance?.secondApproval()
		}

    }
    private def secondApproveSalesOrder(SalesOrder salesOrderInstance){
        salesOrderInstance.approvedTwoBy = FormatUtil.createTimeStamp(authenticateService.userDomain())
        salesOrderInstance?.approved()
		salesOrderInstance?.updateItemCosts()
    }
    
    def excelForm = {
        [categories: ProductCategory.list().sort{it.toString()}, subcategories: ProductSubcategory.list().sort{it.toString()}, itemTypes: ItemType.list().sort{it.toString()}, discountTypes: DiscountType.list().sort{it.toString()}]
    }
    
    def generateExcel = {
    	params["searchStatus"] = "Active"
        def products = productService.generateList(params)
        if(products?.size()==0){
        	flash.message="No Products retrieved with the given criteria."
        	redirect action:"excelForm"
        	return
        }
		def nameOfWorkbook = "Sales_Order_Form.xls"
		// define the location where to store the workbook
		def locationToStoreWorkbook = "/WEB-INF/resource/"
		//create a POIFSFileSystem object to read the data
		def template = applicationContext.getResource(locationToStoreWorkbook + nameOfWorkbook)

		def wb = WorkbookFactory.create(template.getInputStream())
		def sheet = wb.getSheetAt(0);
		def r = sheet.getRow(1)
		def c = r.createCell(1)
		
		c.setCellValue(new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new Date()))
        def count = 3;
		for(product in products){
			def row = sheet.createRow(count)
			def code = row.createCell(1)
			code.setCellValue(product.identifier)

			def desc = row.createCell(2)
			desc.setCellValue(product.formatDescription())
			
			def unit = row.createCell(3)
			unit.setCellValue(product.unit?.toString())

			def packageDetails = row.createCell(4)
			packageDetails.setCellValue(product.formatPackageDetails())

			def price = row.createCell(5)
			if(params.wholesaleRetail == "Wholesale")
				price.setCellValue(product.wholeSalePrice)
			else if(params.wholesaleRetail == "Retail")
				price.setCellValue(product.retailPrice)

			def net = row.createCell(6)
			net.setCellValue(product.isNet)

			def amount = row.createCell(7)
			amount.setCellFormula("A"+(count+1)+"*F"+(count+1))

			def id = row.createCell(8)
			id.setCellValue(product.id)

			def wholesaleRetail = row.createCell(9)
			wholesaleRetail.setCellValue(params.wholesaleRetail)

			def discountType = row.createCell(10)
			discountType.setCellValue(product.type?.toString())

			def itemType = row.createCell(11)
			itemType.setCellValue(product.itemType?.toString())

			count++
		}
		def sdf = new java.text.SimpleDateFormat("ddMMMyyyy")
		response.setContentType("application/xls");
        response.setHeader("Cache-Control", "no-cache");
        response.addHeader("Content-disposition",
                                  "attachment; filename=" +
                                  "SO_"+sdf.format(new Date())+".xls");		
        wb.write(response.outputStream)
    }
    
    def upload = {
        def salesOrderInstance = new SalesOrder()
        def customerList= Customer.findAllByStatusInList([Customer.Status.ACTIVE,Customer.Status.BADACCOUNT]).sort{it.toString()}

        return [salesOrderInstance: salesOrderInstance, customerList:customerList, warehouseList:Warehouse.list(sort:"identifier")]
    }

    def uploadFile = {
    	if(!params.file){
            render(view: "uploadFile", model: ["error":"File has not been specified."])
            return
    	}else{
    		try{
	    		MultipartHttpServletRequest mpr = (MultipartHttpServletRequest)request;  
	    		CommonsMultipartFile file = (CommonsMultipartFile) mpr.getFile("file");  
	    		def items = processFile(file)
    			def salesOrderInstance = new SalesOrder()
    			if(params.id){
    				salesOrderInstance = SalesOrder.get(params.id)
    			}
	    		def duplicates = [:]
  	    		items.each{
  	    			def item = it
  	    			salesOrderInstance.items.each{
  	    				if(item.product.id == it.product.id){
  	    					duplicates[item.product.id] = item.product
  	    				}
  	    			}
  	    		}
	    		session["soitems"] = items
	    		session["soduplicates"] = duplicates
	    		println "items:"+items
	    		[salesOrderInstance:salesOrderInstance, items:items, duplicates: duplicates ]
    		}catch(e){
    			e.printStackTrace()
                render(view: "uploadFile", model: ["error":"There was a problem with the file you uploaded. Error:"+e.getMessage()])
                return
    		}
    	}
    }
    
    def uploadSave = {
		def priceType = PriceType.getTypeByName(params.priceType)
		params.priceType = null
		params.priceType = priceType
		def salesOrderInstance = new SalesOrder(params)
        salesOrderInstance.salesAgent = salesOrderInstance?.customer?.salesAgent
        salesOrderInstance.preparedBy = FormatUtil.createTimeStamp(authenticateService.userDomain())
    	if(!params.file){
    		salesOrderInstance.errors.rejectValue("version", "emptyFile", [message(code: 'salesOrder.label', default: 'SalesOrder')] as Object[], "Uploaded file cannot be empty.")
            render(view: "upload", model: [salesOrderInstance: salesOrderInstance, customerList:Customer.list().sort{it.toString()},warehouseList:Warehouse.list(sort:"identifier")])
            return
    	}else{
    		MultipartHttpServletRequest mpr = (MultipartHttpServletRequest)request;  
    		CommonsMultipartFile file = (CommonsMultipartFile) mpr.getFile("file");  
    		def items = processFile(file)
        	items.each{
        		salesOrderInstance.addToItems(it)
        	}
	        if (salesOrderInstance.save() && params.customer.id != "") {
	            flash.message = "${message(code: 'default.created.message', args: [message(code: 'salesOrder.label', default: 'SalesOrder'), salesOrderInstance.id])}"
	            redirect(action: "show", id: salesOrderInstance.id)
	        }
	        else {
	            render(view: "upload", model: [salesOrderInstance: salesOrderInstance, customerList:Customer.list().sort{it.toString()},warehouseList:Warehouse.list(sort:"identifier")])
	        }
    	}
    }
    
    def uploadUpdate = {
    	def salesOrder = SalesOrder.get(params.id)
		def items = session["soitems"]
		def duplicates = session["soduplicates"]
    	items.each{
    		if(!duplicates.containsKey(it.product.id)){
    			salesOrder.addToItems(it)
    		}
    	}
    	salesOrder.save()
		flash.message = "${message(code: 'default.updated.message', args: [message(code: 'salesOrder.label', default: 'SalesOrder'), salesOrder.id])}"
        redirect(action: "show", id: salesOrder.id)
    }
    
    def processFile(file){
    	def items = []
		def wb = WorkbookFactory.create(file.getInputStream())
		def evaluator
		if(wb instanceof HSSFWorkbook){
			evaluator = new HSSFFormulaEvaluator(wb)
		}else{
			evaluator = new XSSFFormulaEvaluator(wb)
		}
		def sheet = wb.getSheetAt(0);
		for(a in 3..sheet.getPhysicalNumberOfRows()-1){
			def row = sheet.getRow(a)
			def id = getValue(8,row,evaluator)
			def qty = getValue(0,row,evaluator)
			if(qty){
				def soi = new SalesOrderItem()
				soi.product = Product.get(id)
				soi.qty = BigDecimal.valueOf(qty)
				def wholesaleRetail = getValue(9,row,evaluator)
				def price
				if(wholesaleRetail == "Wholesale"){
					price = soi.product?.wholeSalePrice
				}else{
					price = soi.product?.retailPrice
				}
				soi.price = price
				soi.finalPrice = BigDecimal.valueOf(price)
				soi.isNet = getValue(6,row,evaluator)
				soi.cost = soi.product?.runningCost
				items.add(soi)
			}
		}
		items
    }

	def getString(cellNumber,row){
		def cell = row.getCell(cellNumber)
		def text=cell?.getStringCellValue()
		if(!text){
			text=""
		}
		return text
	}

	def getValue(cellNumber,row, evaluator){
		def value;
		def cell = row.getCell(cellNumber)
		CellValue cellValue = evaluator.evaluate(cell)
		if(cellValue) {
			if(cellValue.getCellType() == 0) {
				value = cellValue.getNumberValue()
			} else if (cellValue.getCellType() == 1) {
				value = cellValue.getStringValue()
			} else if (cellValue.getCellType() == 4) {
				value = cellValue.getBooleanValue()
			}
		} else {
			value = ""
		}
		
		return value
	}

	public void setApplicationContext(org.springframework.context.ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}
	
	def balance = {
        def salesOrderInstance = SalesOrder.get(params.id)
        if (!salesOrderInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'salesOrder.label', default: 'SalesOrder'), params.id])}"
            redirect(action: "list")
        }
        else {
            [salesOrderInstance: salesOrderInstance]
        }
	}
}
