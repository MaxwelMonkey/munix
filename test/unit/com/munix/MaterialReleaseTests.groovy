package com.munix

import grails.test.*

class MaterialReleaseTests extends GrailsUnitTestCase {
	def sampleMaterialRelease
	def sampleMaterialReleaseItem1
	def sampleMaterialReleaseItem2
	
    protected void setUp() {
        super.setUp()
		
		sampleMaterialRelease = new MaterialRelease()
		mockDomain(MaterialRelease, [sampleMaterialRelease])
		
		sampleMaterialReleaseItem1 = new MaterialReleaseItem(
			qty: 2,
			cost: new BigDecimal("15")
		)
		sampleMaterialReleaseItem2 = new MaterialReleaseItem(
			qty: 5,
			cost: new BigDecimal("10")
		)
		mockDomain(MaterialReleaseItem, [sampleMaterialReleaseItem1, sampleMaterialReleaseItem2])
		sampleMaterialRelease.items = [sampleMaterialReleaseItem1, sampleMaterialReleaseItem2]
    }

    protected void tearDown() {
        super.tearDown()
    }

	void testCost() {
		assertEquals "should have correct cost", new BigDecimal("80"), sampleMaterialRelease.cost
	}
	
	void testApprove() {
		sampleMaterialRelease.approve()
		
		assertEquals "sample purchase order's status should be correct", MaterialRelease.Status.APPROVED, sampleMaterialRelease.status
	}
	
	void testSecondApprove() {
		sampleMaterialRelease.secondApprove()
		
		assertEquals "sample purchase order's status should be correct", MaterialRelease.Status.SECOND_APPROVED, sampleMaterialRelease.status
	}
	
	void testUnapprove() {
		sampleMaterialRelease.unapprove()
		
		assertEquals "sample purchase order's status should be correct", MaterialRelease.Status.UNAPPROVED, sampleMaterialRelease.status
	}
	
	void testUnapproveSecondApproval() {
		sampleMaterialRelease.unapproveSecondApproval()
		
		assertEquals "sample purchase order's status should be correct", MaterialRelease.Status.APPROVED, sampleMaterialRelease.status
	}
	
	void testCancel() {
		sampleMaterialRelease.cancel()
		
		assertEquals "sample purchase order's status should be correct", MaterialRelease.Status.CANCELLED, sampleMaterialRelease.status
	}

	void testIsApproved() {
		sampleMaterialRelease.status = MaterialRelease.Status.APPROVED
		
		assertTrue "should return true", sampleMaterialRelease.isApproved()
	}
	
	void testIsSecondApproved() {
		sampleMaterialRelease.status = MaterialRelease.Status.SECOND_APPROVED
		
		assertTrue "should return true", sampleMaterialRelease.isSecondApproved()
	}
	
	void testIsUnapproved() {
		assertTrue "should return true", sampleMaterialRelease.isUnapproved()
	}
	
	void testIsCancelled() {
		sampleMaterialRelease.status = MaterialRelease.Status.CANCELLED
		
		assertTrue "should return true", sampleMaterialRelease.isCancelled()
	}
}
