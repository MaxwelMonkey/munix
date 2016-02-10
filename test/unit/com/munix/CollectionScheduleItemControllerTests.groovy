package com.munix

import grails.test.*

class CollectionScheduleItemControllerTests extends ControllerUnitTestCase {
	def sampleCollectionScheduleItemCollection
	def sampleCollectionScheduleItemCounter
	def sampleCollectionScheduleItemBoth
	def sampleCounterReceipt
	
    protected void setUp() {
        super.setUp()
		controller.metaClass.message = {Map map-> map}
		
		sampleCounterReceipt = new CounterReceipt()
		mockDomain(CounterReceipt, [sampleCounterReceipt])			
		
		sampleCollectionScheduleItemCollection = new CollectionScheduleItem(
			counterReceipt: sampleCounterReceipt,
			type: "Collection"
		)
		sampleCollectionScheduleItemCounter = new CollectionScheduleItem(
			counterReceipt: sampleCounterReceipt,
			type: "Counter"
		)
		sampleCollectionScheduleItemBoth = new CollectionScheduleItem(
			counterReceipt: sampleCounterReceipt,
			type: "Both"
		)
		mockDomain(CollectionScheduleItem, [sampleCollectionScheduleItemCollection, sampleCollectionScheduleItemCounter, sampleCollectionScheduleItemBoth])
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testDeleteCollection() {
		sampleCounterReceipt.collectionDate = new Date() 
		mockParams.id = sampleCollectionScheduleItemCollection.id
		def oldSize = CollectionScheduleItem.count()
		assertNotNull "[GUARD] Sample counter receipt collection date should not be null", sampleCounterReceipt.collectionDate
		
        controller.delete()
		
        assertNull "sample counter receipt collection date should be null", sampleCounterReceipt.collectionDate
		assertEquals "sample collection schedule should be deleted", oldSize - 1, CollectionScheduleItem.count()
		assertEquals "should redirect to show", "show", controller.redirectArgs.action
		assertEquals "should redirect to collection schedule controller", "collectionSchedule", controller.redirectArgs.controller
    }
	
	void testDeleteCounter() {
		sampleCounterReceipt.counterDate = new Date()
		mockParams.id = sampleCollectionScheduleItemCounter.id
		def oldSize = CollectionScheduleItem.count()
		assertNotNull "[GUARD] Sample counter receipt counter date should not be null", sampleCounterReceipt.counterDate
		
		controller.delete()
		
		assertNull "sample counter receipt counter date should be null", sampleCounterReceipt.counterDate
		assertEquals "sample collection schedule should be deleted", oldSize - 1, CollectionScheduleItem.count()
		assertEquals "should redirect to show", "show", controller.redirectArgs.action
		assertEquals "should redirect to collection schedule controller", "collectionSchedule", controller.redirectArgs.controller
	}
	
	void testDeleteBoth() {
		sampleCounterReceipt.collectionDate = new Date()
		sampleCounterReceipt.counterDate = new Date()
		mockParams.id = sampleCollectionScheduleItemBoth.id
		def oldSize = CollectionScheduleItem.count()
		assertNotNull "[GUARD] Sample counter receipt collection date should not be null", sampleCounterReceipt.collectionDate
		assertNotNull "[GUARD] Sample counter receipt counter date should not be null", sampleCounterReceipt.counterDate
		
		controller.delete()
		
		assertNull "sample counter receipt collection date should be null", sampleCounterReceipt.collectionDate
		assertNull "sample counter receipt counter date should be null", sampleCounterReceipt.counterDate
		assertEquals "sample collection schedule should be deleted", oldSize - 1, CollectionScheduleItem.count()
		assertEquals "should redirect to show", "show", controller.redirectArgs.action
		assertEquals "should redirect to collection schedule controller", "collectionSchedule", controller.redirectArgs.controller
	}
}
