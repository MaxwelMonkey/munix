package com.munix

class ApprovalProductUpdate {

	ApprovalProcess approvalProcess
	Product product
    ProductBrand brand
    ProductCategory category
    ProductColor color
    ProductMaterial material
    ProductModel model
    ProductSubcategory subcategory
    DiscountType type
    ProductUnit unit
    ItemType itemType
    Boolean isComponent//used a component for the final product
    Boolean isForSale//the product is for sale
    Boolean isForAssembly //the product can be assembled
    String status
    
    static belongsTo = ApprovalProcess

    static constraints = {
        product(nullable:false)
        brand(nullable:true)
        category(nullable:true)
        color(nullable:true)
        material(nullable:true)
        model(nullable:true)
        subcategory(nullable:true)
        type(nullable:true)
        unit(nullable:true)
        itemType(nullable:true)
        isComponent(nullable:true)
        isForSale(nullable:true)
        isForAssembly(nullable:true)
        status(nullable:true)
    }
    
}
