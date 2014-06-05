#!/bin/bash

. config.sh

rm -vrf $TOMCAT_HOME_1/webapps/CCSMbrIntf
cp -vf archives/member-intf-$MEMBER_VERSION.war $TOMCAT_HOME_1/webapps/CCSMbrIntf.war
rm -vrf $TOMCAT_HOME_2/webapps/CCSMbrIntf
cp -vf archives/member-intf-$MEMBER_VERSION.war $TOMCAT_HOME_2/webapps/CCSMbrIntf.war

