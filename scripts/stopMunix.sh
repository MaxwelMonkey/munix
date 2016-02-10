#!/bin/bash
export CATALINA_PID=/tmp/catalina.pid
export CATALINA_HOME=$HOME/tomcat

$CATALINA_HOME/bin/shutdown.sh
