#!/bin/bash

. config.sh

cp -vf archives/segment-module-$MEMBER_VERSION.jar $TOMCAT_HOME/modules/segment-module/segment-module.jar

