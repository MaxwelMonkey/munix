
<%@ page import="com.munix.CheckDeposit" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />

    <g:set var="entityName" value="${message(code: 'checkDeposit.label', default: 'CheckDeposit')}" />
    <title><g:message code="default.create.label" args="[entityName]" /></title>
      <g:javascript src="jquery.ui.core.js" />
      <g:javascript src="jquery.ui.datepicker.js" />
      <link rel="stylesheet" href="${resource(dir:'css',file:'jquery.ui.all.css')}" />
      <style type="text/css">
        .hidden {
            display: none;
        }
        .filter {
            display: none;
        }
      </style>
      <script language="JavaScript">
        var $ = jQuery.noConflict()
        function checkFields(fields, check) {
            if(check.checked) {
                checkAll(fields)
            } else {
                uncheckAll(fields)
            }
        }
        
        function checkAll(field) {
            for (i = 0; i < field.length; i++){
                if(!$(field[i]).parents().hasClass("filter")){
                    field[i].checked = true ;
                }
            }
        }

        function uncheckAll(field) {
            for (i = 0; i < field.length; i++)
                if(!$(field[i]).parents().hasClass("filter")){
                    field[i].checked = false ;
                }
        }
        function filter(){
            var startDate = new Date($("#startDate").val())
            var endDate = new Date($("#endDate").val())
            $(".subTable tbody tr").each(function () {
                if(!$(this).hasClass("hidden")){
                    var textDate= new Date($("td.date",this).text())
                    if($("td.checkNumber",this).text().search(new RegExp($("#checkNumber").val(), "i")) < 0 && $("#checkNumber").val()!="") {
                        $(this).addClass("filter");
                    } else if(($("#startDate").val()!=""||$("#endDate").val()!="")&&(startDate>textDate||endDate<textDate)){
                        $(this).addClass("filter");
                    }else if($("td.bank",this).text().search(new RegExp($("#bank").val(), "i")) != 0 && $("#bank").val()!=""){
                        $(this).addClass("filter");
                    }else if($("td.customer",this).text().search(new RegExp($("#customer").val(), "i")) < 0 && $("#customer").val()!=""){
                        $(this).addClass("filter");
                    }else if($("td.bank",this).text().search(new RegExp($("#branch").val(), "i")) < 0 && $("#branch").val()!=""){
                        $(this).addClass("filter");
                    }else if($("td.type",this).text().search(new RegExp($("#type").val(), "i")) != 0 && $("#type").val()!=""){
                        $(this).addClass("filter");                        
                    }else{
                        $(this).removeClass("filter");
                    }
                }
            });
        }
        $("document").ready(function(){
            $("#startDate").datepicker()
            $("#endDate").datepicker()
            $("#checkNumber").keyup(function () {
                filter()
            });
            $("#bank").change(function () {
                filter()
            });
            $("#customer").keyup(function () {
                filter()
            });
            $("#branch").keyup(function () {
                filter()
            });
            $("#type").change(function () {
                filter()
            });
            $("#startDate").change(function () {
                filter()
            });
            $("#endDate").change(function () {
                filter()
            });

        })
    </script>
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
    <g:hasErrors bean="${checkDepositInstance}">
      <div class="errors">
        <g:renderErrors bean="${checkDepositInstance}" as="list" />
      </div>
    </g:hasErrors>
    <g:form action="save" method="post" >
      <div class="dialog">
        <table>
          <tbody>

            <tr class="prop">
              <td valign="top" class="name">
                <label for="account"><g:message code="checkDeposit.account.label" default="Account" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: checkDepositInstance, field: 'account', 'errors')}">
          <g:select name="account.id" from="${com.munix.BankAccount.list().sort{it.toString()}}" optionKey="id" value="${checkDepositInstance?.account?.id}"  />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="depositDate"><g:message code="checkDeposit.depositDate.label" default="Deposit Date" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: checkDepositInstance, field: 'depositDate', 'errors')}">
          <g:datePicker name="depositDate" precision="day" value="${checkDepositInstance?.depositDate}"  />
          </td>
          </tr>


          <tr class="prop">
            <td valign="top" class="name">
              <label for="billsPurchase"><g:message code="checkDeposit.billsPurchase.label" default="Bills Purchase" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: checkDepositInstance, field: 'billsPurchase', 'errors')}">
          <g:checkBox name="billsPurchase" value="${checkDepositInstance?.billsPurchase}" />
          </td>
          </tr>

          </tbody>
        </table>
      </div>

      <h2>Checks</h2>
      <h2>Search Fields</h2>
      <div class="dialog">
        <table>
          <tr>
            <td>Check Number</td>
            <td><g:textField id="checkNumber" name="checknum"/></td>
          </tr>
          <tr>
            <td>Start Date</td>
            <td><g:textField name="startDate" id="startDate"/></td>
          </tr>
          <tr>
            <td>End Date</td>
            <td><g:textField name="endDate" id="endDate"/></td>
          </tr>
          <tr>
            <td>Customer</td>
            <td><g:textField name="customer" id="customer"/></td>
          </tr>
          <tr>
            <td>Bank</td>
            <td><g:select from="${com.munix.Bank.list().sort{it.toString()}}" id="bank" name="bank" optionKey="identifier" noSelection='["":"All Banks"]'/></td>
          </tr>
          <tr>
            <td>Branch</td>
            <td><g:textField id="branch" name="branch"/></td>
          </tr>
          <tr>
            <td>Type</td>
            <td><g:select from='["Local","Regional"]' id="type" name="type"  noSelection='["":"All Type"]'/></td>
          </tr>
        </table>
      </div>
      <div class="subTable">
      <table>
        <thead>
          <tr>
          	<th class="center"><g:checkBox name="selectAll" checked= "false"  onClick="checkFields(document.getElementsByName('checks'), this)"/></th>
            <th>Check Number</th>
            <th>Check Date</th>
            <th>Customer</th>
            <th>Bank - Branch</th>
            <th>Type</th>
            <th class="right">Amount</th>
          </tr>
        </thead>
        <tbody class="checkTable">
            <g:each in="${checkPayments}" var="i">
              <tr>
                <td class="center"><g:checkBox name="checks" value="${i.id}" checked="false"/></td>
                <td class="checkNumber">${i.checkNumber}</td>
                <td class="date"><g:formatDate date="${i?.date}" format="MM/dd/yyyy"/></td>
                <td class="customer">${i.customer}</td>
                <td class="bank">${i.bank.identifier} - ${i?.branch}</td>
                <td class="type">${i.type.description}</td>
                <td class="right">${i.formatAmount()}</td>
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
