package com.munix




/**
 * User domain class.
 */
class User {
	static transients = ['pass']
	static hasMany = [authorities: Role]
	static belongsTo = Role

	/** Username */
	String username
	/** User Real Name*/
	String userRealName
	/** MD5 Password */
	String passwd
	/** enabled */
	boolean enabled

	String email
	boolean emailShow

	/** description */
	String description = ''

	/** plain password to create a MD5 password */
	String pass = '[secret]'
		
	Date startAccess
	Date endAccess
	Boolean emailSalesReport = false
	Boolean sdPrintoutReviewer = false

	static constraints = {
		username(blank: false, unique: true)
		userRealName(blank: false)
		passwd(blank: false)
		enabled()
		startAccess(nullable:true)
		endAccess(nullable:true)
		emailSalesReport(nullable:true)
		sdPrintoutReviewer(nullable:true)
	}
	
	def hasAccess(date){
		if(startAccess==null || endAccess == null) return true;
		def d = date.format("HH:mm")
		def s = startAccess.format("HH:mm")
		def e = endAccess.format("HH:mm")
        if(s == "00:00" && s == "00:00") return true;
        if(s <= d && d <= e) return true;
        return false;
	}
	
    String toString(){
        this.username
    }
    
    def initials(){
    	def res = ""
    	this.userRealName.split(" ").each{
    		res+=it?.substring(0,1)
    	}
    	res
    }
	
}
