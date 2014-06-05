------------------------------------------------------------------
--  TABLE T_SMS_GATEWAY_ENGINE_ERROR_LOG
------------------------------------------------------------------

CREATE TABLE t_sms_gateway_engine_error_log
(
   log_id        NUMBER NOT NULL,
   error_msg     CLOB,
   create_time   TIMESTAMP (6)
);

COMMENT ON COLUMN t_sms_gateway_engine_error_log.log_id IS
   '引擎日志编号';

COMMENT ON COLUMN t_sms_gateway_engine_error_log.error_msg IS
   '引擎异常信息';


------------------------------------------------------------------
--  TABLE T_SMS_GATEWAY_ENGINE_LOG
------------------------------------------------------------------

CREATE TABLE t_sms_gateway_engine_log
(
   log_id               NUMBER NOT NULL,
   engine_name          VARCHAR2 (100 BYTE),
   delivery_msg         VARCHAR2 (1000 BYTE),
   task_name            VARCHAR2 (100 BYTE),
   task_state           NUMBER (1),
   task_create_time     TIMESTAMP (6),
   task_complete_time   TIMESTAMP (6)
);

ALTER TABLE t_sms_gateway_engine_log
  ADD PRIMARY KEY (log_id);

COMMENT ON COLUMN t_sms_gateway_engine_log.log_id IS '引擎日志编号';

COMMENT ON COLUMN t_sms_gateway_engine_log.engine_name IS '引擎名称';

COMMENT ON COLUMN t_sms_gateway_engine_log.delivery_msg IS
   '从MQ中获取的消息原始内容';

COMMENT ON COLUMN t_sms_gateway_engine_log.task_name IS
   '引擎调用的任务名称';

COMMENT ON COLUMN t_sms_gateway_engine_log.task_state IS
   '任务执行状态：0 未执行 1 执行完成 -1 执行失败';

COMMENT ON COLUMN t_sms_gateway_engine_log.task_create_time IS
   '任务启动时间';

COMMENT ON COLUMN t_sms_gateway_engine_log.task_complete_time IS
   '任务完成时间';


------------------------------------------------------------------
--  TABLE T_SMS_GATEWAY_SEND_LOG
------------------------------------------------------------------

CREATE TABLE t_sms_gateway_send_log
(
   send_id           NUMBER NOT NULL,
   log_id            NUMBER,
   engine_name       VARCHAR2 (100 BYTE),
   task_name         VARCHAR2 (100 BYTE),
   mobile_no         VARCHAR2 (100 BYTE),
   send_msg          VARCHAR2 (1000 BYTE),
   service_up        VARCHAR2 (500 BYTE),
   sp_number         VARCHAR2 (100 BYTE),
   link_id           VARCHAR2 (100 BYTE),
   system_id         VARCHAR2 (100 BYTE),
   settle_id         VARCHAR2 (100 BYTE),
   channel_id        VARCHAR2 (100 BYTE),
   platform          VARCHAR2 (200 BYTE),
   add_queue_state   NUMBER (1),
   add_queue_time    TIMESTAMP (6),
   create_time       TIMESTAMP (6)
);

ALTER TABLE t_sms_gateway_send_log
  ADD PRIMARY KEY (send_id);

COMMENT ON COLUMN t_sms_gateway_send_log.send_id IS '发送日志编号';

COMMENT ON COLUMN t_sms_gateway_send_log.log_id IS '引擎日志编号';

COMMENT ON COLUMN t_sms_gateway_send_log.engine_name IS '引擎名称';

COMMENT ON COLUMN t_sms_gateway_send_log.task_name IS '任务名称';

COMMENT ON COLUMN t_sms_gateway_send_log.mobile_no IS '用户手机号码';

COMMENT ON COLUMN t_sms_gateway_send_log.send_msg IS
   '发送给用户的短信内容';

COMMENT ON COLUMN t_sms_gateway_send_log.sp_number IS 'SP接入号码';

COMMENT ON COLUMN t_sms_gateway_send_log.link_id IS '队列LINKID';

COMMENT ON COLUMN t_sms_gateway_send_log.system_id IS '系统编号';

COMMENT ON COLUMN t_sms_gateway_send_log.settle_id IS '商家结算编码';

COMMENT ON COLUMN t_sms_gateway_send_log.channel_id IS '渠道编码';

COMMENT ON COLUMN t_sms_gateway_send_log.platform IS '平台';

COMMENT ON COLUMN t_sms_gateway_send_log.add_queue_state IS
   '将短信加入下行短信队列状态：0 未添加 1 添加成功 -1 添加失败';

COMMENT ON COLUMN t_sms_gateway_send_log.add_queue_time IS
   '加入队列时间';


------------------------------------------------------------------
--  TABLE T_SMS_GATEWAY_TASK_ERROR_LOG
------------------------------------------------------------------

CREATE TABLE t_sms_gateway_task_error_log
(
   log_id        NUMBER NOT NULL,
   error_msg     CLOB,
   create_time   TIMESTAMP (6)
);

COMMENT ON COLUMN t_sms_gateway_task_error_log.log_id IS '发送日志编号';

COMMENT ON COLUMN t_sms_gateway_task_error_log.error_msg IS
   '发送异常信息';


------------------------------------------------------------------
--  SEQUENCE S_T_SMS_GATEWAY_ENGINE_LOG
------------------------------------------------------------------

CREATE SEQUENCE s_t_sms_gateway_engine_log START WITH 1
                                           INCREMENT BY 1
                                           MAXVALUE 9999999999999999999999999999
                                           NOMINVALUE
                                           NOORDER
                                           NOCYCLE
                                           CACHE 20;


------------------------------------------------------------------
--  SEQUENCE S_T_SMS_GATEWAY_SEND_LOG
------------------------------------------------------------------

CREATE SEQUENCE s_t_sms_gateway_send_log START WITH 1
                                         INCREMENT BY 1
                                         MAXVALUE 9999999999999999999999999999
                                         NOMINVALUE
                                         NOORDER
                                         NOCYCLE
                                         CACHE 20;




