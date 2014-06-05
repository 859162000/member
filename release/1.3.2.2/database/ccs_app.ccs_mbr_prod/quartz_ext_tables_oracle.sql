/*==============================================================*/
/* DBMS name:      ORACLE Version 11g                           */
/* Created on:     2013/11/26 17:21:17                          */
/*==============================================================*/



create sequence S_T_CODE_LIST;

create sequence S_T_JOB_HISTORY;

/*==============================================================*/
/* Table: T_CODE_LIST                                           */
/*==============================================================*/
create table T_CODE_LIST 
(
   CODE_LIST_ID         NUMBER(38)           not null,
   TYPE_ID              VARCHAR2(200)        not null,
   CODE                 VARCHAR2(40)         not null,
   NAME                 VARCHAR2(200)        not null,
   SORT_INDEX           NUMBER(8)            default 0 not null,
   DESCRIPTION          VARCHAR2(1000),
   DELETED              CHAR(1)              default '0' not null,
   constraint PK_T_CODE_LIST primary key (CODE_LIST_ID)
);

/*==============================================================*/
/* Table: T_INSTANCE                                            */
/*==============================================================*/
create table T_INSTANCE 
(
   INSTANCE_ID          VARCHAR(200)         not null,
   MASTER               CHAR(1)              not null,
   ADDRESS              VARCHAR(4000)        not null,
   PING_TIME            TIMESTAMP(6)         not null,
   STARTUP_TIME         TIMESTAMP(6)         not null,
   SHUTDOWN_TIME        TIMESTAMP(6),
   constraint PK_T_INSTANCE primary key (INSTANCE_ID),
   constraint UK_T_INSTANCE_ADDRESS unique (ADDRESS)
);

comment on table T_INSTANCE is
'Store the current running auartz instances';

comment on column T_INSTANCE.INSTANCE_ID is
'Quartz InstanceId';

comment on column T_INSTANCE.MASTER is
'1 master node,  0 slave node';

comment on column T_INSTANCE.ADDRESS is
'Job group';

comment on column T_INSTANCE.PING_TIME is
'Node last update time.';

comment on column T_INSTANCE.STARTUP_TIME is
'Current start time';

comment on column T_INSTANCE.SHUTDOWN_TIME is
'Jobhub server will set this to current time, if it has been shutdown properly.';

/*==============================================================*/
/* Table: T_JOB_HISTORY                                         */
/*==============================================================*/
create table T_JOB_HISTORY 
(
   JOB_HISTORY_ID       INTEGER              not null,
   PHASE                VARCHAR2(128)        not null,
   INSTANCE_ID          VARCHAR(200)         not null,
   JOB_NAME             VARCHAR(200)         not null,
   JOB_GROUP            VARCHAR(200)         not null,
   TRIGGER_NAME         VARCHAR(200)         not null,
   TRIGGER_GROUP        VARCHAR(200)         not null,
   TRIGGER_DATA         VARCHAR2(4000),
   JOB_DATA             VARCHAR2(4000),
   MESSAGE              VARCHAR2(4000),
   RECOVERING           CHAR(1)              default '0',
   REFIRE_COUNT         NUMBER(8),
   NEXT_FIRE_TIME       TIMESTAMP(6),
   SCHEDULED_TIME       TIMESTAMP(6)         not null,
   LOGGING_TIME         TIMESTAMP(6)         default SYSDATE not null,
   constraint PK_T_JOB_HISTORY primary key (JOB_HISTORY_ID)
);

comment on table T_JOB_HISTORY is
'Quartz Job execution history logging table';

comment on column T_JOB_HISTORY.JOB_HISTORY_ID is
'Primary key, created by sequence s_qrtz_job_history';

comment on column T_JOB_HISTORY.PHASE is
'Job execution phase, includes: ''TO_BE_FIRED'',''WAS_VETOED'',''FAILED,''SUCCESS''';

comment on column T_JOB_HISTORY.INSTANCE_ID is
'Quartz Schedulor InstanceId';

comment on column T_JOB_HISTORY.JOB_NAME is
'Job name';

comment on column T_JOB_HISTORY.JOB_GROUP is
'Job group';

comment on column T_JOB_HISTORY.TRIGGER_NAME is
'Trigger name';

comment on column T_JOB_HISTORY.TRIGGER_GROUP is
'Trigger group';

comment on column T_JOB_HISTORY.TRIGGER_DATA is
'Trigger.getJobDataMap() from the JobExecutionContex in JSON format.';

comment on column T_JOB_HISTORY.JOB_DATA is
'JobDetail.getJobDataMap() from the JobExecutionContex in JSON format.';

comment on column T_JOB_HISTORY.MESSAGE is
' ';

comment on column T_JOB_HISTORY.RECOVERING is
'The job is recovering. When the JobDetail.requestRecover set true, and  is recovering now.';

comment on column T_JOB_HISTORY.SCHEDULED_TIME is
'Scheduled fire time';

comment on column T_JOB_HISTORY.LOGGING_TIME is
'Current logging time';

/*==============================================================*/
/* Index: IDX_JOB_HISTORY_JOB_KEY                               */
/*==============================================================*/
create index IDX_JOB_HISTORY_JOB_KEY on T_JOB_HISTORY (
   JOB_NAME ASC,
   JOB_GROUP ASC
);

/*==============================================================*/
/* Index: IDX_JOB_HISTORY_LOGGING_TIME                          */
/*==============================================================*/
create index IDX_JOB_HISTORY_LOGGING_TIME on T_JOB_HISTORY (
   LOGGING_TIME ASC
);

