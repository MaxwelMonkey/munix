
<%@ page import="com.munix.CustomerLedgerEntry; com.munix.CustomerLedger" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:javascript src="jquery.ui.core.js" />
  	    <g:javascript src="jquery.ui.datepicker.js" />
        <link rel="stylesheet" href="${resource(dir:'css',file:'jquery.ui.all.css')}" />
        <title><g:message code="customerLedger.show" default="Show CustomerLedger" /></title>
        <g:javascript>
            var $ = jQuery.noConflict()
            $(document).ready(function (){
                $("#postDateBeforeText").datepicker()
                $("#postDateAfterText").datepicker()
            })
        </g:javascript>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLinkTo(dir: '')}"><g:message code="home" default="Home" /></a></span>
        </div>
        <div class="body">
            <h1><g:message code="customerLedger.show" default="Show Customer Ledger" /></h1>
            <h2>${customerLedgerInstance.customer}</h2>
            <g:if test="${flash.message}">
            <div class="message"><g:message code="${flash.message}" args="${flash.args}" default="${flash.defaultMessage}" /></div>
            </g:if>
            <g:form action="show" method="post" id="${customerLedgerInstance.id}">
                <g:hiddenField name="id" value="${customerLedgerInstance?.id}" />
                <div class="dialog">
                    Post Date:
                    <g:textField name="postDateBeforeText" id="postDateBeforeText" value='${params?.postDateBeforeText}'/>
                    to
                    <g:textField name="postDateAfterText" id="postDateAfterText" value='${params?.postDateAfterText}'/>
                    <input type="submit" class="button" value="Search"/>
                </div>
                <div class="subTable">

                    <table id="customerLedgerEntryTable">
                        <thead>
                            <th style="width:150px">Date Created</th>
                            <th style="width:80px">Post Date</th>
                            <th style="width:200px">Type</th>
                            <th>Remarks</th>
                            <th style="width:80px">ID</th>
                            <th>Amount</th>
                            <th>Debit</th>
                            <th>Credit</th>
                            <th>Running Balance</th>
                        </thead>
                        <tbody>
                            <g:each in="${entries}" var="entry">
                                <g:if test="${!entry.isChild}">
                                    <tr>
                                        <td><g:formatDate date="${entry.dateOpened}" format="MM/dd/yyyy"/></td>
                                        <td><g:formatDate date="${entry.datePosted}" format="MM/dd/yyyy"/></td>
                                        <td>${entry.type} ${entry.details}</td>
                                        <td></td>
                                        <td><g:link url="${entry.generateLink()}">${entry.referenceId}</g:link></td>
                                        <td class="right"><g:formatNumber number="${entry.amount}" format="###,##0.00"/></td>
                                        <td class="right"><g:formatNumber number="${entry.debitAmount}" format="###,##0.00"/></td>
                                        <td class="right"><g:formatNumber number="${entry.creditAmount}" format="###,##0.00"/></td>
                                        <td class="right"><g:formatNumber number="${entry.runningBalance}" format="###,##0.00"/></td>
                                    </tr>
                                    <g:if test="${entry.paymentBreakdown}">
                                        <g:each in="${entry.paymentBreakdown.sort{it.details}}" var="payments">
                                            <tr>
                                                <td></td>
                                                <td></td>
                                                <td>${payments.details}</td>
                                                <td>${payments.remark}</td>
                                                <td></td>
                                                <td class="right"><g:formatNumber number="${payments.amount}" format="###,##0.00"/></td>
                                                <td class="right"><g:formatNumber number="${payments.debitAmount}" format="###,##0.00"/></td>
                                                <td class="right"><g:formatNumber number="${payments.creditAmount}" format="###,##0.00"/></td>
                                                <td></td>
                                                </tr>
                                        </g:each>
                                    </g:if>
                                </g:if>
                            </g:each>
                        </tbody>
                        <tfoot>
                            <tr class="total">
                                <td><strong>Current Running Balance</strong></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td class="right"><strong><g:formatNumber number="${customerLedgerInstance.balance}" format="###,##0.00"/></strong></td>
                            </tr>
                        </tfoot>
                    </table>
                    <div class="paginateButtons">
                      <g:paginate controller="customerLedger" action="show" id="${customerLedgerInstance.id}" total="${entriesTotal}" params='${params}' />
                    </div>

                </div>
                <div class="buttons">
                	<span class="button"><g:link controller="customer" action="show" id="${customerLedgerInstance?.customer?.id}">Back</g:link></span>
                    <span class="button"><g:link class="print"  controller="print" action="customerLedger" target="customerLedger" params="${params}">Print</g:link></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
