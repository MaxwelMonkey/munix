package com.munix
import java.text.SimpleDateFormat;
import java.text.DecimalFormat;

class PurchaseOrderId {
	String purchaseOrderId
	Supplier supplier
	PurchaseOrder po
	String year
	
    static constraints = {
		purchaseOrderId(nullable: false)
		supplier(nullable: false)
		po(nullable: true)
		year(nullable: false)
    }
	
	String currentYear(){
		def newDate=new Date()
		def yearOnly=new SimpleDateFormat("yyyy")
		return yearOnly.format(newDate)
	}
	
	int countPoId(){
		return PurchaseOrderId.findAllBySupplierAndYear(supplier,year).size()
	}
	
	String createNewPoId(){
		def ctr=countPoId()+1
		def number=new DecimalFormat("#000")
		return supplier.identifier+"-"+year+"-"+number.format(ctr)
	}
	
	String toString(){
		return purchaseOrderId
	}
}
