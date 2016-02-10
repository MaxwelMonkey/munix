package com.munix

import java.text.SimpleDateFormat

class GeneralMethodService {
    def removeSameItems(list1,list2){
		def tempList=list1.clone()
		tempList.removeAll(list2)
		return tempList
	}

    def createDate(date){
    	def df = new SimpleDateFormat("MM/dd/yyyy");
		return df.parse(date);
	}

	def createCalendarStructDate(dateString){
		if(dateString) {
			return createDate(dateString)
		} else {
			return ""
		}
	}
	
	def dateToday(){
		def today = Calendar.getInstance()
		return createDate((today.get(Calendar.MONTH) + 1) + "/" + today.get(Calendar.DATE) + "/" + today.get(Calendar.YEAR))
	}

	def getDateFromApprovedBy(String approvedBy) {
    	def df = new SimpleDateFormat("MMM. dd, yyyy - hh:mm a");
		return df.parse(approvedBy.substring(approvedBy.length() - 24, approvedBy.length()))
	}

    def getDateString(Date date, String format){
        def df = new SimpleDateFormat(format);
        return df.format(date)
    }

    def performDayOperationsOnDate(Operation operation, Date date, int number){
        switch (operation) {
			case Operation.ADD:
                Calendar c = Calendar.getInstance();
                c.setTime(date);
                c.add(Calendar.DATE, number);  // number of days to add
				return c.getTime()
            case Operation.SUBTRACT:
                Calendar c = Calendar.getInstance();
                c.setTime(date);
                c.add(Calendar.DATE, (number*-1));  // number of days to add
				return c.getTime()

		}
    }
	
	def paginateList(List list, int total, Map params) {
		if (!params.max) params.max = 100
		if (!params.offset) params.offset = 0

		def startIndex = Integer.parseInt(params.offset.toString())
		def endIndex = startIndex + Integer.parseInt(params.max.toString())
		if (endIndex > total) {
			endIndex = total
		}
		return list.subList(startIndex, endIndex)
	}

    def capitalizeFirstLetter(String string){
        if(string){
            return (Character.toUpperCase(string.charAt(0)).toString() + string.substring(1));
        }
    }

    def addWhiteSpaceAfterCapital(String string){
        if(string){
            return string.replaceAll("(\\p{Ll})(\\p{Lu})","\$1 \$2");
        }
    }
}
