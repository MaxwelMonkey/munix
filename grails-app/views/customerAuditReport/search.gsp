
<%@ page import="com.munix.*" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
    <g:javascript library="reports/filter" />
    <link rel="stylesheet" href="${resource(dir:'css/reports',file:'filter.css')}" />
  <title>Customer Audit Report</title>
</head>
<body>
  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
  </div>
  <div class="body">
    <h1>Customer Audit Report</h1>
    <div class="dialog">
      <g:form name="searchForm" method="post" action="list">
      <h2>Filters</h2>
      
        <table>
          <tr class="prop">
            <td class="name">Date</td>
            <td class="value">
	          From <g:datePicker name="dateFrom" precision="day" noSelection="['': '']" />
	          To <g:datePicker name="dateTo" precision="day" noSelection="['': '']" />
            </td>
          </tr>
		<g:render template="/salesReport/multicheckFilter" model="${['label':'Customer','name':'customer','field':'customer.id','list':com.munix.Customer.findAll([sort:'identifier'])]}"/>
          <tr class="prop">
            <td class="name">Field</td>
            <td class="value">
            	<input type="checkbox" class="checkAll" onclick="checkAll('field', this.checked)" id="fieldCheckAll"> <label for="fieldCheckAll">Check All</label>
            	<div class="multicheckbox">
	    			<g:each in="${fields.keySet()}" var="field">
        			<div class="checkbox"><input class="fieldCheckbox" type="checkbox" name="field" id="field_${field}" value="${field}"> <label for="field${field}">${fields[field]}</label></div>
        			</g:each>
            	</div>
            </td>
          </tr>
          <tr class="prop">
            <td class="name">User</td>
            <td class="value">
            	<input type="checkbox" class="checkAll" onclick="checkAll('user', this.checked)" id="userCheckAll"> <label for="userCheckAll">Check All</label>
            	<div class="multicheckbox">
        			<g:each in="${com.munix.User.findAll([sort:'userRealName'])}" var="user">
        			<div class="checkbox"><input class="userCheckbox" type="checkbox" name="user.id" id="user${user.id}" value="${user.id}"> <label for="user${user.id}">${user.userRealName}</label></div>
        			</g:each>
            	</div>
            </td>
          </tr>
        </table>
      
        <div>
          <input type="submit" class="button" value="Run"/>
        </div>

      </g:form>
    </div>
  </div>
</body>
</html>
