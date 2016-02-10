<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
    <link rel="stylesheet" href="${resource(dir:'css',file:'report.css')}" />
    <link rel="stylesheet" href="${resource(dir:'css/reports',file:'form.css')}" />
    <link rel="stylesheet" href="${resource(dir:'css/reports',file:'backOfCheck.css')}" />
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>${title}</title>
  </head>
  <body>
    <h2></h2>
    <table class="main">
    	<tr>
    		<td colspan="2">
    		<span class="label">Company:</span> ${customerName}
    		</td>
    	</tr>
    	<tr>
    		<td style="width:50%"><span class="label">Ref. No</span><br>${directPayment}</td>
    		<td><span class="label">Date</span><br>${date}</td>
    	</tr>
    	<tr>
    		<td colspan="2"><br>
    		<span class="label">${accountNumber}</span>
    		</td>
    	</tr>
	    	<tr>
    		<td colspan="2">
    		<span class="label">${accountName}</span>
    		</td>
    	</tr>
	</table>
  </body>
</html>
