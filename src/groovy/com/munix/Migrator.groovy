package com.munix

class Migrator{

    def migrate(){
        migrateJobOutList()
        migrateRequisitionList()
        migrateInvoices()
        migrateDeliveryItems()
        migrateCustomerDiscountLogs()
    }

    def migrateJobOutList(){
        JobOrder.list().each{job ->
            def jobOuts = []
            JobOut.findAllByJobOrder(job)?.each{
                jobOuts.add(it)
                it.jobOrder = job
            }
            job.jobOuts = jobOuts
            job.save(flush: true,cascade : true)
        }
    }

    def migrateRequisitionList(){
        MaterialRequisition.list().each{requisition ->
            def items = []
            MaterialRequisitionItem.findAllByRequisition(requisition)?.each{
                items.add(it)
                it.requisition = requisition
            }
            requisition.items = items
            requisition.save(flush: true,cascade : true)
        }
    }

    def migrateInvoices(){
        PurchaseInvoice.list().each{invoice ->
            def items = []
            PurchaseInvoiceItem.findAllByInvoice(invoice)?.each{
                items.add(it)
                it.invoice = invoice
            }
            invoice.items = items
            invoice.save(flush: true,cascade : true)
        }
    }

    def migrateDeliveryItems(){
        PurchaseOrderItem.list().each{item ->
            def items = []
            PurchaseInvoiceItem.findAllByPoItem(item)?.each{
                items.add(it)
                it.poItem = item
            }
            item.receivedItems = items
            item.save(flush: true,cascade : true)
        }
    }

    def migrateCustomerDiscountLogs(){
        Customer.list().each{customer ->
            CustomerDiscount.findAllByCustomer(customer)?.each{
                def discountLog  = new CustomerDiscountLog(customer : customer, discount : it)
                def item = new CustomerDiscountLogItem(date: new Date(), discountGroup: it.discountGroup, discountType : it.discountType)
                discountLog.addToItems(item)
                discountLog.updateCurrent(item)
                discountLog.save(flush: true,cascade : true)
            }
        }
    }


}
