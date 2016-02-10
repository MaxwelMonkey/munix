<%@ page import="com.munix.*" %>
    <div class="dialog" id="filters">
      <g:form name="searchForm" action="${action}" method="post" onsubmit="return filterChecker()">
      <h2>Filters</h2>
      
        <g:if test="${reportType}">
        	<input type="hidden" name="reportType" value="${reportType}">
        </g:if>
        <table>
        <g:if test="${!reportType}">
          <tr class="prop">
            <td class="name">Report Type</td>
            <td class="value">
	          <select name="reportType" onchange="if(this.value=='inventoryReport') { $('#asOfDateRow').show(); } else {$('#asOfDateRow').hide() }" >
                <g:each in="${reportTypes}" status="i" var="reportType">
                	<g:if test="${reportType=='productListCost' || reportType=='inventoryReport'}">
			        <g:ifAnyGranted role="ROLE_SUPER">
		          	<option value="${reportType}">${reportTypeLabels[i]}</option>
		          	</g:ifAnyGranted>
		          	</g:if>
		          	<g:else>
		          	<option value="${reportType}">${reportTypeLabels[i]}</option>
		          	</g:else>
	          	</g:each>
	          </select>
            </td>
          </tr>
          </g:if>
          <tr class="prop" id="asOfDateRow" style="display:none">
            <td class="name">As Of Date</td>
            <td class="value">
	          <g:datePicker name="asOfDate" precision="day" noSelection="['': '']" />
            </td>
          </tr>
          <tr class="prop">
            <td class="name">Wholesale/Retail</td>
            <td class="value">
	          <select name="priceType">
	          	<option value="Wholesale" selected>Wholesale</option>
	          	<option value="Retail">Retail</option>
	          </select>
            </td>
          </tr>
          <tr class="prop">
            <td class="name">Status</td>
            <td class="value">
	          <select name="status">
	          	<option value="">All</option>
	          	<option value="Active"<g:if test="${params.status=='Active'}"> selected</g:if>>Active</option>
	          	<option value="Inactive"<g:if test="${params.status=='Inactive'}"> selected</g:if>>Inactive</option>
	          </select>
            </td>
          </tr>
          <tr class="prop">
            <td class="name">Currency</td>
            <td class="value">
	          <select name="currency">
	          	<option value="">All</option>
	          	<g:each in="${CurrencyType.list()}" var="currency">
	          	<option value="${currency.identifier}">${currency.identifier + '-' + currency.description}</option>
	          	</g:each>
	          </select>
            </td>
          </tr>
          <tr class="prop">
            <td class="name">Component</td>
            <td class="value">
            	<input type="checkbox" name="component" value="Y"/>
            </td>
          </tr>
          <tr class="prop">
            <td class="name">Net Price</td>
            <td class="value">
            	<input type="checkbox" name="isNet" value="Y"/>
            </td>
          </tr>
          <tr class="prop">
            <td class="name">For Sale only</td>
            <td class="value">
            	<input type="checkbox" name="forSale" value="Y"/>
            </td>
          </tr>
          <tr class="prop">
            <td class="name">Is For Assembly</td>
            <td class="value">
            	<input type="checkbox" name="forAssembly" value="Y"/>
            </td>
          </tr>
          <tr class="prop">
            <td class="name">Balance</td>
            <td class="value">
            	<g:select name="balance" id="balance" from="${['<0','>0','=0'] }" noSelection="${['null':'']}"/>
            </td>
          </tr>
		<g:render template="/salesReport/multicheckFilter" model="${['label':'Filter from Warehouse','name':'warehouse','field':'warehouse.id','list':com.munix.Warehouse.findAll([sort:'identifier'])]}"/>
		<g:render template="/salesReport/multicheckFilter" model="${['label':'Supplier','name':'supplier','field':'supplier.id','list':com.munix.Supplier.findAll([sort:'identifier'])]}"/>
		<g:render template="/salesReport/multicheckFilter" model="${['label':'Discount Type','name':'discountType','field':'type.id','list':com.munix.DiscountType.findAll([sort:'description'])]}"/>
		<g:render template="/salesReport/multicheckFilter" model="${['label':'Category','name':'category','field':'category.id','list':com.munix.ProductCategory.findAll([sort:'identifier'])]}"/>
		<g:render template="/salesReport/multicheckFilter" model="${['label':'Subcategory','name':'subcategory','field':'subcategory.id','list':com.munix.ProductSubcategory.findAll([sort:'identifier'])]}"/>
		<g:render template="/salesReport/multicheckFilter" model="${['label':'Brand','name':'brand','field':'brand.id','list':com.munix.ProductBrand.findAll([sort:'identifier'])]}"/>
		<g:render template="/salesReport/multicheckFilter" model="${['label':'Model','name':'model','field':'model.id','list':com.munix.ProductModel.findAll([sort:'identifier'])]}"/>
		<g:render template="/salesReport/multicheckFilter" model="${['label':'Item Type','name':'itemType','field':'itemType.id','list':com.munix.ItemType.findAll([sort:'identifier'])]}"/>
        </table>
      
        <div>
          <input type="submit" class="button" value="Run"/>
        </div>

      </g:form>
    </div>
		