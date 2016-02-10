package com.munix

import org.codehaus.groovy.grails.plugins.springsecurity.AuthorizeTools as AT

class UserAuthenticateService {

    boolean transactional = true
	
	def authenticateService
	
	def validatePurchaseManager(username, pass){
		return validateUser("ROLE_PURCHASING_MANAGER", username, pass)
	}
	
    def ifAnyGranted(user, role){
        return !AT.retainAll(user.authorities,AT.parseAuthoritiesString(role)).isEmpty()
    }

    def validateUser(role, username, pass) {
		def user = User.findByUsernameAndPasswd(username,authenticateService.encodePassword(pass))
		if(!user){
			return false
		}else if (ifAnyGranted(user, role)){
			return true
		}else {
			return false
		}
    }
}
