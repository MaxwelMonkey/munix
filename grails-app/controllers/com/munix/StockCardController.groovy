package com.munix

import java.util.Calendar;

import groovy.sql.Sql

class StockCardController {

    def dataSource

	def show = {
		def stockCardInstance = StockCard.get(params.id)
		if (stockCardInstance) {
			def stocks = Stock.findAllByProduct(stockCardInstance.product)
			def cal = Calendar.getInstance()
			def dateTo = params.dateTo?params.dateTo:cal.getTime()
			cal.add(Calendar.MONTH, -6)
			def dateFrom = params.dateFrom?params.dateFrom:cal.getTime()
			def items = StockCardItem.executeQuery("from StockCardItem s where s.stockCard=? and (s.datePosted<? or s.datePosted is null)",[stockCardInstance, dateTo])
			print items?.size()
			return [stockCardInstance: stockCardInstance, stockCardItems: items, stocks: stocks, dateFrom:dateFrom, dateTo:dateTo]
		} else {
			flash.message = "Stock Card not found."
			redirect(action: "list", controller: "product")
		}

	}
    
    def clean = {
    	def s = []
    	def query = "select sc.id stock_card_id from stock_card sc, product p where sc.product_id=p.id and p.status='Active'"
        Sql sqlQuery = new Sql(dataSource)
        def result = sqlQuery.rows(query)
        result.each{
    		s.add(it.stock_card_id)
    	}
    	[stockCards:s]
    }
	
	def recalculate = {
        Sql sqlQuery = new Sql(dataSource)
        sqlQuery.execute("delete from stock_card_item where stock_card_id=${params.id} and type<>'INITIAL_ENTRY'")
        sqlQuery.execute("insert into stock_card_item(version, balance, cost_foreign, cost_local, date_opened, date_posted, link_id, qty_in, qty_out, reference_id, selling_amount, stock_card_id, type, warehouse_in, warehouse_out, currency_foreign_id, supplier_customer) select 0, 0, null, null, a.date, str_to_date(substring(approved_by,-24), '%b. %d, %Y - %h:%i %p'), a.id, null, b.qty, a.sales_delivery_id, b.price, c.id, 'APPROVED_SALES_DELIVERY', null, d.identifier, null, e.name from sales_delivery a, sales_delivery_item b, stock_card c, warehouse d, customer e where approved_by <> '' and a.id=b.delivery_id and b.product_id=c.product_id and a.warehouse_id=d.id and a.customer_id=e.id and b.qty<>0 and c.id=${params.id}")
        sqlQuery.execute("insert into stock_card_item(version, balance, cost_foreign, cost_local, date_opened, date_posted, link_id, qty_in, qty_out, reference_id, selling_amount, stock_card_id, type, warehouse_in, warehouse_out, currency_foreign_id, supplier_customer) select 0, 0, null, null, a.date, str_to_date(substring(a.approved_two_by,-24), '%b. %d, %Y - %h:%i %p'), a.id, b.old_qty-b.new_qty, null, concat('CM-',lpad(a.id, 5,'0')), b.new_price, e.id, 'APPROVED_CREDIT_MEMO', f.identifier, null, null, g.name from credit_memo a, credit_memo_item b, sales_delivery_item c, sales_delivery d, stock_card e, warehouse f, customer g where a.approved_two_by <> '' and a.id=b.credit_memo_id and b.delivery_item_id = c.id and c.product_id=e.product_id and c.delivery_id = d.id and a.warehouse_id= f.id and d.customer_id=g.id and e.id=${params.id}")
        sqlQuery.execute("insert into stock_card_item(version, balance, cost_foreign, cost_local, date_opened, date_posted, link_id, qty_in, qty_out, reference_id, selling_amount, stock_card_id, type, warehouse_in, warehouse_out, currency_foreign_id, supplier_customer) select 0, 0, null, b.cost,  a.date, str_to_date(substring(a.approved_by,-24), '%b. %d, %Y - %h:%i %p'), a.id, null, b.qty, concat('ML-',lpad(a.id, 8,'0')), null, d.id, 'APPROVED_MATERIAL_RELEASE', null, e.identifier, null, null from material_release a, material_release_item b, material_requisition_item c, stock_card d, warehouse e where a.approved_by <> '' and a.id=b.material_release_id and b.material_requisition_item_id =c.id and c.component_id=d.product_id and a.warehouse_id=e.id and d.id=${params.id}")
        sqlQuery.execute("insert into stock_card_item(version, balance, cost_foreign, cost_local, date_opened, date_posted, link_id, qty_in, qty_out, reference_id, selling_amount, stock_card_id, type, warehouse_in, warehouse_out, currency_foreign_id, supplier_customer) select 0, 0, b.final_price * a.exchange_rate, b.final_price, a.date, str_to_date(substring(a.approved_by,-24), '%b. %d, %Y - %h:%i %p'), a.id, b.qty, null, concat(a.reference, concat(' / ',  a.supplier_reference)), null, d.id, 'APPROVED_PURCHASE_INVOICE', f.identifier, null, e.currency_id, g.name from purchase_invoice a, purchase_invoice_item b, purchase_order_item c, stock_card d, purchase_order e, warehouse f, supplier g where a.approved_by <> '' and b.purchase_invoice_id=a.id and b.purchase_order_item_id = c.id and c.product_id=d.product_id and c.po_id=e.id and b.purchase_invoice_id=a.id and a.warehouse_id=f.id and e.supplier_id=g.id and d.id=${params.id}")
        sqlQuery.execute("insert into stock_card_item(version, balance, cost_foreign, cost_local, date_opened, date_posted, link_id, qty_in, qty_out, reference_id, selling_amount, stock_card_id, type, warehouse_in, warehouse_out, currency_foreign_id, supplier_customer) select 0, 0, null, null, a.date, str_to_date(substring(a.approved_by,-24), '%b. %d, %Y - %h:%i %p'), a.id, a.qty, null, concat('JT-',lpad(a.id, 8,'0')), null, c.id, 'APPROVED_JOB_OUT', d.identifier, null, null, null from job_out a, job_order b, stock_card c, warehouse d where a.approved_by <> '' and a.job_order_id = b.id and b.product_id=c.product_id and a.warehouse_id=d.id and c.id=${params.id}") 
        sqlQuery.execute("insert into stock_card_item(version, balance, cost_foreign, cost_local, date_opened, date_posted, link_id, qty_in, qty_out, reference_id, selling_amount, stock_card_id, type, warehouse_in, warehouse_out, currency_foreign_id, supplier_customer) select 0, 0, null, null, a.date, str_to_date(substring(a.received_by,-24), '%b. %d, %Y - %h:%i %p'), a.id, b.qty, b.qty, concat('ST-',lpad(a.id, 8,'0')), null, c.id, 'APPROVED_INVENTORY_TRANSFER', e.identifier, d.identifier, null, null from inventory_transfer a, inventory_transfer_item b, stock_card c, warehouse d, warehouse e where received_by <> '' and a.id=b.transfer_id and b.product_id=c.product_id and a.origin_warehouse_id = d.id and a.destination_warehouse_id = e.id and c.id=${params.id}")
        sqlQuery.execute("insert into stock_card_item(version, balance, cost_foreign, cost_local, date_opened, date_posted, link_id, qty_in, qty_out, reference_id, selling_amount, stock_card_id, type, warehouse_in, warehouse_out, currency_foreign_id, supplier_customer) select 0, 0, null, null, a.date_generated, str_to_date(substring(a.approved_by,-24), '%b. %d, %Y - %h:%i %p'), b.id, b.new_stock-b.old_stock, null, concat('IA-',lpad(a.id, 8,'0')), null, c.id, 'APPROVED_INVENTORY_ADJUSTMENT', d.identifier, null, null, null from inventory_adjustment a, inventory_adjustment_item b, stock_card c, warehouse d where approved_by <> '' and a.id=b.adjustment_id and b.product_id=c.product_id and a.warehouse_id=d.id and (b.new_stock-b.old_stock)>=0 and c.id=${params.id}")
        sqlQuery.execute("insert into stock_card_item(version, balance, cost_foreign, cost_local, date_opened, date_posted, link_id, qty_in, qty_out, reference_id, selling_amount, stock_card_id, type, warehouse_in, warehouse_out, currency_foreign_id, supplier_customer) select 0, 0, null, null, a.date_generated, str_to_date(substring(a.approved_by,-24), '%b. %d, %Y - %h:%i %p'), b.id, null, b.old_stock-b.new_stock, concat('IA-',lpad(a.id, 8,'0')), null, c.id, 'APPROVED_INVENTORY_ADJUSTMENT', null, d.identifier, null, null from inventory_adjustment a, inventory_adjustment_item b, stock_card c, warehouse d where approved_by <> '' and a.id=b.adjustment_id and b.product_id=c.product_id and a.warehouse_id=d.id and (b.new_stock-b.old_stock)<0 and c.id=${params.id}")
        sqlQuery.close();
		def stocks = [:]
		def s = StockCard.get(Long.parseLong(params.id))
		def b = 0
		s.items.sort{it.datePosted}.each{
			def diff = 0
			if(it.type==StockCardItem.Type.APPROVED_INVENTORY_ADJUSTMENT){
				def iai = InventoryAdjustmentItem.get(it.linkId)
				def currentStock = stocks[iai.adjustment.warehouse?.identifier]
				if(!currentStock) currentStock = 0
				diff = iai.newStock-currentStock 
				if(it.qtyIn) it.qtyIn = diff
				if(it.qtyOut) it.qtyOut = -1*diff
				stocks[iai.adjustment.warehouse?.identifier] = iai.newStock
			}else{
				if(it.qtyIn) {
					diff+=it.qtyIn
					def stk = stocks[it.warehouseIn]
					if(!stk) stk=0
					stk+=it.qtyIn
					stocks[it.warehouseIn] = stk
				}
				if(it.qtyOut) {
					diff+=-1*it.qtyOut
					def stk = stocks[it.warehouseOut]
	 				if(!stk) stk=0
	 				stk-=it.qtyOut
	 				stocks[it.warehouseOut] = stk
				}
			}
			
			b+=diff
			it.balance = b
			it.save(flush:true)
		}
		stocks.keySet().each{
			def w = Warehouse.findByIdentifier(it)
			def stk = Stock.findByWarehouseAndProduct(w, s.product)
			stk.qty = stocks[it]
			stk.save()
		}
		redirect action:"show", id:params.id
	}
}
