<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
    <link rel="stylesheet" href="${resource(dir:'css',file:'report.css')}" />
    <link rel="stylesheet" href="${resource(dir:'css/reports',file:'salesDelivery.css')}" />
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Sales Delivery</title>
  </head>
  <body>
<g:if test="${!params.noPl}">
    <table class="salesDelivery">
    	<thead>
    	<tr>
    		<td>
		    <div class="head">
		      <table>
		        <tr class="prop">
		          <td class="name col1">Customer</td>
		          <td class="value col2"><b>${salesDeliveryInstance?.customer?.name}</b></td>
		          <td class="name col3">Delivery Date</td>
		          <td class="value col4"><g:formatDate date="${salesDeliveryInstance?.date}" format="MM/dd/yyyy"/></td>
		        </tr>
		        <tr class="prop">
		          <td class="name">Address</td>
		          <td class="value">${salesDeliveryInstance?.customer?.formatBusinessAddress()}</td>
		          <td class="name">DR #</td>
		          <td class="value">${salesDeliveryInstance?.deliveryReceiptNumber}</td>
		        </tr>
		        <tr class="prop"><td>&nbsp;</td></tr>
		        <tr class="prop"><td>&nbsp;</td></tr>
		      </table>
		    </div>
		  	</td>
		</tr>
		</thead>
		

    	<tbody>
    	<tr>
    		<td>
		    <div class="body">
		      <g:if test="${salesDeliveryInstance?.items.findAll{!it?.orderItem?.isNet && it?.qty > 0}.size()!=0}">
		      <table>
		        <thead>
		        <tr><th colspan="4">Discounted Items:</th></tr>
		        <tr>
		        <th class="center qty">Qty</th>
		        <th class="center unit">Unit</th>
		        <th class="code">Code</th>
		        <th class="description">Description</th>
		        </tr>
		        </thead>
		        <tbody>
		        <g:each in="${salesDeliveryInstance?.items.sort{it?.product?.description}}" var="i">
		       	  <g:if test="${!i?.orderItem?.isNet && i?.qty > 0}">
		          <tr>
		            <td class="center">${String.format('%,.0f',i?.qty)}</td>
		            <td class="center">${i?.product?.unit}</td>
		            <td>${i?.product?.identifier}</td>
		            <td>${i?.product?.description}</td>
		          </tr>
		          </g:if>
		        </g:each>
		        </tbody>
		      </table>
		      <br><br>
			</g:if>
			<g:if test="${salesDeliveryInstance?.items.findAll{it?.orderItem?.isNet && it?.qty > 0}.size()!=0}">
		      <table>
		        <thead>
		        <tr><th colspan="4">Net Items:</th></tr>
		        <tr>
		        <th class="center qty">Qty</th>
		        <th class="center unit">Unit</th>
		        <th class="code">Code</th>
		        <th class="description">Description</th>
		        </tr>
		        </thead>
		        <tbody>
		        <g:each in="${salesDeliveryInstance?.items.sort{it?.product?.description}}" var="i">
		       	  <g:if test="${i?.orderItem?.isNet && i?.qty > 0}">
		          <tr>
		            <td class="center">${String.format('%,.0f',i?.qty)}</td>
		            <td class="center">${i?.product?.unit}</td>
		            <td>${i?.product?.identifier}</td>
		            <td>${i?.product?.description}</td>
		          </tr>
		          </g:if>
		        </g:each>
		        </tbody>
		      </table>
			</g:if>
		    </div>
		
		    <div class="footer" id="footer">
		      <table>
		        <tr class="prop" style="border-top:solid 2px black">
		          <td class="name" style="width:15%;border-left:solid 2px black;padding-left:5px;">Checked By:</td>
		          <td style="width:40%;border-right:solid 2px black">________________________</td>
		          <td class="name" style="width:50%;padding-left:5px;border-right:solid 2px black">Received by:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Date:</td>
		        </tr>
		        <tr class="prop">
		          <td class="name" style="border-left:solid 2px black;padding-left:5px;">Package Details:</td>
		          <td style="border-right:solid 2px black">
					____carton/s
					____sack/s
					____bundle/s
					____plastic/s
		          </td>
		          <td rowspan="2" style="border-right:solid 2px black"></td>
		        </tr>
		        <tr class="prop" style="border-bottom:solid 2px black">
		          <td class="name" style="border-left:solid 2px black;padding-left:5px;">Remarks:</td>
		          <td style="border-right:solid 2px black">${salesDeliveryInstance?.remark}</td>
		        </tr>
		      </table>
		    </div>
		  	</td>
		</tr>
		</tbody>
	</table>
</g:if>

<g:if test="${params.noPl}"> 
    <table class="salesDelivery withPrice">
    	<thead>
    	<tr>
    		<td>
		    <div class="head">
		      <table>
		        <tr class="prop">
		          <td class="name col1">Customer</td>
		          <td class="value col2"><b>${salesDeliveryInstance?.customer?.name}</b></td>
		          <td class="name col3">SD</td>
		          <td class="value col4"><b>${salesDeliveryInstance}</b></td>
		        </tr>
		        <tr class="prop">
		          <td class="name">Address</td>
		          <td class="value">${salesDeliveryInstance?.customer?.formatBusinessAddress()}</td>
		          <td class="name">Delivery Date</td>
		          <td class="value"><g:formatDate date="${salesDeliveryInstance?.date}" format="MM/dd/yyyy"/></td>
		        </tr>
		        <tr class="prop">
		          <td class="name">Terms</td>
		          <td class="value">${salesDeliveryInstance?.customer?.term}</td>
		          <td class="name">DR #</td>
		          <td class="value">${salesDeliveryInstance?.deliveryReceiptNumber}</td>
		        </tr>
		        <tr class="prop">
		          <td class="name">Sales Agent</td>
		          <td class="value">${salesDeliveryInstance?.salesAgent}</td>
		          <td class="name">SI #</td>
		          <td class="value">${salesDeliveryInstance?.salesDeliveryNumber}</td>
		        </tr>
		      </table>
		    </div>
		  	</td>
		</tr>
		</thead>
		

    	<tbody>
    	<tr>
    		<td>
		    <div class="body">
		      <g:if test="${salesDeliveryInstance?.items.findAll{!it?.orderItem?.isNet && it?.qty > 0}.size()!=0}">
		      <table>
		        <thead>
		        <tr><th colspan="4">Discounted Items:</th></tr>
		        <tr>
		        <th class="center qty">Qty</th>
		        <th class="center unit">Unit</th>
		        <th class="code">Code</th>
		        <th class="description">Description</th>
		        <th class="right price">Unit Price</th>
		        <th class="right amount">Amount</th>
		        </tr>
		        </thead>
		        <tbody>
		        <g:each in="${salesDeliveryInstance?.items.findAll{!it?.orderItem?.isNet && it?.qty > 0}.sort{it?.product?.description}}" var="i">
		       	  <g:if test="${!i?.orderItem?.isNet && i?.qty > 0}">
		          <tr>
		            <td class="center">${String.format('%,.0f',i?.qty)}</td>
		            <td class="center">${i?.product?.unit}</td>
		            <td>${i?.product?.identifier}</td>
		            <td>${i?.product?.description}</td>
		            <td class="right">${i.price==0?"(FREE)":String.format('%,.2f',i?.price)}</td>
		            <td class="right">${String.format('%,.2f',i?.computeAmount())}</td>
		          </tr>
		          </g:if>
		        </g:each>
		        </tbody>
		          <tr>
		            <td></td>
		            <td></td>
		          <td></td>
		            <td class="right"><strong>Total</strong></td>
		            <td></td>
		            <td class="right"><strong>${String.format( '%,.2f',salesDeliveryInstance.computeDiscountedItemsTotal() )}</strong></td>
		        </tr>
		        <tr>
		          <td></td>
		          <td></td>
		          <td></td>
		          <td class="right"><strong>Discount</strong></td>
		          <td class="right"><strong>${salesDeliveryInstance?.invoice?.discountGroup}</strong></td>
		          <td class="right"><strong>${String.format( '%,.2f',salesDeliveryInstance.computeDiscountedDiscount())}</strong></td>
		        </tr>
		        <tr>
		          <td></td>
		          <td></td>
		          <td></td>
		          <td class="right"><strong>Discounted Total</strong></td>
		          <td></td>
		          <td class="right"><strong>${String.format('%,.2f',salesDeliveryInstance.computeDiscountedTotal())}</strong></td>
		        </tr>
		      </table>
				<br><br>		      
			</g:if>
			<g:if test="${salesDeliveryInstance?.items.findAll{it?.orderItem?.isNet && it?.qty > 0}.size()!=0}">
		      <table>
		        <thead>
		        <tr><th colspan="4">Net Items:</th></tr>
		        <tr>
		        <th class="center qty">Qty</th>
		        <th class="center unit">Unit</th>
		        <th class="code">Code</th>
		        <th class="description">Description</th>
		        <th class="price">Unit Price</th>
		        <th class="amount">Amount</th>
		        </tr>
		        </thead>
		        <tbody>
		        <g:each in="${salesDeliveryInstance?.items.findAll{it?.orderItem?.isNet && it?.qty > 0}.sort{it?.product?.description}}" var="i">
		       	  <g:if test="${i?.orderItem?.isNet && i?.qty > 0}">
		          <tr>
		            <td class="center">${String.format('%,.0f',i?.qty)}</td>
		            <td class="center">${i?.product?.unit}</td>
		            <td>${i?.product?.identifier}</td>
		            <td>${i?.product?.description}</td>
		            <td class="right">${i.price==0?"(FREE)":String.format('%,.2f',i?.price)}</td>
		            <td class="right">${String.format('%,.2f',i?.computeAmount())}</td>
		          </tr>
		          </g:if>
		        </g:each>
		        </tbody>
		          <tr>
		            <td></td>
		            <td></td>
		          <td></td>
		            <td class="right"><strong>Total</strong></td>
		            <td></td>
		            <td class="right"><strong>${String.format( '%,.2f',salesDeliveryInstance.computeNetItemsTotal() )}</strong></td>
		        </tr>
		        <tr>
		          <td></td>
		          <td></td>
		          <td></td>
		          <td class="right"><strong>Discount</strong></td>
		          <td class="right"><strong>Net <g:if test="${salesDeliveryInstance?.invoice?.netDiscountGroup}">- ${salesDeliveryInstance?.invoice?.netDiscountGroup}</g:if></strong></td>
		          <td class="right"><strong>${String.format( '%,.2f',salesDeliveryInstance.computeNetDiscount())}</strong></td>
		        </tr>
		        <tr>
		          <td></td>
		          <td></td>
		          <td></td>
		          <td class="right"><strong>Net Total</strong></td>
		          <td></td>
		          <td class="right"><strong>${String.format('%,.2f',salesDeliveryInstance.computeNetTotal())}</strong></td>
		        </tr>
		      </table>
		      <br><br>
			    <div class="subTable">
			      <table>
			        <tr>
			        <th class="center qty"></th>
			        <th class="center unit"></th>
			        <th class="code"></th>
			        <th class="description"></th>
			        <th class="price"></th>
			        <th class="amount"></th>
			        </tr>
			        <thead>
			        </thead>
			        <tbody class="uneditable">
			        </tbody>

			      </table>
			      </g:if>
			    </div>
		    </div>
		
		    <div class="footer" id="footer">
		      <table>
		        <tr class="prop" style="border-top:solid 2px black">
		          <td class="name" style="width:15%;border-left:solid 2px black;padding-left:5px;">Checked By:</td>
		          <td style="width:40%;border-right:solid 2px black">________________________</td>
		          <td class="name" style="width:17%;padding-left:5px;">Received By: Date:</td>
		          <td class="name right" style="padding-right:5px;width:18%;border-left:solid 2px black;border-right:solid 2px black">Discounted Items Total</td>
		          <td class="value right" style="padding-right:5px;width:15%;border-right:solid 2px black"><strong>${String.format('%,.2f',salesDeliveryInstance.computeDiscountedTotal())}</strong></td>
		        </tr>
		        <tr class="prop">
		          <td class="name" style="border-left:solid 2px black;padding-left:5px;">Package Details:</td>
		          <td style="border-right:solid 2px black">
					____carton/s
					____sack/s
					____bundle/s
					____plastic/s
		          </td>
		          <td rowspan="2"></td>
		          <td class="name right" style="padding-right:5px;border-bottom:solid 2px black;border-right:solid 2px black;border-left:solid 2px black">Net Items Total</td>
		          <td class="value right" style="padding-right:5px;border-bottom:solid 2px black;border-right:solid 2px black"><strong>${String.format('%,.2f',salesDeliveryInstance.computeNetTotal())}</strong></td>
		        </tr>
		        <tr class="prop" style="border-bottom:solid 2px black">
		          <td class="name" style="border-left:solid 2px black;padding-left:5px;">Remarks:</td>
		          <td style="border-right:solid 2px black">${salesDeliveryInstance?.remark}</td>
		          <td class="name right" style="padding-right:5px;border-left:solid 2px black;border-right:solid 2px black">Grand Total</td>
		          <td class="value right" style="padding-right:5px;border-right:solid 2px black"><strong>${String.format('%,.2f',salesDeliveryInstance.computeTotalAmount())}</strong></td>
		        </tr>
		      </table>
		    </div>
		  	</td>
		</tr>
		</tbody>
	</table>
</g:if>
	<script>window.onload = function(){window.print();};</script>
  </body>
</html>
