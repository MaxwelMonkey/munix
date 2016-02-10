          <tr class="prop">
            <td class="name">Report Type</td>
            <td class="value">
	          <select name="reportType">
                <g:each in="${reportTypes}" status="i" var="reportType">
		          	<option value="${reportType}">${reportTypeLabels[i]}</option>
	          	</g:each>
	          </select>
            </td>
          </tr>
          <tr class="prop">
            <td class="name">Date</td>
            <td class="value">
	          From <g:datePicker name="dateFrom" precision="day" noSelection="['': '']" />
	          To <g:datePicker name="dateTo" precision="day" noSelection="['': '']" />
            </td>
          </tr>
		<g:render template="/salesReport/multicheckFilter" model="${['label':'Customer','name':'customer','field':'customer.id','list':com.munix.Customer.findAll([sort:'identifier'])]}"/>
		<g:render template="/salesReport/multicheckFilter" model="${['label':'Sales Agent','name':'salesAgent','field':'salesAgent.id','list':com.munix.SalesAgent.findAll([sort:'identifier'])]}"/>
		<g:render template="/salesReport/multicheckFilter" model="${['label':'Discount Type','name':'discountType','field':'type.id','list':com.munix.DiscountType.findAll([sort:'description'])]}"/>
		<g:render template="/salesReport/multicheckFilter" model="${['label':'Product','name':'product','field':'product.id','list':com.munix.Product.findAll([sort:'identifier'])]}"/>
		<g:render template="/salesReport/multicheckFilter" model="${['label':'Category','name':'category','field':'product.category.id','list':com.munix.ProductCategory.findAll([sort:'identifier'])]}"/>
		<g:render template="/salesReport/multicheckFilter" model="${['label':'Subcategory','name':'subcategory','field':'product.subcategory.id','list':com.munix.ProductSubcategory.findAll([sort:'identifier'])]}"/>
		<g:render template="/salesReport/multicheckFilter" model="${['label':'Brand','name':'brand','field':'product.brand.id','list':com.munix.ProductBrand.findAll([sort:'identifier'])]}"/>
		<g:render template="/salesReport/multicheckFilter" model="${['label':'Model','name':'model','field':'product.model.id','list':com.munix.ProductModel.findAll([sort:'identifier'])]}"/>
		<g:render template="/salesReport/multicheckFilter" model="${['label':'Item Type','name':'itemType','field':'product.itemType.id','list':com.munix.ItemType.findAll([sort:'identifier'])]}"/>
