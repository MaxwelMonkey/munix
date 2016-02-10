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
		          	${salesOrderInstance?.customer?.busAddrCity},
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
		          </tr>
		          </g:if>
		        </g:each>
		        </tbody>
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
		          </tr>
		          </g:if>
		        </g:each>
		        </tbody>
		      </table>
		      <br><br>

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
