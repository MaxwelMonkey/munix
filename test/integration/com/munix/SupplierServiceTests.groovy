package com.munix

import grails.test.*
import org.springframework.security.context.SecurityContextHolder as SCH
import org.springframework.security.GrantedAuthority
import org.springframework.security.Authentication
import org.springframework.security.GrantedAuthorityImpl
import org.springframework.security.providers.UsernamePasswordAuthenticationToken
import org.codehaus.groovy.grails.plugins.springsecurity.GrailsUserImpl

class SupplierServiceTests extends GroovyTestCase {
	def supplierService

    def samplePurchasingUser
    def sampleNoRoleUser
    def sampleRole
	def sampleDiscountType
	def sampleProduct
	
    protected void setUp() {
        super.setUp()
        sampleRole = new Role(authority: "ROLE_PURCHASING",
            description: "haha")
        sampleRole.save()
        samplePurchasingUser = new User(
            username:"purchasing",
            userRealName: "purchase",
            passwd: "1",
            enabled: true,
			email: "a@y.com"
        )
        samplePurchasingUser.addToAuthorities(sampleRole)
        samplePurchasingUser.save()
        sampleNoRoleUser = new User(
            username:"NoRole",
            userRealName: "none",
            passwd: "1",
            enabled: true,
			email: "a@y.com"
        )
        sampleNoRoleUser.save()
		
		setupDiscountTypes()
		setupProducts()
    }

    protected void tearDown() {
        super.tearDown()
    }

	void testQueryAvailableProducts() {
		def result = supplierService.queryAvailableProducts("1234")
		
		assertTrue "Result should contain sample product", result.contains(sampleProduct)
		assertEquals "Result should only return 1 product", 1, result.size()
	}
	
    protected Authentication authenticate(User user, String credentials) {

        def authorities = user.authorities.collect { new GrantedAuthorityImpl(it.authority) }

        def userDetails = new GrailsUserImpl(user.username, user.passwd, user.enabled, user.enabled, user.enabled, user.enabled, authorities as GrantedAuthority[], user)

        SCH.context.authentication = new UsernamePasswordAuthenticationToken(userDetails, user.passwd, userDetails.authorities)

    }
    void testThatRolePurchasingIsLoggedIn() {
        authenticate(samplePurchasingUser, "1")
        def result = supplierService.rolePurchasingIsLoggedIn()
        assertTrue "The method must return true",result
    }
    void testThatNoRoleIsLoggedIn() {
        authenticate(sampleNoRoleUser, "1")
        def result = supplierService.rolePurchasingIsLoggedIn()
        assertFalse "The method must return false",result
    }

	private void setupDiscountTypes() {
		sampleDiscountType = new DiscountType(
			identifier: "discount type",
			description: "desc"
		)
		sampleDiscountType.save()
	}
	
	private void setupProducts(){
		sampleProduct = new Product(
				identifier:"1234",
				type: sampleDiscountType
		)
		sampleProduct.save()
	}
}
