<html>
  <head>
    <title><g:layoutTitle default="Munix Information System" /></title>
    <link rel="stylesheet" href="${resource(dir:'css',file:'main.css')}" />
    <link rel="stylesheet" href="${resource(dir:'css',file:'print.css')}" />
    <link rel="stylesheet" href="${resource(dir:'css',file:'filtergrid.css')}" media="screen" />

    <link rel="stylesheet" href="${resource(dir:'css',file:'superfish.css')}" media="screen" />
    <link rel="shortcut icon" href="${resource(dir:'images',file:'favicon.ico')}" type="image/x-icon" />
  <g:javascript src="jquery-1.5.min.js" />
  <g:javascript src="superfish.js" />
  <g:javascript src="hoverIntent.js" />
  <g:javascript library="application" />
  <g:javascript library="prototype" />
  <g:javascript src="tablefilter.js" />
  <g:javascript>

    function sortReport(field){
    	document.frmParams.sort.value = field;
    	var direction = document.frmParams.direction.value;
    	document.frmParams.direction.value = direction=="asc"?"desc":"asc";
    	document.frmParams.submit();
    }
  </g:javascript>
  <g:layoutHead />

</head>
<body>
	<g:form name="frmParams" method="post">
		<g:each in="${params.keySet()}" status="i" var="key">
			<g:if test="${params[key] instanceof String && key != 'sort' && key != 'direction'}">
				<input type="hidden" name="${key}" value="${params[key]}">
			</g:if>
			<g:if test="${params[key] instanceof Date}">
				<input type="hidden" name="${key}" value="date.struct">
			</g:if>
			<g:if test="${params[key] instanceof String[]}">
				<g:each in="${params[key]}" var="param" status="j">
					<input type="hidden" name="${key}" value="${params[key][j]}">
				</g:each>
			</g:if>
		</g:each>
		<input type="hidden" name="sort" value="${params['sort']}">
		<input type="hidden" name="direction" value="${params['direction']}">
	</g:form>
	<div class="body">
	<div class="reportHeader">
	<b>${com.munix.Company.get(1)?.name}</b><br>
	<g:if test="${params.reportType=='date' || params.reportType=='series' || params.reportType=='itemsSold'|| params.reportType=='itemsSoldDetailed'}">
    Sales Report for the period of <g:formatDate format="MMMM d, yyyy" date="${params.dateFrom}"/> to <g:formatDate format="MMMM d, yyyy" date="${params.dateTo}"/><br>
    </g:if>
	<g:if test="${params.reportType=='product' || params.reportType=='customer'}">
	Pending Sales Orders for the period of <g:formatDate format="MMMM d, yyyy" date="${params.dateFrom}"/> to <g:formatDate format="MMMM d, yyyy" date="${params.dateTo}"/><br>
	</g:if>
	<g:if test="${params.reportType=='purchaseProduct' || params.reportType=='purchaseSupplier'}">
	Pending Purchase Orders for the period of <g:formatDate format="MMMM d, yyyy" date="${params.dateFrom}"/> to <g:formatDate format="MMMM d, yyyy" date="${params.dateTo}"/><br>
	</g:if>
	<g:if test="${params.reportType=='directPaymentSeries' || params.reportType=='directPaymentCustomer' || params.reportType=='directPaymentType'}">
	Direct Payment Report for the period of <g:formatDate format="MMMM d, yyyy" date="${params.dateFrom}"/> to <g:formatDate format="MMMM d, yyyy" date="${params.dateTo}"/><br>
	</g:if>
	<g:if test="${params.reportType=='productListSelling' || params.reportType=='productInventoryList' || params.reportType=='productListCost'}">
	Product List as of <g:formatDate  format="MMMM d, yyyy" date="${new Date()}"/><br>
	</g:if>
	<g:if test="${params.reportType=='productPricelist'}">
	${com.munix.Company.get(1)?.landline}<br>
	Pricelist as of <g:formatDate  format="MMMM d, yyyy" date="${new Date()}"/><br>
	</g:if>
	<g:if test="${params.reportType=='inventoryReport'}">
	Inventory Report as of <g:formatDate  format="MMMM d, yyyy" date="${new Date()}"/><br>
	</g:if>
    <g:if test="${params.reportType=='directPaymentSalesAgent'}">
        <g:formatDate format="MMMM d, yyyy" date="${params.dateFrom}"/> to <g:formatDate format="MMMM d, yyyy" date="${params.dateTo}"/><br>
    </g:if>
	<g:if test="${params.reportType=='detailedSalesReport'}">
	Net Sales Report from <g:formatDate format="MMMM d, yyyy" date="${params.dateFrom}"/> to <g:formatDate format="MMMM d, yyyy" date="${params.dateTo}"/><br>
    </g:if>
	<g:if test="${params.reportType=='salesReportCustomerDiscountType'}">
	From <g:formatDate format="MMMM d, yyyy" date="${params.dateFrom}"/> to <g:formatDate format="MMMM d, yyyy" date="${params.dateTo}"/><br>
	Total Sales Report by Customer and Discount Type<br>
    </g:if>
	<g:if test="${params.reportType=='salesReportByMonthComparison'}">
	From <g:formatDate format="MMMM d, yyyy" date="${params.dateFrom}"/> to <g:formatDate format="MMMM d, yyyy" date="${params.dateTo}"/><br>
	Total Sales Report by Month<br>
    </g:if>
	<g:if test="${params.controller=='customerAuditReport'}">
	From <g:formatDate format="MMMM d, yyyy" date="${params.dateFrom}"/> to <g:formatDate format="MMMM d, yyyy" date="${params.dateTo}"/><br>
    </g:if>
	<g:if test="${params.reportType=='customerBalances'}">
	Net Customer Balances as of <g:formatDate  format="MMMM d, yyyy" date="${new Date()}"/><br>
    </g:if>
	<g:if test="${params.reportType=='purchaseInvoiceSupplier' || params.reportType=='purchaseInvoiceSupplierDetailed'}">
        Total Purchases as of date<br/>
        <g:if test="${params.dateType=='invoice'}">Invoice Date: <g:formatDate format="MMMM d, yyyy" date="${params?.invoiceDateFrom}"/> - <g:formatDate format="MMMM d, yyyy" date="${params?.invoiceDateTo}"/><br/></g:if>
        <g:if test="${params.dateType=='delivery'}">Delivery Date: <g:formatDate format="MMMM d, yyyy" date="${params?.deliveryDateFrom}"/> - <g:formatDate format="MMMM d, yyyy" date="${params?.deliveryDateTo}"/><br/></g:if>
	</g:if>
    Prepared by ${username}, <g:formatDate format="MMMM d, yyyy hh:mm a" date="${new Date()}"/>
    </div>
	<g:layoutBody />
	</div>
</body>
</html>