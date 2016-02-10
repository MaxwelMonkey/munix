/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.codehaus.groovy.grails.plugins.filter

import org.codehaus.groovy.grails.plugins.filter.FilterType as Type

/**
 *
 * @author maxwell
 */
class Filter {

    // domain class
    Class filterBean
	
    // field name
    String filterField

    // criteria type
	Type filterCriteria

    // raw values from view
	String filterValue
	String filterValue2

    // formatted filter value
    def filterValueObj
    def filterValue2Obj

	// paginate
	int max
	int offset

	// sort and order
	String order
	String sort

    // the string date format
    String filterDateFormat

}

