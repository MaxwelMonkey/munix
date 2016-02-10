grails.project.plugins.dir = "./plugins"
grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir	= "target/test-reports"
//grails.project.war.file = "target/${appName}-${appVersion}.war"
grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits( "global" ) {
        // uncomment to disable ehcache
        // excludes 'ehcache'
    }
    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    repositories {        
        grailsPlugins()
        grailsHome()
        grailsCentral()

        // uncomment the below to enable remote dependency resolution
        // from public Maven repositories
        //mavenLocal()
        //mavenCentral()
        //mavenRepo "http://snapshots.repository.codehaus.org"
        //mavenRepo "http://repository.codehaus.org"
        //mavenRepo "http://download.java.net/maven/2/"
        //mavenRepo "http://repository.jboss.com/maven2/"
    }
    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.

        // runtime 'mysql:mysql-connector-java:5.1.5'
    }

}

coverage{
	enabledByDefault = true
	xml = true
    exclusions = [
        "**/Assembler*",
        "**/BankAccount*",
        "**/BankBranch*",
        "**/Bank*",
        "**/Charge*",
        "**/CheckDeposit*",
        "**/CheckPayment*",
        "**/CheckType*",
		"**/CheckStatus*",
        "**/CheckWarehouse*",
        "**/CheckWarehousing*",
        "**/City*",
        "**/CollectionSchedule*",
        "**/CollectionScheduleItem*",
        "**/Collector*",
        "**/Company*",
        "**/Country*",
        "**/CreditMemoFixture*",
		"**/CreditMemoRobot*",
        "**/CurrencyType*",
        "**/CustomerDiscount*",
		"**/CustomerLedgerRobot*",
        "**/CustomerType*",
        "**/DirectDelivery*",
        "**/DirectDeliveryPackage*",
        "**/DirectPaymentItem*",
        "**/DiscountGroup*",
        "**/Forwarder*",
		"**/ItemType*",
        "**/JobOrderOut*",
        "**/LedgerEntry*",
        "**/Login*",
        "**/Logout*",
        "**/MaterialRequisition*",
        "**/MaterialRequisitionItem*",
		"**/MigratorService*",
        "**/Packaging*",
        "**/PaymentType*",
        "**/Personnel*",
        "**/PersonnelType*",
        "**/ProductBrand*",
        "**/ProductCategory*",
        "**/ProductColor*",
        "**/ProductComponent*",
        "**/ProductMaterial*",
        "**/ProductModel*",
        "**/ProductSubcategory*",
        "**/DiscountType*",
        "**/ProductUnit*",
        "**/Province*",
        "**/PurchaseDelivery*",
        "**/PurchaseDeliveryItem*",
        "**/PurchaseOrderItem*",
        "**/Region*",
        "**/Report*",
        "**/Requestmap*",
        "**/Role*",
        "**/SalesAgent*",
        "**/SalesInvoice*",
        "**/SalesInvoiceItem*",
		"**/SalesReportController*",
        "**/Schedule*",
		"**/StockController*",
        "**/StockTransfer*",
        "**/StockTransferItem*",
        "**/Supplier",
        "**/SupplierItem*",
        "**/Term*",
        "**/TripTicket*",
        "**/TripTicketItem*",
        "**/Truck*",
        "**/User*",
        "**/Warehouse*",
        "**/Waybill*",
        "**/WaybillPackage*",
        "**/SecurityConfig*",
        "**/BuildConfig*",
	"**/PrintController*",
	"**/HomeController*"
    ]
}

codenarc.reports = {
	MyXmlReport('xml') {
		outputFile = 'CodeNarcReport.xml'
	}
	MyHtmlReport('html') {
		outputFile = 'CodeNarcReport.html'
	}
}
