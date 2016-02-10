package com.munix

import grails.test.*

class MaterialRequisitionTests extends GrailsUnitTestCase {
	def sampleProduct1
	def sampleProduct2
	def sampleJobOrder
	def sampleMaterialRequisition
	def sampleMaterialRequisitionItem1
	def sampleMaterialRequisitionItem2
	def sampleMaterialRelease
	def sampleMaterialReleaseItem1
	
	
    protected void setUp() {
        super.setUp()
		
		sampleProduct1 = new Product(identifier: "product1", runningCost: new BigDecimal("200"))
		sampleProduct2 = new Product(identifier: "product2", runningCost: new BigDecimal("400"))
		mockDomain(Product, [sampleProduct1, sampleProduct2])
		
		sampleJobOrder = new JobOrder(qty: 100)
		mockDomain(JobOrder, [sampleJobOrder])
		
		sampleMaterialRequisition = new MaterialRequisition()
		mockDomain(MaterialRequisition, [sampleMaterialRequisition])
		
		sampleMaterialRequisitionItem1 = new MaterialRequisitionItem(component: sampleProduct1, unitsRequired: 10)
		sampleMaterialRequisitionItem2 = new MaterialRequisitionItem(component: sampleProduct2, unitsRequired: 20)
		mockDomain(MaterialRequisitionItem, [sampleMaterialRequisitionItem1, sampleMaterialRequisitionItem2])		
		
		sampleMaterialRequisition.jobOrder = sampleJobOrder
		sampleMaterialRequisition.addToItems(sampleMaterialRequisitionItem1)
		sampleMaterialRequisition.addToItems(sampleMaterialRequisitionItem2)
		
		sampleMaterialRelease = new MaterialRelease(status: MaterialRelease.Status.APPROVED)
		mockDomain(MaterialRelease,[sampleMaterialRelease])
		
		sampleMaterialReleaseItem1 = new MaterialReleaseItem(materialRequisitionItem: sampleMaterialRequisitionItem1, qty: 90, cost: 1000)
		mockDomain(MaterialReleaseItem, [sampleMaterialReleaseItem1])
		
		sampleMaterialRelease.addToItems(sampleMaterialReleaseItem1)
		MaterialRequisitionItem.metaClass.static.getMaterialReleaseItems = { -> return [sampleMaterialReleaseItem1]}
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testComputeTotalCostOfMaterialsPerAssembly() {
		assertEquals "wrong computation", new BigDecimal("2000"), sampleMaterialRequisition.computeTotalCostOfMaterialsPerAssembly()
    }
	
	void testComputeTotalCostOfMaterialsForJobOrder() {
		assertEquals "wrong computation", new BigDecimal("3000000"), sampleMaterialRequisition.computeTotalCostOfMaterialsForJobOrder()
	}
}
