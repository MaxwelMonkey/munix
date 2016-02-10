
<%@ page import="com.munix.JobOrder" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'jobOrder.label', default: 'JobOrder')}" />
  <title><g:message code="default.show.label" args="[entityName]" /></title>
	<link rel="stylesheet" href="${resource(dir:'css',file:'scrollableTable.css')}" />
    <g:javascript src="numbervalidation.js" />
</head>
<body >
  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
    <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
    <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
  </div>
  <div class="body">
    <h1><g:message code="default.show.label" args="[entityName]" /></h1>
    <g:if test="${flash.message}">
      <div class="message">${flash.message}</div>
    </g:if>
    <g:if test="${flash.error}">
      <div class="errors">${flash.error}</div>
    </g:if>    
  <g:hasErrors bean="${jobOrderInstance}">
    <div class="errors">
      <g:renderErrors bean="${jobOrderInstance}" as="list" />
    </div>
  </g:hasErrors>
  <g:hasErrors bean="${jobOrderInstance?.requisition?.items}">
    <div class="errors">
      <g:renderErrors bean="${jobOrderInstance?.requisition?.items}" as="list" />
    </div>
  </g:hasErrors>
    <div class="dialog">
      <table>
        <tbody>

          <tr class="prop">
            <td valign="top" class="name"><g:message code="jobOrder.id.label" default="Id" /></td>
            <td valign="top" class="value">${jobOrderInstance}</td>
            <td valign="top" class="name"><g:message code="jobOrder.preparedBy.label" default="Prepared By" /></td>
            <td valign="top" class="value">${jobOrderInstance?.preparedBy?.encodeAsHTML()}</td>

          </tr>

          <tr class="prop">
            <td valign="top" class="name"><g:message code="jobOrder.product.label" default="Identifier" /></td>
            <td valign="top" class="value"><g:link controller="product" action="show" id="${jobOrderInstance?.product?.id}" >${jobOrderInstance?.product?.encodeAsHTML()}</g:link></td>
            <td valign="top" class="name"><g:message code="jobOrder.approvedBy.label" default="Job Order Approved By" /></td>
            <td valign="top" class="value">${jobOrderInstance?.approvedBy?.encodeAsHTML()}</td>

          </tr>

          <tr class="prop">

            <td valign="top" class="name"><g:message code="jobOrder.description.label" default="Description" /></td>
            <td valign="top" class="value">${jobOrderInstance?.product?.formatDescription()}</td>
            <td valign="top" class="name"><g:message code="jobOrder.materialReleaseApproveBy.label" default="Material Releases Approved By" /></td>
            <td valign="top" class="value">${jobOrderInstance?.materialReleasesApprovedBy?.encodeAsHTML()}</td>

          </tr>

          <tr class="prop">
            <td valign="top" class="name"><g:message code="jobOrder.qty.label" default="Quantity" /></td>
            <td id="quantity" valign="top" class="value">${jobOrderInstance?.formatQty()}</td>
            <td valign="top" class="name"><g:message code="jobOrder.markAsCompletedBy.label" default="Marked as Completed By" /></td>
            <td valign="top" class="value">${jobOrderInstance?.markAsCompleteBy?.encodeAsHTML()}</td>

          </tr>

          <tr class="prop">
            <td valign="top" class="name"><g:message code="jobOrder.startDate.label" default="Start Date" /></td>
            <td id="startDate" valign="top" class="value"><g:formatDate date="${jobOrderInstance?.startDate}" format="MM/dd/yyyy hh:mm aaa" /></td>
            <td valign="top" class="name"><g:message code="jobOrder.cancelledBy.label" default="Cancelled By" /></td>
            <td valign="top" class="value">${jobOrderInstance?.cancelledBy?.encodeAsHTML()}</td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name"><g:message code="jobOrder.targetDate.label" default="Target Date" /></td>
            <td id="targetDate" valign="top" class="value"><g:formatDate date="${jobOrderInstance?.targetDate}" format="MM/dd/yyyy hh:mm aaa" /></td>
            <td valign="top" class="name"><g:message code="jobOrder.assignedTo.label" default="Assigned To" /></td>
            <td id="assignedTo" valign="top" class="value">${jobOrderInstance?.assignedTo?.encodeAsHTML()}</td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name"><g:message code="jobOrder.endDate.label" default="End Date" /></td>
            <td valign="top" class="value"><g:formatDate date="${jobOrderInstance?.endDate}" format="MM/dd/yyyy hh:mm aaa" /></td>
            <td valign="top" class="name"><g:message code="jobOrder.status.label" default="Status" /></td>
            <td id="status" valign="top" class="value">${fieldValue(bean: jobOrderInstance, field: "status")}</td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name"><g:message code="jobOrder.remark.label" default="Remarks" /></td>
            <td id="remarks" valign="top" class="value">${fieldValue(bean: jobOrderInstance, field: "remark")}</td>
            <td valign="top" class="name"></td>
            <td valign="top" class="value"></td>
          </tr>

        </tbody>
      </table>

    </div>
      <g:ifAnyGranted role="ROLE_MANAGER_PRODUCTION,ROLE_PRODUCTION">
        <g:if test='${!jobOrderInstance?.isCancelled()}'>
          <g:if test='${jobOrderInstance?.isUnapproved()}'>
            <div>
              <g:form>
                <g:hiddenField name="jobOrderId" value="${jobOrderInstance.id}"/>
                <span class="button"><g:actionSubmit value="Edit Job Order" action="jobOrderEdit"/></span>
              </g:form>
            </div>
          </g:if>
        </g:if>
      </g:ifAnyGranted>
    <div class="subTable">
      <table>
        <h2>Requirements</h2>
        <g:ifAnyGranted role="ROLE_MANAGER_PRODUCTION,ROLE_PRODUCTION">
            <g:if test='${!jobOrderInstance?.isCancelled()}'>
              <g:if test='${jobOrderInstance?.isUnapproved()}'>
                <div>
                  <g:form>
                    <g:hiddenField name="jobOrderId" value="${jobOrderInstance.id}"/>
                      <h2><span class="button"><g:actionSubmit class="approve"  action="edit" value="Edit Materials"/></span></h2>
                  </g:form>
                </div>
              </g:if>
            </g:if>
        </g:ifAnyGranted>
        <thead>
          <tr>
            <th class="center">Identifier</th>
            <th class="center">Description</th>
            <th class="center">Units Required</th>
            <th class="center">Quantity</th>
            <th class="center">Balance</th>
            <th class="center">Material Release</th>
            <th class="center">Released Qty</th>
            <g:ifAnyGranted role="ROLE_SUPER">
              <th class="center">Cost Per Unit</th>
              <th class="center">Final Cost (per component)</th>
              <th class="center">Final Cost</th>
            </g:ifAnyGranted>
          </tr>
        </thead>
        <tbody>
        <g:each in="${jobOrderInstance?.requisition?.items?.sort{it?.component?.description}}" var="reqItem" status="idx">
          <tr class="${(idx % 2) == 0 ? 'odd' : 'even'}">
             <g:if test='${reqItem.getMaterialReleaseItems()}'>
				<g:each in="${reqItem.getMaterialReleaseItems()}" var="relItem" status="relItemIdx">  
				     <g:if test='${relItemIdx==0}'>
            			<td><a href="${createLink(uri:'/')}product/show/${reqItem?.component?.id}">${reqItem?.component?.identifier}</a></td>
            			<td>${reqItem?.component?.description}</td>
            			<td class="right">${reqItem?.unitsRequired}</td>
            			<td class="right">${reqItem?.computeQuantity()}</td>
            			<td class="right">${jobOrderInstance?.computeRequisitionItemRemainingBalance(reqItem)}</td>
            			<td>${relItem.materialRelease.toString()}</td>
    	        		<td class="right">${relItem.qty}</td>
                      <g:ifAnyGranted role="ROLE_SUPER">
        	    		<td class="right">${relItem.cost}</td>
            			<td class="right">${reqItem.computeFinalCostPerComponent()}</td>
            			<td class="right"><g:formatNumber number="${reqItem.computeFinalCost()}" format="#,##0.0000" /></td>
                      </g:ifAnyGranted>
            		</g:if>
            		<g:else>
            			</tr>
            			<tr class="${(idx % 2) == 0 ? 'odd' : 'even'}">
            				<td></td>
            				<td></td>
            				<td></td>
            				<td></td>
            				<td></td>
            				<td>${relItem.materialRelease.toString()}</td>
    	        			<td class="right">${relItem.qty}</td>
                          <g:ifAnyGranted role="ROLE_SUPER">
        	    			<td class="right">${relItem.cost}</td>
        	    			<td></td>
        	    			<td></td>
                          </g:ifAnyGranted>
            		</g:else>
            	</g:each>            		
             </g:if>
             <g:else>
                <td><a href="${createLink(uri:'/')}product/show/${reqItem?.component?.id}">${reqItem?.component?.identifier}</a></td>
            	<td>${reqItem?.component?.description}</td>
            	<td class="right">${reqItem?.unitsRequired}</td>
            	<td class="right">${reqItem?.computeQuantity()}</td>
            	<td></td>
            	<td></td>
            	<td></td>
            	<g:ifAnyGranted role="ROLE_SUPER">
            	  <td></td>
            	  <td></td>
            	  <td></td>
                </g:ifAnyGranted>
             </g:else>             
          </tr>
        </g:each>
		</tbody>
         <tfoot class="total"> 
          <tr>
            <td>Total</td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <g:ifAnyGranted role="ROLE_SUPER">
            <td></td>
            <g:if test='${(!jobOrderInstance?.isCancelled() && !jobOrderInstance.isUnapproved()) && !jobOrderInstance?.releases?.isEmpty() && !jobOrderInstance?.hasNoActiveReleases()}'>
            	<td class="right"><strong>PHP <g:formatNumber number="${jobOrderInstance?.requisition?.computeTotalCostOfMaterialsPerAssembly()}" format="#,##0.00" /></strong></td>
            	<td class="right"><strong>PHP <g:formatNumber number="${jobOrderInstance?.requisition?.computeTotalCostOfMaterialsForJobOrder()}" format="#,##0.00" /></strong></td>
            </g:if>
            <g:else>
                <td></td>
                <td></td>
            </g:else>
            </g:ifAnyGranted>
          </tr>
         </tfoot>
      </table>
    </div>
    <div>
	  <g:form>
    	<g:hiddenField name="id" value="${jobOrderInstance.id}"/>
        <g:if test='${!jobOrderInstance?.isCancelled()}'>
	        <g:if test='${jobOrderInstance?.isUnapproved()}'>
    	    	<span class="button"><g:actionSubmit class="approve" action="approve" value="${message(code: 'default.button.approve.label', default: 'Approve')}" onclick="return confirm('${message(code: 'default.button.approve.confirm.message', default: 'Are you sure?')}');" /></span>
			</g:if>
	        <g:if test='${jobOrderInstance?.isJobOrderApproved() && jobOrderInstance?.hasNoActiveReleases()}'>
    	    	<span class="button"><g:actionSubmit class="approve" action="unapprove" value="${message(code: 'default.button.approve.label', default: 'Unapprove')}" onclick="return confirm('${message(code: 'default.button.approve.confirm.message', default: 'Are you sure?')}');" /></span>
			</g:if>			
		</g:if>    	    	
      </g:form>
    </div>

    <div class="subTable">
      <table>
        <h2>Releases</h2>
        <g:ifAnyGranted role="ROLE_MANAGER_PRODUCTION,ROLE_PRODUCTION">
            <g:if test='${!jobOrderInstance?.isCancelled()}'>
              <g:if test='${jobOrderInstance?.isJobOrderApproved()}'>
                <div>
                  <g:form>
                    <g:hiddenField name="id" value="${jobOrderInstance.id}"/>
                      <h2><span class="button"><g:actionSubmit class="approve" action="addMaterialRelease" value="Add Material Release"/></span></h2>
                  </g:form>
                </div>
              </g:if>
            </g:if>
        </g:ifAnyGranted>
        <thead>
          <tr>
            <th>ID</th>
            <th class="center">Date</th>
            <th>Prepared by</th>
            <th>Approved by</th>
            <th>Status</th>
          </tr>
        </thead>
        <tbody class="editable">
        <g:each in="${jobOrderInstance?.releases?.sort{it.id}}" var="i" status="colors">
          <tr class="${(colors % 2) == 0 ? 'odd' : 'even'}" onclick="window.location='${createLink(uri:'/')}materialRelease/show/${i.id}'">
            <td>${i}</td>
            <td class="center"><g:formatDate date="${i?.date}" format="MM/dd/yyyy"/></td>
          <td>${i?.preparedBy}</td>
          <td>${i?.approvedBy}</td>
          <td>${i?.status}</td>
          </tr>
        </g:each>
        </tbody>
      </table>
    </div>
    <div>
	  <g:form>
        <g:if test='${!jobOrderInstance?.isCancelled()}'>
            <g:hiddenField name="id" value="${jobOrderInstance.id}"/>
	        <g:if test='${jobOrderInstance?.isReadyForMaterialReleasesApproval()}'>
    	    	<span class="button"><g:actionSubmit class="approve" action="approveMaterialReleases" value="${message(code: 'default.button.approve.label', default: 'Approve All Material Releases')}" onclick="return confirm('${message(code: 'default.button.approve.confirm.message', default: 'Are you sure?')}');" /></span>
			</g:if>
	        <g:if test='${jobOrderInstance?.materialReleasesApprovedBy&&jobOrderInstance.jobOuts.findAll{!it.isCancelled()}.isEmpty()}'>
    	    	<span class="button"><g:actionSubmit action="unapproveMaterialReleases" value="${message(code: 'default.button.approve.label', default: 'Unapprove')}" onclick="return confirm('${message(code: 'default.button.unapprove.confirm.message', default: 'Are you sure?')}');" /></span>
			</g:if>			
		</g:if>    	    	
      </g:form>
    </div>

    <div class="subTable">
      <table>
        <h2>Job Out</h2>
         <g:ifAnyGranted role="ROLE_MANAGER_PRODUCTION,ROLE_PRODUCTION">
            <g:if test='${!jobOrderInstance?.isCancelled()}'>
              <g:if test='${jobOrderInstance?.isMaterialReleasesApproved()}'>
                <div>
                  <g:form>
                    <g:hiddenField name="id" value="${jobOrderInstance.id}"/>
                      <h2><span class="button"><g:actionSubmit class="approve" action="addJobOut" value="Add Job Out"/></span></h2>
                  </g:form>
                </div>
              </g:if>
            </g:if>
        </g:ifAnyGranted>
        <thead>
          <tr>
            <th>ID</th>
            <th class="center">Date</th>
            <th>Warehouse</th>
            <th>Status</th>
            <th class="right">Quantity</th>
          </tr>
        </thead>
        <tbody class="editable">
        <g:each in="${jobOrderInstance.jobOuts.sort{it.id}}" var="jobOut" status="i">
          <tr class="${(i % 2) == 0 ? 'odd' : 'even'}" onclick="window.location='${createLink(uri:'/')}jobOut/show/${jobOut.id}'">
            <td>${jobOut}</td>
            <td class="center"><g:formatDate date="${jobOut?.date}" format="MM/dd/yyyy hh:mm aaa" /></td>
          <td>${jobOut?.warehouse}</td>
          <td>${jobOut?.status}</td>
          <td class="right"><g:formatNumber number="${jobOut?.qty}" format="#,##0" /></td>
          </tr>
        </g:each>
        </tbody>
        <tfoot class="total">
          <tr>
            <td><strong>Total</strong></td>
            <td></td>
            <td></td>
            <td></td>
            <td class="right"><strong>${jobOrderInstance?.formatOutTotal()}</strong></td>
          </tr>
          <tr>
            <td><strong>Remaining Balance</strong></td>
            <td></td>
            <td></td>
            <td></td>
            <td class="right"><strong><g:formatNumber number="${jobOrderInstance?.computeRemainingBalance()}" format="#,##0" /></strong></td>
          </tr>
				        <g:each in="${com.munix.Warehouse.list().sort{it.identifier}}" var="warehouse">
			            <td class="right">${reqItem?.component?.formatSOH(warehouse)}</td>
				        </g:each>        
        </tfoot>
      </table>
    </div>
	<g:if test='${!jobOrderInstance?.isCancelled()}'>
    <div class="buttons">
    <g:form>
        <g:hiddenField name="id" value="${jobOrderInstance.id}"/>
        <g:ifAnyGranted role="ROLE_MANAGER_PRODUCTION,ROLE_PRODUCTION">
              <g:if test='${jobOrderInstance?.isJobOrderApproved()}'>
                  <g:if test='${(jobOrderInstance?.releases.findAll{ it.status != "Approved" })?.isEmpty() && !jobOrderInstance?.releases?.isEmpty() && jobOrderInstance?.requisition?.isComplete()}'>
                    <span class="button"><g:actionSubmit class="approve" action="approve" value="${message(code: 'default.button.approve.label', default: 'Approve')}" onclick="return confirm('${message(code: 'default.button.approve.confirm.message', default: 'Are you sure?')}');" /></span>
                  </g:if>
              </g:if>
              <g:if test='${jobOrderInstance?.isMaterialReleasesApproved()}'>

                 <g:if test='${canBeCompleted}'>
                    <span class="button"><g:actionSubmit class="approve" action="markAsComplete" value="${message(code: 'default.button.approve.label', default: 'Mark as Complete')}" onclick="return confirm('${message(code: 'default.button.approve.confirm.message', default: 'Are you sure?')}');" /></span>
                 </g:if>
              </g:if>
              <g:ifAnyGranted role="ROLE_MANAGER_PRODUCTION">
                 <g:if test='${canBeUnmarkedAsCompleted}'>
                    <span class="button"><g:actionSubmit class="approve" action="unmarkAsComplete" value="${message(code: 'default.button.approve.label', default: 'Unmark as Complete')}" onclick="return confirm('${message(code: 'default.button.approve.confirm.message', default: 'Are you sure?')}');" /></span>
                </g:if>
                <g:if test='${jobOrderInstance?.isUnapproved()}'>
                <span class="button"><g:actionSubmit class="cancel" action="cancel" value="${message(code: 'default.button.cancel.label', default: 'Cancel')}" onclick="return confirm('${message(code: 'default.button.cancel.confirm.message', default: 'Are you sure?')}');" /></span>
              	</g:if>
              </g:ifAnyGranted>  
	            <span class="button"><g:link class="print" target="blank" controller="print" action="jobOrder" id="${jobOrderInstance?.id}" onclick="javascript:setTimeout('location.reload(true)',500)">Print</g:link></span>
           
        </g:ifAnyGranted>
        <g:ifAnyGranted role="ROLE_ACCOUNTING,ROLE_PRODUCTION">
                <span class="button"><g:link class="print" target="blank" controller="print" action="jobOrderAccounting" id="${jobOrderInstance?.id}" onclick="javascript:setTimeout('location.reload(true)',500)">Print (Accounting)</g:link></span>
        </g:ifAnyGranted>
        <span class="button"><g:link action="stockAvailability" id="${jobOrderInstance?.id}">View Stock Availability</g:link></span>
    </g:form>
    </div>
    </g:if>
  </div>

</body>
</html>
