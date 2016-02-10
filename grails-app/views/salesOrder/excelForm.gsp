
<%@ page import="com.munix.Product" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'salesOrder.label', default: 'SalesOrder')}" />
    <title>Download Sales Order Form</title>
</head>
<body>
  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
    <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
    <span class="menuButton"><g:link class="create" action="create">Create</g:link></span>
    <span class="menuButton"><g:link class="create" action="upload">Create (from SO Form)</g:link></span>
    <span class="menuButton"><g:link class="create" action="excelForm">Download SO Form</g:link></span>
  </div>
  <div class="body">
    <h1>Download Sales Order Form</h1>
    <g:if test="${flash.message}">
      <div class="message">${flash.message}</div>
    </g:if>
    <div class="dialog">
      <g:form controller="salesOrder" action="generateExcel" >
      	<input type="hidden" name="order" value="identifier">
        <table>
          <tr>
            <td class="name">Category</td>
            <td class="value"><g:select name="searchCategory" from="${categories}" optionKey="identifier" noSelection="['':'All']" value ="${params.searchCategory}"/></td>
          </tr>
          <tr>
            <td class="name">Subcategory</td>
            <td class="value"><g:select name="searchSubcategory" from="${subcategories}" optionKey="identifier" noSelection="['':'All']" value ="${params.searchSubcategory}"/></td>
          </tr>
          <tr>
            <td class="name">Item Type</td>
            <td class="value"><g:select name="searchItemType" from="${itemTypes}" optionKey="identifier" noSelection="['':'All']" value="${params.searchItemType}" /></td>
          </tr>
          <tr>
            <td class="name">Discount Type</td>
            <td class="value"><g:select name="searchDiscountType" from="${discountTypes}" optionKey="identifier" noSelection="['':'All']" value="${params.searchDiscountType}" /></td>
          </tr>
          <tr>
            <td class="name">Is Net</td>
            <td class="value"><g:select name="searchIsNet" from="${['True','False']}" noSelection="['':'All']" value="${params.searchIsNet}" /></td>
          </tr>
          <tr>
            <td class="name">Is For Sale</td>
            <td class="value"><g:select name="searchIsForSale" from="${['True','False']}" noSelection="['':'All']" value="${'True'}" /></td>
          </tr>
          <tr>
            <td class="name">Is Component</td>
            <td class="value"><g:select name="searchIsComponent" from="${['True','False']}" noSelection="['':'All']" value="${params.searchIsComponent}" /></td>
          </tr>
          <tr>
            <td class="name">Is Assembly</td>
            <td class="value"><g:select name="searchIsAssembly" from="${['True','False']}" noSelection="['':'All']" value="${params.searchIsAssembly}" /></td>
          </tr>
          <tr>
            <td class="name">Inventory Balance</td>
            <td class="value"><g:select name="inventoryBalance" from="${['>0','=0','<0']}" noSelection="['':'']" value="${params.inventoryBalance}" /></td>
          </tr>
          <tr>
            <td class="name">Wholesale/Retail Price</td>
            <td class="value"><g:select name="wholesaleRetail" from="${['Wholesale','Retail']}" value="${params.wholesaleRetail}" /></td>
          </tr>
        </table>
        <div class="buttons">
          <input type="submit" class="save" value="Generate Excel Form"/>
        </div>

      </g:form>
    </div>
  </div>
</body>
</html>
