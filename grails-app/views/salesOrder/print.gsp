<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
    <link rel="stylesheet" href="${resource(dir:'css',file:'report.css')}" />
    <link rel="stylesheet" href="${resource(dir:'css/reports',file:'salesOrder.css')}" />
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>S.O.</title>
  </head>
  <body>
    <h2>S.O.</h2>
    <table class="salesOrder">
    	<thead>
    	<tr>
    		<td>
		    <div id="head">
		      <table style="border:solid 0px black">
		        <tr class="prop">
		          <td class="name" style="width:20%">Customer Name</td>
		          <td class="value" style="width:50%">${salesOrderInstance?.customer?.name}</td>
		          <td class="name" style="width:20%">S.O. No.</td>
		          <td class="value">${salesOrderInstance}</td>
		        </tr>
		        <tr class="prop">
		          <td class="name">Address</td>
		          <td class="value">
		          	<g:if test="${salesOrderInstance?.customer?.busAddrCity}">
		          	${salesOrderInstance?.customer?.busAddrCity}
		          	</g:if>
		          	<g:if test="${salesOrderInstance?.customer?.busAddrCity?.province}">
		          		${salesOrderInstance?.customer?.busAddrCity?.province?.region}
		          	</g:if>
		          </td>
		          <td class="name">S.O. Date</td>
		          <td class="value"><g:formatDate date="${new Date()}" format="MMM. dd, yyyy"/></td>
		        </tr>
		        <tr class="prop">
		          <td class="name">Forwarder</td>
		          <td class="value">${salesOrderInstance?.customer?.forwarder}</td>
		          <td class="name">Deliver By:</td>
		          <td class="value"><g:formatDate date="${salesOrderInstance?.deliveryDate}"  format="MMM. dd, yyyy"/></td>
		        </tr>
		        <tr class="prop">
		          <td class="name">Discount Type</td>
		          <td class="value">${salesOrderInstance?.discountType}</td>
		        </tr>
		      </table>
		    </div>
		  	</td>
		</tr>
		</thead>
		


    	<tbody>
    	<tr>
    		<td>
    <div id="body">
		      <table>
		        <thead>
		        <tr><th>D. Items:</th></tr>
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
		        <g:each in="${salesOrderInstance?.items.sort{it?.product?.description}}" var="i">
		       	  <g:if test="${!i?.isNet}">
		          <tr>
		            <td class="center">${String.format('%,.0f',i?.qty)}</td>
		            <td class="center">${i?.product?.unit}</td>
		            <td>${i?.product?.identifier}</td>
		            <td>${i?.product?.description} ${i?.product?.partNumber}</td>
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
		            <td class="right"><strong>${String.format( '%,.2f',salesOrderInstance.computeDiscountedItemsTotal() )}</strong></td>
		        </tr>
		        <tr>
		          <td></td>
		          <td></td>
		          <td></td>
		          <td class="right"><strong>Discount</strong></td>
		          <td class="right"><strong>${salesOrderInstance?.discountGroup}</strong></td>
		          <td class="right"><strong>${String.format( '%,.2f',salesOrderInstance.computeDiscountedDiscount())}</strong></td>
		        </tr>
		        <tr>
		          <td></td>
		          <td></td>
		          <td></td>
		          <td class="right"><strong>Discounted Total</strong></td>
		          <td></td>
		          <td class="right"><strong>${String.format('%,.2f',salesOrderInstance.computeDiscountedTotal())}</strong></td>
		        </tr>
		      </table>
				<br><br>		      
		      <table>
		        <thead>
		        <tr><th>N. Items:</th></tr>
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
		        <g:each in="${salesOrderInstance?.items.sort{it?.product?.description}}" var="i">
		       	  <g:if test="${i?.isNet}">
		          <tr>
		            <td class="center">${String.format('%,.0f',i?.qty)}</td>
		            <td class="center">${i?.product?.unit}</td>
		            <td>${i?.product?.identifier}</td>
		            <td>${i?.product?.description} ${i?.product?.partNumber}</td>
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
		            <td class="right"><strong>${String.format('%,.2f',salesOrderInstance.computeNetItemsTotal())}</strong></td>
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
			        	<tr>
				          <td></td>
				          <td></td>
				          <td></td>
				          <td class="right"><strong>Discounted Items Total</strong></td>
				          <td></td>
				          <td class="right"><strong>${String.format('%,.2f',salesOrderInstance.computeDiscountedTotal())}</strong></td>
				        </tr>
			        	<tr>
				          <td></td>
				          <td></td>
				          <td></td>
				          <td class="right"><strong>Net Price items Total</strong></td>
				          <td></td>
				            <td class="right"><strong>${String.format('%,.2f',salesOrderInstance.computeNetItemsTotal())}</strong></td>
				        </tr>
				        <tr>
				          <td></td>
				          <td></td>
				          <td></td>
				          <td class="right"><strong>Grand Total</strong></td>
				          <td></td>
				          <td class="right"><strong>${String.format('%,.2f',salesOrderInstance.computeGrandTotal())}</strong></td>
				        </tr>
			      </table>
    </div>

    <div id="footer">
      <table>
        <tr class="prop">
          <td class="name">Remarks</td>
          <td class="value">${salesOrderInstance?.remark}</td>
          <td class="name">Prepared By</td>
          <td class="value">________________________</td>
        </tr>
        <tr class="prop">
          <td class="name">Created By</td>
          <td class="value">${salesOrderInstance?.preparedBy}</td>
          <td class="name">Date & Time Prepared</td>
          <td class="value">________________________</td>
        </tr>
        <tr class="prop">
          <td class="name">Approved 1 By</td>
          <td class="value">${salesOrderInstance?.approvedBy}</td>
          <td class="name">Date & Time Finished</td>
          <td class="value">________________________</td>
        </tr>
        <tr class="prop">
          <td class="name">Approved 2 By</td>
          <td class="value">${salesOrderInstance?.approvedTwoBy}</td>
        </tr>
      </table>
    </div>
		  	</td>
		</tr>
		</tbody>
	</table>
	<script>window.onload = function(){window.print();};</script>
  </body>
</html>
