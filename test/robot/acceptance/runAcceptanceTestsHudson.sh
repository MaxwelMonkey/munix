#!/bin/bash

export CLASSPATH=$CLASSPATH:$WORKSPACE/target/classes/:$GRAILS_HOME//lib/:$GRAILS_HOME/lib/groovy-all-1.7.5.jar
jybot credit_memo_acceptance.txt
exit 0
