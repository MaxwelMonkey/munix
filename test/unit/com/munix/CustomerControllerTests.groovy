package com.munix

import grails.test.ControllerUnitTestCase
/**
 * Created by IntelliJ IDEA.
 * User: charles
 * Date: 9/15/11
 * Time: 1:17 PM
 * To change this template use File | Settings | File Templates.
 */

class CustomerControllerTests extends ControllerUnitTestCase {

    void testList() {
        def mockCustomerService = mockFor(CustomerService)
		mockCustomerService.demand.getListOfCustomersWithCriteria(1..1){x->
			return [customerList:"1",totalCount:"2"]
		}
		controller.customerService= mockCustomerService.createMock()
        def result = controller.list()
        assertEquals "The customerList must be 1",'1',result.customerInstanceList.customerList
        assertEquals "The totalCount must be 2",'2',result.customerInstanceTotal
    }

}
