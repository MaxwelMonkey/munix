<html>
  <head>
    <title><g:layoutTitle default="Munix Information System" /></title>
    <link rel="stylesheet" href="${resource(dir:'css',file:'main.css')}" />
    <link rel="stylesheet" href="${resource(dir:'css',file:'filtergrid.css')}" media="screen" />

    <link rel="stylesheet" href="${resource(dir:'css',file:'superfish.css')}" media="screen" />
    <link rel="shortcut icon" href="${resource(dir:'images',file:'favicon.ico')}" type="image/x-icon" />
  <g:javascript library="prototype" />
  <g:javascript src="jquery-1.5.min.js" />
  <g:javascript>
  	jQuery.noConflict();
  </g:javascript>
  <g:javascript src="superfish.js" />
  <g:javascript src="hoverIntent.js" />
  <g:javascript src="tablefilter.js" />
  <g:javascript library="application" />
  <g:javascript>
  	var $=jQuery.noConflict()
    $(document).ready(function(){
    	$("ul.sf-menu").superfish();
  	});
  	
  	var base_url="${createLink(uri:'/')}"
  </g:javascript>
  <g:layoutHead />

</head>
<body>
  <div id="logo" class="logo">
    <table class="header">
      <tr>
        <td><a href="${createLink(uri:'/')}"><img src="${resource(dir:'images',file:'munix.png')}" alt="Grails" border="0" /></a></td>
        <td>

          <ul class="sf-menu">
            <li class="current"><a id=menuMaintenance href="#">Maintenance</a>
              <ul>

                <li><a id="menuAdmin" href="#">Admin</a>
                  <ul>
                    <li><a id="menuUser" href="${createLink(controller:'user')}">User</a></li>
                    <li><a id="menuRole" href="${createLink(controller:'role')}">Role</a></li>
                    <li><a id="menuRequestMap" href="${createLink(controller:'requestmap')}">Requestmap</a></li>
                  </ul>
                </li>

                <li><a id="menuAccounting" href="#">Accounting</a>
                  <ul>
                    <li><a id="menuBank" href="${createLink(controller:'bank')}">Bank</a></li>
                    <li><a id="menuBankAccount" href="${createLink(controller:'bankAccount')}">Bank Account</a></li>
                    <li><a id="menuCharge" href="${createLink(controller:'charge')}">Charge</a></li>
                    <li><a id="menuCheckStatus" href="${createLink(controller:'checkStatus')}">Check Status</a></li>
                    <li><a id="menuCheckType" href="${createLink(controller:'checkType')}">Check Type</a></li>
                    <li><a id="menuCheckWarehouse" href="${createLink(controller:'checkWarehouse')}">Check Warehouse</a></li>
                    <li><a id="menuCollector" href="${createLink(controller:'collector')}">Collector</a></li>
                    <li><a id="menuReason" href="${createLink(controller:'reason')}">Credit Memo Reason</a></li>
                    <li><a id="menuCurrency" href="${createLink(controller:'currencyType')}">Currency</a></li>
                    <li><a id="menuPaymentType" href="${createLink(controller:'paymentType')}">Payment Type</a></li>
                  </ul>
                </li>

                <li><a id="menuProduction" href="#">Production</a>
                  <ul>
                    <li><a id="menuAssembler" href="${createLink(controller:'assembler')}">Assembler</a></li>
                    <li><a id="menuBrand" href="${createLink(controller:'productBrand')}">Brand</a></li>
                    <li><a id="menuCategory" href="${createLink(controller:'productCategory')}">Category</a></li>
                    <li><a id="menuColor" href="${createLink(controller:'productColor')}">Color</a></li>
                    <li><a id="menuItemType" href="${createLink(controller:'itemType')}">Item Type</a></li>
                    <li><a id="menuMaterial" href="${createLink(controller:'productMaterial')}">Material</a></li>
                    <li><a id="menuModel" href="${createLink(controller:'productModel')}">Model</a></li>
                    <li><a id="menuSubcategory" href="${createLink(controller:'productSubcategory')}">Subcategory</a></li>
                    <li><a id="menuDiscountType" href="${createLink(controller:'discountType')}">Discount Type</a></li>
                    <li><a id="menuUnit" href="${createLink(controller:'productUnit')}">Unit</a></li>
                    <li><a id="menuItemLocation" href="${createLink(controller:'itemLocation')}">Item Location</a></li>
                  </ul>
                </li>

                <li><a id="menuSales" href="#">Sales</a>
                  <ul>
                    <li><a id="menuCustomerType" href="${createLink(controller:'customerType')}">Customer Type</a></li>
                    <li><a id="menuDiscountGroup" href="${createLink(controller:'discountGroup')}">Discount Group</a></li>
                  </ul>
                </li>

                <li><a id="menuMisc" href="#">Misc</a>
                  <ul>
                    <li><a id="menuCompany" href="${createLink(controller:'company')}">Company</a></li>
                    <li><a id="menuCity" href="${createLink(controller:'city')}">City</a></li>
                    <li><a id="menuCountry" href="${createLink(controller:'country')}">Country</a></li>
                    <li><a id="menuPackaging" href="${createLink(controller:'packaging')}">Packaging</a></li>
                    <li><a id="menuPersonnel" href="${createLink(controller:'personnel')}">Personnel</a></li>
                    <li><a id="menuPersonnelType" href="${createLink(controller:'personnelType')}">Personnel Type</a></li>
                    <li><a id="menuProvince" href="${createLink(controller:'province')}">Province</a></li>
                    <li><a id="menuRegion" href="${createLink(controller:'region')}">Region</a></li>
                    <li><a id="menuTerms" href="${createLink(controller:'term')}">Terms</a></li>
                    <li><a id="menuTruck" href="${createLink(controller:'truck')}">Truck</a></li>
                  </ul>
                </li>
              </ul>
            </li>
            <li><a id="menuMaster" href="#">Master</a>
              <ul>
                <li><a id="menuCustomer" href="${createLink(controller:'customer')}">Customer</a></li>
                <li><a id="menuForwarder" href="${createLink(controller:'forwarder')}">Forwarder</a></li>
                <li><a id="menuProduct" href="${createLink(controller:'product')}">Product</a></li>
                <li><a id="menuSalesAgent" href="${createLink(controller:'salesAgent')}">Sales Agent</a></li>
                <li><a id="menuSupplier" href="${createLink(controller:'supplier')}">Supplier</a></li>
                <li><a id="menuCheck" href="${createLink(controller:'checkPayment')}">Check</a></li>
                <li><a id="menuWarehouse" href="${createLink(controller:'warehouse')}">Inventory Warehouse</a></li>

              </ul>
            </li>
            <li><a id="menuTransactions" href="#">Transactions</a>
              <ul>
                <li><a id="menuAccounting" href="#">Accounting</a>
                  <ul>
                    <li><a id="menuCustomerCharge" href="${createLink(controller:'customerCharge')}">Customer Charges</a></li>
                    <li><a id="menuDirectPayment" href="${createLink(controller:'directPayment')}">Direct Payment</a></li>
                    <li><a id="menuCounterReceipt" href="${createLink(controller:'counterReceipt')}">Counter Receipt</a></li>
                    <li><a id="menuCollectionSchedule" href="${createLink(controller:'collectionSchedule')}">Collection Schedule</a></li>
                    <li><a id="menuDepositCheck" href="${createLink(controller:'checkDeposit')}">Deposit Checks</a></li>
                    <li><a id="menuBouncedCheck" href="${createLink(controller:'bouncedCheck')}">Bounced Check</a></li>
                    <li><a id="menuWarehousing" href="${createLink(controller:'checkWarehousing')}">Check Warehousing</a></li>
                    <li><a id="menuSupplierPayment" href="${createLink(controller:'supplierPayment')}">Supplier Payment</a></li>
                    <li><a id="menuPriceAdjustment" href="${createLink(controller:'priceAdjustment')}">Price Adjustment</a></li>
                    <li><a id="menuCustomerAccountSummary" href="${createLink(controller:'customer', action:'accountsSummary', id:0)}">Customer Accounts Summary</a></li>
                  </ul>
                </li>

                <li><a id="menuDelivery" href="#">Delivery</a>
                  <ul>
                    <li><a id="menuTripTicket" href="${createLink(controller:'tripTicket')}">Trip Ticket</a></li>
                    <li><a id="menuWaybill" href="${createLink(controller:'waybill')}">Waybill</a></li>
                    <li><a id="menuDirectDelivery" href="${createLink(controller:'directDelivery')}">Direct Delivery</a></li>
                  </ul>
                </li>

                <li><a id="menuInventory" href="#">Inventory</a>
                  <ul>
                  	<li><a id="menuInventoryAdjustment" href="${createLink(controller:'inventoryAdjustment')}">Inventory Adjustment</a>
                    <li><a id="menuTransfer" href="${createLink(controller:'inventoryTransfer')}">Transfer</a></li>                  </ul>
                </li>

                <li><a id="menuTransactionsProduction" href="#">Production</a>
                  <ul>
                    <li><a id="menuJobOrder" href="${createLink(controller:'jobOrder')}">Job Order</a></li>
                    <li><a id="menuJobOut" href="${createLink(controller:'jobOut')}">Job Out</a></li>
                    <li><a id="menuMaterialRelease" href="${createLink(controller:'materialRelease')}">Material Release</a></li>
                  </ul>
                </li>

                <li><a id="menuPurchasing" href="#">Purchasing</a>
                  <ul>
                    <li><a id="menuPurchaseOrder" href="${createLink(controller:'purchaseOrder')}">Order</a></li>
                    <li><a id="menuPurchaseInvoice" href="${createLink(controller:'purchaseInvoice')}">Invoice</a></li>
                  </ul>
                </li>

                <li><a id="menuTransactionsSales" href="#">Sales</a>
                  <ul>

                    <li><a id="menuSalesOrder" href="${createLink(controller:'salesOrder')}">Order</a></li>
                    <li><a id="menuSalesDelivery" href="${createLink(controller:'salesDelivery')}">Delivery</a></li>
                    <li><a id="menuCreditMemo" href="${createLink(controller:'creditMemo')}">Credit Memo</a></li>
                  </ul>
                </li>

              </ul>
            </li>
            <li><a id="menuReport" href="#">Report</a>
              <ul>
                <li><a id="menuReport" href="${createLink(controller:'salesReport',action:'creditMemoSearch')}">Credit Memo</a></li>
                <li><a id="menuReport" href="${createLink(controller:'salesReport',action:'search')}">Sales Report</a></li>
                <li><a id="menuReport" href="${createLink(controller:'salesReport',action:'pendingSearch')}">Pending Sales Orders</a></li>
                <li><a id="menuReport" href="#">Purchase Reports</a>
                    <ul>
                        <li><a id="menuReport" href="${createLink(controller:'salesReport',action:'purchaseSearch')}">Order</a></li>
                        <li><a id="menuReport" href="${createLink(controller:'salesReport',action:'purchaseInvoiceSearch')}">Invoice</a></li>
                    </ul>
                </li>
                <li><a id="menuReport" href="${createLink(controller:'salesReport',action:'directPaymentSearch')}">Direct Payment Report</a></li>
                <li><a id="menuReport" href="${createLink(controller:'productReport',action:'search')}">Product Report</a></li>
			    <g:ifAnyGranted role="ROLE_SUPER">
                <li><a id="menuReport" href="${createLink(controller:'productReport',action:'productCostVsSelling',params:['reportType':'productCostVsSelling'])}">Cost vs. Selling</a></li>
                </g:ifAnyGranted>
                <li><a id="menuReport" href="${createLink(controller:'accountReceivables',action:'index')}">Account Receivables</a></li>
                <li><a id="menuReport" href="${createLink(controller:'yearEndReport',action:'search')}">Year-end Accounting</a></li>
                <li><a id="menuReport" href="${createLink(controller:'customerReport',action:'search')}">Customer</a></li>
                <li><a id="menuReport" href="${createLink(controller:'agingInventoryReport',action:'list')}">Aging of Inventory</a></li>
                <li><a id="menuReport" href="${createLink(controller:'assemblyReport',action:'search')}">Assembly</a></li>
                <li><a href="${createLink(controller:'customerAuditReport',action:'search')}">Customer Audit</a></li>
              </ul>
             </li>
            <li><a id="menuLogout" href="${createLink(controller:'logout')}">Logout</a></li>

          </ul>
          <text class="loggedInInfo">You are logged in as <strong>${loggedInUserInfo(field:'username')}(${loggedInUserInfo(field:'userRealName')})</strong></text>
        </td>
      </tr>
    </table>
    
    <table>
	  <tr class="login">
    	<td class="companyName">
    		<g:set var="companyName" value="${com.munix.Company?.findById('1')?.name}"/>${companyName}
    	</td>
      </tr>
    </table>
    
  </div>
<div id="processingCover">
</div>
<div id="loadingPopup">Processing.. <img src="${resource(dir:'images',file:'spinner.gif')}" alt="loading" border="0" /></div>
<g:layoutBody />
</body>
</html>
