<%@ page import="com.munix.StockCard; com.munix.StockCardItem" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:javascript src="jquery.ui.core.js" />
        <title><g:message code="stockCard.show" default="Show StockCard" /></title>
	    <g:javascript src="table/jquery.dataTables.js" />
		<g:set var="warehouses" value="${com.munix.Warehouse.list()}"/>
        <script>
        $(document).ready(function(){
        	$("#stockCardItemTable").dataTable({
				"bPaginate": false, 
				"bSort": false
        	});
        	
        	addWarehousesCheckboxes();
        });
        
        function addWarehousesCheckboxes(){
        	<g:each in="${warehouses}" var="wh">
        	$("#stockCardItemTable_filter").append($("<span style='margin-left:30px'>${wh.identifier}</span>&nbsp;<input type='checkbox' class='warehouseFilter' style='height:auto;' value='${wh.id}'/>"));
        	</g:each>
        	$(".warehouseFilter").click(function(){
        		if(this.checked)
        			$(".warehouseColumn_"+$(this).val()).show();
        		else
        			$(".warehouseColumn_"+$(this).val()).hide();
        	});
        }
        </script>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLinkTo(dir: '')}"><g:message code="home" default="Home" /></a></span>
        </div>
        <div class="body">
            <h1><g:message code="stockCard.show" default="Current Stock" /></h1>
            <g:if test="${flash.message}">
            <div class="message"><g:message code="${flash.message}" args="${flash.args}" default="${flash.defaultMessage}" /></div>
            </g:if>
            
            <div class="dialog">
      			<table>
        			<tbody>

          				<tr class="prop">
            				<td valign="top" class="name"><g:message code="product.identifier.label" default="Identifier" /></td>
        					<td valign="top" class="value">${stockCardInstance.product}</td>
        					<td valign="top" class="name"></td>
        					<td valign="top" class="value"></td>
        				</tr>
        				<tr class="prop">
            				<td valign="top" class="name"><g:message code="product.description.label" default="Description" /></td>
        					<td valign="top" class="value">${stockCardInstance.product.description}</td>
        					<td valign="top" class="name"></td>
        					<td valign="top" class="value"></td>
        				</tr>
        				
       				</tbody>
    			</table>
   			</div>
        
            <div class="subTable">
            	<table>
                	<tr>
                		<g:each in="${stocks}" var="stock">
                			<td>${stock.warehouse}</td>
                		</g:each>
                	</tr>
                	<tr>
                		<g:each in="${stocks}" var="stock">
                			<td>${stock.qty}</td>
                		</g:each>
                	</tr>
               	</table>
            </div>
            <g:form action="show" method="post">
                <g:hiddenField name="id" value="${stockCardInstance?.id}" />
		          From <g:datePicker name="dateFrom" precision="day" noSelection="['': '']" value="${dateFrom}"/>
		          To <g:datePicker name="dateTo" precision="day" noSelection="['': '']"  value="${dateTo}"/>
		          <input type="submit" value="Apply"/>
                <div class="subTable">
                    <table id="stockCardItemTable">
                        <thead>
                            <th style="width:150px">Date Created</th>
                            <th style="width:80px">Post Date</th>
                            <th style="width:200px">Type</th>
                            <th style="width:80px">ID</th>
                            <th>Supplier/Customer</th>
							<g:ifAnyGranted role="ROLE_SUPER">
                            <th>Cost (Foreign)</th>
                            <th>Cost</th>
                            </g:ifAnyGranted>
							<g:ifAnyGranted role="ROLE_SALES">
                            <th>Selling Amount</th>
                            </g:ifAnyGranted>
                            <th>Warehouse</th>
                            <th>In</th>
                            <th>Warehouse</th>
                            <th>Out</th>
                            <th>Running Balance</th>
                            <g:each in="${warehouses}" var="wh">
                            <th class="warehouseColumn_${wh.id}" style="display:none">${wh.identifier}</th>
                            </g:each>
                        </thead>
                        <tbody>
                        	<g:set var="balance" value="${0}"/>
                        	<% def stocks = [:]
                        	%>
                            <g:each in="${stockCardItems.sort{map1, map2 -> map1.datePosted <=> map2.datePosted ?: map1.id <=> map2.id}}" var="item" status="c">
                            	<% 
                            	if(item.warehouseIn){
                            		def s = stocks[item.warehouseIn]
                            		if(!s) s=0
                            		s+=item.qtyIn
                            		stocks[item.warehouseIn] = s
                            		}
                            	if(item.warehouseOut){
                            		def s = stocks[item.warehouseOut]
                            		if(!s) s=0
                            		s-=item.qtyOut
                            		stocks[item.warehouseOut] = s
                            		}
                            	%>
                            	<tr <g:if test="${item.datePosted?.getTime()<dateFrom?.getTime() && item.datePosted!=null}">style="display:none"</g:if>>
                                	<td><g:formatDate date="${item.dateOpened}" format="MM/dd/yyyy" /></td>
                                	<td><g:formatDate date="${item.datePosted}" format="MM/dd/yyyy" /></td>
                                    <td>${item.type}</td>
                                    <td>
                                    	<g:if test="${item.type?.toString().indexOf('Sales Delivery')>=0}">
                                    	<g:link controller="salesDelivery" action="show" id="${item.linkId}">${item.referenceId}</g:link>
                                    	</g:if>
                                    	<g:elseif test="${item.type?.toString().indexOf('Purchase Invoice')>=0}">
                                    	<g:link controller="purchaseInvoice" action="show" id="${item.linkId}">${item.referenceId}</g:link>
                                    	</g:elseif>
                                    	<g:elseif test="${item.type?.toString().indexOf('Material Release')>=0}">
                                    	<g:link controller="materialRelease" action="show" id="${item.linkId}">${item.referenceId}</g:link>
                                    	</g:elseif>
                                    	<g:elseif test="${item.type?.toString().indexOf('Credit Memo')>=0 || item.type?.toString().indexOf('Debit Memo')>=0}">
                                    	<g:link controller="creditMemo" action="show" id="${item.linkId}">${item.referenceId}</g:link>
                                    	</g:elseif>
                                    	<g:elseif test="${item.type?.toString().indexOf('Job Out')>=0}">
                                    	<g:link controller="jobOut" action="show" id="${item.linkId}">${item.referenceId}</g:link>
                                    	</g:elseif>
                                    	<g:elseif test="${item.type?.toString().indexOf('Inventory Transfer')>=0}">
                                    	<g:link controller="inventoryTransfer" action="show" id="${item.linkId}">${item.referenceId}</g:link>
                                    	</g:elseif>
                                    	<g:elseif test="${item.type?.toString().indexOf('Inventory Adjustment')>=0}">
                                    	<g:link controller="inventoryAdjustment" action="show" id="${com.munix.InventoryAdjustmentItem.get(item.linkId)?.adjustment?.id}">${item.referenceId}</g:link>
                                    	</g:elseif>
                                    </td>
									<td>${item.supplierCustomer}</td>
									<g:ifAnyGranted role="ROLE_SUPER">
                                    <td class="right">${item?.formatCostForeign()}</td>
                                    <td class="right"><g:formatNumber number="${item.costLocal}" format="###,##0.00"/></td>
                                    </g:ifAnyGranted>
									<g:ifAnyGranted role="ROLE_SALES">
                                    <td class="right"><g:formatNumber number="${item.sellingAmount}" format="###,##0.00"/></td>
                                    </g:ifAnyGranted>
                                    <td>${item.warehouseIn}</td>
                                    <td class="right"><g:formatNumber number="${item.qtyIn}" format="###,##0.00"/></td>
                                    <td>${item.warehouseOut}</td>
                                    <td class="right"><g:formatNumber number="${item.qtyOut}" format="###,##0.00"/></td>
                                    	<g:if test="${c==0}">
		                        	<g:set var="balance" value="${item.balance}"/>
                                    	</g:if>
                                    	<g:else>
		                        	<g:set var="balance" value="${item.qtyOut?balance-item.qtyOut:balance}"/>
		                        	<g:set var="balance" value="${item.qtyIn?balance+item.qtyIn:balance}"/>
                                    	</g:else>
                                    <td class="right">
                                    		<g:formatNumber number="${balance}" format="###,##0.00"/>
                                    </td>
		                            <g:each in="${warehouses}" var="wh">
		                            <td class="warehouseColumn_${wh.id}" style="display:none">${stocks[wh.identifier]}</td>
		                            </g:each>
                                </tr>
                            </g:each>
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                	<span class="button"><g:link controller="product" action="show" id="${stockCardInstance?.product?.id}">Back</g:link></span>
                	<span class="button"><g:link controller="stockCard" action="recalculate" id="${stockCardInstance?.id}">Recalculate</g:link></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
