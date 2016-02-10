package com.munix

class EditBouncedCheckService {

    static transactional = false

    def addChecksToBouncedCheck(BouncedCheck bouncedCheckInstance, List<Long> checkList) {
        checkList.each{
        	def check = CheckPayment.get(it)
            bouncedCheckInstance.addToChecks(check)
			check.addToBouncedChecks(bouncedCheckInstance)
        }
        return bouncedCheckInstance
    }
    def removeChecksFromBouncedCheck(BouncedCheck bouncedCheckInstance, List<Long> checkList) {
        checkList.each{
			def check = CheckPayment.get(it)
            bouncedCheckInstance.removeFromChecks(check)
			check.removeFromBouncedChecks(bouncedCheckInstance)
        }
        return bouncedCheckInstance
    }
}
