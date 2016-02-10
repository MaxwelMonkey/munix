<%@ page import="com.munix.Customer" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'customer.label', default: 'Customer')}" />
    <link rel="stylesheet" href="${resource(dir:'css/reports',file:'filter.css')}" />
    <g:javascript library="reports/filter" />
    <title>Sales Summary</title>
    <style>
    	tr.flagged td, tr.flagged td a {
    		color:red !important;
    		
    	}
    </style>
</head>
<body>
  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
    <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
  </div>
  <div class="body">
    <h1>Sales Summary for ${customer.name}</h1>
    <g:if test="${flash.message}">
      <div class="message">${flash.message}</div>
    </g:if>
      <h2>Filters</h2>
      
    <div id="search" style="width:700px;">
      <g:form controller="customer" action="salesSummary" >
      	<input type="hidden" name="id" value="${params.id}"/>
        <table>
          <tr class="prop">
            <td class="name">Date</td>
            <td class="value">
	          From <g:datePicker name="dateFrom" precision="day" noSelection="['': '']" value="${params.dateFrom}"/>
	          To <g:datePicker name="dateTo" precision="day" noSelection="['': '']" value="${params.dateTo}"/>
            </td>
          </tr>
		<g:render template="/salesReport/multicheckFilter" model="${['label':'Discount Type','name':'discountType','field':'discountType.id','list':com.munix.DiscountType.findAll([sort:'identifier'])]}"/>
		<g:each in="${params.discountType?.id}" var="dt">
			<script>$("#discountType${dt}").attr("checked","true");</script>
		</g:each>
		<g:if test="${params.discountType?.id?.size() == com.munix.DiscountType.count()}">
			<script>$("#discountTypeCheckAll").attr("checked","true");</script>
		</g:if>
          <tr class="prop">
            <td class="name">Product Code</td>
            <td class="value"><g:textField name="product" value="${params?.product}"/></td>
          </tr>
        </table>
        <div>
          <input type="submit" class="button" value="Filter"/>
        </div>
      </g:form>
    </div>
    <div class="list">
      <table>
        <thead>
        	<tr>
          <th>Date</th>
          <th>Reference #</th>
          <th>Discount Type</th>
          <th>Product Code</th>
          <th>Product Description</th>
          <th>Quantity</th>
          <th>Price</th>
          <th>Amount</th>
          <th>Discount</th>
          <th>Net Amount</th>
          </tr>
        </thead>
        <tbody>
        <g:set var="total" value="${0.00}"/>
        <g:each in="${result.sort{it.date}}" status="i" var="sd">
          <tr class="${(i % 2) == 0 ? 'odd' : 'even'} <g:if test="${sd.flag}" >flagged</g:if>">
           	<td><g:formatDate date="${sd.date}" format="MM/dd/yyyy"/></td>
          	<td>
          		<g:if test="${sd.type=='Credit Memo'}">
          		<g:link controller="creditMemo" action="show" id="${sd.reference.id}">${sd.reference}</g:link>
          		</g:if>
          		<g:else>
          		<g:link controller="salesDelivery" action="show" id="${sd.reference.id}">${sd.reference}</g:link>
          		</g:else>
          	</td>
          	<td>${sd.discountType}</td>
          	<td><g:link controller="product" action="show" id="${sd.productCode.id}">${sd.productCode}</g:link></td>
          	<td>${sd.productDescription}</td>
          	<td>${sd.qty}</td>
          	<td>${sd.price}</td>
          	<td>${sd.amount}</td>
			<td>${sd.discount}</td>
			<td>${String.format('%,.2f',sd.netAmount)}</td>
	        <g:set var="total" value="${total+sd.netAmount}"/>
          </tr>
        </g:each>

        </tbody>
		<tfoot>
			<tr>
				<th colspan='8'></th>
	          <th><strong>Total</strong></th>
	          <th><strong>${String.format('%,.2f',total)}</strong></th>
			</tr>
		</tfoot>
      </table>
    </div>
    
    <div class="buttons">
      <g:form>
        <g:hiddenField name="id" value="${customerInstance?.id}" />
       	  <span class="button"><g:actionSubmit class="show" action="show" value="${message(code: 'default.button.show.label', default: 'Back to Customer')}" /></span>
      </g:form>
    </div>

  </div>
</body>
</html>
