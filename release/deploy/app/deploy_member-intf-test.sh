#!/bin/bash

. config.sh

rm -vrf $TOMCAT_HOME/webapps/CCSMbrIntf
cp -vf archives/member-intf-$MEMBER_VERSION.war $TOMCAT_HOME/webapps/CCSMbrIntf.war

