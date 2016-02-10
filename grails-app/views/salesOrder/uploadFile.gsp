<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'salesOrder.label', default: 'SalesOrder')}" />
  <title><g:message code="default.show.label" args="[entityName]" /></title>
</head>
<body>
  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
    <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
    <span class="menuButton"><g:link class="create" action="create">Create</g:link></span>
    <span class="menuButton"><g:link class="create" action="upload">Create (from SO Form)</g:link></span>
    <span class="menuButton"><g:link class="create" action="excelForm">Download SO Form</g:link></span>
  </div>
  <div class="body">
		The following items will be added to the Sales Order:
		<g:if test="${duplicates?.keySet()?.size()>0}">
		<div style="color:red">
			Warning: The following Products were found in the Sales Order Form but already exist in the Sales Order. These Products will not be updated:<br>
			<g:each in="${duplicates.keySet()}" var="i">
				${duplicates[i].identifier}<br>
			</g:each>
		</div>
		</g:if>
	<div id="result">
	<g:if  test="${error}">
		${error}
	</g:if>
	<g:else>
	      <div class="subTable">
	        <div style="clear:both" ></div>
	             <h2>Discounted Items</h2>
	             <g:set var="discountedItemsTotal" value="${0.0}"/>
	           <table  id="discountedItems" class="soItems">
	              <thead>
	                  <tr>
	                      <th>Identifier</th>
	                      <th>Description</th>
	                      <th>Part Number</th>
	                      <th>Package Details</th>
	                      <th width="15%">Price</th>
	                      <th width="15%">Final Price</th>
	                      <th width="15%">Quantity</th>
	                      <th width="15%">Amount</th>
	                  </tr>
	              </thead>
	              <tbody>
	                  <g:each in="${items}" status="i" var="orderItem">
	                    <g:if test="${!orderItem?.isNet && !duplicates.containsKey(orderItem.product.id)}">
	                      <tr>
	                        <td class="identifier">${orderItem?.product?.identifier}</td>
	                        <td class="identifier">${orderItem?.product?.partNumber}</td>
	                        <td class="description">${orderItem?.product?.description}</td>
	                        <td class="packageDetails">${orderItem?.product?.formatPackageDetails()}</td>
	                        <td class="right price">${orderItem?.formatPrice()}</td>
	                        <td class="right price">${orderItem?.formatFinalPrice()}</td>
	                        <td class="right price">${orderItem?.formatQty()}</td>
	                        <td class="right amount">${orderItem?.formatAmount()}</td>
				             <g:set var="discountedItemsTotal" value="${discountedItemsTotal + orderItem?.computeAmount()}"/>
	                      </tr>
	                    </g:if>
	                  </g:each>
	              </tbody>
	              <tfoot>
	                <tr class="total">
	                    <td><strong>Total</strong></td>
	                    <td></td>
	                    <td></td>
	                    <td></td>
	                    <td></td>
	                    <td></td>
	                    <td></td>
	                    <td class="unDiscountedTotal right">
	                        <strong>Php ${String.format('%,.2f',discountedItemsTotal)}</strong>
	                    </td>
	                </tr>
	            </table>
	           </table>
	
	        <div style="clear:both" ></div>
	             <h2>Net Price Items</h2>
	             <g:set var="netItemsTotal" value="${0.0}"/>
	           <table id="netItems" class="soItems">
	              <thead>
	                  <tr>
	                      <th>Identifier</th>
	                      <th>Description</th>
	                      <th>Part Number</th>
	                      <th>Package Details</th>
	                      <th width="15%">Price</th>
	                      <th width="15%">Final Price</th>
	                      <th width="15%">Quantity</th>
	                      <th width="15%">Amount</th>
	                  </tr>
	              </thead>
	              <tbody>
	                  <g:each in="${items}" status="i" var="orderItem">
	                    <g:if test="${orderItem?.isNet && !duplicates.containsKey(orderItem.product.id)}">
	                      <tr>
	                        <td class="identifier">${orderItem?.product?.identifier}</td>
	                        <td class="identifier">${orderItem?.product?.partNumber}</td>
	                        <td class="description">${orderItem?.product?.description}</td>
	                        <td class="packageDetails">${orderItem?.product?.formatPackageDetails()}</td>
	                        <td class="right price">${orderItem?.formatPrice()}</td>
	                        <td class="right price">${orderItem?.formatFinalPrice()}</td>
	                        <td class="right price">${orderItem?.formatQty()}</td>
	                        <td class="right amount">${orderItem?.formatAmount()}</td>
				             <g:set var="netItemsTotal" value="${netItemsTotal + orderItem?.computeAmount()}"/>
	                      </tr>
	                    </g:if>
	                  </g:each>
	              </tbody>
	              <tfoot>
	                <tr class="total">
	                    <td><strong>Total</strong></td>
	                    <td></td>
	                    <td></td>
	                    <td></td>
	                    <td></td>
	                    <td></td>
	                    <td></td>
	                    <td class="netTotal right">
	                        <strong>Php ${String.format('%,.2f',netItemsTotal)}</strong>
	                    </td>
	                </tr>
	                
	              </tfoot>
	            </table>
	      </div>
	</g:else>
	</div>
    <div class="buttons">
      <g:form action="uploadUpdate" method="post">
          <g:hiddenField name="id" value="${salesOrderInstance?.id}" />
          <span class="button"><input type="submit" value="Save Changes"></span>
          <span class="button"><g:link controller="salesOrder" action="show" id="${salesOrderInstance?.id}">Back</g:link></span>
		</g:form>
	</div>	
  </div>
	<script>
		if(window.parent && window.parent.document.getElementById("uploadedItems")){
			window.parent.document.getElementById("uploadedItems").innerHTML = document.getElementById("result").innerHTML;
		}
	</script>
</body>
</html>