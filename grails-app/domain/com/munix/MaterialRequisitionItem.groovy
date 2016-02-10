package com.munix

class MaterialRequisitionItem {
    Product component
    BigDecimal unitsRequired
    boolean isDeleted
    static transients = [ 'isDeleted' ]

    static belongsTo = [requisition:MaterialRequisition]
    
    static constraints = {
        component(nullable: false)
        unitsRequired(nullable: false, min: 0.0001, scale: 4)
        requisition(nullable: true)
    }

    String formatUnitsRequired(){
        "${String.format('%,.2f',unitsRequired)}"
    }

    BigDecimal computeQuantity(){
        (unitsRequired * requisition.jobOrder.qty).setScale(0, BigDecimal.ROUND_HALF_UP);
    }

    BigDecimal computeCostPerUnit(){
        component.runningCost?:0
    }

    BigDecimal computeCostPerAssembly(){
        unitsRequired * (computeCostPerUnit()?:0)
    }

    BigDecimal computeTotalCost(){
        (computeCostPerAssembly()?:0) * requisition.jobOrder.qty
    }

    void setProductComponent(id){
        this.component = Product.findById(id)
    }
	
	List<MaterialReleaseItem> getMaterialReleaseItems() {
		def materialReleaseItems = MaterialReleaseItem.withCriteria {
            eq('materialRequisitionItem',this)
            materialRelease{
                eq("status", MaterialRelease.Status.APPROVED)
            }
        }
		def releaseItems = []
		materialReleaseItems.each {
            if(it.qty > 0){
                releaseItems.add(it)
            }
		}
		return releaseItems
	}
	
	BigDecimal computeFinalCostPerComponent() {
		def releaseItems = getMaterialReleaseItems()
		def totalQty = 0
		def totalCost = 0
		
		releaseItems.each{
            totalCost += it.qty * it.cost
            totalQty += it.qty
		}
		
		if(totalQty!=0) {
			return totalCost/totalQty
		} else {
			return 0
		}
	}
	
	BigDecimal computeFinalCost() {
		return computeFinalCostPerComponent() * computeQuantity()
	}
	
}
