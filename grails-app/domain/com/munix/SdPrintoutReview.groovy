package com.munix
import org.apache.commons.collections.list.LazyList;
import org.apache.commons.collections.FactoryUtils;

import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date;
import java.math.RoundingMode;

class SdPrintoutReview {
    
	SalesDelivery salesDelivery
	User reviewer
	Boolean reviewed = false
	
	static belongsTo = [salesDelivery:SalesDelivery, reviewer:User]
	
    static constraints = {
		salesDelivery(nullable:false)
		reviewer(nullable:false)
		reviewed(nullable:true)
    }
	
}
