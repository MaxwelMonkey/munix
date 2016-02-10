package com.munix

class MunixTagLib {
    
    static namespace = "munix"
    def table = null
    def fields = null

    //Do not erase
    def selectorConfig = {
        out << """
        //Configuration for the selector
        var selector_table_config = {
          sort: false,
          sort_config: { sort_types:['Number', 'string', 'String', 'Number'] },
          on_keyup: true,
          single_search_filter: true,
          on_keyup_delay: 100,
          highlight_keywords: true,
          sort_select: false,
          status_bar: false,
          rows_counter: false,
          btn_reset: false,
          alternate_rows: true,
          paging: false,
          paging_length: 5, <!--default 10 -->
        }"""
    }
    
    def createCart = { attrs ->
        table = attrs['table']
        fields = attrs['fields'].split(',')
        def total = attrs['total']
        def identifier = attrs['identifier']
        def price = attrs['price']
        def qty = attrs['qty']
        def amount = attrs['amount']
        def counter = 0
      
        //Add Row
        out << """
         var counter=0;
         function addRow() {
            var table = document.getElementById('${table}');"""

        fields.each{
            out << """
                var ${it} = document.getElementById('${it}').value;
                """
        }
        
        out << """var tr = document.createElement('TR');"""

        fields.each{
            counter++
            out << """
                var td${counter} = document.createElement('TD');
                var input${counter} = document.createElement('INPUT');"""
        }
        counter++
        out << """
            var td${counter} = document.createElement('TD');
            var rowCount = table.rows.length;
            var newqty;
            var newamount;
            if (rowCount > 0){
                for(var i=0; i<rowCount; i++){
                    var row = table.rows[i];"""
                    counter = 0
                    fields.each{
                        
                        out << """
                            var cell${it} = row.cells[${counter}].childNodes[0];"""
                        counter++
                    }
                    out << """
                    if(cell${identifier}.value == ${identifier}){
                      newqty = parseFloat(cell${qty}.value) + parseFloat(${qty});
                      newamount = parseFloat(cell${price}.value) * parseFloat(newqty);
                      ${price} = cell${price}.value;
                      ${qty} = newqty;
                      ${amount} = roundNumber(newamount, 2);
                      table.deleteRow(i);
                      rowCount--;
                      i--;
                      counter--;
                    }
                 }
              }
              table.appendChild(tr);"""

        counter = 0
        fields.each{
            counter++
            out << """
               tr.appendChild(td${counter});"""
        }
        counter++
        out << """
        tr.appendChild(td${counter});"""
        counter = 0
        fields.each{
             counter++
            out << """
              input${counter}.setAttribute('name', '${it}'+counter);
              input${counter}.setAttribute('value', ${it});
              input${counter}.setAttribute('readOnly', true);
              td${counter}.appendChild(input${counter});"""
              
        }
        counter ++
        out << """
            var a = document.createElement('a');
            var remove = document.createTextNode("X");
            a.appendChild(remove);
            td${counter}.appendChild(a);
            if(counter>=0){
            a.onclick = function(){
                removeRow(tr);
                computeTotal();
                }
            }
            computeTotal();
            clear();
            counter++;
        }"""

        //Compute Total
        out << """
        function computeTotal(){
            var table = document.getElementById("${table}");
            rowCount = table.rows.length;
            amt = 0;
            if(rowCount > 0){
                for(var i=0; i<rowCount; i++) {
                    var row = table.rows[i];
                    var totalAmt = row.cells[3].childNodes[0];"""
                    counter = 0
                  
                   fields.each{
                    
                    
                    out << """
                            var cell${it} = row.cells[${counter}].childNodes[0];
                            cell${it}.setAttribute('name', '${it}'+i);
                            cell${it}.setAttribute('value',cell${it}.value);"""
                         counter++
                    }
                    out << """
                    amt += parseFloat(totalAmt.value);
                    document.getElementById("${total}").value = roundNumber(amt, 2);
                }
            }
            else{
                document.getElementById("${total}").value = roundNumber(amt, 2);
            }
        }"""

        out << """
        function getCounter(){
            document.getElementById("counter").value = counter;
            return true;
        }

        function computeAmount(){
          var ${price} = document.getElementById("${price}").value;
          var ${qty} = document.getElementById("${qty}").value;
          if(${price} != null && ${price} != "" && ${qty} != null && ${qty} != ""){
            document.getElementById("${amount}").value = roundNumber(${price}*${qty},2);
          }
        }

        function removeRow(tr){
          tr.parentNode.removeChild(tr);
          counter--;
          clear();
          computeTotal();
        }

        function roundNumber(num, dec){
          return Math.round(num*Math.pow(10, dec))/Math.pow(10,dec);
        }

        function clear(){"""
          fields.each{
              out << """document.getElementById("${it}").value ="";"""
          }
        out << """}"""
        
    }

    def createChart = {
        out << """
    var g = new JSGantt.GanttChart('g',document.getElementById('GanttChartDIV'), 'day');
  g.setShowRes(1);
  g.setShowDur(1);
  g.setShowComp(1);
  g.setCaptionType('Resource');
  if( g ) {
  // Parameters (pID, pName,pStart,pEnd,pColor,pLink,pMile, pRes,  pComp, pGroup, pParent, pOpen, pDepend, pCaption)
  g.AddTaskItem(new JSGantt.TaskItem(1,'Define Chart API','','','ff0000','http://help.com', 0,'Brian',0, 1, 0, 1));
  
  g.AddTaskItem(new JSGantt.TaskItem(11,'Chart Object','7/20/2008','7/20/2008','ff00ff','http://www.yahoo.com', 1,'Shlomy',100, 0, 1, 1));
  g.Draw();
  }

  else
  {
  alert("not defined");
  }


        """

    }
}
