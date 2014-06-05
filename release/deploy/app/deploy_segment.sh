#!/bin/bash

. config.sh

rm -vrf $TOMCAT_HOME/webapps/segment
cp -vf archives/segment-main-$MEMBER_VERSION.war $TOMCAT_HOME/webapps/segment.war 
