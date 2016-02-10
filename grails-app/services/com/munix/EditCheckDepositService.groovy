package com.munix

class EditCheckDepositService {

    static transactional = false

    def addChecksToCheckDeposit(List<Long> checkIdList, CheckDeposit checkDepositInstance){
        checkIdList.each{
            checkDepositInstance.addToChecks(CheckPayment.get(it))
        }
        return checkDepositInstance
    }
    def removeChecksFromCheckDeposit(List<Long> checkIdList, CheckDeposit checkDepositInstance){
        checkIdList.each{
			def check = CheckPayment.get(it)
            checkDepositInstance.checks.remove(check)
			check.checkDeposits.remove(checkDepositInstance)
        }
        return checkDepositInstance
    }
}
