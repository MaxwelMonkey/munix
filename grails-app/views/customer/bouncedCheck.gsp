
<%@ page import="com.munix.Customer" %>
<%@ page import="com.munix.CustomerDiscountLog" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'customer.label', default: 'Customer')}" />
    <title><g:message code="default.show.label" args="[entityName]" /></title>
    <g:javascript src="jquery.dataTables.js" />
    <script type="text/javascript">
        var $=jQuery.noConflict()
        $(document).ready(function(){
            $("#bouncedCheck").dataTable({
              			"bAutoWidth": false,
              			"bPaginate": false,
						"aaSorting": [[ 2, "asc" ]]
            })
        })


  </script>
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
    <div class="dialog">
      <table>
        <tbody>

          <tr class="prop">
            <td valign="top" class="name"><g:message code="customer.identifier.label" default="Identifier" /></td>
        <td valign="top" class="value">${fieldValue(bean: customerInstance, field: "identifier")}</td>
        <td class="name"></td>
        <td class="value"></td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="customer.name.label" default="Name" /></td>
        <td valign="top" class="value">${fieldValue(bean: customerInstance, field: "name")}</td>
        <td class="name"></td>
        <td class="value"></td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="customer.owner.label" default="Owner" /></td>
        <td valign="top" class="value">${customerInstance?.owner?.encodeAsHTML()}</td>
        <td class="name"></td>
        <td class="value"></td>
        </tr>


        <tr class="prop">
          <td valign="top" class="name"><g:message code="customer.salesAgent.label" default="Sales Agent" /></td>
        <td valign="top" class="value"><g:link controller="salesAgent" action="show" id="${customerInstance?.salesAgent?.id}">${customerInstance?.salesAgent?.encodeAsHTML()}</g:link></td>
        <td valign="top" class="name"><g:message code="customer.commissionRate.label" default="Commission Rate" /></td>
        <td valign="top" class="value">${customerInstance?.formatCommissionRate()}</td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="customer.accountNumber.label" default="Account Number" /></td>
        <td valign="top" class="value">${customerInstance?.accountNumber?.encodeAsHTML()}</td>
        <td valign="top" class="name"><g:message code="customer.collectionPreference.label" default="Collection Preference" /></td>
        <td valign="top" class="value">${customerInstance?.collectionPreference?.encodeAsHTML()}</td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="customer.accountName.label" default="Account Name" /></td>
        <td valign="top" class="value">${customerInstance?.accountName?.encodeAsHTML()}</td>
        <td valign="top" class="name"><g:message code="customer.collectionSchedule.label" default="Collection Schedule" /></td>
        <td valign="top" class="value">${customerInstance?.collectionSchedule?.encodeAsHTML()}</td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="customer.status.label" default="Status" /></td>
        <td valign="top" class="value">${fieldValue(bean: customerInstance, field: "status")}</td>
        <td class="name"></td>
        <td class="value"></td>
        </tr>

        </tbody>
      </table>
    </div>

    <div class="subTable">
      <table id="bouncedCheck">
        <h2>Bounced Check</h2>
        <thead>
          <tr>
            <th>DP Reference#</th>
            <th>BC Reference#</th>
            <th>Date of Check</th>
            <th>Bank</th>
            <th>Branch</th>
            <th>Amount</th>
            <th>Status</th>
          </tr>
        </thead>
        <tbody>
        <g:each in="${customerInstance?.bouncedChecks.sort{it.id}}" var="i">
          <g:each in="${i.checks}" var="j" status="colors">
            <tr class="${(colors % 2) == 0 ? 'odd' : 'even'}" onclick="window.location='${createLink(uri:'/')}bouncedCheck/show/${i.id}'">
              <td>${j?.directPayment?.toString()}</td>
              <td>${i?.toString()}</td>
              <td><g:formatDate date="${j?.date}" format="MM/dd/yyyy"/></td>
              <td>${j?.bank}</td>
              <td>${j?.branch}</td>
              <td>${j?.amount}</td>
              <td>${j?.status}</td>
            </tr>
          </g:each>
        </g:each>
        </tbody>
      </table>
    </div>

  </div>
</body>
</html>
