package com.munix

import grails.test.*

class EditBouncedCheckServiceTests extends GrailsUnitTestCase {
    def editBouncedCheckService
    def sampleCheckPayment
    def sampleCheckPayment2
    def sampleBouncedCheck
    protected void setUp() {
        super.setUp()
        editBouncedCheckService = new EditBouncedCheckService()
        sampleCheckPayment = new CheckPayment()
        sampleCheckPayment2 = new CheckPayment()
        mockDomain(CheckPayment,[sampleCheckPayment, sampleCheckPayment2])
        sampleBouncedCheck = new BouncedCheck(checks:[])
        mockDomain(BouncedCheck, [sampleBouncedCheck])
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testAddChecksToBouncedCheckAddOne() {
        assertTrue "[GUARD] The checks for bounced check is not empty", sampleBouncedCheck.checks.isEmpty()
        
		sampleBouncedCheck = editBouncedCheckService.addChecksToBouncedCheck(sampleBouncedCheck, [sampleCheckPayment.id])
        
		assertEquals "The check list contains more than expected", 1, sampleBouncedCheck.checks.size()
        assertTrue "The checks list does not contain the added item", sampleBouncedCheck.checks.contains(sampleCheckPayment)
		assertEquals "The check payment contains more than expected bounced checks ", 1, sampleCheckPayment.bouncedChecks.size()
		assertTrue "The check payment does not contain the bounced check", sampleCheckPayment.bouncedChecks.contains(sampleBouncedCheck)
    }
	
    void testAddChecksToBouncedCheckAddTwoMore() {
        assertTrue "[GUARD] The checks for bounced check is not empty", sampleBouncedCheck.checks.isEmpty()
        
		sampleBouncedCheck = editBouncedCheckService.addChecksToBouncedCheck(sampleBouncedCheck, [sampleCheckPayment.id,sampleCheckPayment2.id])
        
		assertEquals "The check list contains more than expected", 2, sampleBouncedCheck.checks.size()
        assertTrue "The checks list does not contain the added item", sampleBouncedCheck.checks.contains(sampleCheckPayment)
        assertTrue "The checks list does not contain the added item", sampleBouncedCheck.checks.contains(sampleCheckPayment2)
		assertEquals "The check payment contains more than expected bounced checks ", 1, sampleCheckPayment.bouncedChecks.size()
		assertEquals "The second check payment contains more than expected bounced checks ", 1, sampleCheckPayment2.bouncedChecks.size()
		assertTrue "The check payment does not contain the bounced check", sampleCheckPayment.bouncedChecks.contains(sampleBouncedCheck)
		assertTrue "The second check payment does not contain the bounced check", sampleCheckPayment2.bouncedChecks.contains(sampleBouncedCheck)
    }
	
    void testAddChecksToBouncedCheckAddNone() {
        assertTrue "[GUARD] The checks for bounced check is not empty", sampleBouncedCheck.checks.isEmpty()
        
		sampleBouncedCheck = editBouncedCheckService.addChecksToBouncedCheck(sampleBouncedCheck, [])
        
		assertTrue "The checks list is not empty", sampleBouncedCheck.checks.isEmpty()
    }
	
    void testRemoveChecksFromBouncedCheckRemoveDoesNotExist() {
        
		assertTrue "[GUARD] The checks for bounced check is not empty", sampleBouncedCheck.checks.isEmpty()
        
		sampleBouncedCheck = editBouncedCheckService.removeChecksFromBouncedCheck(sampleBouncedCheck, [sampleCheckPayment.id])
        
		assertTrue "The checks list is not empty", sampleBouncedCheck.checks.isEmpty()
    }
	
    void testRemoveChecksFromBouncedCheckRemoveOneCheck() {
        assertTrue "[GUARD] The checks for bounced check is not empty", sampleBouncedCheck.checks.isEmpty()
        sampleBouncedCheck.addToChecks(sampleCheckPayment)
		sampleCheckPayment.addToBouncedChecks(sampleBouncedCheck)
		
        sampleBouncedCheck = editBouncedCheckService.removeChecksFromBouncedCheck(sampleBouncedCheck, [sampleCheckPayment.id])

        assertTrue "The checks list is not empty", sampleBouncedCheck.checks.isEmpty()
		assertTrue "The check payment's bounced check list is not empty", sampleCheckPayment.bouncedChecks.isEmpty()
    }

    void testRemoveChecksFromBouncedCheckRemoveNone() {
        assertTrue "[GUARD] The checks for bounced check is not empty", sampleBouncedCheck.checks.isEmpty()
        sampleBouncedCheck.addToChecks(sampleCheckPayment)
		sampleCheckPayment.addToBouncedChecks(sampleBouncedCheck)
		
        sampleBouncedCheck = editBouncedCheckService.removeChecksFromBouncedCheck(sampleBouncedCheck, [])
        
		assertTrue "The checks list does not contain the expected result", sampleBouncedCheck.checks.contains(sampleCheckPayment)
        assertEquals "The checks list contains more than the expected result", 1, sampleBouncedCheck.checks.size()
		assertTrue "The check payment's bounced check list does not contain the expected result", sampleCheckPayment.bouncedChecks.contains(sampleBouncedCheck)
    }
}
