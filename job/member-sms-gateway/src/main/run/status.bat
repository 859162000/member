rem start client (*_*)... 
@echo off

netstat -n -a | find /c ":9123" > a_come_scan_port_temp.txt

FOR /F %%i in (a_come_scan_port_temp.txt) do set row_num=%%i

del a_come_scan_port_temp.txt

if %row_num% EQU 0 goto notstart

telnet 127.0.0.1 9123

goto end

:notstart

echo [LOG] COMS'Client not start, pleaase run start.bat

:end

pause;