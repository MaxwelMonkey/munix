insert into requestmap (url, config_attribute) values (	"url","config_attribute"	);
insert into requestmap (url, config_attribute) values (	"/","ROLE_PURCHASING,ROLE_MANAGER_PURCHASING,ROLE_ADMIN_PURCHASING,ROLE_PRODUCTION,ROLE_MANAGER_PRODUCTION,ROLE_SALES,ROLE_MANAGER_SALES,ROLE_DELIVERY,ROLE_MANAGER_DELIVERY,ROLE_ACCOUNTING,ROLE_MANAGER_ACCOUNTING"	);
insert into requestmap (url, config_attribute) values (	"/**","IS_AUTHENTICATED_FULLY"	);
insert into requestmap (url, config_attribute) values (	"/assemble/**","ROLE_ADMIN_PURCHASING"	);
insert into requestmap (url, config_attribute) values (	"/bank/**","ROLE_MANAGER_ACCOUNTING"	);
insert into requestmap (url, config_attribute) values (	"/bankAccount/**","ROLE_MANAGER_ACCOUNTING"	);
insert into requestmap (url, config_attribute) values (	"/bankBranch/**","ROLE_ACCOUNTING"	);
insert into requestmap (url, config_attribute) values (	"/captcha/**","IS_AUTHENTICATED_ANONYMOUSLY"	);
insert into requestmap (url, config_attribute) values (	"/charge/**","ROLE_MANAGER_ACCOUNTING"	);

insert into requestmap (url, config_attribute) values (	"/checkDeposit/approve","ROLE_MANAGER_ACCOUNTING");

insert into requestmap (url, config_attribute) values (	"/checkPayment/**","ROLE_ACCOUNTING"	);
insert into requestmap (url, config_attribute) values (	"/checkType/**","ROLE_ADMIN"	);
insert into requestmap (url, config_attribute) values (	"/checkWarehouse/**","ROLE_MANAGER_ACCOUNTING"	);
insert into requestmap (url, config_attribute) values (	"/city/**","ROLE_MANAGER_ACCOUNTING"	);
insert into requestmap (url, config_attribute) values (	"/collector/**","ROLE_MANAGER_ACCOUNTING"	);
insert into requestmap (url, config_attribute) values (	"/company/**","ROLE_MANAGER_ACCOUNTING"	);
insert into requestmap (url, config_attribute) values (	"/country/**","ROLE_MANAGER_ACCOUNTING"	);

insert into requestmap (url, config_attribute) values (	"/creditMemo/**","ROLE_MANAGER_SALES,ROLE_SALES,ROLE_ACCOUNTING,ROLE_MANAGER_ACCOUNTING");
insert into requestmap (url, config_attribute) values (	"/creditMemo/index","ROLE_MANAGER_SALES,ROLE_SALES,ROLE_ACCOUNTING,ROLE_MANAGER_ACCOUNTING");
insert into requestmap (url, config_attribute) values (	"/creditMemo/list","ROLE_MANAGER_ACCOUNTING, ROLE_MANAGER_SALES");
insert into requestmap (url, config_attribute) values (	"/creditMemo/unpaidList","ROLE_MANAGER_SALES,ROLE_SALES,ROLE_ACCOUNTING,ROLE_MANAGER_ACCOUNTING");
insert into requestmap (url, config_attribute) values (	"/creditMemo/create","ROLE_SALES");
insert into requestmap (url, config_attribute) values (	"/creditMemo/save","ROLE_SALES");
insert into requestmap (url, config_attribute) values (	"/creditMemo/show","ROLE_MANAGER_SALES,ROLE_SALES,ROLE_ACCOUNTING,ROLE_MANAGER_ACCOUNTING");
insert into requestmap (url, config_attribute) values (	"/creditMemo/edit","ROLE_SALES");
insert into requestmap (url, config_attribute) values (	"/creditMemo/update","ROLE_SALES");
insert into requestmap (url, config_attribute) values (	"/creditMemo/doPrint","ROLE_MANAGER_SALES,ROLE_SALES,ROLE_ACCOUNTING,ROLE_MANAGER_ACCOUNTING");
insert into requestmap (url, config_attribute) values (	"/creditMemo/approve","ROLE_MANAGER_SALES");
insert into requestmap (url, config_attribute) values (	"/creditMemo/approveTwo","ROLE_MANAGER_ACCOUNTING");
insert into requestmap (url, config_attribute) values (	"/creditMemo/unapprove","ROLE_ACCOUNTING");
insert into requestmap (url, config_attribute) values (	"/creditMemo/cancel","ROLE_ACCOUNTING");


insert into requestmap (url, config_attribute) values (	"/css/**","IS_AUTHENTICATED_ANONYMOUSLY"	);
insert into requestmap (url, config_attribute) values (	"/currencyType/**","ROLE_ADMIN_PURCHASING"	);
insert into requestmap (url, config_attribute) values (	"/customer/create","ROLE_ACCOUNTING"	);
insert into requestmap (url, config_attribute) values (	"/customer/list","ROLE_SALES,ROLE_ACCOUNTING"	);
insert into requestmap (url, config_attribute) values (	"/customerLedger/show","ROLE_ACCOUNTING"	);
insert into requestmap (url, config_attribute) values (	"/customerType/**","ROLE_MANAGER_ACCOUNTING"	);
insert into requestmap (url, config_attribute) values (	"/discountGroup/**","ROLE_ADMIN"	);
insert into requestmap (url, config_attribute) values (	"/forwarder/**","ROLE_SALES, ROLE_ACCOUNTING, ROLE_PURCHASING"	);
insert into requestmap (url, config_attribute) values (	"/images/**","IS_AUTHENTICATED_ANONYMOUSLY"	);
insert into requestmap (url, config_attribute) values (	"/invoiceType/**","ROLE_MANAGER_ACCOUNTING"	);

insert into requestmap (url, config_attribute) values (	"/jobOrder/**","ROLE_MANAGER_PRODUCTION");
insert into requestmap (url, config_attribute) values (	"/jobOrder/index","ROLE_PRODUCTION, ROLE_MANAGER_PRODUCTION");
insert into requestmap (url, config_attribute) values (	"/jobOrder/list","ROLE_PRODUCTION, ROLE_MANAGER_PRODUCTION");
insert into requestmap (url, config_attribute) values (	"/jobOrder/create","ROLE_PRODUCTION");
insert into requestmap (url, config_attribute) values (	"/jobOrder/save","ROLE_PRODUCTION");
insert into requestmap (url, config_attribute) values (	"/jobOrder/show","ROLE_PRODUCTION, ROLE_MANAGER_PRODUCTION");
insert into requestmap (url, config_attribute) values (	"/jobOrder/edit","ROLE_PRODUCTION");
insert into requestmap (url, config_attribute) values (	"/jobOrder/jobOrderEdit","ROLE_PRODUCTION");
insert into requestmap (url, config_attribute) values (	"/jobOrder/udpateJobOrder","ROLE_PRODUCTION");
insert into requestmap (url, config_attribute) values (	"/jobOrder/markAsComplete","ROLE_MANAGER_PRODUCTION");
insert into requestmap (url, config_attribute) values (	"/jobOrder/unmarkAsComplete","ROLE_MANAGER_PRODUCTION");
insert into requestmap (url, config_attribute) values (	"/jobOrder/approve","ROLE_MANAGER_PRODUCTION");
insert into requestmap (url, config_attribute) values (	"/jobOrder/unapprove","ROLE_MANAGER_PRODUCTION");
insert into requestmap (url, config_attribute) values (	"/jobOrder/addMaterialRelease","ROLE_PRODUCTION");
insert into requestmap (url, config_attribute) values (	"/jobOrder/addJobOut","ROLE_PRODUCTION");
insert into requestmap (url, config_attribute) values (	"/jobOrder/cancel","ROLE_PRODUCTION, ROLE_MANAGER_PRODUCTION");
insert into requestmap (url, config_attribute) values (	"/jobOrder/approveMaterialReleases","ROLE_MANAGER_PRODUCTION");
insert into requestmap (url, config_attribute) values (	"/jobOrder/unapproveMaterialReleases","ROLE_MANAGER_PRODUCTION");

insert into requestmap (url, config_attribute) values (	"/jobOut/edit","ROLE_PRODUCTION");
insert into requestmap (url, config_attribute) values (	"/jobOut/view","ROLE_PRODUCTION");
insert into requestmap (url, config_attribute) values (	"/jobOut/list","ROLE_PRODUCTION");
insert into requestmap (url, config_attribute) values (	"/jobOut/cancel","ROLE_MANAGER_PRODUCTION");
insert into requestmap (url, config_attribute) values (	"/jobOut/approve","ROLE_MANAGER_PRODUCTION");
insert into requestmap (url, config_attribute) values (	"/jobOut/unapprove","ROLE_MANAGER_PRODUCTION");

insert into requestmap (url, config_attribute) values (	"/js/**","IS_AUTHENTICATED_ANONYMOUSLY"	);
insert into requestmap (url, config_attribute) values (	"/login/**","IS_AUTHENTICATED_ANONYMOUSLY"	);
insert into requestmap (url, config_attribute) values (	"/materialRelease/**","ROLE_PRODUCTION"	);
insert into requestmap (url, config_attribute) values (	"/materialRequisiton/**","ROLE_PRODUCTION"	);
insert into requestmap (url, config_attribute) values (	"/packaging/**","ROLE_MANAGER_SALES"	);
insert into requestmap (url, config_attribute) values (	"/paymentType/**","ROLE_ADMIN"	);
insert into requestmap (url, config_attribute) values (	"/personnel/**","ROLE_SALES"	);
insert into requestmap (url, config_attribute) values (	"/personnelType/**","ROLE_SALES"	);
insert into requestmap (url, config_attribute) values (	"/product/create","ROLE_MANAGER_PURCHASING"	);
insert into requestmap (url, config_attribute) values (	"/product/list","ROLE_SALES,ROLE_PURCHASING,ROLE_ACCOUNTING,ROLE_PRODUCTION"	);
insert into requestmap (url, config_attribute) values (	"/productBrand/**","ROLE_MANAGER_PURCHASING"	);
insert into requestmap (url, config_attribute) values (	"/productColor/**","ROLE_MANAGER_PURCHASING"	);
insert into requestmap (url, config_attribute) values (	"/productMaterial/**","ROLE_MANAGER_PURCHASING"	);
insert into requestmap (url, config_attribute) values (	"/productModel/**","ROLE_MANAGER_PURCHASING"	);
insert into requestmap (url, config_attribute) values (	"/productSubcategory/**","ROLE_MANAGER_PURCHASING"	);
insert into requestmap (url, config_attribute) values (	"/productType/**","ROLE_MANAGER_PURCHASING"	);
insert into requestmap (url, config_attribute) values (	"/productUnit/**","ROLE_MANAGER_PURCHASING"	);
insert into requestmap (url, config_attribute) values (	"/prodvince/**","ROLE_MANAGER_ACCOUNTING"	);

insert into requestmap (url, config_attribute) values (	"/purchaseInvoice/**","ROLE_MANAGER_PURCHASING");
insert into requestmap (url, config_attribute) values (	"/purchaseInvoice/index","ROLE_PURCHASING, ROLE_MANAGER_PURCHASING");
insert into requestmap (url, config_attribute) values (	"/purchaseInvoice/filter","ROLE_PURCHASING");
insert into requestmap (url, config_attribute) values (	"/purchaseInvoice/list","ROLE_PURCHASING");
insert into requestmap (url, config_attribute) values (	"/purchaseInvoice/create","ROLE_PURCHASING");
insert into requestmap (url, config_attribute) values (	"/purchaseInvoice/save","ROLE_PURCHASING");
insert into requestmap (url, config_attribute) values (	"/purchaseInvoice/show","ROLE_PURCHASING, ROLE_MANAGER_PURCHASING");
insert into requestmap (url, config_attribute) values (	"/purchaseInvoice/edit","ROLE_PURCHASING");
insert into requestmap (url, config_attribute) values (	"/purchaseInvoice/update","ROLE_PURCHASING");
insert into requestmap (url, config_attribute) values (	"/purchaseInvoice/approve","ROLE_MANAGER_PURCHASING");
insert into requestmap (url, config_attribute) values (	"/purchaseInvoice/unapprove","ROLE_MANAGER_PURCHASING");
insert into requestmap (url, config_attribute) values (	"/purchaseInvoice/cancel","ROLE_MANAGER_PURCHASING");
insert into requestmap (url, config_attribute) values (	"/purchaseInvoice/retrievePurchaseOrderItems","ROLE_PURCHASING");

insert into requestmap (url, config_attribute) values (	"/purchaseOrder/**","ROLE_MANAGER_PURCHASING");
insert into requestmap (url, config_attribute) values (	"/purchaseOrder/index","ROLE_PURCHASING, ROLE_MANAGER_PURCHASING");
insert into requestmap (url, config_attribute) values (	"/purchaseOrder/list","ROLE_PURCHASING, ROLE_MANAGER_ACCOUNTING");
insert into requestmap (url, config_attribute) values (	"/purchaseOrder/unpaidList","ROLE_ACCOUNTING,ROLE_MANAGER_ACCOUNTING,ROLE_PURCHASING, ROLE_MANAGER_SALES");
insert into requestmap (url, config_attribute) values (	"/purchaseOrder/create","ROLE_PURCHASING");
insert into requestmap (url, config_attribute) values (	"/purchaseOrder/save","ROLE_PURCHASING");
insert into requestmap (url, config_attribute) values (	"/purchaseOrder/show","ROLE_PURCHASING, ROLE_MANAGER_ACCOUNTING");
insert into requestmap (url, config_attribute) values (	"/purchaseOrder/edit","ROLE_PURCHASING");
insert into requestmap (url, config_attribute) values (	"/purchaseOrder/update","ROLE_PURCHASING");
insert into requestmap (url, config_attribute) values (	"/purchaseOrder/doPrint","ROLE_MANAGER_ACCOUNTING, ROLE_PURCHASING");
insert into requestmap (url, config_attribute) values (	"/purchaseOrder/approve","ROLE_MANAGER_PURCHASING");
insert into requestmap (url, config_attribute) values (	"/purchaseOrder/approveTwo","ROLE_MANAGER_ACCOUNTING");
insert into requestmap (url, config_attribute) values (	"/purchaseOrder/unapprove","ROLE_MANAGER_PURCHASING,ROLE_MANAGER_ACCOUNTING");
insert into requestmap (url, config_attribute) values (	"/purchaseOrder/cancel","ROLE_PURCHASING");

insert into requestmap (url, config_attribute) values (	"/region/**","ROLE_MANAGER_ACCOUNTING"	);
insert into requestmap (url, config_attribute) values (	"/requestmap/**","ROLE_ADMIN"	);
insert into requestmap (url, config_attribute) values (	"/role/**","ROLE_ADMIN"	);
insert into requestmap (url, config_attribute) values (	"/salesAgent/create","ROLE_ACCOUNTING");
insert into requestmap (url, config_attribute) values (	"/salesAgent/list","ROLE_ACCOUNTING,ROLE_SALES");

insert into requestmap (url, config_attribute) values (	"/salesDelivery/**","ROLE_MANAGER_SALES"	);
insert into requestmap (url, config_attribute) values (	"/salesDelivery/index","ROLE_MANAGER_SALES,ROLE_SALES,ROLE_ACCOUNTING,ROLE_MANAGER_ACCOUNTING");
insert into requestmap (url, config_attribute) values (	"/salesDelivery/create","ROLE_SALES");
insert into requestmap (url, config_attribute) values (	"/salesDelivery/list","ROLE_SALES,ROLE_ACCOUNTING");
insert into requestmap (url, config_attribute) values (	"/salesDelivery/show","ROLE_SALES,ROLE_ACCOUNTING");
insert into requestmap (url, config_attribute) values (	"/salesDelivery/unpaidList","ROLE_SALES,ROLE_ACCOUNTING");
insert into requestmap (url, config_attribute) values (	"/salesDelivery/save","ROLE_SALES");
insert into requestmap (url, config_attribute) values (	"/salesDelivery/edit","ROLE_SALES");
insert into requestmap (url, config_attribute) values (	"/salesDelivery/update","ROLE_SALES");
insert into requestmap (url, config_attribute) values (	"/salesDelivery/approve","ROLE_MANAGER_SALES");
insert into requestmap (url, config_attribute) values (	"/salesDelivery/unapprove","ROLE_MANAGER_SALES,ROLE_MANAGER_ACCOUNTING");
insert into requestmap (url, config_attribute) values (	"/salesDelivery/print","ROLE_SALES,ROLE_MANAGER_ACCOUNTING");
insert into requestmap (url, config_attribute) values (	"/salesDelivery/cancel","ROLE_MANAGER_SALES,ROLE_MANAGER_ACCOUNTING");
insert into requestmap (url, config_attribute) values (	"/salesDelivery/viewPriceMargin","ROLE_MANAGER_SALES,ROLE_MANAGER_ACCOUNTING");

insert into requestmap (url, config_attribute) values (	"/salesOrder/**","ROLE_MANAGER_SALES"	);
insert into requestmap (url, config_attribute) values (	"/salesOrder/create","ROLE_SALES"	);
insert into requestmap (url, config_attribute) values (	"/salesOrder/index","ROLE_MANAGER_SALES,ROLE_SALES,ROLE_ACCOUNTING,ROLE_MANAGER_ACCOUNTING");
insert into requestmap (url, config_attribute) values (	"/salesOrder/list","ROLE_SALES,ROLE_ACCOUNTING");
insert into requestmap (url, config_attribute) values (	"/salesOrder/show","ROLE_SALES,ROLE_ACCOUNTING");
insert into requestmap (url, config_attribute) values (	"/salesOrder/edit","ROLE_SALES");
insert into requestmap (url, config_attribute) values (	"/salesOrder/cancel","ROLE_SALES,ROLE_ACCOUNTING");
insert into requestmap (url, config_attribute) values (	"/salesOrder/approve","ROLE_MANAGER_SALES,ROLE_SALES");
insert into requestmap (url, config_attribute) values (	"/salesOrder/unapprove","ROLE_MANAGER_SALES");
insert into requestmap (url, config_attribute) values (	"/salesOrder/filter","ROLE_SALES,ROLE_ACCOUNTING");
insert into requestmap (url, config_attribute) values (	"/salesOrder/save","ROLE_SALES");
insert into requestmap (url, config_attribute) values (	"/salesOrder/update","ROLE_SALES");
insert into requestmap (url, config_attribute) values (	"/salesOrder/retrieveProductsForSale","ROLE_SALES");
insert into requestmap (url, config_attribute) values (	"/salesOrder/retrieveCustomerRemainingCredit","ROLE_SALES");
insert into requestmap (url, config_attribute) values (	"/salesOrder/retrieveDiscountGroups","ROLE_SALES");
insert into requestmap (url, config_attribute) values (	"/salesOrder/retrieveCustomerDiscounts","ROLE_SALES");
insert into requestmap (url, config_attribute) values (	"/salesOrder/approveTwo","ROLE_ACCOUNTING");
insert into requestmap (url, config_attribute) values (	"/salesOrder/print","ROLE_MANAGER_SALES,ROLE_SALES,ROLE_ACCOUNTING,ROLE_MANAGER_ACCOUNTING");
insert into requestmap (url, config_attribute) values (	"/salesOrder/printNoPrice","ROLE_MANAGER_SALES,ROLE_SALES,ROLE_ACCOUNTING,ROLE_MANAGER_ACCOUNTING");
insert into requestmap (url, config_attribute) values (	"/salesOrder/markAsComplete","ROLE_SALES");
insert into requestmap (url, config_attribute) values (	"/salesOrder/viewPriceMargin","ROLE_MANAGER_SALES");



insert into requestmap (url, config_attribute) values (	"/salesReport/directPaymentSearch","ROLE_ACCOUNTING"	);
insert into requestmap (url, config_attribute) values (	"/salesReport/pendingSearch","ROLE_SALES"	);
insert into requestmap (url, config_attribute) values (	"/salesReport/purchaseSearch","ROLE_PURCHASING"	);
insert into requestmap (url, config_attribute) values (	"/salesReport/search","ROLE_ACCOUNTING"	);
insert into requestmap (url, config_attribute) values (	"/supplier/create","ROLE_PURCHASING"	);
insert into requestmap (url, config_attribute) values (	"/supplier/list","ROLE_PURCHASING"	);
insert into requestmap (url, config_attribute) values (	"/term/**","ROLE_MANAGER_ACCOUNTING"	);
insert into requestmap (url, config_attribute) values (	"/tripTicket/create","ROLE_SALES"	);
insert into requestmap (url, config_attribute) values (	"/tripTicket/list","ROLE_SALES,ROLE_ACCOUNTING"	);
insert into requestmap (url, config_attribute) values (	"/tripTicket/show","ROLE_SALES,ROLE_ACCOUNTING"	);
insert into requestmap (url, config_attribute) values (	"/truck/**","ROLE_MANAGER_SALES"	);
insert into requestmap (url, config_attribute) values (	"/user/**","ROLE_ADMIN"	);
insert into requestmap (url, config_attribute) values (	"/warehouse/**","ROLE_MANAGER_PURCHASING"	);
insert into requestmap (url, config_attribute) values (	"/waybill/create","ROLE_SALES"	);
insert into requestmap (url, config_attribute) values (	"/waybill/list","ROLE_SALES,ROLE_ACCOUNTING"	);
insert into requestmap (url, config_attribute) values (	"/waybill/show","ROLE_SALES,ROLE_ACCOUNTING"	);
