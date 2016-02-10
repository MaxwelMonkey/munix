
class UserAccessFilters {
	
	def authenticateService

	def filters = {
        userAccessFilter(controller:'log*', invert:true) {
            before = {
            	
           		session.userAccess = "T"
           		def user = authenticateService.userDomain()
           		user = com.munix.User.get(user.id)
            	if(!user?.hasAccess(new Date())){
            		println "NO ACCESS for : "+user
            		println "DATE: "+new Date()
            		session.userAccess = "F"
            		redirect(controller:"login", action:"accesshours")
            		return false
            	}
           		return true
            }
        }
    }
}
