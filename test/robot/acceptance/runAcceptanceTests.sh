#!/bin/bash

export CLASSPATH=$CLASSPATH:../../../target/classes/:$GRAILS_HOME//lib/:$GRAILS_HOME/lib/groovy-all-1.7.5.jar

if [ $# -eq 1 ]
then
jybot -i $1 ../acceptance
else
jybot ../acceptance
fi

exit 0
