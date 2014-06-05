@echo off
title dw_sync
color 2

rem schtasks /delete /tn "dw_sync"
rem schtasks /query

schtasks /create /sc daily /st 06:10:00 /tn "dw_sync" /tr C:\BaseETL\CCS_MBR_DW_ETL\full_sync\BIN\mbr_dw_full_sync.BAT /ru administrator