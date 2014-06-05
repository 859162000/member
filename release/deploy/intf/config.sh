#!/bin/bash

#If the $MEMBER_VERSION not defined, the set $MEMBER_VERSION , $TOMCAT_HOME_1 , $TOMCAT_HOME_2
if [ -z "$MEMBER_VERSION" ]; then 
  if [ ! -z "$1" ]; then
    MEMBER_VERSION=$1
  else
    echo Please give version! Example: get.sh 1.3.1.1
	exit 0;
  fi

  TOMCAT_HOME_1=/opt/intf8180
  TOMCAT_HOME_2=/opt/intf8280

  echo MEMBER_VERSION=$MEMBER_VERSION
  echo TOMCAT_HOME_1=$TOMCAT_HOME_1
  echo TOMCAT_HOME_2=$TOMCAT_HOME_2
fi