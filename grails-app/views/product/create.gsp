
<%@ page import="com.munix.Product" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'product.label', default: 'Product')}" />
  <title><g:message code="default.create.label" args="[entityName]" /></title>
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
    <g:hasErrors bean="${productInstance}">
      <div class="errors">
        <g:renderErrors bean="${productInstance}" as="list" />
      </div>
    </g:hasErrors>
    <g:form action="save" method="post" enctype="multipart/form-data">
      <div class="dialog">
        <table>
          <tbody>

            <tr class="prop">
              <td valign="top" class="name">
                <label for="photo"><g:message code="product.photo.label" default="Photo" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: productInstance, field: 'photo', 'errors')}">
                <input type="file" id="photo" name="photo" />
              </td>
            </tr>

            <tr class="prop">
              <td valign="top" class="name">
                <label for="identifier"><g:message code="product.identifier.label" default="Identifier" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: productInstance, field: 'identifier', 'errors')}">
          <g:textField name="identifier" value="${productInstance?.identifier}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="type"><g:message code="product.type.label" default="Discount Type" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: productInstance, field: 'type', 'errors')}">
          <g:select name="type.id" from="${com.munix.DiscountType.list().sort{it.id}}" optionKey="id" value="${productInstance?.type?.id}" noSelection="['null': '']" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="itemType"><g:message code="product.itemType.label" default="Item Type" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: productInstance, field: 'itemType', 'errors')}">
          <g:select name="itemType.id" from="${com.munix.ItemType.list().sort{it.id}}" optionKey="id" value="${productInstance?.itemType?.id}" noSelection="['null': '']" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="unit"><g:message code="product.unit.label" default="Unit" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: productInstance, field: 'unit', 'errors')}">
          <g:select name="unit.id" from="${com.munix.ProductUnit.list().sort{it.toString()}}" optionKey="id" value="${productInstance?.unit?.id}" noSelection="['null': '']" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="category"><g:message code="product.category.label" default="Category" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: productInstance, field: 'category', 'errors')}">
          <g:select name="category.id" from="${com.munix.ProductCategory.list().sort{it.toString()}}" optionKey="id" value="${productInstance?.category?.id}" noSelection="['null': '']" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="subcategory"><g:message code="product.subcategory.label" default="Subcategory" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: productInstance, field: 'subcategory', 'errors')}">
          <g:select name="subcategory.id" from="${com.munix.ProductSubcategory.list().sort{it.toString()}}" optionKey="id" value="${productInstance?.subcategory?.id}" noSelection="['null': '']" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="brand"><g:message code="product.brand.label" default="Brand" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: productInstance, field: 'brand', 'errors')}">
          <g:select name="brand.id" from="${com.munix.ProductBrand.list().sort{it.toString()}}" optionKey="id" value="${productInstance?.brand?.id}" noSelection="['null': '']" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="model"><g:message code="product.model.label" default="Model" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: productInstance, field: 'model', 'errors')}">
          <g:select name="model.id" from="${com.munix.ProductModel.list().sort{it.toString()}}" optionKey="id" value="${productInstance?.model?.id}" noSelection="['null': '']" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="modelNumber"><g:message code="product.modelNumber.label" default="Model Number" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: productInstance, field: 'modelNumber', 'errors')}">
          <g:textField name="modelNumber" value="${productInstance?.modelNumber}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="material"><g:message code="product.material.label" default="Material" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: productInstance, field: 'material', 'errors')}">
          <g:select name="material.id" from="${com.munix.ProductMaterial.list().sort{it.toString()}}" optionKey="id" value="${productInstance?.material?.id}" noSelection="['null': '']" />
          </td>
          </tr>
 
          <tr class="prop">
            <td valign="top" class="name">
              <label for="size"><g:message code="product.size.label" default="Size" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: productInstance, field: 'size', 'errors')}">
          <g:textField name="size" value="${productInstance?.size}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="addedDescription"><g:message code="product.addedDescription.label" default="Added Description" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: productInstance, field: 'addedDescription', 'errors')}">
          <g:textField name="addedDescription" value="${productInstance?.addedDescription}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="packageDetails"><g:message code="product.packageDetails.label" default="Package Details" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: productInstance, field: 'packageDetails', 'errors')}">
          <g:textField name="packageDetails" value="${productInstance?.packageDetails}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="color"><g:message code="product.color.label" default="Color" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: productInstance, field: 'color', 'errors')}">
          <g:select name="color.id" from="${com.munix.ProductColor.list().sort{it.toString()}}" optionKey="id" value="${productInstance?.color?.id}" noSelection="['null': '']" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="partNumber"><g:message code="product.partNumber.label" default="Part Number" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: productInstance, field: 'partNumber', 'errors')}">
          <g:textField name="partNumber" value="${productInstance?.partNumber}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="price"><g:message code="product.retailPrice.label" default="Retail Price" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: productInstance, field: 'retailPrice', 'errors')}">
          <g:textField name="retailPrice" value="${productInstance?.retailPrice}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="price"><g:message code="product.wholeSalePrice.label" default="Wholesale Price" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: productInstance, field: 'wholeSalePrice', 'errors')}">
            <g:textField name="wholeSalePrice" value="${productInstance?.wholeSalePrice}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="reorderPoint"><g:message code="product.reorderPoint.label" default="Reorder Point" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: productInstance, field: 'reorderPoint', 'errors')}">
          <g:textField name="reorderPoint" value="${productInstance?.reorderPoint}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="eoq"><g:message code="product.eoq.label" default="EOQ" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: productInstance, field: 'eoq', 'errors')}">
          <g:textField name="eoq" value="${productInstance?.eoq}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="isNet"><g:message code="product.isNet.label" default="Net Price" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: product, field: 'isNet', 'errors')}">
          <g:checkBox name="isNet" value="${productInstance?.isNet}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="isComponent"><g:message code="product.isComponent.label" default="Use as Component" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: product, field: 'isComponent', 'errors')}">
          <g:checkBox name="isComponent" value="${productInstance?.isComponent}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="isForAssembly"><g:message code="product.isForAssembly.label" default="For Assembly" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: product, field: 'isForAssembly', 'errors')}">
          <g:checkBox name="isForAssembly" value="${productInstance?.isForAssembly}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="isForSale"><g:message code="product.isForSale.label" default="For Sale" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: product, field: 'isForSale', 'errors')}">
          <g:checkBox name="isForSale" value="${productInstance?.isForSale}"/>
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="remark"><g:message code="product.remark.label" default="Remarks" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: product, field: 'remark', 'errors')}">
                <textarea name="remark">${productInstance?.remark}</textarea>
          </td>
          </tr>
          </tbody>
        </table>

	
	<g:if test="${!com.munix.ItemLocation.list().isEmpty()}">
		<table>
			<thead>
				<tr class="prop">
					<td valign="top" class="name"><label for="warehouse"><g:message
						code="product.warehouse.label" default="Item Location" /></label></td>
				</tr>
			</thead>
			<tbody>
				<g:each in="${warehouses}" var="warehouse">
						<tr class="prop">
							<td valign="top" class="name"><label for="warehouse"><g:message
								code="product.warehouse.label" default="${warehouse?.itemLocationWarehouse?.description}" /></label></td>
							<td valign="top"
								class="value ${hasErrors(bean: productInstance, field: 'itemLocation', 'errors')}">
							<g:select class="itemLocation" name="itemLocation"
								from="${warehouse?.itemLocationWarehouse?.itemLocations.sort{it.toString()}}"
								optionKey="id" noSelection="['no selection': '']" /></td>
						</tr>
				</g:each>
			</tbody>
		</table>
	</g:if>
	</div>
      <div class="buttons">
        <span class="button"><g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" /></span>
      </div>
    </g:form>
  </div>
</body>
</html>
