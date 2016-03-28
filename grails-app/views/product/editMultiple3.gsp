
<%@ page import="com.munix.Product" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'product.label', default: 'Product')}" />
  <title><g:message code="default.edit.label" args="[entityName]" /></title>
<link rel="stylesheet" href="//ajax.googleapis.com/ajax/libs/jqueryui/1.11.0/themes/smoothness/jquery-ui.css" />
<script src="//ajax.googleapis.com/ajax/libs/jqueryui/1.11.0/jquery-ui.min.js"></script>
	<style>
		#productList{
			background-color: white;
			margin-bottom: 10px;
			padding: 10px;
			border: 1px solid lightcyan;
			border-radius: 10px;
		}
		
		#productList a{
			font-size:80%;
		}
	</style>
	<script>
		$(document).ready(function(){
			$("#term").autocomplete({
				source:function( request, response ) {
                $.ajax({
                    url: "${resource(dir:'product',file:'productList')}",
                    dataType: "json",
                    data: {term: request.term},
                    success: function(data) {	
                                response($.map(data, function(item) {
                                return {
                                    label: item.identifier,
                                    id: item.id,
                                    abbrev: item.identifier
                                    };
                            }));
                        }
                    });
                },
                minLength: 1,
                select: function( event, ui ) {
                	addProduct(ui.item.id, ui.item.label);
                	$(this).val(''); return false;
                }
			})
		});
		
		function addProduct(id, identifier){
			$("#productList").append("<input type='hidden' id='product"+id+"' name='productId' value='"+id+"'>");
			$("#productList").append("<span id='label"+id+"'>"+identifier+"</span>&nbsp;");
			$("#productList").append("<a id='remove"+id+"' class='removeLink' href='#' onclick='removeLink(this)' productId='"+id+"'>Remove</a>");
			$("#productList").append("<br id='br"+id+"'>");
		}
		
		function removeLink(elem){
			var id = $(elem).attr("productId");
			$("#br"+id).remove()
			$("#label"+id).remove()
			$("#remove"+id).remove()
			$("#product"+id).remove()
		}
	</script>
</head>
<body>
  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
    <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
    <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
  </div>
  <div class="body">
    <h1><g:message code="default.editMultiple.label" args="[entityName]" default="Edit Multiple Products"/></h1>
    <g:if test="${flash.message}">
      <div class="message">${flash.message}</div>
    </g:if>
    <g:hasErrors bean="${productInstance}">
      <div class="errors">
        <g:renderErrors bean="${productInstance}" as="list" />
      </div>
    </g:hasErrors>
    <g:form method="post" >
    	<g:hiddenField name="id" value="${approval.id}"/>
      <div class="dialog">
	<div id="productList">
		<h4>Products to be Updated:
		</h4>
        <table class="list">
        	<thead>
        		<tr>
        			<th>Item Code</th>
        			<th>Item Description</th>
        			<th>Field</th>
        			<th>Old Value</th>
        			<th>New Value</th>
        		</tr>
        	</thead>
        	<tbody>
        	<g:each in="${approvalProducts}" var="product">
        		<tr>
        			<td class="product${product.id}">${product.product.identifier}</td>
        			<td class="product${product.id}">${product.product.formatDescription()}</td>
        			<g:set var="counter" value="${0}"/>
		        	<g:each in="${fields}" var="field">
		        		<g:if test="${product[field]!=null}">
							<g:if test="${counter>0}"><tr></g:if>		        		
        				<td><strong>${fieldLabels[field]}</strong></td>
        				<td>${product.product[field]}</td>
        				<td>${product[field]}</td>
        				</tr>
	        			<g:set var="counter" value="${counter+1}"/>
        				</g:if>
        			</g:each>
        		</tr>
			<script>
				$("td.product${product.id}").attr("rowspan","${counter}");
			</script>
        	</g:each>
        	</tbody>
        </table>
	</div>
      <g:ifAnyGranted role="ROLE_MANAGER_SALES">
      <div class="buttons">
        <span class="button"><g:actionSubmit class="save" action="approveUpdateMultiple" value="${message(code: 'default.button.approve.label', default: 'Approve')}" onclick="return confirm('Are you sure you want to approve this update?')"/></span>
        <span class="button"><g:actionSubmit class="delete" action="rejectUpdateMultiple" value="${message(code: 'default.button.reject.label', default: 'Reject')}" onclick="return confirm('Are you sure you want to reject this update?')"/></span>
      </div>
      </g:ifAnyGranted>
    </g:form>
  </div>
</body>
</html>
