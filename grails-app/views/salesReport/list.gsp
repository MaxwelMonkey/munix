
<%@ page import="com.munix.SalesDelivery" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="report" />
  <g:set var="entityName" value="${message(code: 'salesDelivery.label', default: 'SalesDelivery')}" />
  <title><g:message code="default.list.label" args="[entityName]" /></title>
</head>
<body>
  <div class="body">
    <h1><g:message code="default.list.label" args="[entityName]" /></h1>
    <div class="list">
      <table>
        <thead>
          <tr>
          <g:each in="${columns}" status="i" var="column">
	        <th>${message(code: modelName+'.'+column+'.label', default: column)}</th>
	       </g:each>
        </tr>
        </thead>
        <tbody>
	        <g:each in="${list}" status="i" var="bean">
    	    	<tr>
	        	<g:each in="${columns}" status="j" var="column">
	        		<g:if test="${bean[column] instanceof java.util.Date}">
	        			<td><g:formatDate date="${bean[column]}" format="MM/dd/yyyy"/></td>
	        		</g:if>
	        		<g:else>
			        	<td>${fieldValue(bean: bean, field: column)}</td>
		        	</g:else>
		       	</g:each>
		    	</tr>
	    	</g:each>
        </tbody>
      </table>
    </div>
  </div>
</body>
</html>
