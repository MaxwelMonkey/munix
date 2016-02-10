package com.munix

import grails.test.*
import java.text.SimpleDateFormat

class GeneralMethodServiceTests extends GrailsUnitTestCase {
    def generalMethodService = new GeneralMethodService()
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testRemoveSameItems(){
		def list1=[1,2]
		def list2=[2,3]
		def list3 = generalMethodService.removeSameItems(list1,list2)
		def result=[1]
		assertEquals result,list3
	}
	
	void testGetDateFromApprovedBy() {
		def approvedBy = "Rechilda Naval, Apr. 02, 2011 - 04:14 PM"
		def df = new SimpleDateFormat("MM/dd/yyyy HH:mm")
		def expected = df.parse("04/02/2011 16:14")
		
		assertEquals "Parsing of Approved By has problems", expected, generalMethodService.getDateFromApprovedBy(approvedBy)
	}
}
