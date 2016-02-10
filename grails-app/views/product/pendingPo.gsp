<%@ page import="com.munix.Customer" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'product.label', default: 'Product')}" />
    <link rel="stylesheet" href="${resource(dir:'css/reports',file:'filter.css')}" />
    <g:javascript library="reports/filter" />
    <title>Pending PO</title>
</head>
<body>
  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
    <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
    <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
  </div>
  <div class="body">
    <h1>Pending PO for ${product}</h1>
    <g:if test="${flash.message}">
      <div class="message">${flash.message}</div>
    </g:if>
    <div class="list">
    	<h2>Pending PO</h2>
      <table>
        <thead>
          <tr>
			<th>Date</th>
			<th>Reference #</th>
		    <g:ifAnyGranted role="ROLE_ADMIN_PURCHASING">
	        <th class="right">Price</th>
	        <th class="right">Final Price</th>
	          </g:ifAnyGranted>
	        <th class="right">Quantity</th>
	        <th class="right">Received</th>
	        <th class="right">Remaining</th>
		    <g:ifAnyGranted role="ROLE_ADMIN_PURCHASING">
	        <th class="right">Amount</th>
	          </g:ifAnyGranted>
	      </tr>
        </thead>
        <tbody>
        <g:set var="total" value="${0.00}"/>
        <g:each in="${pendingPo}" status="i" var="poi">
          <tr class="${(i % 2) == 0 ? 'odd' : 'even'}" >
           	<td><g:formatDate date="${poi.po.date}" format="MM/dd/yyyy"/></td>
          	<td><g:link controller="purchaseOrder" action="show" id="${poi.po.id}">${poi.po.formatId()}</g:link></td>
		    <g:ifAnyGranted role="ROLE_ADMIN_PURCHASING">
	          <td class="right">
          	  	${poi?.formatPrice()}
          	  </td>
	          	
	          <td class="right">
	          	${poi?.formatFinalPrice()}
	          </td>
	          </g:ifAnyGranted>
	          <td class="right">${poi?.formatQty()}</td>
	          <td class="right">${poi?.formatReceivedQty()}</td>
	          <td class="right">${poi?.formatRemainingBalance()}</td>
		    <g:ifAnyGranted role="ROLE_ADMIN_PURCHASING">
	          <td class="right">${poi?.formatAmount()}</td>
	          </g:ifAnyGranted>
          </tr>
          <g:set var="total" value="${total + poi.computeRemainingBalance()}"/>
        </g:each>
		<tr class="odd">
			<g:ifAnyGranted role="ROLE_ADMIN_PURCHASING"><td></td><td></td></g:ifAnyGranted>
			<td colspan="4" class="right"><strong>Total</strong></td>
			<td class="right"><strong>${String.format('%,.0f',total)}</strong></td>
			<td/>
		</tr>
        </tbody>
      </table>
    </div>

    <div class="list">
    	<h2>Pending Unapproved PO</h2>
      <table>
        <thead>
          <tr>
			<th>Date</th>
			<th>Reference #</th>
		    <g:ifAnyGranted role="ROLE_ADMIN_PURCHASING">
	        <th class="right">Price</th>
	        <th class="right">Final Price</th>
	        </g:ifAnyGranted>
	        <th class="right">Quantity</th>
	        <th class="right">Received</th>
	        <th class="right">Remaining</th>
		    <g:ifAnyGranted role="ROLE_ADMIN_PURCHASING">
	        <th class="right">Amount</th>
	        </g:ifAnyGranted>
	      </tr>
        </thead>
        <tbody>
        <g:set var="total" value="${0.00}"/>
        <g:each in="${pendingUnapprovedPo}" status="i" var="poi">
          <tr class="${(i % 2) == 0 ? 'odd' : 'even'}" >
           	<td><g:formatDate date="${poi.po.date}" format="MM/dd/yyyy"/></td>
          	<td><g:link controller="purchaseOrder" action="show" id="${poi.po.id}">${poi.po.formatId()}</g:link></td>
		    <g:ifAnyGranted role="ROLE_ADMIN_PURCHASING">
	          <td class="right">
          	  	${poi?.formatPrice()}
          	  </td>
	          	
	          <td class="right">
	          	${poi?.formatFinalPrice()}
	          </td>
	        </g:ifAnyGranted>
	          <td class="right">${poi?.formatQty()}</td>
	          <td class="right">${poi?.formatReceivedQty()}</td>
	          <td class="right">${poi?.formatRemainingBalance()}</td>
		    <g:ifAnyGranted role="ROLE_ADMIN_PURCHASING">
	          <td class="right">${poi?.formatAmount()}</td>
	        </g:ifAnyGranted>
          </tr>
          <g:set var="total" value="${total + poi.computeRemainingBalance()}"/>
        </g:each>
		<tr class="odd">
			<g:ifAnyGranted role="ROLE_ADMIN_PURCHASING"><td></td><td></td></g:ifAnyGranted>
			<td colspan="4" class="right"><strong>Total</strong></td>
			<td class="right"><strong>${String.format('%,.0f',total)}</strong></td>
			<td/>
		</tr>
        </tbody>
      </table>
    </div>
    
    <div class="buttons">
      <g:form>
        <g:hiddenField name="id" value="${product?.id}" />
       	  <span class="button"><g:link action="show" id="${product.id}">${message(code: 'default.button.show.label', default: 'Back to Product')}</g:link></span>
      </g:form>
    </div>

  </div>
</body>
</html>
