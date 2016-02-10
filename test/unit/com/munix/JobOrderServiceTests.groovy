package com.munix

import grails.test.*

class JobOrderServiceTests extends GrailsUnitTestCase {
    def sampleJobOrder
    def sampleJobOut
    def jobOrderService = new JobOrderService()
    protected void setUp() {
        super.setUp()
        sampleJobOut = new JobOut(status:JobOut.Status.APPROVED)
        mockDomain(JobOut,[sampleJobOut])
        sampleJobOrder = new JobOrder(jobOuts: [sampleJobOut])
        mockDomain(JobOrder,[sampleJobOrder])

    }

    protected void tearDown() {
        super.tearDown()
    }

    void testIsCompletable() {
        sampleJobOrder.metaClass.static.computeRemainingBalance = { -> return 0}
        def result = jobOrderService.isCompletable(sampleJobOrder)
        assertTrue "The result must be true",result
        def remove =  GroovySystem.metaClassRegistry.&removeMetaClass
        remove JobOrder
    }
    void testIsCompletableRemaningBalanceNotEqualTo0() {
        sampleJobOrder.metaClass.static.computeRemainingBalance = { -> return 1}
        def result = jobOrderService.isCompletable(sampleJobOrder)
        assertFalse "The result must be false",result
        def remove =  GroovySystem.metaClassRegistry.&removeMetaClass
        remove JobOrder
    }
    void testIsCompletableJobOutIsUnapprove() {
        sampleJobOut.unapprove()
        sampleJobOrder.metaClass.static.computeRemainingBalance = { -> return 0}
        def result = jobOrderService.isCompletable(sampleJobOrder)
        assertFalse "The result must be false",result
        def remove =  GroovySystem.metaClassRegistry.&removeMetaClass
        remove JobOrder
    }
    void testIsCompletableJobOutIsCancelled() {
        sampleJobOrder.metaClass.static.computeRemainingBalance = { -> return 0}
        def result = jobOrderService.isCompletable(sampleJobOrder)
        assertTrue "The result must be true",result
        def remove =  GroovySystem.metaClassRegistry.&removeMetaClass
        remove JobOrder
    }
}
