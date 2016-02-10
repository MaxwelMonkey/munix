package org.codehaus.groovy.grails.plugins.filter.controller

import org.codehaus.groovy.grails.commons.GrailsDomainClass
import org.codehaus.groovy.grails.commons.DomainClassArtefactHandler

import org.codehaus.groovy.grails.plugins.filter.FilterBuilder
import org.codehaus.groovy.grails.plugins.filter.Filter

import org.apache.commons.lang.StringUtils as SU


class FilterController {

	def filter = {

		Class clazz = grailsApplication.getArtefact(DomainClassArtefactHandler.TYPE,params.filterBean).getClazz()

		if(!params.filterCriteria || !params.filterValue) {

			if(!params.max) params.max = 10
			def model = [ total:clazz.count() ]
			String list = "${SU.uncapitalize(params.filterBean)}List".toString()
			model.put( list,clazz.list(params) )
            render( template:params.filterTemplate,model: model )
			return

		}

		FilterBuilder builder = null

        if(!params.max) params.max = 10
        if(!params.offset) params.offset = 0
        if(!params.order) params.order = "asc"
		
        Filter filter = new Filter()

        bindData(filter,params)
        
		try {

			builder = new FilterBuilder(filter)
          
		} catch( IllegalArgumentException e ) {
			response.status = 666 
			render "<div class='error' id='filterError'>${message(code:'Filter.error.'+e.message)}</div>"
			return
		}

		def query = builder.simpleCriteria()

		def _query = builder.simpleCount()

		def result = clazz.createCriteria().list(query)

		int total = clazz.createCriteria().list(_query).size()

		def model = [ total:total,filterCriteria:filter.filterCriteria,filterField:filter.filterField,filterValue:filter.filterValue,filterValue2:filter.filterValue2 ]

		String list = "${SU.uncapitalize(params.filterBean)}List".toString()

		model.put(list,result)

		if( request.getHeader("X-Requested-With")=="XMLHttpRequest" )
            render( template:params.filterTemplate,model: model )         

	}
}
