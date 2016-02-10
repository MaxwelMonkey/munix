package com.munix

import grails.test.*

class UserAuthenticateServiceTests extends GrailsUnitTestCase {
    def authenticateService
    def userAuthenticateService
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    private mockData(){
        def role = new Role(authority : "ROLE_PURCHASING_MANAGER", description : "purchasing manager")
        def user = new User(username : "test", 
                passwd : authenticateService.encodePassword("test"), 
                userRealName : "test", authorities : [role],
                email: "test@test.com")
        role.people = [user]
        user.save(flush : true)
        assertTrue "Errors in saving!", !user.hasErrors()

    }

    void testValidateUserSuccess() {
        mockData()
        assertTrue "user should be granted", userAuthenticateService.validateUser("ROLE_PURCHASING_MANAGER", "test", "test")
    }

    void testValidateUserFail() {
        mockData()
        assertFalse "user should not be granted", userAuthenticateService.validateUser("ROLE_PURCHASING_MANAGER", "fail", "fail")
    }
}
