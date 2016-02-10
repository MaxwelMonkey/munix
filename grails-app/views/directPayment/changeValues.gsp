
<%@ page import="com.munix.DirectPayment" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
    <style type="text/css">
    	.ui-datepicker-calendar th {
    		color: #000000
    	}
    </style>
    
  	<g:set var="entityName" value="${message(code: 'directPayment.label', default: 'DirectPayment')}" />
	<g:javascript src="jquery.ui.core.js" />
  	<g:javascript src="jquery.ui.datepicker.js" />
  	<g:javascript src="directPaymentShow.js" />
  	<g:javascript src="numbervalidation.js" />
  	<link rel="stylesheet" href="${resource(dir:'css',file:'jquery.ui.all.css')}" />
  	<title><g:message code="default.show.label" args="[entityName]" /></title>
    <script>
        counter='0'
        paymentType=""
        bankList=""
        function amountChecker(){
			var status=true
			$("input[name^='textApplied']").each(function(){
				var text=$(this).closest("tr").find("amountDue").text()
				var removeComma=replaceAll(text,",","")
				var num=parseFloat(removeComma)
				if(num<parseFloat($(this).val())){
					alert("One of the amount applied is more than the due")
					status=false
				}
			})
	  		if($("amountApplied").text()!=$("paymentAmount").text()){
		  		alert("The total payment value is not equal to the total invoices value")
				status=false
		  	}
		  	if(status==true){
		  		status=checker()
			}
	  		if(status==true){
	  			$("#counter").val(counter)
	  			alert($("#counter").val())
	  			return confirm('${message(code: 'default.button.delete.approve.message', default: 'Are you sure?')}');
		  	}
            return status
		}

		function removeNegativeCreditMemo(){
			$("amountDue").each(function(){
				var text=$(this).text()
				var replaced=text.replace("-","")
				$(this).text(replaced)
			})
		}

		function solveTotal(){
			var total=0
       		$("input[name^='textAmount']").each(function(){
       			var num=0
       			if($(this).val()!=""){
       				num=parseFloat($(this).val())
           		}
	       		total+=num
       		});
       		$("paymentAmount").text(addCommas(total))
       		solveBalance()
	    }

		function checker(){
			var status=true
            var attr
		    $("input[name^='text']").each(function(){
		      $(this).css('border-color', '');
              attr = $(this).attr("disabled")
			  if(typeof attr == 'undefined' || attr == false){
				if($(this).val()==""){
					$(this).css('border-color', 'red');
					status=false
				}
			  }
			})
			if(status == false){
				alert('Some text fields do not have a value');
			}
			$("#counter").val(counter)
			return status
		  }

		function solveBalance(){
			var removeCommaApplied=changeToFloat($("amountAppliedTotal").text())
			var removeCommaPayment=parseFloat(replaceAll($("paymentAmount").text(),",",""))
			var balanceValue=removeCommaPayment-removeCommaApplied
			$("balance").text(addCommas(balanceValue))
		}

		function disableEnable(element){
		  	if($(element).find('option:selected').hasClass("isCheck")){
			  	$(element).closest("tr").find('input[id^="textCheckDate"]').removeAttr("disabled")
			  	$(element).closest("tr").find('input[id^="textCheckNum"]').removeAttr("disabled")
			  	$(element).closest("tr").find('[id^="textBranch"]').removeAttr("disabled")
			  	$(element).closest("tr").find('td:nth-child(6)').find(':first-child').removeAttr("disabled")
			  	$(element).closest("tr").find('td:nth-child(7)').find(':first-child').removeAttr("disabled")
		  	}else{
			  	var tempDate=$(element).closest("tr").find('input[id^="textCheckDate"]')
			  	tempDate.attr("disabled","disabled")
			  	tempDate.val("")
			  	var tempCheck=$(element).closest("tr").find('input[id^="textCheckNum"]')
			  	tempCheck.attr("disabled","disabled")
			  	tempCheck.val("")
			  	var tempBranch=$(element).closest("tr").find('input[id^="textBranch"]')
			  	tempBranch.attr("disabled","disabled")
			  	tempBranch.val("")
			  	$(element).closest("tr").find('td:nth-child(6)').find(':first-child').attr("disabled","true")
			  	$(element).closest("tr").find('td:nth-child(7)').find(':first-child').attr("disabled","true")
		  	}
	  	}
        function updateDeductibleFromSales(element){
            var elem = $(element).closest("tr").find('deductible')
		  	if($(element).find('option:selected').hasClass("isNotDeductibleFromSales")){
                elem.text("false")
            }else{
                elem.text("true")
            }
        }
	  	function reduceDue(inputBox){
			var amountDue=inputBox.closest("tr").find("netDue")
			var due=changeToFloat(inputBox.closest("tr").find("amountDue").text())
			var applied=changeToFloat(inputBox.val())
			if(isNaN(applied)) {
				applied = 0
			}
			var total=due-applied
			amountDue.text(formatString(total))
		}
        function updateAmountAppliedTotal(){
            var appliedAmount
            var total = 0
            $("input[ id ^= 'textApplied' ]").each(function(){
				appliedAmount=changeToFloat($(this).val())
				if(isNaN(appliedAmount)) {
					appliedAmount = 0
				}
                total += appliedAmount
			});
            $('amountAppliedTotal').text(formatString(total))
        }
        function updateNetDueTotal(){
            var appliedAmount
            var total = 0
            $("netDue").each(function(){
				appliedAmount=changeToFloat($(this).text())
                total += appliedAmount
			});
            $('netDueTotal').text(formatString(total))
        }
		function instantiateAllDue(){
			$("input[ id ^= 'textApplied' ]").each(function(){
				reduceDue($(this))
			});
		}
        function createCheckDateTd(values){
            var tdDateName = "textCheckDate"+counter
            var tdDateId = tdDateName
            var tdDisabled = 'disabled="disabled"'
            var tdValue = ""

            if(values!=null){
                if(values.checkDate==null){
                    values.checkDate =""
                }
                tdDateName = "textCheckDateUpdate"+values.id
                tdDateId = tdDateName
                if(values.payment!='CM'&&values.isCheck){
                    tdDisabled=""
                }
                tdValue=values.checkDate
            }
            var inputDateField = $('<input type="text" MAXLENGTH=\"11\" readonly="true" style="width:80px" '+tdDisabled+' name="'+tdDateName+'" id="'+tdDateId+'" value="'+tdValue+'"/>').datepicker();
            return $("<td></td>").append(inputDateField)
        }

        function createDateTd(values){
            var td=$("<td></td>")
            if(values!=null){
                tdDateName = "dateUpdate"+values.id
                tdDateId = tdDateName
                tdValue=values.date
                var inputDateField = $("<input maxlength='11' type='text' readonly='true' style='width:80px' value='"+values.date+"' name='dateUpdate"+values.id+"' id='dateUpdate"+values.id+"'/>")
                if(values.payment!="CM"){
					inputDateField.datepicker()
                }
                td.append(inputDateField)
            }else{
     	       	var tdName = "dateUpdate"+counter
	           var tdId = tdName
                var inputDateField = $("<input maxlength='11' type='text' readonly='true' style='width:80px' name='"+tdId+"' id='"+tdId+"'/>")
			inputDateField.datepicker()
                td.append(inputDateField)
                //td.append('<g:formatDate date="${new Date()}" format="MM/dd/yyyy" />')
            }
            return td
        }

        function createPaymentTypeTd(values){
            var td=$("<td></td>")
            var tdName = "paymentType"+counter
            var tdId = tdName

            if(values!=null){
                tdName = "paymentTypeUpdate"+values.id
                tdId = tdName
            }

            var selectPaymentType=$("<select style='width:100px' name='"+tdName+"' id='"+tdId+"' optionKey='id'></select>")
            $.each(paymentType, function(index,value){
                var option = $('<option value="'+value.typeId+'" id='+value.typeId+'>'+value.name+'</option>')
                if(values!=null){
                    if(value.name==values.payment){
                        option.attr('selected',"selected")
                    }
                }

                if(value.isCheck){
                    option.addClass("isCheck")
                }
                if(value.deductibleFromSales){
                    option.addClass("isDeductibleFromSales")
                }
                else{
                    option.addClass("isNotDeductibleFromSales")
                }
                if(value.name!="CM"){
                    selectPaymentType.append(option)
                }
                else {
                	if(values!=null){
	                    if(values.payment=="CM"){
							selectPaymentType.append(option)
							selectPaymentType.attr("disabled","disabled")
	                	}
	                }
                }
            });
            return td.append(selectPaymentType)
        }
        function createCheckNumberTd(values){
            var td=$("<td></td>")
            var tdName = "textCheckNum"+counter
            var tdId = tdName
            var tdDisabled = 'disabled="disabled"'
            var tdValue = ""

            if(values!=null){
                if(values.checkNum == null){
                    values.checkNum = ""
                }
                tdName = "textCheckNumUpdate"+values.id
                tdId = tdName
                if(values.payment!='CM'&&values.isCheck){
                    tdDisabled=""
                }
                tdValue=values.checkNum
            }
            return td.append("<input maxlength='255' type='text' "+tdDisabled+" style='width:80px' value='"+tdValue+"' name='"+tdName+"' id='"+tdId+"'/>")
        }
        function createRemoveTd(){
            var removeLink = $("<img src='../images/cancel.png'/>");
            return $('<td class="center"></td>').append(removeLink);
        }
        function createBank(values){
            var td=$("<td></td>")
            var tdName = "bankType"+counter
            var tdId = tdName
            var tdDisabled = 'disabled="disabled"'
            var tdValue = ""

            if(values!=null){
                tdName = "bankTypeUpdate"+values.id
                tdId = tdName
                if(values.payment!='CM'&&values.isCheck){
                    tdDisabled=""
                }
            }
            var selectBankType=$("<select style='width:200px' "+tdDisabled+" optionKey='id' name='"+tdName+"' id='"+tdId+"'></select>")
            $.each(bankList, function(index,value){
                var selected=""
                if(values!=null){
                    if(value.typeId==values.bank){
                        selected="SELECTED"
                    }
                }
                selectBankType.append('<option value="'+value.typeId+'" id='+value.typeId+' '+selected+'>'+value.name+'</option>')
            });
            return selectBankType
        }

        function createTypeTd(values){
            var td=$("<td></td>")
            var tdName = "checkType"+counter
            var tdId = tdName
            var tdDisabled = 'disabled="disabled"'
            var tdValue = ""

            if(values!=null){
                tdName = "checkTypeUpdate"+values.id
                tdId = tdName
                if(values.payment!='CM'&&values.isCheck){
                    tdDisabled=""
                }
            }
            var selectCheckType=$("<select style='width:120px' "+tdDisabled+" optionKey='id' name='"+tdName+"' id='"+tdId+"'></select>")
            $.each(checkType, function(index,value){
                var selected=""
                if(values!=null){
                    if(value.name==values.type){
                        selected="SELECTED"
                    }
                }
                selectCheckType.append('<option  value="'+value.typeId+'" id='+value.typeId+' '+selected+'>'+value.name+'-'+value.branch+'</option>')
            });
            var tdCheck = $('<td></td>').append(selectCheckType);
            return td.append(selectCheckType)
        }

        function createBranch(values){
            var td=$("<td></td>")
            var tdName = "textBranch"+counter
            var tdId = tdName
            var tdDisabled = 'disabled="disabled"'
            var tdValue = ""

            if(values!=null){
                if(values.branch==null){
                    values.branch=""
                }
                tdName = "textBranchUpdate"+values.id
                tdId = tdName
                if(values.payment!='CM'&&values.isCheck){
                    tdDisabled=""
                }
                tdValue=values.branch
            }
            return inputBranch=$('<br><input '+tdDisabled+' maxlength="255" value="'+tdValue+'" id="'+tdId+'" name="'+tdName+'"/>')

        }
        function createDeductibleFromSalesTd(values){
            var td=$("<td id='deductibleFromSales'></td>")
            var value = "<deductible>false</deductible>"
            if(paymentType[0].deductibleFromSales){
                value = "<deductible>true</deductible>"
            }

            if(values!=null){
                value = "<deductible>"+values.deductibleFromSales+"</deductible>"
            }

            return td.append(value)
        }
        function createRemarksTd(values){
            var td=$("<td></td>")
            var tdName = "remark"+counter
            var tdId = tdName
            var tdValue = ""

            if(values!=null){
                if(values.remark == null) {
                    values.remark = "";
                }
                tdName = "remarkUpdate"+values.id
                tdId = tdName
                tdValue=values.remark
            }
            var inputBranch=$('<input maxlength="255" style="width:100px" value="'+tdValue+'" id="'+tdId+'" name="'+tdName+'"/>')
            return td.append(inputBranch)
        }
        function createAmountTd(values){
            var td=$("<td></td>")
            var tdName = "textAmount"+counter
            var tdId = tdName
            var tdValue = "0"

            if(values!=null){
                tdName = "textAmountUpdate"+values.id
                tdId = tdName
                tdValue=values.amount
            }
            var inputBranch=$('<input maxlength="16" style="width:120px" value="'+tdValue+'" id="'+tdId+'" name="'+tdName+'"/>').ForceNumericOnly(true);
            if(values!=null){
            	if(values.payment=="CM"){
            		inputBranch.attr("readonly","readonly")
            	}
            }
            return td.append(inputBranch)
        }

        function addPaymentItem(values){
            var emptyTd = $("<td></td>")
            var removeLink = createRemoveTd()	
            if(values ==null){
                counter++
            }else if(values.payment=="CM"){
            	removeLink=emptyTd
            }
            
            var bankTd = $("<td></td>")
            bankTd.append(createBank(values))
            .append(createBranch(values))
            var paymentTypeTd = createPaymentTypeTd(values)
            var amountTd = createAmountTd(values)
            
            
            var tr=$("<tr></tr>")
            tr.append(removeLink)
            .append(paymentTypeTd)
            .append(createDateTd(values))
            .append(createCheckNumberTd(values))
            .append(createCheckDateTd(values))
            .append(bankTd)
            .append(createTypeTd(values))
            .append(createDeductibleFromSalesTd(values))
            .append(createRemarksTd(values))
            .append(amountTd)
            if(values!=null){
                tr.append("<input type='hidden' name='itemId' value='"+values.id+"'/>");
            }else{
                tr.append("<input type='hidden' name='createdItemId' value='"+counter+"'/>");
            }

            $(paymentTypeTd).change(function(){
            	$("input[name^='text']").each(function(){
   				 	$(this).css('border-color', '');
   				})
                disableEnable(this)
                updateDeductibleFromSales(this)
            })
            $(removeLink).click(function(){
                	$(tr).remove();
            });
            $(amountTd).change(function(){
                solveTotal()
            })

            $(".checkTable").append(tr)
        }

        $(document).ready(function(){
			$.ajax({ url: "${createLink(uri:'/')}directPayment/showSelectValues",
				data: "directPaymentId="+$('#id').val(),
	            success: function(resp){
                    paymentType=resp.paymentType
					checkType=resp.checkType
					bankList=resp.bankList
                    $.each(resp.previousItems, function(index, value){
						addPaymentItem(value)
					});
					solveTotal()
					removeNegativeCreditMemo()
					solveBalance()
					instantiateAllDue()
	       		}
	       	});
            $(".addItem").click(function(){
                addPaymentItem(null)
            })
            $("input[ id ^= 'textApplied' ]").each(function(){
				$(this).ForceNumericOnly(true)
			})
            $("input[ id ^= 'textApplied' ]").change(function(){
                reduceDue($(this))
                updateAmountAppliedTotal()
                updateNetDueTotal()
                solveBalance()
            })

        })
        
        function applyAll(checked){
        	if(checked){
	        	$(".amountApplied").each(function(){
	        		$(this).val($(this).attr("due"));
	                reduceDue($(this))
	        	});
                updateAmountAppliedTotal()
                updateNetDueTotal()
                solveBalance()
        	}
        }
	</script>
</head>
<body>
  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
    <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
    <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
  </div>
  <div class="body">
    <h1><g:message code="default.show.label" args="[entityName]" /></h1>
    <g:if test="${flash.message}">
      <div class="message">${flash.message}</div>
    </g:if>
    <div class="dialog">
      <table>
        <tbody>
         <tr class="prop">
            <td valign="top" class="name"><g:message code="directPayment.id.label" default="Id" /></td>
        <td valign="top" class="value">${directPaymentInstance}</td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="directPayment.date.label" default="Date" /></td>
        <td valign="top" class="value"><g:formatDate date="${directPaymentInstance?.date}" format="MM/dd/yyyy"/></td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="directPayment.customer.label" default="Customer" /></td>
        <td valign="top" class="value"><g:link controller="customer" action="show" id="${directPaymentInstance?.customer?.id}">${directPaymentInstance?.customer?.encodeAsHTML()}</g:link></td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="directPayment.customerTerm.label" default="Term" /></td>
        <td valign="top" class="value">${directPaymentInstance?.customer?.term?.encodeAsHTML()}</td>
        </tr>


        <tr class="prop">
          <td valign="top" class="name"><g:message code="directPayment.preparedBy.label" default="Prepared By" /></td>
        <td valign="top" class="value">${directPaymentInstance?.preparedBy}</td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="directPayment.approvedBy.label" default="Approved By" /></td>
        <td valign="top" class="value">${directPaymentInstance?.approvedBy}</td>
        </tr>
        
        <tr class="prop">
          <td valign="top" class="name"><g:message code="directPayment.remark.label" default="Remarks" /></td>
          <td valign="top" class="value">${fieldValue(bean: directPaymentInstance, field: "remark")}</td>
		</tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="directPayment.status.label" default="Status" /></td>
        <td valign="top" class="value">${fieldValue(bean: directPaymentInstance, field: "status")}</td>
        </tr>
		
        </tbody>
      </table>
    </div>
   
    <g:form>
    <div class="subTable">
      <h2>Items</h2>
      <table>
        <button type="button" class="addItem">Add Items</button>
        <thead>
          <tr>
          	<th class="center">Cancel</th>
            <th class="center">Payment Type</th>
            <th class="center">Date</th>
            <th class="center">Check #</th>
            <th class="center">Check Date</th>
            <th class="center">Bank</th>
            <th class="center">Type</th>
            <th class="center">Deductible From Sales</th>
            <th class="center">Remarks</th>
            <th class="center">Amount</th>
          </tr>
        </thead>
        <tbody class="checkTable">
        </tbody>
        <tfoot>
          <tr class="total">
            <td><strong>Total</strong></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td class="right"><strong>PHP <paymentAmount> 0.00</paymentAmount></strong></td>
          </tr>
        </tfoot>
      </table>
    </div>
 	
    <div class="subTable">
      <table>
        <h2>Invoices</h2>
        <thead>
          <tr>
            <th>Invoice</th>
            <th>Type</th>
            <th class="center">Date</th>
            <th class="center">Discounted Discount</th>
            <th class="center">Net Discount</th>
            <th class="center">Amount</th>
            <th class="center">Due</th>
            <th class="center">Amount Applied <input type="checkbox" onclick="applyAll(this.checked);"/></th>
            <th class="center">Net Due</th>
          </tr>
        </thead>
        <tbody class="editable">
        <g:each in="${invoices}" var="invoice" status="colors">
          <tr class="${(colors % 2) == 0 ? 'odd' : 'even'}" >
           	  <td>${invoice?.customerPayment}</td>
              <td>${invoice?.type}</td>
              <td class="right"><g:formatDate date="${invoice?.date}" format="MMM. dd, yyyy"/></td>
              <td class="right">${invoice?.discountedDiscount}</td>
              <td class="right">${invoice?.netDiscount}</td>
              <td class="right">PHP <g:formatNumber number="${invoice?.amount}" format="###,##0.00" /></td>
	          <td class="right">PHP <amountDue><g:formatNumber number="${invoice?.due}" format="###,##0.00" /></amountDue></td>
	          <td class="right">
	          	<g:if test="${invoice?.isInstallment}">
		          PHP <amountApplied><input class="amountApplied" due="${invoice?.due}" type="text" MAXLENGTH="16" name="textApplied${invoice.id}" id="textApplied${invoice.id}" value="${invoice?.applied}"/></amountApplied>
		        </g:if><g:else>
		          PHP <amountApplied><g:formatNumber number="${invoice?.applied}" format="###,##0.00" /></amountApplied>
		        </g:else>
	          </td>
	          <td class="right"><netDue>PHP <g:formatNumber number="${invoice?.net}" format="###,##0.00" /></netDue></td>
          </tr>
        </g:each>
        </tbody>
        <tfoot>
          <tr class="total">
            <td><strong>Total</strong></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td class="right"><strong>PHP <g:formatNumber number="${invoiceTotals?.amount}" format="###,##0.00" /></strong></td>
            <td class="right"><strong>PHP <g:formatNumber number="${invoiceTotals?.due}" format="###,##0.00" /></strong></td>
            <td class="right"><strong> <amountAppliedTotal>PHP <g:formatNumber number="${invoiceTotals?.applied}" format="###,##0.00" /></amountAppliedTotal></strong></td>
          	<td class="right"><strong> <netDueTotal>PHP <g:formatNumber number="${invoiceTotals?.net}" format="###,##0.00" /></netDueTotal></strong></td>
          </tr>
        </tfoot>
      </table>
    </div>
   
	<div class="buttons">
      	<strong>Balance: PHP <balance></balance></strong>
    </div>
    <div class="buttons">
      
      	<g:hiddenField name="counter" id="counter" value="0" />
        <g:hiddenField name="id" id="id" value="${directPaymentInstance?.id}" />
        <g:if test="${directPaymentInstance?.status != 'Approved'}">
        	<span class="button"><g:actionSubmit action="update" value="Save" id="save" onclick="return checker()"/></span>
        </g:if>
      </g:form>
    </div>
  </div>
</body>
</html>
