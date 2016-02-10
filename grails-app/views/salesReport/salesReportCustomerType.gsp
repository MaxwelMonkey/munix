<%@ page import="com.munix.SalesDelivery" %>
<%@ page import="com.munix.*" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="report" />
  <g:set var="entityName" value="${message(code: 'salesDelivery.label', default: 'SalesDelivery')}" />
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
	div.list table tr td,div.list table tr th{
		
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
          <th onclick="sortReport('date')">Date</th>
          	<g:each in="${customerTypes}" status="k" var="cType">
	        	<th onclick="sortReport('${cType}')">${cType }</th>
	         </g:each>
	        <th onclick="sortReport('total')">Total</th>
        </tr>
        </thead>
        <tbody>
        	<g:set var="grandTotal" value="${[:]}"/>
    		<g:each in="${byDateList.keySet()}" status="i" var="bean">
    	    	<tr>
    	    		<g:set var="rowTotal" value="${0}"/>
	       			<td>${bean}</td>
	       			<g:each in="${customerTypes}" status="k" var="cType">
	       				<td class="right">
	       				<g:set var="totalList" value="${0}"/>
	       					<g:each in="${byDateList.get(bean).get(cType.identifier)}" status="h" var="objects">
	       						<g:if test="${objects!=null}">
	       							<g:each in="${objects}" status="r" var="object">
	       								<g:if test="${object!=null}">
	       									<g:if test="${object.status!='Cancelled'}">
		        								<g:set var="totalList" value="${totalList + object.computeTotalAmount()}"/>
		        								<g:if test="${grandTotal.get(cType.identifier)!=null}">
		        									<% grandTotal.put(cType.identifier,  grandTotal.get(cType.identifier) + object.computeTotalAmount()) %>
		        								</g:if>
		        								<g:else>
		        									<% grandTotal.put(cType.identifier,  object.computeTotalAmount()) %>
		        								</g:else>
		        							</g:if>
	       								</g:if>
	       							</g:each>
	       						</g:if>
	       					</g:each>
	       				<g:formatNumber number="${totalList}" format="###,##0.00" />
	       				<g:set var="rowTotal" value="${rowTotal + totalList}"/>
	       				<g:if test="${grandTotal.get('rowTotals')!=null}">
		        				<% grandTotal.put('rowTotals',  grandTotal.get('rowTotals') + totalList) %>
		        		</g:if>
		        		<g:else>
		        				<% grandTotal.put('rowTotals',  totalList) %>
		        		</g:else>
	       				</td>
	       			</g:each>
    	    		<td class="right"><g:formatNumber number="${rowTotal}" format="###,##0.00" /></td>
		    	</tr>
	  		</g:each>
             <g:if test="${params.totalHidden != 'Y'}">
	  		 <tr>
          		<td><b>Totals</b></td>
          		<g:each in="${customerTypes}" status="k" var="cType">
          			<g:if test="${grandTotal.get(cType.identifier)!=null }">
	        			<td  class="right"><g:formatNumber number="${grandTotal.get(cType.identifier)}" format="###,##0.00" /></td>
	         		</g:if>
	         		<g:else>
	         			<td  class="right"><g:formatNumber number="${0.00}" format="###,##0.00" /></td>
	         		</g:else>
	         	</g:each>
	        	<g:if test="${grandTotal.get('rowTotals')!=null }">
	        			<td  class="right"><g:formatNumber number="${grandTotal.get('rowTotals')}" format="###,##0.00" /></td>
	         	</g:if>
	         	<g:else>
	         			<td  class="right"><g:formatNumber number="${0.00}" format="###,##0.00" /></td>
	         	</g:else>
             </tr>
             </g:if>
        </tbody>    
      </table>
    </div>
</body>
</html>
