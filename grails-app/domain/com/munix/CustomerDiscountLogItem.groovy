package com.munix

import java.text.DateFormat
import java.text.SimpleDateFormat

class CustomerDiscountLogItem {
    User user
    Date date
    DiscountGroup discountGroup
    DiscountType discountType
	CustomerDiscount.Type type

	static belongsTo = [customerDiscountLog: CustomerDiscountLog]
	 
    static constraints = {
        user(nullable:true)
        date(nullable:true)
        discountGroup(nullable:true)
        discountType(nullable:true)
    }

    String formatLog() {
		DateFormat sdf = new SimpleDateFormat("MMM. dd, yyyy")
		 
		def log = "Date: " + sdf.format(date)
        def userName = user? user?.userRealName : "N/A"
        log += ", User: " + userName
        if(discountGroup) {
            log += ", Discount Group: ${discountGroup}"
        } else {
            log += ", Discount Group: N/A"
        }
        if(discountType) {
            log += ", Discount Type: ${discountType}"
        } else {
            log += ", Discount Type: N/A"
        }
		if(type) {
			log += ", Type: ${type}"
		} else {
			log += ", Type: N/A"
		}
		return log
    }
}
