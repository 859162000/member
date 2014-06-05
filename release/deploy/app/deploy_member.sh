#!/bin/bash

. config.sh

rm -vrf $TOMCAT_HOME/webapps/member
cp -vf archives/member-web-$MEMBER_VERSION.war $TOMCAT_HOME/webapps/member.war

