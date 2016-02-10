package com.munix

import grails.test.*

class CollectionScheduleServiceTests extends GroovyTestCase {

    def collectionScheduleService
	def generalMethodService
    def sampleCollectionSchedule
    def sampleCollector

    protected void setUp() {
        super.setUp()
        setUpCollector()
        setUpCollectionSchedule()
    }

    protected void tearDown() {
        super.tearDown()
    }

    private void setUpCollector() {
        sampleCollector= new Collector(
            identifier: "collector",
            description:"desc")
        sampleCollector.save()
    }
    private void setUpCollectionSchedule() {
        sampleCollectionSchedule = new CollectionSchedule(
            identifier:"1234",
            description:"5678",
            startDate:new Date(),
            endDate:new Date(),
            preparedBy:"me",
            collector:sampleCollector
        )
        sampleCollectionSchedule.save(flush: true)
    }
    void testGetCollectionScheduleListWithCriteriaHasSearchForId() {
        def objectParam = [:]
        objectParam["searchId"]=sampleCollectionSchedule.id.toString()

        def result = collectionScheduleService.getCollectionScheduleListWithCriteria(objectParam)
        assertEquals "The result must only have one object in it", 1, result.size()
        assertTrue "the return value of the method must contain sampleCollectionSchedule", result.contains(sampleCollectionSchedule)
    }
    void testGetCollectionScheduleListWithCriteriaHasSearchForIdNoMatchingResult() {
        def objectParam = [:]
        objectParam["searchId"]=(sampleCollectionSchedule.id+1).toString()

        def result = collectionScheduleService.getCollectionScheduleListWithCriteria(objectParam)
        assertTrue "The result must be empty", result.isEmpty()
    }
    void testGetCollectionScheduleListWithCriteriaHasSearchForIdentifier() {
        def objectParam = [:]
        objectParam["searchIdentifier"]="1234"

        def result = collectionScheduleService.getCollectionScheduleListWithCriteria(objectParam)
        assertEquals "The result must only have one object in it", 1, result.size()
        assertTrue "the return value of the method must contain sampleCollectionSchedule", result.contains(sampleCollectionSchedule)
    }
    void testGetCollectionScheduleListWithCriteriaHasSearchForIdentifierIncompleteInput() {
        def objectParam = [:]
        objectParam["searchIdentifier"]="12"

        def result = collectionScheduleService.getCollectionScheduleListWithCriteria(objectParam)
        assertEquals "The result must only have one object in it", 1, result.size()
        assertTrue "the return value of the method must contain sampleCollectionSchedule", result.contains(sampleCollectionSchedule)
    }
    void testGetCollectionScheduleListWithCriteriaHasSearchForIdentifierNoMatchingResult() {
        def objectParam = [:]
        objectParam["searchIdentifier"]="678"

        def result = collectionScheduleService.getCollectionScheduleListWithCriteria(objectParam)
        assertTrue "The result must be empty", result.isEmpty()
    }
    void testGetCollectionScheduleListWithCriteriaHasSearchForDescription() {
        def objectParam = [:]
        objectParam["searchDescription"]="5678"

        def result = collectionScheduleService.getCollectionScheduleListWithCriteria(objectParam)
        assertEquals "The result must only have one object in it", 1, result.size()
        assertTrue "the return value of the method must contain sampleCollectionSchedule", result.contains(sampleCollectionSchedule)
    }
    void testGetCollectionScheduleListWithCriteriaHasSearchForDescriptionIncompleteInput() {
        def objectParam = [:]
        objectParam["searchDescription"]="56"

        def result = collectionScheduleService.getCollectionScheduleListWithCriteria(objectParam)
        assertEquals "The result must only have one object in it", 1, result.size()
        assertTrue "the return value of the method must contain sampleCollectionSchedule", result.contains(sampleCollectionSchedule)
    }
    void testGetCollectionScheduleListWithCriteriaHasSearchForDescriptionNoMatchingResult() {
        def objectParam = [:]
        objectParam["searchDescription"]="123"

        def result = collectionScheduleService.getCollectionScheduleListWithCriteria(objectParam)
        assertTrue "The result must be empty", result.isEmpty()
    }
    void testGetCollectionScheduleListWithCriteriaHasSearchForStatus() {
        def objectParam = [:]
        objectParam["searchStatus"]="Processing"

        def result = collectionScheduleService.getCollectionScheduleListWithCriteria(objectParam)
        assertEquals "The result must only have one object in it", 1, result.size()
        assertTrue "the return value of the method must contain sampleCollectionSchedule", result.contains(sampleCollectionSchedule)
    }
    void testGetCollectionScheduleListWithCriteriaHasSearchForStatusNoMatchingResult() {
        def objectParam = [:]
        objectParam["searchStatus"]="Inactive"

        def result = collectionScheduleService.getCollectionScheduleListWithCriteria(objectParam)
        assertTrue "The result must be empty", result.isEmpty()
    }
    void testGetCollectionScheduleListWithCriteriaHasSearchForSalesAgent() {
        def objectParam = [:]
        objectParam["searchCollector"]="collector"

        def result = collectionScheduleService.getCollectionScheduleListWithCriteria(objectParam)
        assertEquals "The result must only have one object in it", 1, result.size()
        assertTrue "the return value of the method must contain sampleCollectionSchedule", result.contains(sampleCollectionSchedule)
    }
    void testGetCollectionScheduleListWithCriteriaHasSearchForSalesAgentIncompleteInput() {
        def objectParam = [:]
        objectParam["searchCollector"]="collec"

        def result = collectionScheduleService.getCollectionScheduleListWithCriteria(objectParam)
        assertEquals "The result must only have one object in it", 1, result.size()
        assertTrue "the return value of the method must contain sampleCollectionSchedule", result.contains(sampleCollectionSchedule)
    }
    void testGetCollectionScheduleListWithCriteriaHasSearchForSalesAgentNoMatchingResult() {
        def objectParam = [:]
        objectParam["searchCollector"]="adg"

        def result = collectionScheduleService.getCollectionScheduleListWithCriteria(objectParam)
        assertTrue "The result must be empty", result.isEmpty()
    }
    void testGetCollectionScheduleListWithCriteriaNoSearchValues() {
        def objectParam = [:]
        def result = collectionScheduleService.getCollectionScheduleListWithCriteria(objectParam)
        assertEquals "The result must only have one object in it", 1, result.size()
        assertTrue "the return value of the method must contain sampleCollectionSchedule", result.contains(sampleCollectionSchedule)
    }

	void testCheckIfUnmarkableAsCompleteSameDay() {
		sampleCollectionSchedule.status = CollectionSchedule.Status.COMPLETE
		sampleCollectionSchedule.endDate = generalMethodService.dateToday()
		sampleCollectionSchedule.save(flush: true)
		
		assertTrue "should be unmarkable as complete", collectionScheduleService.checkIfUnmarkableAsComplete(sampleCollectionSchedule)
	}
	
	void testCheckIfUnmarkableAsComplete() {
		sampleCollectionSchedule.status = CollectionSchedule.Status.COMPLETE
		sampleCollectionSchedule.endDate = generalMethodService.performDayOperationsOnDate(Operation.ADD, generalMethodService.dateToday(), 1)
		sampleCollectionSchedule.save(flush: true)
		
		assertTrue "should be unmarkable as complete", collectionScheduleService.checkIfUnmarkableAsComplete(sampleCollectionSchedule)
	}
	
	void testCheckIfUnmarkableAsCompleteNotMarkable() {
		sampleCollectionSchedule.status = CollectionSchedule.Status.COMPLETE
		sampleCollectionSchedule.endDate = generalMethodService.createDate("10/10/2001")
		sampleCollectionSchedule.save(flush: true)
		
		assertFalse "should not be unmarkable as complete", collectionScheduleService.checkIfUnmarkableAsComplete(sampleCollectionSchedule)
	}
}
