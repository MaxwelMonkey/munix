
<%@ page import="com.munix.Product" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'product.label', default: 'Product')}" />
  <g:javascript src="jquery.dataTables.min.js" />
  <g:javascript src="FixedHeader.min.js" />
    <title><g:message code="default.list.label" args="[entityName]" /></title>
    <style>
    	div.list table tr *{
    		font-size:9px !important;
    	}
    	
		th{
		    border-left: 1px solid #ddd;
		    border: 1px solid #E5EFF8;
		    padding: 8px;
		    background: #267596;
		}
		
		th:hover{
		    background: #4B9BBD;
		}
		
		th a, th{
    		font-size:9px !important;
		}
		
		div.fixedHeader table{
    		margin-top:-10px !important;
		}
    </style>
</head>
<body>
  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
    <span class="menuButton"><g:link class="list" action="list" controller="productComponent">View Product Components</g:link></span>
    <span class="menuButton"><g:link class="list" action="editMultiple">Edit Multiple Products</g:link></span>
    <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
  </div>
  <div class="body">
    <h1><g:message code="default.list.label" args="[entityName]" /></h1>
    <g:if test="${flash.message}">
      <div class="message">${flash.message}</div>
    </g:if>
    <report:report id="product" report="product" format="PDF" > Generate Report
    </report:report>

    <div id="search">
      <g:form controller="product" action="list" >
        <table>
          <tr>
            <td class="name">Identifier</td>
            <td class="value"><g:textField name="searchIdentifier" value ="${params.searchIdentifier}"/></td>
          </tr>
          <tr>
            <td class="name">Category</td>
            <td class="value"><g:textField name="searchCategory" value ="${params.searchCategory}"/></td>
          </tr>
          <tr>
            <td class="name">Subcategory</td>
            <td class="value"><g:textField name="searchSubcategory" value ="${params.searchSubcategory}"/></td>
          </tr>
          <tr>
            <td class="name">Item Type</td>
            <td class="value"><g:select name="searchItemType" from="${itemTypes}" optionKey="identifier" noSelection="['':'']" value="${params.searchItemType}" /></td>
          </tr>
          <tr>
            <td class="name">Discount Type</td>
            <td class="value"><g:select name="searchDiscountType" from="${discountTypes}" optionKey="identifier" noSelection="['':'']" value="${params.searchDiscountType}" /></td>
          </tr>
          <tr>
            <td class="name">Brand</td>
            <td class="value"><g:textField name="searchBrand" value ="${params.searchBrand}"/></td>
          </tr>
          <tr>
            <td class="name">Model</td>
            <td class="value"><g:textField name="searchModel" value ="${params.searchModel}"/></td>
          </tr>
          <tr>
            <td class="name">Model Number</td>
            <td class="value"><g:textField name="searchModelNumber" value ="${params.searchModelNumber}"/></td>
          </tr>
          <tr>
            <td class="name">Material</td>
            <td class="value"><g:textField name="searchMaterial" value ="${params.searchMaterial}"/></td>
          </tr>
          <tr>
            <td class="name">Size</td>
            <td class="value"><g:textField name="searchSize" value ="${params.searchSize}"/></td>
          </tr>
          <tr>
            <td class="name">Additional Description</td>
            <td class="value"><g:textField name="searchAdditional" value ="${params.searchAdditional}"/></td>
          </tr>
          <tr>
            <td class="name">Color</td>
            <td class="value"><g:textField name="searchColor" value ="${params.searchColor}"/></td>
          </tr>
          <tr>
            <td class="name">Part Number</td>
            <td class="value"><g:textField name="searchPartNumber" value ="${params.searchPartNumber}"/></td>
          </tr>
          <tr>
            <td class="name">Status</td>
            <td class="value"><g:select name="searchStatus" from="${['Active','Inactive']}" noSelection="['':'']" value="${params.searchStatus}" /></td>
          </tr>
          <tr>
            <td class="name">Is Net</td>
            <td class="value"><g:select name="searchIsNet" from="${['True','False']}" noSelection="['':'']" value="${params.searchIsNet}" /></td>
          </tr>
          <tr>
            <td class="name">Is For Sale</td>
            <td class="value"><g:select name="searchIsForSale" from="${['True','False']}" noSelection="['':'']" value="${params.searchIsForSale}" /></td>
          </tr>
          <tr>
            <td class="name">Is Component</td>
            <td class="value"><g:select name="searchIsComponent" from="${['True','False']}" noSelection="['':'']" value="${params.searchIsComponent}" /></td>
          </tr>
          <tr>
            <td class="name">Is Assembly</td>
            <td class="value"><g:select name="searchIsAssembly" from="${['True','False']}" noSelection="['':'']" value="${params.searchIsAssembly}" /></td>
          </tr>
          <tr>
            <td class="name">Keyword</td>
            <td class="value"><g:textField name="searchKeyword" value ="${params.searchKeyword}"/></td>
          </tr>
        </table>
        <div>
          <input type="submit" class="button" value="Search"/>
        </div>

      </g:form>
    </div>
    <div class="list">
      <table id="list">
        <thead>
          <tr>

        <g:sortableColumn property="identifier" title="${message(code: 'product.identifier.label', default: 'Identifier')}" params="${params}"/>
        <g:sortableColumn property="unit" title="${message(code: 'product.unit.label', default: 'Unit')}" params="${params}"/>
        <g:sortableColumn property="description" title="${message(code: 'product.description.label', default: 'Description')}" params="${params}"/>
        <g:sortableColumn property="partNumber" title="${message(code: 'product.partNumber.label', default: 'Part Number')}" params="${params}"/>
        <g:sortableColumn property="itemType" title="${message(code: 'product.itemType.label', default: 'Item Type')}" params="${params}"/>
		<g:ifAnyGranted role="ROLE_SALES,ROLE_PURCHASING">
	        <g:sortableColumn property="wholeSalePrice" class="right" title="${message(code: 'product.wholeSalePrice.label', default: 'Price (Wholesale)')}" params="${params}"/>        
    	    <g:sortableColumn property="retailPrice" class="right" title="${message(code: 'product.retailPrice.label', default: 'Price (Retail)')}" params="${params}"/>
        </g:ifAnyGranted>
        <g:sortableColumn property="type" title="${message(code: 'product.type.label', default: 'Discount Type')}" params="${params}"/>
        <g:sortableColumn property="isNet" title="${message(code: 'product.isNet.label', default: 'Net Price')}" params="${params}"/>
        <g:sortableColumn property="isComponent" title="${message(code: 'product.isComponent.label', default: 'Comp.')}" params="${params}"/>
        <g:sortableColumn property="isForSale" title="${message(code: 'product.isForSale.label', default: 'For Sale')}" params="${params}"/>
        <g:sortableColumn property="isForAssembly" title="${message(code: 'product.isForAssembly.label', default: 'Assy.')}" params="${params}"/>
        <g:each in="${com.munix.Warehouse.list().sort{it.identifier}}" var="i">
          <th class="right">${i?.identifier}</th>
        </g:each>        
        <g:sortableColumn property="reorderPoint" class="right" title="${message(code: 'product.reorderPoint.label', default: 'Reorder Point')}" params="${params}"/>
        <g:sortableColumn property="eoq" class="right" title="${message(code: 'product.eoq.label', default: 'EOQ')}" params="${params}"/>
        <g:sortableColumn property="status" title="${message(code: 'product.status.label', default: 'Status')}" params="${params}"/>
     <g:ifAnyGranted role="ROLE_SUPER">
        <g:sortableColumn property="runningCostInForeignCurrency" title="${message(code: 'product.runningCostInForeignCurrency.label', default: 'Foreign Cost')}" params="${params}"/>
        <g:sortableColumn property="runningCost" title="${message(code: 'product.runningCost.label', default: 'Local Cost')}" params="${params}"/>
     </g:ifAnyGranted>
        <th>${message(code: 'product.pendingSo.label', default: 'Pending SO')}</th>
        <th>${message(code: 'product.pendingPo.label', default: 'Pending PO')}</th>
        </tr>
        </thead>
        <tbody>
        <g:each in="${productInstanceList}" status="i" var="productInstance">
          <tr class="${(i % 2) == 0 ? 'odd' : 'even'}" onclick="window.location='${createLink(uri:'/')}product/show/${productInstance.id}'">
            <td id="rowProductIdentifier${i}">${fieldValue(bean: productInstance, field: "identifier")}</td>
            <td>${fieldValue(bean: productInstance, field: "unit")}</td>
            <td>${fieldValue(bean: productInstance, field: "description")}</td>
            <td>${fieldValue(bean: productInstance, field: "partNumber")}</td>
            <td>${fieldValue(bean: productInstance, field: "itemType")}</td>
            
			<g:ifAnyGranted role="ROLE_SALES,ROLE_PURCHASING">
            <g:if test="${productInstance?.wholeSalePrice}">
            <td class="right">${String.format('%,.2f',productInstance?.wholeSalePrice)}</td>
            </g:if>
            <g:else>
                <td class="right"></td>                
            </g:else>

            <g:if test="${productInstance?.retailPrice}">
                <td class="right">${String.format('%,.2f',productInstance?.retailPrice)}</td>
            </g:if>
            <g:else>
                <td class="right"></td>                
            </g:else>
            </g:ifAnyGranted>

            <td>${fieldValue(bean: productInstance, field: "type")}</td>

          <g:if test="${productInstance?.isNet}">
            <td class="center">X</td>
          </g:if>
          <g:else>
            <td class="center"></td>
          </g:else>

          <g:if test="${productInstance?.isComponent}">
            <td class="center">X</td>
          </g:if>
          <g:else>
            <td class="center"></td>
          </g:else>

          <g:if test="${productInstance?.isForSale}">
            <td class="center">X</td>
          </g:if>
          <g:else>
            <td class="center"></td>
          </g:else>

          <g:if test="${productInstance?.isForAssembly}">
            <td class="center">X</td>
          </g:if>
          <g:else>
            <td class="center"></td>
          </g:else>

          <g:each in="${com.munix.Warehouse.list().sort{it.identifier}}" var="warehouse">
        	        		<g:set var="positive" value="${''}"/>
        	<g:if test="${productInstance?.getStock(warehouse).qty>0}">
        		<g:set var="positive" value="${'positive'}"/>
        	</g:if> 
        	<g:if test="${productInstance?.getStock(warehouse).qty<0}">
        		<g:set var="positive" value="${'negative'}"/>
        	</g:if> 
            <td class="right ${positive}">${productInstance?.formatSOH(warehouse)}</td>
          </g:each>

          <td class="right">${fieldValue(bean: productInstance, field: "reorderPoint")}</td>
          <td class="right">${fieldValue(bean: productInstance, field: "eoq")}</td>
          <td class="center">${fieldValue(bean: productInstance, field: "status")}</td>
	      <g:ifAnyGranted role="ROLE_SUPER">
          <td class="right"><g:link controller="stockCostHistory" action="show" id="${productInstance?.id}">${productInstance?.formatRunningCostInForeignCurrency()}</g:link></td>
          <td class="right">${String.format('%,.2f',productInstance?.runningCost)}</td>
          </g:ifAnyGranted>
          <td class="right"><g:link action="pendingSo" id="${productInstance.id}">${pendingMap[productInstance.id]}</g:link></td>
          <td class="right"><g:link action="pendingPo" id="${productInstance.id}">${poPendingMap[productInstance.id]}</g:link></td>
          </tr>
        </g:each>

        </tbody>
      </table>
    </div>
    <div class="paginateButtons">
      <g:paginate total="${productInstanceTotal}" params="${params}" />
    </div>
  </div>
  <script>
		$(document).ready( function () {
			var oTable = $('#list').dataTable({
		        "bPaginate": false,
		        "bLengthChange": false,
		        "bFilter": false,
		        "bSort": false,
		        "bInfo": false,
		        "bAutoWidth": false
		    } );
			new FixedHeader( oTable );
		} );
  </script>
</body>
</html>
