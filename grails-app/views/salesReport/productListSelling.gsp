<%@ page import="com.munix.Product" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="report" />
  <g:set var="entityName" value="${message(code: 'product.label', default: 'Product')}" />
  <title><g:message code="default.list.label" args="[entityName]" /></title>
  <style type="text/css" media="print,screen">
	@media print {
	   thead {display: table-header-group;}
	}
	
	div.list table tr td{
		border-left:0px;
		border-right:0px;
	}

	div.list table tr th{
		background-color:white;
		color:black;
		border-top:0px;
		border-left:0px;
		border-right:0px;
		cursor:pointer;
	}

	div.list table{
		border-left:0px;
		border-right:0px;
	}
	
	.right{
		text-align:right;
	}
  </style>
</head>
<body>
    <div class="list">
      <table>
        <thead>
          <tr>	
	        <th>Picture</th>
	        <th onclick="sortReport('identifier')">Code</th>
	        <th onclick="sortReport('formatDescription()')">Description</th>
	        <%--<th onclick="sortReport('unit')">Unit</th> --%>
	        <th onclick="sortReport(<g:if test="${priceType=='Retail'}">'retailPrice'</g:if><g:else>'wholeSalePrice'</g:else>)">Price</th>
	        <th>&nbsp;</th>
        </tr>
        </thead>
        <tbody>
	        <g:each in="${list}" status="i" var="bean">
    	    	<tr>
    	    		<td><img src="${createLink(controller:'product',action:'viewImage', id:bean.id)}" height="100" width="150" alt="${fieldValue(bean:productInstance, field:'identifier')}"/></td>
		        	<td>${bean}</td>
		        	<td>${bean.formatDescription()}</td>
		        	<%--<td class="center">${bean.unit}</td>--%>
		        	<td class="right">${String.format('%,.2f',bean.getProductPrice(priceType))}</td>
		        	<td class="center">${bean.isNet?"NET":""}</td>
		    	</tr>
	    	</g:each>
        </tbody>
      </table>
    </div>
</body>
</html>
