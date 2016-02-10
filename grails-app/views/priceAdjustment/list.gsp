
<%@ page import="com.munix.PriceAdjustment" %>
<html>
    <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
    <link rel="stylesheet" href="${resource(dir:'css',file:'jquery.ui.all.css')}" />
    <g:set var="entityName" value="${message(code: 'priceAdjustment.label', default: 'Price Adjustment')}" />
        
    <title><g:message code="default.list.label" args="[entityName]" /></title>
    <g:javascript src="jquery.ui.core.js" />
    <g:javascript src="jquery.ui.datepicker.js" />
    <g:javascript src="generalmethods.js" />
    <g:javascript>
        var $ = jQuery.noConflict()
        $(document).ready(function (){
      	    $("#searchPriceAdjustmentId").ForceNumericOnlyEnterAllowed(true)
            $("#searchDateGeneratedFrom").datepicker()
            $("#searchDateGeneratedTo").datepicker()
            $("#searchEffectiveDateFrom").datepicker()
            $("#searchEffectiveDateTo").datepicker()
        })
    </g:javascript>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLinkTo(uri: '/')}">Home</a></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.list.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
	    <div id="search">
    	  <g:form controller="priceAdjustment" action="list" >
	        <table>
    	      <tr>
        	    <td class="name" width="400px">ID</td>
            	<td class="value" colspan="3"><g:textField name="searchPriceAdjustmentId" value ="${params.searchPriceAdjustmentId}"/></td>
	          </tr>
			  <tr>
	           	<td class="name">Price Type</td>
	            <td class="value" colspan="3"><g:select name="searchPriceType" noSelection="${['':'']}" from="${['Wholesale', 'Retail']}" value ="${params.searchPriceType}"/></td>
       	      </tr>
    	      <tr>
        	    <td class="name">Item Type</td>
            	<td class="value" colspan="3"><g:select name="searchItemType" noSelection="${['':'']}" from="${['Accessories','Dunlop','Parts']}" value ="${params.searchItemType}"/></td>
	          </tr>       	      
    	      <tr>
	            <td class="name">Date Created</td>
    	        <td class="value" width="900px">
    	        	<g:textField name="searchDateGeneratedFrom" id="searchDateGeneratedFrom" value ="${params.searchDateGeneratedFrom}"/>
        	   		to
           			<g:textField name="searchDateGeneratedTo" id="searchDateGeneratedTo" value ="${params.searchDateGeneratedTo}"/>
           		</td>
	          </tr>
    	      <tr>
	            <td class="name">Effective Date</td>
    	        <td class="value" width="900px">
    	        	<g:textField name="searchEffectiveDateFrom" id="searchEffectiveDateFrom" value ="${params.searchEffectiveDateFrom}"/>
        	   		to
           			<g:textField name="searchEffectiveDateTo" id="searchEffectiveDateTo" value ="${params.searchEffectiveDateTo}"/>
           		</td>
	          </tr>	          
			  <tr>
	           	<td class="name">Status</td>
	            <td class="value" colspan="3"><g:select name="searchStatus" noSelection="${['':'']}" from="${['Unapproved', 'Approved', 'Cancelled', 'Applied']}" value ="${params.searchStatus}"/></td>
       	      </tr>
            </table>
        	<div>
          		<input type="submit" class="button" value="Search"/>
        	</div>
      	  </g:form>
    	</div>
            
            <div class="list">
                <table>
                    <thead>
                        <tr>
                        
                   	    <g:sortableColumn property="priceAdjustmentId" title="${message(code: 'priceAdjustment.identifier.label', default: 'ID')}" params="${params}" />
                   	    
                   	    <g:sortableColumn property="priceType" title="${message(code: 'priceAdjustment.priceType.label', default: 'Price Type')}" params="${params}" />
                        
                        <g:sortableColumn property="itemType" title="${message(code: 'priceAdjustment.itemType.label', default: 'Item Type')}" params="${params}" />
                        
                        <g:sortableColumn property="dateGenerated" title="${message(code: 'priceAdjustment.dateGenerated.label', default: 'Date Generated')}" params="${params}" />
                        
                        <g:sortableColumn property="effectiveDate" title="${message(code: 'priceAdjustment.effectiveDate.label', default: 'Effective Date')}" params="${params}" />
                        
                   	    <g:sortableColumn property="status" title="${message(code: 'priceAdjustment.status.label', default: 'Status')}" params="${params}" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${priceAdjustmentInstanceList}" status="i" var="priceAdjustmentInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}" onclick="window.location='${createLink(uri:'/')}priceAdjustment/show/${priceAdjustmentInstance.id}'">

                        	<td id="rowPriceAdjustmentId${i}">${fieldValue(bean: priceAdjustmentInstance, field: "priceAdjustmentId")}</td>
                        	
                            <td>${fieldValue(bean: priceAdjustmentInstance, field: "priceType")}</td>
                            
                            <td>${fieldValue(bean: priceAdjustmentInstance, field: "itemType")}</td>
                            
                            <td><g:formatDate date="${priceAdjustmentInstance?.dateGenerated}" format="MMMM dd, yyyy" /></td>
                            
                            <td><g:formatDate date="${priceAdjustmentInstance?.effectiveDate}" format="MMMM dd, yyyy" /></td>
                            
						    <td>${fieldValue(bean: priceAdjustmentInstance, field: "status")}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${priceAdjustmentInstanceTotal}" params="${params}"/>
            </div>
        </div>
    </body>
</html>
