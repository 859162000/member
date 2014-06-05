#!/bin/bash

. config.sh

rm -vrf $TOMCAT_HOME/webapps/jobhub
cp -vf archives/member-jobhub-$MEMBER_VERSION.war $TOMCAT_HOME/webapps/jobhub.war

