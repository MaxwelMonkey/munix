
<%@ page import="com.munix.Supplier" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'supplier.label', default: 'Supplier')}" />
  <title><g:message code="default.show.label" args="[entityName]" /></title>
  <g:javascript src="jquery-1.4.1.min.js" />
	    <g:javascript src="table/jquery.dataTables.js" />
  <g:javascript>
    <munix:selectorConfig />
    var $ = jQuery.noConflict()
    var addSelectorListener = function(){
        $("#item").click(function(){
            $("#selector").toggle()
        })
    }

    function fillData(data){
        var arr = data.split('||');
        $('#product\\.id').val(arr[0]);
        $('#item').val(arr[1]);
        $('#selector').hide();
    }

    $(document).ready(
        function(){
            $("#selector").hide()
            addSelectorListener()
	    	$("#itemsTable").dataTable({
	    		"aaSorting": [[ 2, "asc" ]],
				"bPaginate": false, 
				"bFilter": false
	    	});
        }
    )


  </g:javascript>
</head>
<body>
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
    <div class="dialog">
      <table>
        <tbody>

          <tr class="prop">
            <td valign="top" class="name"><g:message code="supplier.identifier.label" default="Identifier" /></td>
        <td valign="top" class="value">${fieldValue(bean: supplierInstance, field: "identifier")}</td>
        <td class="name"></td>
        <td class="value"></td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="supplier.name.label" default="Name" /></td>
        <td valign="top" class="value">${fieldValue(bean: supplierInstance, field: "name")}</td>
        <td class="name"><g:message code="supplier.localType.label" default="Locale Type" /></td>
        <td class="value">${fieldValue(bean: supplierInstance, field: "localeType")}</td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="supplier.country.label" default="Country" /></td>
        <td valign="top" class="value">${supplierInstance?.country?.encodeAsHTML()}</td>
        <td class="name"></td>
        <td class="value"></td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="supplier.address.label" default="Address" /></td>
        <td valign="top" class="value">${supplierInstance?.address?.encodeAsHTML()}</td>
        <td class="name"></td>
        <td class="value"></td>
        </tr>

        <tr class="prop">
          <td class="name"></td>
          <td class="value"></td>
          <td class="name"></td>
          <td class="value"></td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="supplier.tin.label" default="TIN" /></td>
        <td valign="top" class="value">${fieldValue(bean: supplierInstance, field: "tin")}</td>
        <td class="name"></td>
        <td class="value"></td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="supplier.term.label" default="Terms" /></td>
        <td valign="top" class="value">${supplierInstance?.term?.encodeAsHTML()}</td>
        <td class="name"></td>
        <td class="value"></td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="supplier.currency.label" default="Currency" /></td>
        <td valign="top" class="value">${supplierInstance?.currency?.encodeAsHTML()}</td>
        <td class="name"></td>
        <td class="value"></td>
        </tr>

        <tr class="prop">
          <td class="name"></td>
          <td class="value"></td>
          <td class="name"></td>
          <td class="value"></td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="supplier.contact.label" default="Contact" /></td>
        <td valign="top" class="value">${fieldValue(bean: supplierInstance, field: "contact")}</td>
        <td valign="top" class="name"><g:message code="supplier.email.label" default="Email" /></td>
        <td valign="top" class="value">${fieldValue(bean: supplierInstance, field: "email")}</td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="supplier.landline.label" default="Landline" /></td>
        <td valign="top" class="value">${fieldValue(bean: supplierInstance, field: "landline")}</td>
        <td valign="top" class="name"><g:message code="supplier.skype.label" default="Skype" /></td>
        <td valign="top" class="value">${fieldValue(bean: supplierInstance, field: "skype")}</td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="supplier.fax.label" default="Fax" /></td>
        <td valign="top" class="value">${fieldValue(bean: supplierInstance, field: "fax")}</td>
        <td valign="top" class="name"><g:message code="supplier.yahoo.label" default="Yahoo" /></td>
        <td valign="top" class="value">${fieldValue(bean: supplierInstance, field: "yahoo")}</td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="supplier.website.label" default="Website" /></td>
        <td valign="top" class="value">${fieldValue(bean: supplierInstance, field: "website")}</td>
        <td class="name"></td>
        <td class="value"></td>
        </tr>

        </tbody>
      </table>
    </div>

    <div class="buttons">
      <g:form>
        <g:hiddenField name="id" value="${supplierInstance?.id}" />
        <g:if test="${showEditButton}">
            <g:ifAnyGranted role="ROLE_PURCHASING">
              <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
            </g:ifAnyGranted>
		</g:if>
		<span class="button"><g:actionSubmit class="edit" action="editSupplierItems" value="${message(code: 'default.button.editSupplierItems.label', default: 'Edit Supplier Items')}" /></span>
      </g:form>
    </div>
    
    <div class="subTable">
      <table id="itemsTable">
        <thead>
          <tr style="cursor:pointer">
            <th class="center">Identifier</th>
            <th class="center">Supplier's Code</th>
            <th class="center">Description</th>
            <th class="center">Status</th>
          </tr>
        </thead>
        <tbody id="items" class="editable">
        <g:each in="${supplierInstance.items?.sort{it?.id}}" var="i" status="colors">
          <tr class="${(colors % 2) == 0 ? 'odd' : 'even'}" >
            <td>${i?.product}</td>
            <td>${i?.productCode}</td>
            <td>${i?.product?.description}</td>
            <td>${i?.status}</td>
          </tr>
        </g:each>
        </tbody>
      </table>
    </div>

  </div>
</body>
</html>
