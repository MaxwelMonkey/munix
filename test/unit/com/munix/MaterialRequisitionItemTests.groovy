package com.munix

import grails.test.*

class MaterialRequisitionItemTests extends GrailsUnitTestCase {
	def sampleJobOrder
	def sampleMaterialRequisition
	def sampleMaterialRequisitionItem
	
    protected void setUp() {
		super.setUp()
		sampleJobOrder = new JobOrder(qty: 3)
		mockDomain(JobOrder, [sampleJobOrder])
		
		sampleMaterialRequisition = new MaterialRequisition(jobOrder: sampleJobOrder)
		mockDomain(MaterialRequisition, [sampleMaterialRequisition])
		
		sampleMaterialRequisitionItem = new MaterialRequisitionItem(
			requisition: sampleMaterialRequisition,
			unitsRequired: new BigDecimal("33.3333"))
		mockDomain(MaterialRequisitionItem, [sampleMaterialRequisitionItem])
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testComputeQuantity() {
		assertEquals "wrong quantity", new BigDecimal("100"), sampleMaterialRequisitionItem.computeQuantity()
    }
}
