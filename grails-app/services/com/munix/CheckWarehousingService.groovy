package com.munix

class CheckWarehousingService {

    static transactional = true

    def getCheckPaymentsWithCriteria(params,originWarehouse) {
        def searchDateBefore = params.startDate
        def searchDateAfter = params.endDate
        def checkPayments = CheckPayment.withCriteria{
            and{
                if(searchDateBefore&&searchDateAfter){
                    ge('date',searchDateBefore)
                    le('date',searchDateAfter)
                }else if(searchDateBefore){
                    ge('date',searchDateBefore)
                }else if(searchDateAfter){
                    le('date',searchDateAfter)
                }
                eq('status',CheckPayment.Status.PENDING)
                eq('warehouse',originWarehouse)
            }
        }
        return checkPayments
    }

    def getDirectPaymentChecksWithCriteria(params,originWarehouse) {
        def searchDateBefore = params.startDate
        def searchDateAfter = params.endDate
        def checkPayments = CheckPayment.withCriteria{
            and{
                if(searchDateBefore&&searchDateAfter){
                    ge('date',searchDateBefore)
                    le('date',searchDateAfter)
                }else if(searchDateBefore){
                    ge('date',searchDateBefore)
                }else if(searchDateAfter){
                    le('date',searchDateAfter)
                }
                eq('status',CheckPayment.Status.PENDING)
                eq('warehouse',originWarehouse)
                directPaymentItem{
                	directPayment{
                		eq('status','Approved')
                	}
                }
                
            }
        }
        return checkPayments
    }
}
