

insert into T_DIMTYPEDEF (TYPEID, TYPENAME, ISDELETE, UPDATETIME)
values (2011, '��������ƫ��', 0, null);
insert into T_DIMTYPEDEF (TYPEID, TYPENAME, ISDELETE, UPDATETIME)
values (2012, '��Ӱʱ��', 0, null);
insert into T_DIMTYPEDEF (TYPEID, TYPENAME, ISDELETE, UPDATETIME)
values (2013, '������Ӧ����', 0, null);
insert into T_DIMTYPEDEF (TYPEID, TYPENAME, ISDELETE, UPDATETIME)
values (2014, '�ڼ���', 0, null);
insert into T_DIMTYPEDEF (TYPEID, TYPENAME, ISDELETE, UPDATETIME)
values (2015, '����������', 0, null);
--insert into T_DIMTYPEDEF (TYPEID, TYPENAME, ISDELETE, UPDATETIME)
--values (2016, '�Ƿ�����', 0, null);
commit;


insert into T_DIMDEF (CODE, NAME, SEQID, PARENT, VALIDSTART, VALIDEND, TYPEID, ISDELETE, UPDATETIME, CREATE_BY, UPDATE_DATE, UPDATE_BY, CREATE_DATE, VERSION, DESCRIPTION)
values ('1', '����',  S_T_DIMDEF.nextval, null, null, null, 2011, 0, null, null, null, null, to_timestamp('22-11-2013 17:24:35.342473', 'dd-mm-yyyy hh24:mi:ss.ff'), 0, null);
insert into T_DIMDEF (CODE, NAME, SEQID, PARENT, VALIDSTART, VALIDEND, TYPEID, ISDELETE, UPDATETIME, CREATE_BY, UPDATE_DATE, UPDATE_BY, CREATE_DATE, VERSION, DESCRIPTION)
values ('2', '����',  S_T_DIMDEF.nextval, null, null, null, 2011, 0, null, null, null, null, to_timestamp('22-11-2013 17:24:35.836114', 'dd-mm-yyyy hh24:mi:ss.ff'), 0, null);
insert into T_DIMDEF (CODE, NAME, SEQID, PARENT, VALIDSTART, VALIDEND, TYPEID, ISDELETE, UPDATETIME, CREATE_BY, UPDATE_DATE, UPDATE_BY, CREATE_DATE, VERSION, DESCRIPTION)
values ('3', 'δ֪',  S_T_DIMDEF.nextval, null, null, null, 2011, 0, null, null, null, null, to_timestamp('22-11-2013 17:24:35.938264', 'dd-mm-yyyy hh24:mi:ss.ff'), 0, null);
insert into T_DIMDEF (CODE, NAME, SEQID, PARENT, VALIDSTART, VALIDEND, TYPEID, ISDELETE, UPDATETIME, CREATE_BY, UPDATE_DATE, UPDATE_BY, CREATE_DATE, VERSION, DESCRIPTION)
values ('1', '����',  S_T_DIMDEF.nextval, null, null, null, 2012, 0, null, null, null, null, to_timestamp('22-11-2013 17:24:35.975450', 'dd-mm-yyyy hh24:mi:ss.ff'), 0, null);
insert into T_DIMDEF (CODE, NAME, SEQID, PARENT, VALIDSTART, VALIDEND, TYPEID, ISDELETE, UPDATETIME, CREATE_BY, UPDATE_DATE, UPDATE_BY, CREATE_DATE, VERSION, DESCRIPTION)
values ('2', '����',  S_T_DIMDEF.nextval, null, null, null, 2012, 0, null, null, null, null, to_timestamp('22-11-2013 17:24:36.024964', 'dd-mm-yyyy hh24:mi:ss.ff'), 0, null);
insert into T_DIMDEF (CODE, NAME, SEQID, PARENT, VALIDSTART, VALIDEND, TYPEID, ISDELETE, UPDATETIME, CREATE_BY, UPDATE_DATE, UPDATE_BY, CREATE_DATE, VERSION, DESCRIPTION)
values ('3', 'ҹ��',  S_T_DIMDEF.nextval, null, null, null, 2012, 0, null, null, null, null, to_timestamp('22-11-2013 17:24:36.069021', 'dd-mm-yyyy hh24:mi:ss.ff'), 0, null);
insert into T_DIMDEF (CODE, NAME, SEQID, PARENT, VALIDSTART, VALIDEND, TYPEID, ISDELETE, UPDATETIME, CREATE_BY, UPDATE_DATE, UPDATE_BY, CREATE_DATE, VERSION, DESCRIPTION)
values ('1', '�Ƽ���Ӧ',  S_T_DIMDEF.nextval, null, null, null, 2013, 0, null, null, null, null, to_timestamp('22-11-2013 17:24:36.099567', 'dd-mm-yyyy hh24:mi:ss.ff'), 0, null);
insert into T_DIMDEF (CODE, NAME, SEQID, PARENT, VALIDSTART, VALIDEND, TYPEID, ISDELETE, UPDATETIME, CREATE_BY, UPDATE_DATE, UPDATE_BY, CREATE_DATE, VERSION, DESCRIPTION)
values ('2', '������Ӧ',  S_T_DIMDEF.nextval, null, null, null, 2013, 0, null, null, null, null, to_timestamp('22-11-2013 17:24:36.133161', 'dd-mm-yyyy hh24:mi:ss.ff'), 0, null);
insert into T_DIMDEF (CODE, NAME, SEQID, PARENT, VALIDSTART, VALIDEND, TYPEID, ISDELETE, UPDATETIME, CREATE_BY, UPDATE_DATE, UPDATE_BY, CREATE_DATE, VERSION, DESCRIPTION)
values ('chunjie', '����',  S_T_DIMDEF.nextval, null, null, null, 2014, 0, null, null, null, null, to_timestamp('22-11-2013 17:24:36.213018', 'dd-mm-yyyy hh24:mi:ss.ff'), 0, null);
insert into T_DIMDEF (CODE, NAME, SEQID, PARENT, VALIDSTART, VALIDEND, TYPEID, ISDELETE, UPDATETIME, CREATE_BY, UPDATE_DATE, UPDATE_BY, CREATE_DATE, VERSION, DESCRIPTION)
values ('wuyi', '��һ',  S_T_DIMDEF.nextval, null, null, null, 2014, 0, null, null, null, null, to_timestamp('22-11-2013 17:24:36.245449', 'dd-mm-yyyy hh24:mi:ss.ff'), 0, null);
insert into T_DIMDEF (CODE, NAME, SEQID, PARENT, VALIDSTART, VALIDEND, TYPEID, ISDELETE, UPDATETIME, CREATE_BY, UPDATE_DATE, UPDATE_BY, CREATE_DATE, VERSION, DESCRIPTION)
values ('shiyi', 'ʮһ',  S_T_DIMDEF.nextval, null, null, null, 2014, 0, null, null, null, null, to_timestamp('22-11-2013 17:24:36.272152', 'dd-mm-yyyy hh24:mi:ss.ff'), 0, null);
insert into T_DIMDEF (CODE, NAME, SEQID, PARENT, VALIDSTART, VALIDEND, TYPEID, ISDELETE, UPDATETIME, CREATE_BY, UPDATE_DATE, UPDATE_BY, CREATE_DATE, VERSION, DESCRIPTION)
values ('duanwu', '����',  S_T_DIMDEF.nextval, null, null, null, 2014, 0, null, null, null, null, to_timestamp('22-11-2013 17:24:36.337483', 'dd-mm-yyyy hh24:mi:ss.ff'), 0, null);
insert into T_DIMDEF (CODE, NAME, SEQID, PARENT, VALIDSTART, VALIDEND, TYPEID, ISDELETE, UPDATETIME, CREATE_BY, UPDATE_DATE, UPDATE_BY, CREATE_DATE, VERSION, DESCRIPTION)
values ('yuandan', 'Ԫ��',  S_T_DIMDEF.nextval, null, null, null, 2014, 0, null, null, null, null, to_timestamp('22-11-2013 17:24:36.365840', 'dd-mm-yyyy hh24:mi:ss.ff'), 0, null);
insert into T_DIMDEF (CODE, NAME, SEQID, PARENT, VALIDSTART, VALIDEND, TYPEID, ISDELETE, UPDATETIME, CREATE_BY, UPDATE_DATE, UPDATE_BY, CREATE_DATE, VERSION, DESCRIPTION)
values ('zhongqiu', '����',  S_T_DIMDEF.nextval, null, null, null, 2014, 0, null, null, null, null, to_timestamp('22-11-2013 17:24:36.395321', 'dd-mm-yyyy hh24:mi:ss.ff'), 0, null);
insert into T_DIMDEF (CODE, NAME, SEQID, PARENT, VALIDSTART, VALIDEND, TYPEID, ISDELETE, UPDATETIME, CREATE_BY, UPDATE_DATE, UPDATE_BY, CREATE_DATE, VERSION, DESCRIPTION)
values ('qingming', '����',  S_T_DIMDEF.nextval, null, null, null, 2014, 0, null, null, null, null, to_timestamp('22-11-2013 17:24:36.427727', 'dd-mm-yyyy hh24:mi:ss.ff'), 0, null);
insert into T_DIMDEF (CODE, NAME, SEQID, PARENT, VALIDSTART, VALIDEND, TYPEID, ISDELETE, UPDATETIME, CREATE_BY, UPDATE_DATE, UPDATE_BY, CREATE_DATE, VERSION, DESCRIPTION)
values ('shengdanjie', 'ʥ����',  S_T_DIMDEF.nextval, null, null, null, 2014, 0, null, null, null, null, to_timestamp('22-11-2013 17:24:36.427727', 'dd-mm-yyyy hh24:mi:ss.ff'), 0, null);
insert into T_DIMDEF (CODE, NAME, SEQID, PARENT, VALIDSTART, VALIDEND, TYPEID, ISDELETE, UPDATETIME, CREATE_BY, UPDATE_DATE, UPDATE_BY, CREATE_DATE, VERSION, DESCRIPTION)
values ('guanggunjie', '�����',  S_T_DIMDEF.nextval, null, null, null, 2014, 0, null, null, null, null, to_timestamp('22-11-2013 17:24:36.427727', 'dd-mm-yyyy hh24:mi:ss.ff'), 0, null);
insert into T_DIMDEF (CODE, NAME, SEQID, PARENT, VALIDSTART, VALIDEND, TYPEID, ISDELETE, UPDATETIME, CREATE_BY, UPDATE_DATE, UPDATE_BY, CREATE_DATE, VERSION, DESCRIPTION)
values ('qingrenjie', '���˽�',  S_T_DIMDEF.nextval, null, null, null, 2014, 0, null, null, null, null, to_timestamp('22-11-2013 17:24:36.427727', 'dd-mm-yyyy hh24:mi:ss.ff'), 0, null);
insert into T_DIMDEF (CODE, NAME, SEQID, PARENT, VALIDSTART, VALIDEND, TYPEID, ISDELETE, UPDATETIME, CREATE_BY, UPDATE_DATE, UPDATE_BY, CREATE_DATE, VERSION, DESCRIPTION)
values ('yurenjie', '���˽�',  S_T_DIMDEF.nextval, null, null, null, 2014, 0, null, null, null, null, to_timestamp('22-11-2013 17:24:36.427727', 'dd-mm-yyyy hh24:mi:ss.ff'), 0, null);
insert into T_DIMDEF (CODE, NAME, SEQID, PARENT, VALIDSTART, VALIDEND, TYPEID, ISDELETE, UPDATETIME, CREATE_BY, UPDATE_DATE, UPDATE_BY, CREATE_DATE, VERSION, DESCRIPTION)
values ('baiseqingrenjie', '��ɫ���˽�',  S_T_DIMDEF.nextval, null, null, null, 2014, 0, null, null, null, null, to_timestamp('22-11-2013 17:24:36.427727', 'dd-mm-yyyy hh24:mi:ss.ff'), 0, null);
insert into T_DIMDEF (CODE, NAME, SEQID, PARENT, VALIDSTART, VALIDEND, TYPEID, ISDELETE, UPDATETIME, CREATE_BY, UPDATE_DATE, UPDATE_BY, CREATE_DATE, VERSION, DESCRIPTION)
values ('fuhuojie', '�����',  S_T_DIMDEF.nextval, null, null, null, 2014, 0, null, null, null, null, to_timestamp('22-11-2013 17:24:36.427727', 'dd-mm-yyyy hh24:mi:ss.ff'), 0, null);
insert into T_DIMDEF (CODE, NAME, SEQID, PARENT, VALIDSTART, VALIDEND, TYPEID, ISDELETE, UPDATETIME, CREATE_BY, UPDATE_DATE, UPDATE_BY, CREATE_DATE, VERSION, DESCRIPTION)
values ('wanshengjie', '��ʥ��',  S_T_DIMDEF.nextval, null, null, null, 2014, 0, null, null, null, null, to_timestamp('22-11-2013 17:24:36.427727', 'dd-mm-yyyy hh24:mi:ss.ff'), 0, null);
insert into T_DIMDEF (CODE, NAME, SEQID, PARENT, VALIDSTART, VALIDEND, TYPEID, ISDELETE, UPDATETIME, CREATE_BY, UPDATE_DATE, UPDATE_BY, CREATE_DATE, VERSION, DESCRIPTION)
values ('UP', '����',  S_T_DIMDEF.nextval, null, null, null, 2015, 0, null, null, null, null, to_timestamp('25-11-2013 12:36:58.295856', 'dd-mm-yyyy hh24:mi:ss.ff'), 0, null);
insert into T_DIMDEF (CODE, NAME, SEQID, PARENT, VALIDSTART, VALIDEND, TYPEID, ISDELETE, UPDATETIME, CREATE_BY, UPDATE_DATE, UPDATE_BY, CREATE_DATE, VERSION, DESCRIPTION)
values ('DOWN', '����',  S_T_DIMDEF.nextval, null, null, null, 2015, 0, null, null, null, null, to_timestamp('25-11-2013 12:36:58.913557', 'dd-mm-yyyy hh24:mi:ss.ff'), 0, null);
--insert into T_DIMDEF (CODE, NAME, SEQID, PARENT, VALIDSTART, VALIDEND, TYPEID, ISDELETE, UPDATETIME, CREATE_BY, UPDATE_DATE, UPDATE_BY, CREATE_DATE, VERSION, DESCRIPTION)
--values ('0', '��',  S_T_DIMDEF.nextval, null, null, null, 2016, 0, null, null, null, null, to_timestamp('26-11-2013 11:39:33.673401', 'dd-mm-yyyy hh24:mi:ss.ff'), 0, null);
--insert into T_DIMDEF (CODE, NAME, SEQID, PARENT, VALIDSTART, VALIDEND, TYPEID, ISDELETE, UPDATETIME, CREATE_BY, UPDATE_DATE, UPDATE_BY, CREATE_DATE, VERSION, DESCRIPTION)
--values ('1', '��',  S_T_DIMDEF.nextval, null, null, null, 2016, 0, null, null, null, null, to_timestamp('26-11-2013 11:39:33.746880', 'dd-mm-yyyy hh24:mi:ss.ff'), 0, null);
commit;

