<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'bouncedCheck.label', default: 'BouncedCheck')}" />
  <title><g:message code="default.create.label" args="[entityName]" /></title>
  <g:javascript>
    var $ = jQuery.noConflict()
    function checkFields(fields, check){
        if(check.checked){
            checkAll(fields)
        }else{
            uncheckAll(fields)
        }
    }
    function checkAll(field)
    {
        for (i = 0; i < field.length; i++)
            field[i].checked = true ;
    }

    function uncheckAll(field)
    {
        for (i = 0; i < field.length; i++)
            field[i].checked = false ;
    }
  </g:javascript>
</head>
<body>
  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
    <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
  </div>
  <div class="body">
    <h1><g:message code="default.create.label" args="[entityName]" /></h1>
    <g:if test="${flash.message}">
      <div class="message">${flash.message}</div>
    </g:if>
    <g:if test="${flash.error}">
      <div class="errors">${flash.error}</div>
    </g:if>
    <g:hasErrors bean="${bouncedCheckInstance}">
      <div class="errors">
        <g:renderErrors bean="${bouncedCheckInstance}" as="list" />
      </div>
    </g:hasErrors>
    <g:form action="save" method="post" >
      <div class="dialog">
        <table>
          <tbody>
            <tr class="prop">
              <td valign="top" class="name">
                <label for="customer"><g:message code="bouncedCheck.customer.label" default="Customer" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: bouncedCheckInstance, field: 'customer', 'errors')}">
                ${customer}
                <g:hiddenField name="customer.id" value="${customer.id}" />
                <g:hiddenField name="id" value="${customer.id}" />
              </td>
            </tr>
            <tr class="prop">
              <td valign="top" class="name">
                <label for="remark"><g:message code="bank.remark.label" default="Remarks" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: bouncedCheckInstance, field: 'remark', 'errors')}">
                <g:textArea name="remark" value="${bouncedCheckInstance?.remark}" />
              </td>
            </tr>
            <tr class="prop">
              <td valign="top" class="name">
                <label for="remark">Status</label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: bouncedCheckInstance, field: 'status', 'errors')}">
                <g:select name="bouncedCheckStatus.id" from="${statuses}" optionKey="id" optionValue="description" value="${bouncedCheckInstance?.bouncedCheckStatus?.id}"/>
              </td>
            </tr>
            <tr class="prop">
              <td valign="top" class="name">
                <label for="remark">For Redeposit</label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: bouncedCheckInstance, field: 'forRedeposit', 'errors')}">
                <g:checkBox name="forRedeposit" value="${bouncedCheckInstance?.forRedeposit}"/>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
      <div class="subtable">
          <table>
              <thead>
                <th class="center"><g:checkBox name="checkAll" onClick="checkFields(document.getElementsByName('check'), this)"/></th>
                <th>Check Number</th>
                <th>Bank</th>
                <th class="center">Date</th>
                <th class="right">Amount</th>
              </thead>
              <tbody>
                <g:each in="${checkPayments}" var="i" >
                  <tr>
                    <td class="center"><g:checkBox name="check" value="${i.id}" checked="false"/></td>
                    <td>${i.checkNumber}</td>
                    <td>${i.bank} - ${i?.branch}</td>
                    <td class="center"><g:formatDate date="${i?.date}" format="MM/dd/yyyy"/></td>
                    <td class="right">${i?.formatAmount()}</td>
                  </tr>
                </g:each>
              </tbody>
          </table>
      </div>
      <div class="buttons">
        <span class="button"><g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" /></span>
      </div>
    </g:form>
  </div>
</body>
</html>
