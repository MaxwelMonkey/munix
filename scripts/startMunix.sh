#!/bin/bash
export JAVA_HOME=/usr/lib/jvm/java-6-sun
export CATALINA_PID=/tmp/catalina.pid
export CATALINA_HOME=$HOME/tomcat
export CATALINA_OPTS="-Xms512m -Xmx2054m -XX:PermSize=256m -XX:MaxPermSize=384m -XX:+UseConcMarkSweepGC"

$CATALINA_HOME/bin/startup.sh
