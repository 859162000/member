

insert into T_DIMTYPEDEF (TYPEID, TYPENAME, ISDELETE, UPDATETIME)
values (2011, '电子渠道偏好', 0, null);
insert into T_DIMTYPEDEF (TYPEID, TYPENAME, ISDELETE, UPDATETIME)
values (2012, '观影时段', 0, null);
insert into T_DIMTYPEDEF (TYPEID, TYPENAME, ISDELETE, UPDATETIME)
values (2013, '波次响应类型', 0, null);
insert into T_DIMTYPEDEF (TYPEID, TYPENAME, ISDELETE, UPDATETIME)
values (2014, '节假日', 0, null);
insert into T_DIMTYPEDEF (TYPEID, TYPENAME, ISDELETE, UPDATETIME)
values (2015, '级别变更类型', 0, null);
--insert into T_DIMTYPEDEF (TYPEID, TYPENAME, ISDELETE, UPDATETIME)
--values (2016, '是否类型', 0, null);
commit;


insert into T_DIMDEF (CODE, NAME, SEQID, PARENT, VALIDSTART, VALIDEND, TYPEID, ISDELETE, UPDATETIME, CREATE_BY, UPDATE_DATE, UPDATE_BY, CREATE_DATE, VERSION, DESCRIPTION)
values ('1', '线上',  S_T_DIMDEF.nextval, null, null, null, 2011, 0, null, null, null, null, to_timestamp('22-11-2013 17:24:35.342473', 'dd-mm-yyyy hh24:mi:ss.ff'), 0, null);
insert into T_DIMDEF (CODE, NAME, SEQID, PARENT, VALIDSTART, VALIDEND, TYPEID, ISDELETE, UPDATETIME, CREATE_BY, UPDATE_DATE, UPDATE_BY, CREATE_DATE, VERSION, DESCRIPTION)
values ('2', '线下',  S_T_DIMDEF.nextval, null, null, null, 2011, 0, null, null, null, null, to_timestamp('22-11-2013 17:24:35.836114', 'dd-mm-yyyy hh24:mi:ss.ff'), 0, null);
insert into T_DIMDEF (CODE, NAME, SEQID, PARENT, VALIDSTART, VALIDEND, TYPEID, ISDELETE, UPDATETIME, CREATE_BY, UPDATE_DATE, UPDATE_BY, CREATE_DATE, VERSION, DESCRIPTION)
values ('3', '未知',  S_T_DIMDEF.nextval, null, null, null, 2011, 0, null, null, null, null, to_timestamp('22-11-2013 17:24:35.938264', 'dd-mm-yyyy hh24:mi:ss.ff'), 0, null);
insert into T_DIMDEF (CODE, NAME, SEQID, PARENT, VALIDSTART, VALIDEND, TYPEID, ISDELETE, UPDATETIME, CREATE_BY, UPDATE_DATE, UPDATE_BY, CREATE_DATE, VERSION, DESCRIPTION)
values ('1', '上午',  S_T_DIMDEF.nextval, null, null, null, 2012, 0, null, null, null, null, to_timestamp('22-11-2013 17:24:35.975450', 'dd-mm-yyyy hh24:mi:ss.ff'), 0, null);
insert into T_DIMDEF (CODE, NAME, SEQID, PARENT, VALIDSTART, VALIDEND, TYPEID, ISDELETE, UPDATETIME, CREATE_BY, UPDATE_DATE, UPDATE_BY, CREATE_DATE, VERSION, DESCRIPTION)
values ('2', '下午',  S_T_DIMDEF.nextval, null, null, null, 2012, 0, null, null, null, null, to_timestamp('22-11-2013 17:24:36.024964', 'dd-mm-yyyy hh24:mi:ss.ff'), 0, null);
insert into T_DIMDEF (CODE, NAME, SEQID, PARENT, VALIDSTART, VALIDEND, TYPEID, ISDELETE, UPDATETIME, CREATE_BY, UPDATE_DATE, UPDATE_BY, CREATE_DATE, VERSION, DESCRIPTION)
values ('3', '夜间',  S_T_DIMDEF.nextval, null, null, null, 2012, 0, null, null, null, null, to_timestamp('22-11-2013 17:24:36.069021', 'dd-mm-yyyy hh24:mi:ss.ff'), 0, null);
insert into T_DIMDEF (CODE, NAME, SEQID, PARENT, VALIDSTART, VALIDEND, TYPEID, ISDELETE, UPDATETIME, CREATE_BY, UPDATE_DATE, UPDATE_BY, CREATE_DATE, VERSION, DESCRIPTION)
values ('1', '推荐响应',  S_T_DIMDEF.nextval, null, null, null, 2013, 0, null, null, null, null, to_timestamp('22-11-2013 17:24:36.099567', 'dd-mm-yyyy hh24:mi:ss.ff'), 0, null);
insert into T_DIMDEF (CODE, NAME, SEQID, PARENT, VALIDSTART, VALIDEND, TYPEID, ISDELETE, UPDATETIME, CREATE_BY, UPDATE_DATE, UPDATE_BY, CREATE_DATE, VERSION, DESCRIPTION)
values ('2', '关联响应',  S_T_DIMDEF.nextval, null, null, null, 2013, 0, null, null, null, null, to_timestamp('22-11-2013 17:24:36.133161', 'dd-mm-yyyy hh24:mi:ss.ff'), 0, null);
insert into T_DIMDEF (CODE, NAME, SEQID, PARENT, VALIDSTART, VALIDEND, TYPEID, ISDELETE, UPDATETIME, CREATE_BY, UPDATE_DATE, UPDATE_BY, CREATE_DATE, VERSION, DESCRIPTION)
values ('chunjie', '春节',  S_T_DIMDEF.nextval, null, null, null, 2014, 0, null, null, null, null, to_timestamp('22-11-2013 17:24:36.213018', 'dd-mm-yyyy hh24:mi:ss.ff'), 0, null);
insert into T_DIMDEF (CODE, NAME, SEQID, PARENT, VALIDSTART, VALIDEND, TYPEID, ISDELETE, UPDATETIME, CREATE_BY, UPDATE_DATE, UPDATE_BY, CREATE_DATE, VERSION, DESCRIPTION)
values ('wuyi', '五一',  S_T_DIMDEF.nextval, null, null, null, 2014, 0, null, null, null, null, to_timestamp('22-11-2013 17:24:36.245449', 'dd-mm-yyyy hh24:mi:ss.ff'), 0, null);
insert into T_DIMDEF (CODE, NAME, SEQID, PARENT, VALIDSTART, VALIDEND, TYPEID, ISDELETE, UPDATETIME, CREATE_BY, UPDATE_DATE, UPDATE_BY, CREATE_DATE, VERSION, DESCRIPTION)
values ('shiyi', '十一',  S_T_DIMDEF.nextval, null, null, null, 2014, 0, null, null, null, null, to_timestamp('22-11-2013 17:24:36.272152', 'dd-mm-yyyy hh24:mi:ss.ff'), 0, null);
insert into T_DIMDEF (CODE, NAME, SEQID, PARENT, VALIDSTART, VALIDEND, TYPEID, ISDELETE, UPDATETIME, CREATE_BY, UPDATE_DATE, UPDATE_BY, CREATE_DATE, VERSION, DESCRIPTION)
values ('duanwu', '端午',  S_T_DIMDEF.nextval, null, null, null, 2014, 0, null, null, null, null, to_timestamp('22-11-2013 17:24:36.337483', 'dd-mm-yyyy hh24:mi:ss.ff'), 0, null);
insert into T_DIMDEF (CODE, NAME, SEQID, PARENT, VALIDSTART, VALIDEND, TYPEID, ISDELETE, UPDATETIME, CREATE_BY, UPDATE_DATE, UPDATE_BY, CREATE_DATE, VERSION, DESCRIPTION)
values ('yuandan', '元旦',  S_T_DIMDEF.nextval, null, null, null, 2014, 0, null, null, null, null, to_timestamp('22-11-2013 17:24:36.365840', 'dd-mm-yyyy hh24:mi:ss.ff'), 0, null);
insert into T_DIMDEF (CODE, NAME, SEQID, PARENT, VALIDSTART, VALIDEND, TYPEID, ISDELETE, UPDATETIME, CREATE_BY, UPDATE_DATE, UPDATE_BY, CREATE_DATE, VERSION, DESCRIPTION)
values ('zhongqiu', '中秋',  S_T_DIMDEF.nextval, null, null, null, 2014, 0, null, null, null, null, to_timestamp('22-11-2013 17:24:36.395321', 'dd-mm-yyyy hh24:mi:ss.ff'), 0, null);
insert into T_DIMDEF (CODE, NAME, SEQID, PARENT, VALIDSTART, VALIDEND, TYPEID, ISDELETE, UPDATETIME, CREATE_BY, UPDATE_DATE, UPDATE_BY, CREATE_DATE, VERSION, DESCRIPTION)
values ('qingming', '清明',  S_T_DIMDEF.nextval, null, null, null, 2014, 0, null, null, null, null, to_timestamp('22-11-2013 17:24:36.427727', 'dd-mm-yyyy hh24:mi:ss.ff'), 0, null);
insert into T_DIMDEF (CODE, NAME, SEQID, PARENT, VALIDSTART, VALIDEND, TYPEID, ISDELETE, UPDATETIME, CREATE_BY, UPDATE_DATE, UPDATE_BY, CREATE_DATE, VERSION, DESCRIPTION)
values ('shengdanjie', '圣诞节',  S_T_DIMDEF.nextval, null, null, null, 2014, 0, null, null, null, null, to_timestamp('22-11-2013 17:24:36.427727', 'dd-mm-yyyy hh24:mi:ss.ff'), 0, null);
insert into T_DIMDEF (CODE, NAME, SEQID, PARENT, VALIDSTART, VALIDEND, TYPEID, ISDELETE, UPDATETIME, CREATE_BY, UPDATE_DATE, UPDATE_BY, CREATE_DATE, VERSION, DESCRIPTION)
values ('guanggunjie', '光棍节',  S_T_DIMDEF.nextval, null, null, null, 2014, 0, null, null, null, null, to_timestamp('22-11-2013 17:24:36.427727', 'dd-mm-yyyy hh24:mi:ss.ff'), 0, null);
insert into T_DIMDEF (CODE, NAME, SEQID, PARENT, VALIDSTART, VALIDEND, TYPEID, ISDELETE, UPDATETIME, CREATE_BY, UPDATE_DATE, UPDATE_BY, CREATE_DATE, VERSION, DESCRIPTION)
values ('qingrenjie', '情人节',  S_T_DIMDEF.nextval, null, null, null, 2014, 0, null, null, null, null, to_timestamp('22-11-2013 17:24:36.427727', 'dd-mm-yyyy hh24:mi:ss.ff'), 0, null);
insert into T_DIMDEF (CODE, NAME, SEQID, PARENT, VALIDSTART, VALIDEND, TYPEID, ISDELETE, UPDATETIME, CREATE_BY, UPDATE_DATE, UPDATE_BY, CREATE_DATE, VERSION, DESCRIPTION)
values ('yurenjie', '愚人节',  S_T_DIMDEF.nextval, null, null, null, 2014, 0, null, null, null, null, to_timestamp('22-11-2013 17:24:36.427727', 'dd-mm-yyyy hh24:mi:ss.ff'), 0, null);
insert into T_DIMDEF (CODE, NAME, SEQID, PARENT, VALIDSTART, VALIDEND, TYPEID, ISDELETE, UPDATETIME, CREATE_BY, UPDATE_DATE, UPDATE_BY, CREATE_DATE, VERSION, DESCRIPTION)
values ('baiseqingrenjie', '白色情人节',  S_T_DIMDEF.nextval, null, null, null, 2014, 0, null, null, null, null, to_timestamp('22-11-2013 17:24:36.427727', 'dd-mm-yyyy hh24:mi:ss.ff'), 0, null);
insert into T_DIMDEF (CODE, NAME, SEQID, PARENT, VALIDSTART, VALIDEND, TYPEID, ISDELETE, UPDATETIME, CREATE_BY, UPDATE_DATE, UPDATE_BY, CREATE_DATE, VERSION, DESCRIPTION)
values ('fuhuojie', '复活节',  S_T_DIMDEF.nextval, null, null, null, 2014, 0, null, null, null, null, to_timestamp('22-11-2013 17:24:36.427727', 'dd-mm-yyyy hh24:mi:ss.ff'), 0, null);
insert into T_DIMDEF (CODE, NAME, SEQID, PARENT, VALIDSTART, VALIDEND, TYPEID, ISDELETE, UPDATETIME, CREATE_BY, UPDATE_DATE, UPDATE_BY, CREATE_DATE, VERSION, DESCRIPTION)
values ('wanshengjie', '万圣节',  S_T_DIMDEF.nextval, null, null, null, 2014, 0, null, null, null, null, to_timestamp('22-11-2013 17:24:36.427727', 'dd-mm-yyyy hh24:mi:ss.ff'), 0, null);
insert into T_DIMDEF (CODE, NAME, SEQID, PARENT, VALIDSTART, VALIDEND, TYPEID, ISDELETE, UPDATETIME, CREATE_BY, UPDATE_DATE, UPDATE_BY, CREATE_DATE, VERSION, DESCRIPTION)
values ('UP', '升级',  S_T_DIMDEF.nextval, null, null, null, 2015, 0, null, null, null, null, to_timestamp('25-11-2013 12:36:58.295856', 'dd-mm-yyyy hh24:mi:ss.ff'), 0, null);
insert into T_DIMDEF (CODE, NAME, SEQID, PARENT, VALIDSTART, VALIDEND, TYPEID, ISDELETE, UPDATETIME, CREATE_BY, UPDATE_DATE, UPDATE_BY, CREATE_DATE, VERSION, DESCRIPTION)
values ('DOWN', '降级',  S_T_DIMDEF.nextval, null, null, null, 2015, 0, null, null, null, null, to_timestamp('25-11-2013 12:36:58.913557', 'dd-mm-yyyy hh24:mi:ss.ff'), 0, null);
--insert into T_DIMDEF (CODE, NAME, SEQID, PARENT, VALIDSTART, VALIDEND, TYPEID, ISDELETE, UPDATETIME, CREATE_BY, UPDATE_DATE, UPDATE_BY, CREATE_DATE, VERSION, DESCRIPTION)
--values ('0', '否',  S_T_DIMDEF.nextval, null, null, null, 2016, 0, null, null, null, null, to_timestamp('26-11-2013 11:39:33.673401', 'dd-mm-yyyy hh24:mi:ss.ff'), 0, null);
--insert into T_DIMDEF (CODE, NAME, SEQID, PARENT, VALIDSTART, VALIDEND, TYPEID, ISDELETE, UPDATETIME, CREATE_BY, UPDATE_DATE, UPDATE_BY, CREATE_DATE, VERSION, DESCRIPTION)
--values ('1', '是',  S_T_DIMDEF.nextval, null, null, null, 2016, 0, null, null, null, null, to_timestamp('26-11-2013 11:39:33.746880', 'dd-mm-yyyy hh24:mi:ss.ff'), 0, null);
commit;

