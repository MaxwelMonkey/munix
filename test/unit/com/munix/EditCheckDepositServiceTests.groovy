package com.munix

import grails.test.*

class EditCheckDepositServiceTests extends GrailsUnitTestCase {
    def editCheckDepositService = new EditCheckDepositService()
    def sampleCheckPayment
    def sampleCheckPayment2
    def sampleCheckDeposit
    protected void setUp() {
        super.setUp()
        sampleCheckPayment = new CheckPayment()
        sampleCheckPayment2 = new CheckPayment()
        mockDomain(CheckPayment,[sampleCheckPayment, sampleCheckPayment2])
        sampleCheckDeposit = new CheckDeposit(checks:[])
        mockDomain(CheckDeposit, [sampleCheckDeposit])
		sampleCheckPayment.addToCheckDeposits(sampleCheckDeposit)
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testAddChecksToCheckDepositAddOne() {
        assertTrue "The checks for check deposit is not empty", sampleCheckDeposit.checks.isEmpty()
        sampleCheckDeposit = editCheckDepositService.addChecksToCheckDeposit([sampleCheckPayment.id],sampleCheckDeposit )
        assertEquals "The check list contains more than expected", 1, sampleCheckDeposit.checks.size()
        assertTrue "The checks list does not contain the added item", sampleCheckDeposit.checks.contains(sampleCheckPayment)
    }
    void testAddChecksToCheckDepositAddTwoMore() {
        assertTrue "The checks for check deposit is not empty", sampleCheckDeposit.checks.isEmpty()
        sampleCheckDeposit = editCheckDepositService.addChecksToCheckDeposit([sampleCheckPayment.id,sampleCheckPayment2.id],sampleCheckDeposit)
        assertEquals "The check list contains more than expected", 2, sampleCheckDeposit.checks.size()
        assertTrue "The checks list does not contain the added item", sampleCheckDeposit.checks.contains(sampleCheckPayment)
        assertTrue "The checks list does not contain the added item", sampleCheckDeposit.checks.contains(sampleCheckPayment2)
    }
    void testAddChecksToCheckDepositAddNone() {
        assertTrue "The checks for check deposit is not empty", sampleCheckDeposit.checks.isEmpty()
        sampleCheckDeposit = editCheckDepositService.addChecksToCheckDeposit([], sampleCheckDeposit )
        assertTrue "The checks list is not empty", sampleCheckDeposit.checks.isEmpty()
    }
    void testRemoveChecksFromCheckDepositRemoveDoesNotExist() {
        assertTrue "The checks for check deposit is not empty", sampleCheckDeposit.checks.isEmpty()
        sampleCheckDeposit = editCheckDepositService.removeChecksFromCheckDeposit([sampleCheckPayment.id],sampleCheckDeposit )
        assertTrue "The checks list is not empty", sampleCheckDeposit.checks.isEmpty()
    }
    void testRemoveChecksFromCheckDepositRemoveOneCheck() {
        assertTrue "The checks for check deposit is not empty", sampleCheckDeposit.checks.isEmpty()
        sampleCheckDeposit.addToChecks(sampleCheckPayment)
        sampleCheckDeposit = editCheckDepositService.removeChecksFromCheckDeposit([sampleCheckPayment.id],sampleCheckDeposit )
        assertTrue "The checks list is not empty", sampleCheckDeposit.checks.isEmpty()
    }
    void testRemoveChecksFromCheckDepositRemoveNone() {
        assertTrue "The checks for check deposit is not empty", sampleCheckDeposit.checks.isEmpty()
        sampleCheckDeposit.addToChecks(sampleCheckPayment)
        sampleCheckDeposit = editCheckDepositService.removeChecksFromCheckDeposit([],sampleCheckDeposit )
        assertTrue "The checks list does not contain the expected result", sampleCheckDeposit.checks.contains(sampleCheckPayment)
        assertEquals "The checks list contains more than the expected result", 1, sampleCheckDeposit.checks.size()
    }
}
