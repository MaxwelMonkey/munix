package com.munix
import org.apache.commons.collections.list.LazyList;
import org.apache.commons.collections.FactoryUtils;

class MaterialRequisition {
    Date date = new Date()
    String preparedBy
    String approvedBy
    static belongsTo = [jobOrder:JobOrder]
    List items = new ArrayList()
    static hasMany = [items:MaterialRequisitionItem]

    static constraints = {
        date(nullable:false)
        preparedBy(nullable:false)
        jobOrder(nullable:false)
        items(cascade:"all,delete-orphan")
        approvedBy(nullable: true)
    }

    String toString() {
        "MR-${formatId()}"
    }

    String formatId(){
        "${id}".padLeft(8,'0')
    }

    def isComplete(){
        def incompleteItems = items?.findAll{ it?.computeRemainingBalance()?.intValue() > 0}
        return incompleteItems?.isEmpty() && !items?.isEmpty()
    }

    def getMaterialList(){
        return LazyList.decorate(items, FactoryUtils.instantiateFactory(MaterialRequisitionItem.class))
    }

	BigDecimal computeTotalCostOfMaterialsPerAssembly() {
		def total = BigDecimal.ZERO
		items.each {
			total += it.computeFinalCostPerComponent()
		}
		return total
	}
	
	BigDecimal computeTotalCostOfMaterialsForJobOrder() {
		def total = BigDecimal.ZERO
		items.each {
			total += it.computeFinalCost()
		}
		return total
	}
}
