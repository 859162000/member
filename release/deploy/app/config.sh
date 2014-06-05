#!/bin/bash

#If the $MEMBER_VERSION not defined, the set $MEMBER_VERSION and $TOMCAT_HOME
if [ -z "$MEMBER_VERSION" ]; then 
  if [ ! -z "$1" ]; then
    MEMBER_VERSION=$1
  else
    echo Please give version! Example: get.sh 1.3.1.1
	exit 0;
  fi

  TOMCAT_HOME=/opt/tomcat8080
  echo MEMBER_VERSION=$MEMBER_VERSION
  echo TOMCAT_HOME=$TOMCAT_HOME
fi