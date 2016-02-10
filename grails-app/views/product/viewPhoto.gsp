<%@ page import="com.munix.Product" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'product.label', default: 'Product')}" />
  <title><g:message code="default.show.label" args="[entityName]" /></title>
</head>
<body>
  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
    <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
    <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
  </div>
  <div class="body">
    <div class="dialog">
	  <img src="${createLink(action:'viewImage', id:productInstance.id)}" alt="${fieldValue(bean:productInstance, field:'identifier')}"/>        
	  <div class="buttons">
	    <g:form>
	      <g:hiddenField name="id" value="${productInstance?.id}" />
	      <span class="button"><g:actionSubmit class="back" action="show" value="${message(code: 'default.button.back.label', default: 'Back')}" /></span>
	    </g:form>
	  </div>
    </div>
  </div>
</body>
</html>
