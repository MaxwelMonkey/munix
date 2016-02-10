package org.codehaus.groovy.grails.plugins.filter

import org.codehaus.groovy.grails.plugins.filter.FilterType as Type
import java.text.SimpleDateFormat as SDF
import java.text.ParseException

/**
 * Dynamic Filter builder, based on hibernate criteria
 * @author maxwell
 */
public class FilterBuilder {

	//public static final def DATE_REGEX = /\d{4}[-]\d{2}[-]\d{2}/

	//public static final def DATE_TIME_REGEX = /\d{4}[-]\d{2}[-]\d{2}\s\d{2}:\d{2}:\d{2}/

	//public static final String TIME_REGEX = /.?\d{2}:\d{2}:\d{2}$/

	//public static final String LAST_TIME = " 23:59:59"

	//public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"

    Filter filter

	public FilterBuilder(Filter filter) {		
		this.filter = filter
        // parse the values from controller to create criteria
        String type = filter.filterBean.getDeclaredField(filter.filterField).getType().getSimpleName().toUpperCase()
        FieldType.valueOf(type).parse(filter)
	}

	public def simpleCriteria() {

		def query = {

			if(filter.sort)
				order(filter.sort,filter.order)

			maxResults(filter.max)
			firstResult(filter.offset)

			switch(filter.filterCriteria) {

				case Type.EQUALS:
					eq(filter.filterField,filter.filterValueObj)
					break
				case Type.LIKE:
					ilike(filter.filterField, "%${filter.filterValueObj}%")
					break
				case Type.GREATER:
					ge(filter.filterField,filter.filterValueObj)
					break
				case Type.LESS:
					le(filter.filterField,filter.filterValueObj)
					break
				case Type.BETWEEN:
					between(filter.filterField,filter.filterValueObj,filter.filterValue2Obj)
					break
			}

		}

		return query

	}

	public def simpleCount() {

      def query = {

			switch(filter.filterCriteria) {

				case Type.EQUALS:
					eq(filter.filterField,filter.filterValueObj)
					break
				case Type.LIKE:
					ilike(filter.filterField, "%${filter.filterValueObj}%")
					break
				case Type.GREATER:
					ge(filter.filterField,filter.filterValueObj)
					break
				case Type.LESS:
					le(filter.filterField,filter.filterValueObj)
					break
				case Type.BETWEEN:
					between(filter.filterField,filter.filterValueObj,filter.filterValue2Obj)
					break

			}

		}

		return query
	}

	public String value() {
		if(!filter.filterValue)
			return ""
		if(filter.filterValue.class == Date)
			return format.format(filter.filterValue)
		return filter.filterValue
	}

	public String value2() {
		if(!filter.filterValue2)
			return ""
		if(filter.filterValue2.class == Date)
			return format.format(filter.filterValue2)
		return filter.filterValue2
	}

}