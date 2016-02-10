import org.codehaus.groovy.grails.commons.GrailsDomainClass
import org.codehaus.groovy.grails.commons.DomainClassArtefactHandler
import org.codehaus.groovy.grails.plugins.filter.FilterType as Type

/**
 * Filter tag lib collection
 */
class FilterTagLib {

	static namespace = 'filter'

   /**
   * Dynamic filter
    */
	def dynamic = { attrs, body ->

        if(!attrs.bean)
            throwTagError("Tag [dynamic] is missing required attribute [bean]")

		GrailsDomainClass domainClass = (GrailsDomainClass) grailsApplication.getArtefact(DomainClassArtefactHandler.TYPE,attrs.bean);

		String prefix = attrs.prefix ? attrs.prefix : attrs.bean + ".field"
		String filter = attrs.filter ? attrs.filter : "${message(code:'Filter.dynamic.filter')}"
		String label = attrs.label ? attrs.label : "${message(code:'Filter.dynamic.search')}"
		String controller = attrs.controller ? attrs.controller : "filter"
		String action = attrs.action ? attrs.action : "filter"
        String template = attrs.template ? attrs.template : "/${controllerName}/list"
        String success = attrs.success ? attrs.success : "list"
        String failure =  attrs.failure ? attrs.failure : "filterError"


        // ignore association fields
		def fields = []
		domainClass.persistentProperties.each {
			if(!it.isAssociation())
				fields.add(it)
		}

        // add grails-filter.js reference
        out << "<script type=\"text/javascript\" src=\"${createLinkTo(dir:pluginContextPath,file:"js/grails-filter.js")}\" ></script>"

		out << "<div id='filterError'>&nbsp;</div>"

		out << "<div class='filter'><form>"

		out << "<input type='hidden' name='filterBean' value='${attrs.bean}'/>"

		out << "<input type='hidden' name='filterTemplate' value='${template}'/>"

		out << label

		out << "&nbsp;&nbsp;"

		out << "${select(from:fields,valueMessagePrefix:prefix,optionKey:'name',name:'filterField')}"

		out << "&nbsp;&nbsp;"

        //TODO show only criteria that can be applied to field
		out << "${select(from:Type,name:'filterCriteria',onchange:'criteriaChanged()')}"

		out << "&nbsp;&nbsp;"

		out << "${textField(name:'filterValue')}"

		out << "&nbsp;&nbsp;"

		out << "${textField(name:'filterValue2',style:'display:none;')}"

		out << "&nbsp;&nbsp;"

		out << "${submitToRemote(action:action,controller:controller,value:filter,update:[success:success,failure:failure],onLoading:'filterInitialized()',onComplete:'filterFinished()' )}"

		out << "&nbsp;&nbsp;"

        out << "<img id='filterBusy' style='display:none;' src='${createLinkTo(dir:pluginContextPath,file:'images/filterSpinner.gif')}' />"

		out << "</form></div>"

	}

}
