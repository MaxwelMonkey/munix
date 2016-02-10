
<%@ page import="com.munix.Product" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <title><g:message code="default.sellingHistory.label" default="Product Selling History" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLinkTo(dir: '')}"><g:message code="home" default="Home" /></a></span>
        </div>
        <div class="body">
            <h1><g:message code="default.sellingHistory.label" default="Product Selling History" /></h1>
            <g:if test="${flash.message}">
              <div class="message">${flash.message}</div>
            </g:if>
            <g:if test="${flash.error}">
              <div class="errors">${flash.error}</div>
            </g:if>
            <g:form>
                <g:set var="prod" value="${productInstance}" />
                <div class="dialog">
                    <table>
                        <tbody>
                            <tr class="prop">
                                <td valign="top" class="name"><g:message code="sellingHistory.identifier.label" default="Identifier" /></td>
                                <td valign="top" class="value">${prod?.identifier}</td>
                            </tr>
							<tr class="prop">
                                <td valign="top" class="name"><g:message code="sellingHistory.description.label" default="Description" /></td>
                                <td valign="top" class="value">${prod?.description}</td>
                            </tr>
                                            
                        </tbody>
                    </table>
                </div>
                <div class="subTable">
    				<table>
        				<thead>
        					<th class="center">Date Created</th>
        				    <th class="center">Date Approved</th>
          					<th class="center">Delivery</th>
          					<th class="center">Customer</th>
          					<th class="center">Net Item Discount</th>
          					<th class="center">Discounted Item Discount</th>
          					<th class="center">Quantity</th>
          					<th class="center">Selling Price</th>
        				</thead>
        				<tbody>
        					<g:each in="${approvedSalesDeliveries.sort{it.delivery?.dateApproved}}" var="item">
          						<tr class="prop">
          							<td class="center"><g:formatDate date="${item?.delivery?.date}" format="MMMM dd,yyyy" /></td>
            						<td class="center"><g:formatDate date="${item?.delivery?.dateApproved}" format="MMMM dd,yyyy" /></td>
            						<td class="center"><g:link controller="salesDelivery" action="show" id="${item?.delivery?.id}">${item?.delivery}</g:link></td>
           							<td class="left">${item?.delivery?.customer?.toString()}</td>
           							<td class="right">${item?.delivery?.invoice?.formatDiscountLabel()}</td>
           							<td class="right">${item?.delivery?.invoice?.formatNetDiscountLabel()}</td>
           							<td class="right">${item?.qty}</td>
           							<td class="right">${item?.formatPrice()}</td>
          						</tr>
        					</g:each>
        				</tbody>
    				</table>
				</div>

            </g:form>
        <div class="paginateButtons">
      		<g:paginate total="${deliveriesTotal}" params="${params}"/>
    	</div>
        </div>
    </body>
</html>
