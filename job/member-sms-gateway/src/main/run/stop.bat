rem start SMS GATEWAY (*_*)... 
@echo off

if "%JAVA_HOME%" == "" goto error

echo ************** ENV:JAVA_HOME **************
echo [LOG] JAVA_HOME : %JAVA_HOME%
echo ************** ENV:JAVA_HOME **************

echo ************** ENV:WORK_HOME **************
echo [LOG] WORK_HOME : %cd%
echo ************** ENV:WORK_HOME **************

set gateway_home=%cd%

netstat -n -a | find /c ":9123" > a_come_scan_port_temp.txt

FOR /F %%i in (a_come_scan_port_temp.txt) do set row_num=%%i

del a_come_scan_port_temp.txt

if %row_num% EQU 0 goto started

echo [LOG] SMS GATEWAY not start, start it .... 

java -cp ./lib/ant-1.9.2.jar;./lib/ant-launcher-1.9.2.jar org.apache.tools.ant.Main -f stop.xml -Djvm.xmx=512M -Dgateway_home=%gateway_home% -Dmain.arg=start

goto :end

:started

echo [LOG] SMS GATEWAY has bean started,please run stats.bat view run detail

goto end

:end

pause;