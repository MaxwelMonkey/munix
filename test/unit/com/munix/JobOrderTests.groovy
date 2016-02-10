package com.munix

import grails.test.*

class JobOrderTests extends GrailsUnitTestCase {
    def sampleJobOrder
    def sampleProduct
    def sampleMaterialRelease
    def sampleMaterialRelease2
    def sampleMaterialReleaseItem
    def sampleMaterialReleaseItem2
    def sampleMaterialRequisitionItem
    def sampleMaterialRequisition
	
    protected void setUp() {
        super.setUp()
        setupProducts()
        setupMaterialRequisitions()
        setupMaterialRequisitionItems()
        setupMaterialReleaseItems()
        setupMaterialReleases()
        setupJobOrders()
    }

    protected void tearDown() {
        super.tearDown()
    }
	
	void testCost() {
		assertEquals "should have correct cost", new BigDecimal("80"), sampleJobOrder.cost
	}

    void testComputeReleasesItemTotal() {
		sampleMaterialRelease.approve()
		sampleMaterialRelease2.approve()
        def result = sampleJobOrder.computeReleasesItemTotal(sampleProduct)
        assertEquals "The result must the summation of the qty of the two release items which is 7", 7, result
    }

    void testComputeRequisitionItemRemainingBalance() {
		sampleMaterialRelease.approve()
		sampleMaterialRelease2.approve()
        def result = sampleJobOrder.computeRequisitionItemRemainingBalance(sampleMaterialRequisitionItem)
        assertEquals "The result must be the difference of the summation of release item and the quantity required which is 3", 3, result
    }

	void testIsJobOrderApproved() {
		sampleJobOrder.status = JobOrder.Status.JOB_ORDER_APPROVED
		
		assertTrue "job order should be approved for item release", sampleJobOrder.isJobOrderApproved()
	}
	
	void testApproveJobOrder() {
		sampleJobOrder.approveJobOrder()
		
		assertTrue "job order status should be approved for item release", sampleJobOrder.isJobOrderApproved()
	}
	
	void testIsMaterialReleasesApproved() {
		sampleJobOrder.status = JobOrder.Status.MATERIAL_RELEASES_APPROVED
		
		assertTrue "job order should be approved for job out creation", sampleJobOrder.isMaterialReleasesApproved()
	}
	
	void testApproveMaterialReleases() {
		sampleJobOrder.approveMaterialReleases()
		
		assertTrue "job order status should be approved for job out creation", sampleJobOrder.isMaterialReleasesApproved()
	}
	
	void testIsMaterialReleasesUnapproved() {
		sampleJobOrder.status = JobOrder.Status.JOB_ORDER_APPROVED
		
		assertTrue "job order should be approved for item release", sampleJobOrder.isJobOrderApproved()
	}
	
	void testUnapproveMaterialReleases() {
		sampleJobOrder.unapproveMaterialReleases()
		
		assertTrue "job order status should be approved for item release", sampleJobOrder.isJobOrderApproved()
	}
	
	void testHasAllMaterialReleasesApproved() {
		sampleMaterialRelease.approve()
		sampleMaterialRelease2.approve()
	
		assertTrue "should have all material releases approved", sampleJobOrder.hasAllMaterialReleasesApproved()
	}
	
	void testHasAllMaterialReleasesApprovedNotAllApproved() {
		sampleMaterialRelease2.approve()
	
		assertFalse "should not have all material releases approved", sampleJobOrder.hasAllMaterialReleasesApproved()
	}
	
	void testHasAllMaterialReleasesApprovedNoReleases() {
		sampleJobOrder.releases = []
	
		assertFalse "should not have all material releases approved", sampleJobOrder.hasAllMaterialReleasesApproved()
	}
	
	void testHasAllMaterialReleasesApprovedNullReleases() {
		sampleJobOrder.releases = null
	
		assertFalse "should not have all material releases approved", sampleJobOrder.hasAllMaterialReleasesApproved()
	}
	private void setupProducts(){
		sampleProduct = new Product(identifier: "asdf")
		mockDomain(Product, [sampleProduct])
	}

	private void setupJobOrders(){
		sampleJobOrder = new JobOrder(
			releases: [sampleMaterialRelease,sampleMaterialRelease2],
			requisition: sampleMaterialRequisition,
			qty: 10
		)
		mockDomain(JobOrder, [sampleJobOrder])
		sampleMaterialRequisition.jobOrder = sampleJobOrder
	}
	
	private void setupMaterialReleases(){
		sampleMaterialRelease = new MaterialRelease(items:[sampleMaterialReleaseItem])
		sampleMaterialRelease2 = new MaterialRelease(items:[sampleMaterialReleaseItem2])
		mockDomain(MaterialRelease, [sampleMaterialRelease, sampleMaterialRelease2])
	}
	
	private void setupMaterialReleaseItems(){
		sampleMaterialReleaseItem = new MaterialReleaseItem(
			materialRequisitionItem: sampleMaterialRequisitionItem,
			qty: 5,
			cost: new BigDecimal("10")
		)
		sampleMaterialReleaseItem2 = new MaterialReleaseItem(
			materialRequisitionItem: sampleMaterialRequisitionItem,
			qty: 2,
			cost: new BigDecimal("15")
		)
		mockDomain(MaterialReleaseItem, [sampleMaterialReleaseItem, sampleMaterialReleaseItem2])

	}

	private void setupMaterialRequisitions(){
		sampleMaterialRequisition = new MaterialRequisition(
			jobOrder: sampleJobOrder,
			items: [sampleMaterialRequisitionItem]
		)
		mockDomain(MaterialRequisition, [sampleMaterialRequisition])

	}
	
	private void setupMaterialRequisitionItems(){
		sampleMaterialRequisitionItem = new MaterialRequisitionItem(
			component: sampleProduct,
			unitsRequired: 1,
			requisition: sampleMaterialRequisition
		)
		mockDomain(MaterialRequisitionItem, [sampleMaterialRequisitionItem])
	}
}
