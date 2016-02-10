
<%@ page import="com.munix.SalesDelivery" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'salesDelivery.label', default: 'SalesDelivery')}" />
  <title><g:message code="default.list.label" args="[entityName]" /></title>
  <link rel="stylesheet" href="${resource(dir:'css',file:'jquery.ui.all.css')}" />
  <g:javascript src="numbervalidation.js" />
  <g:javascript src="jquery.ui.core.js" />
  <g:javascript src="jquery.ui.datepicker.js" />
  <g:javascript src="generalmethods.js" />
  <g:javascript src="calendarStructTemplate.js" />
  <script>
    var $=jQuery.noConflict();
    $(document).ready(function(){
    	  $("#searchInvoice").ForceNumericOnly(false)
    	  setCalendarStruct($("#searchDateFrom"), $("#searchDateFrom_value"))
    	  setCalendarStruct($("#searchDateTo"), $("#searchDateTo_value"))

			doneNa=0;
	  	 $(".reviewerCheckbox").each(function(){
	  	 	if(doneNa<50000){
	  	 		doneNa++;
		  		var reviewerId = $(this).attr("reviewer");
		  		var salesDeliveryId = $(this).attr("salesDelivery");
		  		$.ajax({
				type: "POST",
				url: "${resource(dir:'salesDelivery',file:'reviewerCheckbox')}",
				data: { reviewerId: reviewerId, salesDeliveryId:salesDeliveryId }
				})
				.done(function( msg ) {
					$(".reviewerCheckbox_"+reviewerId+"_"+salesDeliveryId).html(msg);
			    	if(reviewerId!="${userId}"){
			    		$(".reviewerCheckbox_"+reviewerId+"_"+salesDeliveryId+" input[type=checkbox]").attr("disabled","disabled");
			    	}
				});
			}
	  	});
	  	
	  	$("tr.salesDeliveryRow td.col").click(function(){ 
			window.location='${createLink(uri:'/')}salesDelivery/show/'+$(this).parent().attr("salesDeliveryId");
	  	});

    });
    
    function saveReviewerCheckbox(elem){
    	var reviewerId=$(elem).attr("reviewer");
    	var salesDeliveryId=$(elem).attr("salesDelivery");
    	var checked = elem.checked?"Y":"N"
  		$.ajax({
		type: "POST",
		url: "${resource(dir:'salesDelivery',file:'saveReviewerCheckbox')}",
		data: { reviewerId: reviewerId, salesDeliveryId:salesDeliveryId, checked:checked }
		})
		.done(function( msg ) {
		});
    }
    
    function toggleCheckbox(checked, column){
    	if(checked)
    		$("."+column+"Column").show();
    	else
    		$("."+column+"Column").hide();
    }
    

  </script>
</head>
<body>
  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
  </div>
  <div class="body">
  	<calendar:resources lang="en" theme="aqua"/>
    <h1><g:message code="default.list.label" args="[entityName]" /></h1>
    <div id="search">
      <g:form controller="salesDelivery" action="list" >
        <table>
          <tr>
            <td class="name">ID</td>
            <td class="value"><g:textField name="searchIdentifier" value="${params?.searchIdentifier}"/></td>
          </tr>
          <tr>
            <td class="name">Sales Order</td>
            <td class="value"><g:textField name="searchInvoice" id="searchInvoice" value="${params?.searchInvoice}"/></td>
          </tr>
          <tr>
            <td class="name">Customer ID</td>
            <td class="value"><g:textField name="searchCustomerId" value="${params?.searchCustomerId}"/></td>
          </tr>
          <tr>
            <td class="name">Customer Name</td>
            <td class="value"><g:textField name="searchCustomerName" value="${params?.searchCustomerName}"/></td>
          </tr>
          <tr>
            <td class="name">Date</td>
            <td class="value"">
              <calendar:datePicker name="searchDateFrom"  years="2009,2030" value="${dateMap.searchDateFrom}"/>
           	  to
           	  <calendar:datePicker name="searchDateTo"  years="2009,2030" value="${dateMap.searchDateTo}"/>
           	</td>
          </tr>
          <tr>
            <td class="name">Customer Type</td>
            <td class="value"><g:select name="searchCustomerType" noSelection="${['':'Select One...']}" from="${com.munix.CustomerType.list().sort{it.description}}" optionKey="description" optionValue="description" value="${params?.searchCustomerType}"/></td>
          </tr>
          <tr>
            <td class="name">Discount Type</td>
            <td class="value"><g:select name="searchDiscountType" noSelection="${['':'Select One...']}" from="${com.munix.DiscountType.list().sort{it.description}}" optionKey="description" optionValue="description" value="${params?.searchDiscountType}"/></td>
          </tr>
          <tr>
            <td class="name">Delivery Type</td>
            <td class="value"><g:select name="searchDeliveryType" noSelection="${['':'Select One...']}" from="${['Deliver','Pickup']}" value="${params?.searchDeliveryType}"/></td>
          </tr>
          <tr>
            <td class="name">Status</td>
            <td class="value"><g:select name="searchStatus" noSelection="${['':'All statuses except Cancelled and Paid']}" from="${['All statuses', 'Unapproved','Unpaid','Taken','Closed','Cancelled','Paid']}" value="${params?.searchStatus}"/></td>
          </tr>

        </table>
        <div>
          <input type="submit" class="button" value="Search"/>
        </div>

      </g:form>
    </div>
    <g:if test="${flash.message}">
      <div class="message">${flash.message}</div>
    </g:if>
    <br/>
    <input type="checkbox" checked="true" style='cursor:pointer;height:auto;' onclick="toggleCheckbox(this.checked, 'delivery');" name="deliveryCheckbox"> Delivery
    <input type="checkbox" checked="true" style='cursor:pointer;height:auto;' onclick="toggleCheckbox(this.checked, 'tripTicket');" name="tripTicketCheckbox"> Trip Ticket
    <input type="checkbox" checked="true" style='cursor:pointer;height:auto;' onclick="toggleCheckbox(this.checked, 'counterReceipt');" name="counterReceiptCheckbox"> Counter Receipt
    <div class="list">
      <table>
        <thead>
          <tr>
        <g:sortableColumn class="center" property="id" title="${message(code: 'salesDelivery.id.label', default: 'Id')}" params="${params}"/>
        <g:sortableColumn class="center" property="invoice" title="Sales Order" params="${params}"/>
        <g:sortableColumn class="center" property="customer" title="${message(code: 'salesDelivery.customer.label', default: 'Customer')}" params="${params}"/>
        <g:sortableColumn class="center" property="customer.type" title="${message(code: 'salesDelivery.customer.type.label', default: 'Customer Type')}" params="${params}"/>
        <g:sortableColumn class="center" property="invoice.discountType" title="${message(code: 'salesDelivery.invoice.discountType.label', default: 'Discount Type')}" params="${params}"/>
        <g:sortableColumn class="center" property="salesDeliveryNumber" title="${message(code: 'salesDelivery.salesDeliveryNumber.label', default: 'Sales Invoice #')}" params="${params}"/>
        <g:sortableColumn class="center" property="deliveryReceiptNumber" title="${message(code: 'salesDelivery.deliveryReceiptNumber.label', default: 'Delivery Receipt #')}" params="${params}"/>
        <g:sortableColumn class="center" property="deliveryType" title="${message(code: 'salesDelivery.deliveryType.label', default: 'Delivery Type')}" params="${params}"/>
        <th class="center deliveryColumn">Delivery</th>
        <th class="center tripTicketColumn">Trip Ticket</th>
        <th class="center counterReceiptColumn">Counter Receipt</th>
        <th class="center">Amount</th>
        <th class="center">Balance</th>
        <g:sortableColumn class="center" property="status" title="${message(code: 'salesDelivery.status.label', default: 'Status')}" params="${params}"/>
        <g:sortableColumn class="center" property="date" title="${message(code: 'salesDelivery.date.label', default: 'Date')}" params="${params}"/>
        <th class="center">Print Count</th>
        <g:each in="${reviewerList}" status="i" var="reviewer">
        <th class="reviewer">${reviewer.initials()}</th>
        </g:each>
        </tr>
        </thead>
        <tbody>
        <g:each in="${salesDeliveryInstanceList}" status="i" var="salesDeliveryInstance">
          <tr class="salesDeliveryRow ${(i % 2) == 0 ? 'odd' : 'even'}" salesDeliveryId="${salesDeliveryInstance.id}">
            <td id="rowSalesDeliveryId${i}" class="center col">${salesDeliveryInstance}</td>
            <td class="center col"><g:link controller="salesOrder" action="show" id="${salesDeliveryInstance?.invoice?.id}">${fieldValue(bean: salesDeliveryInstance, field: "invoice")}</g:link></td>
          <td class="col">${fieldValue(bean: salesDeliveryInstance, field: "customer")}</td>
          <td class="col">${fieldValue(bean: salesDeliveryInstance, field: "customer.type.description")}</td>
          <td class="col">${fieldValue(bean: salesDeliveryInstance, field: "invoice.discountType.description")}</td>
          <td class="col">${salesDeliveryInstance?.salesDeliveryNumber}</td>
          <td class="col">${fieldValue(bean: salesDeliveryInstance, field: "deliveryReceiptNumber")}</td>
          <td class="col">${fieldValue(bean: salesDeliveryInstance, field: "deliveryType")}</td>
          <td class="col deliveryColumn">
          <g:if test="${salesDeliveryInstance?.waybill == null}">
<g:link controller="directDelivery" action="show" id="${salesDeliveryInstance?.directDelivery?.id}">${fieldValue(bean: salesDeliveryInstance, field: "directDelivery")}</g:link>
          </g:if>
          <g:else>
<g:link controller="waybill" action="show" id="${salesDeliveryInstance?.waybill?.id}">${fieldValue(bean: salesDeliveryInstance, field: "waybill")}</g:link>
          </g:else>
          </td>
          <td class="col tripTicketColumn">
          <g:if test="${salesDeliveryInstance?.waybill == null}">
            <g:link controller="tripTicket" action="show" id="${salesDeliveryInstance?.directDelivery?.tripTicket?.tripTicket?.id}">${salesDeliveryInstance?.directDelivery?.tripTicket}</g:link>
          </g:if>
          <g:else>
            <g:link controller="tripTicket" action="show" id="${salesDeliveryInstance?.waybill?.tripTicket?.tripTicket?.id}">${salesDeliveryInstance?.waybill?.tripTicket}</g:link>
          </g:else>
          </td>
          <td class="col counterReceiptColumn">
          <g:each in="${salesDeliveryInstance?.counterReceipts}" var="cr">
            <g:link controller="counterReceipt" action="show" id="${cr?.id}">${cr}</g:link><br/>
          </g:each>
          </td>
          <g:if test="${salesDeliveryInstance.isCancelled()}">
            <td class="right col">PHP 0.00</td>
            <td class="right col">PHP 0.00</td>
          </g:if>
          <g:else>
            <td class="right col">PHP <g:formatNumber number="${salesDeliveryInstance?.computeTotalAmount()}" format="###,##0.00" /></td>
            <td class="right col">PHP <g:formatNumber number="${salesDeliveryInstance?.computeAmountDue()}" format="###,##0.00" /></td>
          </g:else>
          <td class="col">${fieldValue(bean: salesDeliveryInstance, field: "status")}</td>
          <td class="center col"><g:formatDate date="${salesDeliveryInstance.date}" format="MM/dd/yyyy"/></td>
          <td class="right col">${salesDeliveryInstance?.printLogs?.size()}</td>
        <g:each in="${reviewerList}" var="reviewer">
        	<td class="reviewer reviewerCheckbox reviewerCheckbox_${reviewer.id}_${salesDeliveryInstance.id}" reviewer="${reviewer.id}" salesDelivery="${salesDeliveryInstance.id}"><img src="${resource(dir:'images',file:'spinner.gif')}"></td>
        </g:each>
          </tr>
        </g:each>
        </tbody>
      </table>
    </div>
    <div class="paginateButtons">
      <g:paginate total="${salesDeliveryInstanceTotal}" params="${params}" />
    </div>
  </div>
</body>
</html>
