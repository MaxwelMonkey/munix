import com.munix.DirectPayment
import com.munix.CustomerCharge
import com.munix.CounterReceipt
import com.munix.CollectionSchedule
import com.munix.CheckDeposit
import com.munix.CheckWarehouse
import com.munix.SupplierPayment
import com.munix.PriceAdjustment
import com.munix.TripTicket
import com.munix.Waybill
import com.munix.DirectDelivery
import com.munix.InventoryAdjustment
import com.munix.InventoryTransfer
import com.munix.SalesOrder
import com.munix.SalesDelivery
import com.munix.BouncedCheck
import com.munix.PurchaseOrder
import com.munix.PurchaseInvoice
import com.munix.MaterialRelease
import com.munix.JobOrder
import com.munix.JobOut
import com.munix.CheckWarehousing
import com.munix.CustomerChargeItem
import com.munix.CollectionScheduleItem
import com.munix.CreditMemo
import com.munix.CreditMemoItem
import com.munix.SupplierPaymentItem

class EditTransactionFilters {
    def domainMap = ["directPayment":DirectPayment,
            "customerCharge":CustomerCharge,
            "counterReceipt":CounterReceipt,
            "collectionSchedule":CollectionSchedule,
            "checkDeposit":CheckDeposit,
            "checkWarehouse":CheckWarehouse,
            "supplierPayment":SupplierPayment,
            "supplierPaymentItem":SupplierPaymentItem,
            "priceAdjustment":PriceAdjustment,
            "tripTicket":TripTicket,
            "waybill":Waybill,
            "directDelivery":DirectDelivery,
            "inventoryAdjustment":InventoryAdjustment,
            "inventoryTransfer":InventoryTransfer,
			"salesOrder":SalesOrder,
			"salesDelivery":SalesDelivery,
			"bouncedCheck":BouncedCheck,
			"purchaseOrder":PurchaseOrder,
			"purchaseInvoice":PurchaseInvoice,
			"materialRelease":MaterialRelease,
			"jobOrder":JobOrder,
            "checkWarehousing":CheckWarehousing,
			"jobOut":JobOut,
			"creditMemo":CreditMemo,
			"creditMemoItem":CreditMemoItem]
    def checkIfEditable(id,String module){
        def domain = domainMap.get(module)
        if(id){
            def object = domain.findById(id)
            if(!object.isUnapproved()){
                return false
            }
        }
        return true

    }
    def filters = {
        directPaymentEditFilter(controller:'directPayment', action:'edit') {
            before = {
                def isEditable = checkIfEditable(params.id, controllerName)
                if(!isEditable){
                    flash.error = "Direct Payment can not be edited in this status"
                    redirect(action:"show", controller: controllerName, id:params.id)
                }
                return isEditable
            }
        }
        directPaymentChangeValuesFilter(controller:'directPayment', action:'changeValues') {
            before = {
                def isEditable = checkIfEditable(params.id, controllerName)
                if(!isEditable){
                    flash.error = "Direct Payment can not be edited in this status"
                    redirect(action:"show", controller: controllerName, id:params.id)
                }
                return isEditable
            }
        }
        directPaymentUpdateFilter(controller:'directPayment', action:'update') {
            before = {
                def isEditable = checkIfEditable(params.id, controllerName)
                if(!isEditable){
                    flash.error = "Direct Payment can not be edited in this status"
                    redirect(action:"show", controller: controllerName, id:params.id)
                }
                return isEditable
            }
        }
        directPaymentUpdateTransactionFilter(controller:'directPayment', action:'updateDirectPayment') {
            before = {
                def isEditable = checkIfEditable(params.id, controllerName)
                if(!isEditable){
                    flash.error = "Direct Payment can not be edited in this status"
                    redirect(action:"show", controller: controllerName, id:params.id)
                }
                return isEditable
            }
        }
        customerChargeEditFilter(controller:"customerCharge", action:"edit"){
            before = {
                def isEditable = checkIfEditable(params.id, controllerName)
                if(!isEditable){
                    flash.error = "Customer Charge can not be edited in this status"
                    redirect(action:"show", controller: controllerName, id:params.id)
                }
                return isEditable
            }
        }
        customerChargeUpdateFilter(controller:"customerCharge", action:"update"){
            before = {
                def isEditable = checkIfEditable(params.id, controllerName)
                if(!isEditable){
                    flash.error = "Customer Charge can not be edited in this status"
                    redirect(action:"show", controller: controllerName, id:params.id)
                }
                return isEditable
            }
        }
        customerChargeItemEditFilter(controller:"customerChargeItem", action:"edit"){
            before = {
                def customerChargeItem = CustomerChargeItem.get(params.id)
                def customerCharge = customerChargeItem.customerCharge
                def isEditable = checkIfEditable(customerCharge.id, "customerCharge")
                if(!isEditable){
                    flash.error = "Customer Charge can not be edited in this status"
                    redirect(action:"show", controller: "customerCharge", id:customerCharge.id)
                }
                return isEditable
            }
        }
        customerChargeItemUpdateFilter(controller:"customerChargeItem", action:"update"){
            before = {
                def customerChargeItem = CustomerChargeItem.get(params.id)
                def customerCharge = customerChargeItem.customerCharge
                def isEditable = checkIfEditable(customerCharge.id, "customerCharge")
                if(!isEditable){
                    flash.error = "Customer Charge can not be edited in this status"
                    redirect(action:"show", controller: "customerCharge", id:customerCharge.id)
                }
                return isEditable
            }
        }
        customerChargeItemCreateFilter(controller:"customerChargeItem", action:"create"){
            before = {
                def isEditable = checkIfEditable(params.id, "customerCharge")
                if(!isEditable){
                    flash.error = "Customer Charge can not be edited in this status"
                    redirect(action:"show", controller: "customerCharge", id:params.id)
                }
                return isEditable
            }
        }
        customerChargeItemSaveFilter(controller:"customerChargeItem", action:"save"){
            before = {
                def isEditable = checkIfEditable(params.id, "customerCharge")
                if(!isEditable){
                    flash.error = "Customer Charge can not be edited in this status"
                    redirect(action:"show", controller: "customerCharge", id:params.id)
                }
                return isEditable
            }
        }
        customerChargeItemDeleteFilter(controller:"customerChargeItem", action:"delete"){
            before = {
                def customerChargeItem = CustomerChargeItem.get(params.id)
                def customerCharge = customerChargeItem.customerCharge
                def isEditable = checkIfEditable(customerCharge.id, "customerCharge")
                if(!isEditable){
                    flash.error = "Customer Charge Item can not be deleted in this status"
                    redirect(action:"show", controller: "customerCharge", id:customerCharge.id)
                }
                return isEditable
            }
        }


        counterReceiptEditFilter(controller:"counterReceipt", action:"edit"){
            before = {
                def isEditable = checkIfEditable(params.id, controllerName)
                if(!isEditable){
                    flash.error = "Counter Receipt can not be edited in this status"
                    redirect(action:"show", controller: controllerName, id:params.id)
                }
                return isEditable
            }
        }
        counterReceiptUpdateFilter(controller:"counterReceipt", action:"update"){
            before = {
                def isEditable = checkIfEditable(params.id, controllerName)
                if(!isEditable){
                    flash.error = "Counter Receipt can not be edited in this status"
                    redirect(action:"show", controller: controllerName, id:params.id)
                }
                return isEditable
            }
        }
        collectionScheduleEditFilter(controller:"collectionSchedule", action:"edit"){
            before = {
                def isEditable = checkIfEditable(params.id, controllerName)
                if(!isEditable){
                    flash.error = "Collection Schedule can not be edited in this status"
                    redirect(action:"show", controller: controllerName, id:params.id)
                }
                return isEditable
            }
        }
        collectionScheduleUpdateFilter(controller:"collectionSchedule", action:"update"){
            before = {
                def isEditable = checkIfEditable(params.id, controllerName)
                if(!isEditable){
                    flash.error = "Collection Schedule can not be updated in this status"
                    redirect(action:"show", controller: controllerName, id:params.id)
                }
                return isEditable
            }
        }
        collectionScheduleItemEditFilter(controller:"collectionScheduleItem", action:"edit"){
            before = {
                def collectionScheduleItem = CollectionScheduleItem.get(params.id)
                def collectionSchedule = collectionScheduleItem.schedule
                def isEditable = checkIfEditable(collectionSchedule.id, "collectionSchedule")
                if(!isEditable){
                    flash.error = "Collection Schedule can not be edited in this status"
                    redirect(action:"show", controller: "collectionSchedule", id:collectionSchedule.id)
                }
                return isEditable
            }
        }
        collectionScheduleItemUpdateFilter(controller:"collectionScheduleItem", action:"update"){
            before = {
                def collectionScheduleItem = CollectionScheduleItem.get(params.id)
                def collectionSchedule = collectionScheduleItem.schedule
                def isEditable = checkIfEditable(collectionSchedule.id, "collectionSchedule")
                if(!isEditable){
                    flash.error = "Collection Schedule can not be updated in this status"
                    redirect(action:"show", controller: "collectionSchedule", id:collectionSchedule.id)
                }
                return isEditable
            }
        }
        collectionScheduleItemCreateFilter(controller:"collectionScheduleItem", action:"create"){
            before = {
                def isEditable = checkIfEditable(params.id, "collectionSchedule")
                if(!isEditable){
                    flash.error = "Collection Schedule can not be edited in this status"
                    redirect(action:"show", controller: "collectionSchedule", id:params.id)
                }
                return isEditable
            }
        }
        collectionScheduleItemSaveFilter(controller:"collectionScheduleItem", action:"save"){
            before = {
                def isEditable = checkIfEditable(params.id, "collectionSchedule")
                if(!isEditable){
                    flash.error = "Collection Schedule can not be edited in this status"
                    redirect(action:"show", controller: "collectionSchedule", id:params.id)
                }
                return isEditable
            }
        }
        checkDepositEditFilter(controller:"checkDeposit", action:"edit"){
            before = {
                def isEditable = checkIfEditable(params.id, controllerName)
                if(!isEditable){
                    flash.error = "Check Deposit can not be edited in this status"
                    redirect(action:"show", controller: controllerName, id:params.id)
                }
                return isEditable
            }
        }
        checkDepositUpdateFilter(controller:"checkDeposit", action:"update"){
            before = {
                def isEditable = checkIfEditable(params.id, controllerName)
                if(!isEditable){
                    flash.error = "Check Deposit can not be edited in this status"
                    redirect(action:"show", controller: controllerName, id:params.id)
                }
                return isEditable
            }
        }
        bouncedCheckEditFilter(controller:"bouncedCheck", action:"edit"){
            before = {
                def isEditable = checkIfEditable(params.id, controllerName)
                if(!isEditable){
                    flash.error = "Bounced Check can not be edited in this status"
                    redirect(action:"show", controller: controllerName, id:params.id)
                }
                return isEditable
            }
        }
        bouncedCheckUpdateFilter(controller:"bouncedCheck", action:"update"){
            before = {
                def isEditable = checkIfEditable(params.id, controllerName)
                if(!isEditable){
                    flash.error = "Bounced Check can not be edited in this status"
                    redirect(action:"show", controller: controllerName, id:params.id)
                }
                return isEditable
            }
        }
        checkWarehouseEditFilter(controller:"checkWarehouse", action:"edit"){
            before = {
                def isEditable = checkIfEditable(params.id, controllerName)
                if(!isEditable){
                    flash.error = "Check Warehouse can not be edited in this status"
                    redirect(action:"show", controller: controllerName, id:params.id)
                }
                return isEditable
            }
        }
        checkWarehouseUpdateFilter(controller:"checkWarehouse", action:"update"){
            before = {
                def isEditable = checkIfEditable(params.id, controllerName)
                if(!isEditable){
                    flash.error = "Check Warehouse can not be edited in this status"
                    redirect(action:"show", controller: controllerName, id:params.id)
                }
                return isEditable
            }
        }
        supplierPaymentEditFilter(controller:"supplierPayment", action:"edit"){
            before = {

                def isEditable = checkIfEditable(params.id, controllerName)
                if(!isEditable){
                    flash.error = "Supplier Payment can not be edited in this status"
                    redirect(action:"show", controller: controllerName, id:params.id)
                }
                return isEditable
            }
        }
        supplierPaymentItemEditFilter(controller:"supplierPaymentItem", action:"edit"){
            before = {
                def supplierPaymentItem = SupplierPaymentItem.get(params.id)
                def supplierPayment = supplierPaymentItem.payment
                def isEditable = checkIfEditable(supplierPayment.id, "supplierPayment")
                if(!isEditable){
                    flash.error = "Supplier Payment can not be edited in this status"
                    redirect(action:"show", controller: "supplierPayment", id:supplierPayment.id)
                }
                return isEditable
            }
        }
        supplierPaymentItemEditFilter(controller:"supplierPaymentItem", action:"delete"){
            before = {
                def supplierPaymentItem = SupplierPaymentItem.get(params.id)
                def supplierPayment = supplierPaymentItem.payment
                def isEditable = checkIfEditable(supplierPayment.id, "supplierPayment")
                if(!isEditable){
                    flash.error = "Supplier Payment can not be edited in this status"
                    redirect(action:"show", controller: "supplierPayment", id:supplierPayment.id)
                }
                return isEditable
            }
        }
        supplierPaymentUpdateFilter(controller:"supplierPayment", action:"update"){
            before = {
                def isEditable = checkIfEditable(params.id, controllerName)
                if(!isEditable){
                    flash.error = "Supplier Payment can not be edited in this status"
                    redirect(action:"show", controller: controllerName, id:params.id)
                }
                return isEditable
            }
        }
        priceAdjustmentEditFilter(controller:"priceAdjustment", action:"edit"){
            before = {
                def isEditable = checkIfEditable(params.id, controllerName)
                if(!isEditable){
                    flash.error = "Price Adjustment can not be edited in this status"
                    redirect(action:"show", controller: controllerName, id:params.id)
                }
                return isEditable
            }
        }
        priceAdjustmentUpdateFilter(controller:"priceAdjustment", action:"update"){
            before = {
                def isEditable = checkIfEditable(params.id, controllerName)
                if(!isEditable){
                    flash.error = "Price Adjustment can not be edited in this status"
                    redirect(action:"show", controller: controllerName, id:params.id)
                }
                return isEditable
            }
        }
        tripTicketEditFilter(controller:"tripTicket", action:"edit"){
            before = {
                def isEditable = checkIfEditable(params.id, controllerName)
                if(!isEditable){
                    flash.error = "Trip Ticket can not be edited in this status"
                    redirect(action:"show", controller: controllerName, id:params.id)
                }
                return isEditable
            }
        }
        tripTicketUpdateFilter(controller:"tripTicket", action:"update"){
            before = {
                def isEditable = checkIfEditable(params.id, controllerName)
                if(!isEditable){
                    flash.error = "Trip Ticket can not be edited in this status"
                    redirect(action:"show", controller: controllerName, id:params.id)
                }
                return isEditable
            }
        }
        waybillEditFilter(controller:"waybill", action:"edit"){
            before = {
                def isEditable = checkIfEditable(params.id, controllerName)
                if(!isEditable){
                    flash.error = "Waybill can not be edited in this status"
                    redirect(action:"show", controller: controllerName, id:params.id)
                }
                return isEditable
            }
        }
        waybillUpdateFilter(controller:"waybill", action:"update"){
            before = {
                def isEditable = checkIfEditable(params.id, controllerName)
                if(!isEditable){
                    flash.error = "Waybill can not be edited in this status"
                    redirect(action:"show", controller: controllerName, id:params.id)
                }
                return isEditable
            }
        }
        directDeliveryEditFilter(controller:"directDelivery", action:"edit"){
            before = {
                def isEditable = checkIfEditable(params.id, controllerName)
                if(!isEditable){
                    flash.error = "Direct Delivery can not be edited in this status"
                    redirect(action:"show", controller: controllerName, id:params.id)
                }
                return isEditable
            }
        }
        directDeliveryUpdateFilter(controller:"directDelivery", action:"update"){
            before = {
                def isEditable = checkIfEditable(params.id, controllerName)
                if(!isEditable){
                    flash.error = "Direct Delivery can not be edited in this status"
                    redirect(action:"show", controller: controllerName, id:params.id)
                }
                return isEditable
            }
        }
        inventoryAdjustmentEditFilter(controller:"inventoryAdjustment", action:"edit"){
            before = {
                def isEditable = checkIfEditable(params.id, controllerName)
                if(!isEditable){
                    flash.error = "Inventory Adjustment can not be edited in this status"
                    redirect(action:"show", controller: controllerName, id:params.id)
                }
                return isEditable
            }
        }
        inventoryAdjustmentUpdateFilter(controller:"inventoryAdjustment", action:"update"){
            before = {
                def isEditable = checkIfEditable(params.id, controllerName)
                if(!isEditable){
                    flash.error = "Inventory Adjustment can not be edited in this status"
                    redirect(action:"show", controller: controllerName, id:params.id)
                }
                return isEditable
            }
        }
        inventoryTransferEditFilter(controller:"inventoryTransfer", action:"edit"){
            before = {
                def isEditable = checkIfEditable(params.id, controllerName)
                if(!isEditable){
                    flash.error = "Inventory Transfer can not be edited in this status"
                    redirect(action:"show", controller: controllerName, id:params.id)
                }
                return isEditable
            }
        }
        inventoryTransferUpdateFilter(controller:"inventoryTransfer", action:"update"){
            before = {
                def isEditable = checkIfEditable(params.id, controllerName)
                if(!isEditable){
                    flash.error = "Inventory Transfer can not be edited in this status"
                    redirect(action:"show", controller: controllerName, id:params.id)
                }
                return isEditable
            }
        }
		salesOrderEditFilter(controller:'salesOrder', action:'edit') {
			before = {
				def isEditable = checkIfEditable(params.id, controllerName)
				if(!isEditable){
					flash.error = "Sales Order can not be edited in this status"
					redirect(action:"show", controller: controllerName, id:params.id)
				}
				return isEditable
			}
		}
		salesOrderUpdateFilter(controller:'salesOrder', action:'update') {
			before = {
				def isEditable = checkIfEditable(params.id, controllerName)
				if(!isEditable){
					flash.error = "Sales Order can not be edited in this status"
					redirect(action:"show", controller: controllerName, id:params.id)
				}
				return isEditable
			}
		}
		salesDeliveryEditFilter(controller:'salesDelivery', action:'edit') {
			before = {
				def isEditable = checkIfEditable(params.id, controllerName)
				if(!isEditable){
					flash.error = "Sales Delivery can't be edited in this status!"
					redirect(action:"show", controller: controllerName, id:params.id)
				}
				return isEditable
			}
		}
		salesDeliveryUpdateFilter(controller:'salesDelivery', action:'update') {
			before = {
				def isEditable = checkIfEditable(params.id, controllerName)
				if(!isEditable){
					flash.error = "Sales Delivery can't be updated in this status!"
					redirect(action:"show", controller: controllerName, id:params.id)
				}
				return isEditable
			}
		}
		bouncedCheckEditFilter(controller:'bouncedCheck', action:'edit') {
			before = {
				def isEditable = checkIfEditable(params.id, controllerName)
				if(!isEditable){
					flash.error = "Bounced Check can not be edited in this status!"
					redirect(action:"show", controller: controllerName, id:params.id)
				}
				return isEditable
			}
		}
		bouncedCheckUpdateFilter(controller:'bouncedCheck', action:'update') {
			before = {
				def isEditable = checkIfEditable(params.id, controllerName)
				if(!isEditable){
					flash.error = "Bounced Check cannot be updated in this status!"
					redirect(action:"show", controller: controllerName, id:params.id)
				}
				return isEditable
			}
		}
		purchaseOrderEditFilter(controller:'purchaseOrder', action:'changeValues') {
			before = {
				def isEditable = checkIfEditable(params.id, controllerName)
				if(!isEditable){
					flash.error = "Purchase Order can't be edited in this status!"
					redirect(action:"show", controller: controllerName, id:params.id)
				}
				return isEditable
			}
		}
		purchaseOrderUpdateFilter(controller:'purchaseOrder', action:'update') {
			before = {
				def isEditable = checkIfEditable(params.id, controllerName)
				if(!isEditable){
					flash.error = "Purchase Order can't be updated in this status!"
					redirect(action:"show", controller: controllerName, id:params.id)
				}
				return isEditable
			}
		}
		purchaseInvoiceEditFilter(controller:'purchaseInvoice', action:'edit') {
			before = {
				def isEditable = checkIfEditable(params.id, controllerName)
				if(!isEditable){
					flash.error = "Purchase Invoice can't be edited in this status!"
					redirect(action:"show", controller: controllerName, id:params.id)
				}
				return isEditable
			}
		}
		purchaseInvoiceUpdateFilter(controller:'purchaseInvoice', action:'update') {
			before = {
				def isEditable = checkIfEditable(params.id, controllerName)
				if(!isEditable){
					flash.error = "Purchase Invoice can't be updated in this status!"
					redirect(action:"show", controller: controllerName, id:params.id)
				}
				return isEditable
			}
		}
		materialReleaseEditFilter(controller:'materialRelease', action:'edit') {
			before = {
				def isEditable = checkIfEditable(params.id, controllerName)
				if(!isEditable){
					flash.error = "Material Release can't be edited in this status!"
					redirect(action:"show", controller: controllerName, id:params.id)
				}
				return isEditable
			}
		}
		materialReleaseUpdateFilter(controller:'materialRelease', action:'update') {
			before = {
				def isEditable = checkIfEditable(params.id, controllerName)
				if(!isEditable){
					flash.error = "Material Release can't be updated in this status!"
					redirect(action:"show", controller: controllerName, id:params.id)
				}
				return isEditable
			}
		}
		jobOrderEditFilter(controller:'jobOrder', action:'jobOrderEdit') {
			before = {
				def isEditable = checkIfEditable(params.jobOrderId, controllerName)
				if(!isEditable){
					flash.error = "Job Order can't be edited in this status!"
					redirect(action:"show", controller: controllerName, params:[jobOrderId:params.jobOrderId])
				}
				return isEditable
			}
		}
		jobOrderUpdateFilter(controller:'jobOrder', action:'updateJobOrder') {
			before = {
				def isEditable = checkIfEditable(params.id, controllerName)
				if(!isEditable){
					flash.error = "Job Order can't be updated in this status!"
					redirect(action:"show", controller: controllerName, params:[jobOrderId:params.id])
				}
				return isEditable
			}
		}
		jobOrderEditMaterialsFilter(controller:'jobOrder', action:'edit') {
			before = {
				def isEditable = checkIfEditable(params.jobOrderId, controllerName)
				if(!isEditable){
					flash.error = "Materials of job order can't be edited in this status!"
					redirect(action:"show", controller: controllerName, params:[jobOrderId:params.jobOrderId])
				}
				return isEditable
			}
		}
		jobOrderUpdateMaterialsFilter(controller:'jobOrder', action:'update') {
			before = {
				def isEditable = checkIfEditable(params.id, controllerName)
				if(!isEditable){
					flash.error = "Materials of job order can't be updated in this status!"
					redirect(action:"show", controller: controllerName, params:[jobOrderId:params.id])
				}
				return isEditable
			}
		}
		checkWarehousingUpdateFilter(controller:'checkWarehousing', action:'update') {
			before = {
				def isEditable = checkIfEditable(params.id, controllerName)
				if(!isEditable){
					flash.error = "Check Warehousing can't be updated in this status!"
					redirect(action:"show", controller: controllerName, id:params.id)
				}
				return isEditable
			}
		}
		checkWarehousingEditFilter(controller:'checkWarehousing', action:'edit') {
			before = {
				def isEditable = checkIfEditable(params.id, controllerName)
				if(!isEditable){
					flash.error = "Check Warehousing can't be edited in this status!"
					redirect(action:"show", controller: controllerName, id:params.id)
				}
				return isEditable
			}
		}
		jobOutEditFilter(controller:'jobOut', action:'edit') {
			before = {
				def isEditable = checkIfEditable(params.id, controllerName)
				if(!isEditable){
					flash.error = "Job Out can't be edited in this status!"
					redirect(action:"show", controller: controllerName, id:params.id)
				}
				return isEditable
			}
		}
		jobOutUpdateFilter(controller:'jobOut', action:'update') {
			before = {
				def isEditable = checkIfEditable(params.id, controllerName)
				if(!isEditable){
					flash.error = "Job Out can't be updated in this status!"
					redirect(action:"show", controller: controllerName, id:params.id)
				}
				return isEditable
			}
		}
		creditMemoEditFilter(controller:'creditMemo', action:'edit') {
			before = {
				def isEditable = checkIfEditable(params.id, controllerName)
				if(!isEditable){
					flash.error = "Credit Memo can't be edited in this status!"
					redirect(action:"show", controller: controllerName, id:params.id)
				}
				return isEditable
			}
		}
		creditMemoUpdateFilter(controller:'creditMemo', action:'update') {
			before = {
				def isEditable = checkIfEditable(params.id, controllerName)
				if(!isEditable){
					flash.error = "Credit Memo can't be updated in this status!"
					redirect(action:"show", controller: controllerName, id:params.id)
				}
				return isEditable
			}
		}
		creditMemoItemEditFilter(controller:'creditMemoItem', action:'edit') {
			before = {
				def isEditable = checkIfEditable(params.id, controllerName)
				if(!isEditable){
					def creditMemoItemInstance = CreditMemoItem.get(params.id)
					params.id = creditMemoItemInstance.creditMemo.id
					flash.error = "Credit Memo Item can't be edited in this status!"
					redirect(action:"show", controller: "creditMemo", id:params.id)
				}
				return isEditable
			}
		}
		creditMemoItemUpdateFilter(controller:'creditMemoItem', action:'update') {
			before = {
				def isEditable = checkIfEditable(params.id, controllerName)
				if(!isEditable){
					def creditMemoItemInstance = CreditMemoItem.get(params.id)
					params.id = creditMemoItemInstance.creditMemo.id
					flash.error = "Credit Memo Item can't be updated in this status!"
					redirect(action:"show", controller: "creditMemo", id:params.id)
				}
				return isEditable
			}
		}

    }
}
