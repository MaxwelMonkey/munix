@artifact.package@

/**
 * Note that this is a fixture example for the @artifact.name@ specification that 
 * has been kindly generated by the Grails Concordion plugin.
 *
 * You should change this example to adapt it conveniently to your application 
 * under test.
 */
class @artifact.name@ {

    String greetingFor(String userName) {
	userName == 'Count Tyrone Rugen' ?
	    'Hello. My name is Inigo Montoya. You killed my father. Prepare to die.' :
	    "Hello ${userName}!"
    }

}