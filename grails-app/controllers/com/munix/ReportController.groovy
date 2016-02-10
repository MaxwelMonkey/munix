package com.munix

class ReportController {

    ReportService reportService
    
    def where, whereParams

    def index = {
        def reportFileName = reportService.reportFileName("${params.file}")
        def reportFile = servletContext.getResource(reportFileName)
        if(params.id != null) {
            params.id = Long.parseLong(params.id)
        }
        if(reportFile == null){
            throw new FileNotFoundException("""\"${reportFileName}\" file must be in
            reports repository.""")
        }
        Date dateToday = new Date()
        def fileName = "${params.file}-${dateToday.format('MMddyyyy')}"
        // Call the ReportService to invoke the reporting engine
        switch(params.format){
            case "PDF":
            createPdfFile(reportService.generateReport(reportFile,
                    reportService.PDF_FORMAT,params).toByteArray(),fileName)
            break
            case "HTML":
            render(text:reportService.generateReport(reportFile,
                    reportService.HTML_FORMAT,params),contentType:"text/html")
            break
            case "CSV":
            render(text:reportService.generateReport(reportFile,
                    reportService.CSV_FORMAT,params),contentType:"text")
            break
            case "XLS":
            createXlsFile(reportService.generateReport(reportFile,
                    reportService.XLS_FORMAT,params).toByteArray(),params.file)
            break
            case "RTF":
            createRtfFile(reportService.generateReport(reportFile,
                    reportService.RTF_FORMAT,params).toByteArray(),params.file)
            break
            case "XML":
            render(text:reportService.generateReport(reportFile,
                    reportService.XML_FORMAT,params),contentType:"text")
            break
            case "TXT":
            render(text:reportService.generateReport(reportFile,
                    reportService.TEXT_FORMAT,params),contentType:"text")
            break
            default:
            throw new Exception("Invalid format")
            break
        }

    }
    /**
     * Output a PDF response
     */
    def createPdfFile = { contentBinary, fileName ->
        response.setHeader("Content-disposition", "attachment; filename=" +
            fileName + ".pdf");
        response.contentType = "application/pdf"
        response.outputStream << contentBinary
    }
    /**
     * Output an Excel response
     */
    def createXlsFile = { contentBinary, fileName ->
        response.setHeader("Content-disposition", "attachment; filename=" +
            fileName + ".xls");
        response.contentType = "application/vnd.ms-excel"
        response.outputStream << contentBinary
    }
    /**
     * Output an RTF response
     */
    def createRtfFile = { contentBinary, fileName ->
        response.setHeader("Content-disposition", "attachment; filename=" +
            fileName + ".rtf");
        response.contentType = "application/rtf"
        response.outputStream << contentBinary
    }

    def create = {
        render(view:'create')
    }
    
    // new functions
    def appendWhereQuery (list, columnName, where, whereParams) {
		if(list instanceof String){
            where += " and ${columnName} = ?"
        	whereParams.add(list)
		}else{
            def idsString = ""
        	list.each{
            	if(idsString!="") idsString += ","
            	idsString += "?"
        	    whereParams.add(it)
        	}
            where += " and ${columnName} in (${idsString})"
		}
		return where
	}
	
	def buildWhere(params){
		where = "where 1=1"
        whereParams = []
        if(params.id){
        	where = appendWhereQuery (params.id, "product.id", where, whereParams)
        }
		if(params.status){
    		if(params.status!=""){
	    		where += " and status=?"
	    		whereParams.add(params.status)
    		}
    	}
		if(params.currency){
    		if(params.currency!=""){
	    		where += " and currency=?"
	    		whereParams.add(params.currency)
    		}
    	}
		if(params.isNet){
    		if(params.isNet!=""){
	    		where += " and is_net=true"
    		}
    	}
    	if(params.component){
    		if(params.component!=""){
    			where += " and exists (select 1 from product_component c where c.COMPONENT_ID = product.ID)"
    		}
    	}
		if(params.forSale){
    		if(params.forSale!=""){
	    		where += " and is_for_sale=true"
    		}
    	}
		if(params.forAssembly){
    		if(params.forAssembly!=""){
	    		where += " and is_for_assembly=true"
    		}
    	}
    	if(params.supplier?.id){
    		if(params.supplier.id instanceof String){
    			where += " and exists (select 1 from supplier_item c where c.PRODUCT_ID = product.ID AND c.SUPPLIER_ID = ?)"
            	whereParams.add(params.supplier.id)
    		}else{
                def idsString = ""
            	params.supplier.id.each{
                	if(idsString!="") idsString += ","
                	idsString += "?"
            	    whereParams.add(it)
            	}
    			where += " and exists (select 1 from supplier_item c where c.PRODUCT_ID = product.ID AND c.SUPPLIER_ID in (${idsString}))"
    		}
    	}
    	if(params.customer?.id){
    		where = appendWhereQuery (params.customer.id, "customer_id", where, whereParams)
    	}
    	if(params.type?.id){
    		where = appendWhereQuery (params.type.id, "type_id", where, whereParams)
    	}
       	if(params.category?.id){
       		where = appendWhereQuery (params.category.id, "category_id", where, whereParams)
    	}
       	if(params.subcategory?.id){
       		where = appendWhereQuery (params.subcategory.id, "subcategory_id", where, whereParams)
    	}
       	if(params.brand?.id){
       		where = appendWhereQuery (params.brand.id, "brand_id", where, whereParams)
    	}
       	if(params.model?.id){
       		where = appendWhereQuery (params.model.id, "model_id", where, whereParams)
    	}
		if(params.itemType?.id){
			where = appendWhereQuery (params.itemType.id, "item_type_id", where, whereParams)
    	}
		println params.balance
		if(params.balance!='null' && params.warehouse?.id){
			if(params.warehouse.id instanceof String){
				where += " and exists (select 1 from stock where stock.PRODUCT_ID = product.ID AND stock.WAREHOUSE_ID = ?"
				whereParams.add(params.warehouse.id)
			}else{
	            def idsString = ""
            	params.warehouse.id.each{
                	if(idsString!="") idsString += ","
                	idsString += "?"
            	    whereParams.add(it)
            	}
				where += " and exists (select 1 from stock where stock.PRODUCT_ID = product.ID AND stock.WAREHOUSE_ID in (${idsString})"
			}
			where += " AND stock.QTY ${params.balance})"
		}
	}
}
