REM@echo off


SET JAVA_HOME=C:\jdk1.6.0_35\jdk1.6.0_35\jre


rem 获取时间
set dt=%date:~0,10%
set dy=%dt:~0,4%
set dm=%dt:~5,2%
set dd=%dt:~8,2%
set cdt=%dy%-%dm%-%dd%
rem 获取时分秒
set hh=%time:~0,2%
set mi=%time:~3,2%
set ss=%time:~6,2%
set ctm=%hh%:%mi%:%ss%


rem 判断是否为重跑，重跑参数为R，正常跑的参数为N
rem 输入运行参数为R的时候，需要输入重跑的日期，日期格式为yyyy-mm-dd
rem 输入运行参数为N的时候，不需要输入日期，日期会根据系统时间生成


if "%1" == "" goto ST


goto RERUN



:ST
rem 判断时间类型
if %dm%-%dd%==01-01 goto L01
if %dm%-%dd%==02-01 goto L02
if %dm%-%dd%==03-01 goto L07
if %dm%-%dd%==04-01 goto L02
if %dm%-%dd%==05-01 goto L04
if %dm%-%dd%==06-01 goto L02
if %dm%-%dd%==07-01 goto L04
if %dm%-%dd%==08-01 goto L02
if %dm%-%dd%==09-01 goto L02
if %dm%-%dd%==10-01 goto L05
if %dm%-%dd%==11-01 goto L03
if %dm%-%dd%==12-01 goto L06

rem 判断日类型,分两种类型,10以内和10
if %dd%==02 goto L10
if %dd%==03 goto L10
if %dd%==04 goto L10
if %dd%==05 goto L10
if %dd%==06 goto L10
if %dd%==07 goto L10
if %dd%==08 goto L10
if %dd%==09 goto L10
if %dd%==10 goto L11
set /A dd=dd-1
set dt=%dy%-%dm%-%dd%
goto END

rem 当时间为单数且不为1的情况下
:L10
set /A dd=%dd:~1,1%-1
set dt=%dy%-%dm%-0%dd%
set dd=0%dd%
goto END

rem 当时间为10的情况下
:L11
set dt=%dy%-%dm%-09
set dd=09
goto END

:L02
set /A dm=%dm:~1,1%-1
set dt=%dy%-0%dm%-31
set dm=0%dm%
set dd=31
goto END
:L04
set /A dm=dm-1
set dt=%dy%-0%dm%-30
set dm=0%dm%
set dd=30
goto END

:L05
set dt=%dy%-09-30
set dm=09
set dd=30
goto END
:L03
set dt=%dy%-10-31
set dm=10
set dd=31
goto END
:L06
set dt=%dy%-11-30
set dm=11
set dd=30
goto END
:L01
set /A dy=dy-1
set dt=%dy%-12-31
set dm=12
set dd=31
goto END

:L07
set /A "dd=dy%%4"
if not %dd%==0 goto L08
set /A "dd=dy%%100"
if not %dd%==0 goto L09
set /A "dd=dy%%400"
if %dd%==0 goto L09
:L08
set dt=%dy%-02-28
set dm=02
set dd=28
goto END
:L09
set dt=%dy%-02-29
set dm=02
set dd=29
goto END

:RERUN
set dt=%2
goto END

:END

::C:\BaseETL\Kettle\Kitchen.bat /file:C:\BaseETL\CCS_MBR_DW_ETL\full_sync\JOB\full_sync.kjb /level:Detailed > C:\BaseETL\CCS_MBR_DW_ETL\full_sync\log\full_sync_%cdt%.txt
C:\BaseETL\Kettle\Kitchen.bat /file:C:\BaseETL\CCS_MBR_DW_ETL\full_sync\JOB\full_sync.kjb /param:CHECKDATE=%dt% /level:Detailed > C:\BaseETL\CCS_MBR_DW_ETL\full_sync\log\full_sync_%cdt%.txt