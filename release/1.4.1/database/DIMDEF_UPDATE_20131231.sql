

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
commit;


insert into T_DIMDEF (CODE, NAME, SEQID, PARENT, VALIDSTART, VALIDEND, TYPEID, ISDELETE, UPDATETIME, CREATE_BY, UPDATE_DATE, UPDATE_BY, CREATE_DATE, VERSION, DESCRIPTION)
values ('1', '����',  S_T_DIMDEF.nextval, null, null, null, 2011, 0, null, null, sysdate, null, sysdate, 0, null);
insert into T_DIMDEF (CODE, NAME, SEQID, PARENT, VALIDSTART, VALIDEND, TYPEID, ISDELETE, UPDATETIME, CREATE_BY, UPDATE_DATE, UPDATE_BY, CREATE_DATE, VERSION, DESCRIPTION)
values ('2', '����',  S_T_DIMDEF.nextval, null, null, null, 2011, 0, null, null, sysdate, null, sysdate, 0, null);
insert into T_DIMDEF (CODE, NAME, SEQID, PARENT, VALIDSTART, VALIDEND, TYPEID, ISDELETE, UPDATETIME, CREATE_BY, UPDATE_DATE, UPDATE_BY, CREATE_DATE, VERSION, DESCRIPTION)
values ('3', 'δ֪',  S_T_DIMDEF.nextval, null, null, null, 2011, 0, null, null, sysdate, null, sysdate, 0, null);
insert into T_DIMDEF (CODE, NAME, SEQID, PARENT, VALIDSTART, VALIDEND, TYPEID, ISDELETE, UPDATETIME, CREATE_BY, UPDATE_DATE, UPDATE_BY, CREATE_DATE, VERSION, DESCRIPTION)
values ('1', '����',  S_T_DIMDEF.nextval, null, null, null, 2012, 0, null, null, sysdate, null, sysdate, 0, null);
insert into T_DIMDEF (CODE, NAME, SEQID, PARENT, VALIDSTART, VALIDEND, TYPEID, ISDELETE, UPDATETIME, CREATE_BY, UPDATE_DATE, UPDATE_BY, CREATE_DATE, VERSION, DESCRIPTION)
values ('2', '����',  S_T_DIMDEF.nextval, null, null, null, 2012, 0, null, null, sysdate, null, sysdate, 0, null);
insert into T_DIMDEF (CODE, NAME, SEQID, PARENT, VALIDSTART, VALIDEND, TYPEID, ISDELETE, UPDATETIME, CREATE_BY, UPDATE_DATE, UPDATE_BY, CREATE_DATE, VERSION, DESCRIPTION)
values ('3', 'ҹ��',  S_T_DIMDEF.nextval, null, null, null, 2012, 0, null, null,sysdate, null, sysdate, 0, null);
insert into T_DIMDEF (CODE, NAME, SEQID, PARENT, VALIDSTART, VALIDEND, TYPEID, ISDELETE, UPDATETIME, CREATE_BY, UPDATE_DATE, UPDATE_BY, CREATE_DATE, VERSION, DESCRIPTION)
values ('1', '�Ƽ���Ӧ',  S_T_DIMDEF.nextval, null, null, null, 2013, 0, null, null, sysdate, null, sysdate, 0, null);
insert into T_DIMDEF (CODE, NAME, SEQID, PARENT, VALIDSTART, VALIDEND, TYPEID, ISDELETE, UPDATETIME, CREATE_BY, UPDATE_DATE, UPDATE_BY, CREATE_DATE, VERSION, DESCRIPTION)
values ('2', '������Ӧ',  S_T_DIMDEF.nextval, null, null, null, 2013, 0, null, null, sysdate, null, sysdate, 0, null);
insert into T_DIMDEF (CODE, NAME, SEQID, PARENT, VALIDSTART, VALIDEND, TYPEID, ISDELETE, UPDATETIME, CREATE_BY, UPDATE_DATE, UPDATE_BY, CREATE_DATE, VERSION, DESCRIPTION)
values ('chunjie', '����',  S_T_DIMDEF.nextval, null, null, null, 2014, 0, null, null, sysdate, null, sysdate, 0, null);
insert into T_DIMDEF (CODE, NAME, SEQID, PARENT, VALIDSTART, VALIDEND, TYPEID, ISDELETE, UPDATETIME, CREATE_BY, UPDATE_DATE, UPDATE_BY, CREATE_DATE, VERSION, DESCRIPTION)
values ('wuyi', '��һ',  S_T_DIMDEF.nextval, null, null, null, 2014, 0, null, null, sysdate, null, sysdate, 0, null);
insert into T_DIMDEF (CODE, NAME, SEQID, PARENT, VALIDSTART, VALIDEND, TYPEID, ISDELETE, UPDATETIME, CREATE_BY, UPDATE_DATE, UPDATE_BY, CREATE_DATE, VERSION, DESCRIPTION)
values ('shiyi', 'ʮһ',  S_T_DIMDEF.nextval, null, null, null, 2014, 0, null, null, sysdate, null, sysdate, 0, null);
insert into T_DIMDEF (CODE, NAME, SEQID, PARENT, VALIDSTART, VALIDEND, TYPEID, ISDELETE, UPDATETIME, CREATE_BY, UPDATE_DATE, UPDATE_BY, CREATE_DATE, VERSION, DESCRIPTION)
values ('duanwu', '����',  S_T_DIMDEF.nextval, null, null, null, 2014, 0, null, null, sysdate, null, sysdate, 0, null);
insert into T_DIMDEF (CODE, NAME, SEQID, PARENT, VALIDSTART, VALIDEND, TYPEID, ISDELETE, UPDATETIME, CREATE_BY, UPDATE_DATE, UPDATE_BY, CREATE_DATE, VERSION, DESCRIPTION)
values ('yuandan', 'Ԫ��',  S_T_DIMDEF.nextval, null, null, null, 2014, 0, null, null, sysdate, null, sysdate, 0, null);
insert into T_DIMDEF (CODE, NAME, SEQID, PARENT, VALIDSTART, VALIDEND, TYPEID, ISDELETE, UPDATETIME, CREATE_BY, UPDATE_DATE, UPDATE_BY, CREATE_DATE, VERSION, DESCRIPTION)
values ('zhongqiu', '����',  S_T_DIMDEF.nextval, null, null, null, 2014, 0, null, null, sysdate, null, sysdate, 0, null);
insert into T_DIMDEF (CODE, NAME, SEQID, PARENT, VALIDSTART, VALIDEND, TYPEID, ISDELETE, UPDATETIME, CREATE_BY, UPDATE_DATE, UPDATE_BY, CREATE_DATE, VERSION, DESCRIPTION)
values ('qingming', '����',  S_T_DIMDEF.nextval, null, null, null, 2014, 0, null, null, sysdate, null, sysdate, 0, null);
insert into T_DIMDEF (CODE, NAME, SEQID, PARENT, VALIDSTART, VALIDEND, TYPEID, ISDELETE, UPDATETIME, CREATE_BY, UPDATE_DATE, UPDATE_BY, CREATE_DATE, VERSION, DESCRIPTION)
values ('shengdanjie', 'ʥ����',  S_T_DIMDEF.nextval, null, null, null, 2014, 0, null, null,sysdate, null, sysdate, 0, null);
insert into T_DIMDEF (CODE, NAME, SEQID, PARENT, VALIDSTART, VALIDEND, TYPEID, ISDELETE, UPDATETIME, CREATE_BY, UPDATE_DATE, UPDATE_BY, CREATE_DATE, VERSION, DESCRIPTION)
values ('guanggunjie', '�����',  S_T_DIMDEF.nextval, null, null, null, 2014, 0, null, null, sysdate, null, sysdate, 0, null);
insert into T_DIMDEF (CODE, NAME, SEQID, PARENT, VALIDSTART, VALIDEND, TYPEID, ISDELETE, UPDATETIME, CREATE_BY, UPDATE_DATE, UPDATE_BY, CREATE_DATE, VERSION, DESCRIPTION)
values ('qingrenjie', '���˽�',  S_T_DIMDEF.nextval, null, null, null, 2014, 0, null, null, sysdate, null, sysdate, 0, null);
insert into T_DIMDEF (CODE, NAME, SEQID, PARENT, VALIDSTART, VALIDEND, TYPEID, ISDELETE, UPDATETIME, CREATE_BY, UPDATE_DATE, UPDATE_BY, CREATE_DATE, VERSION, DESCRIPTION)
values ('yurenjie', '���˽�',  S_T_DIMDEF.nextval, null, null, null, 2014, 0, null, null, sysdate, null, sysdate, 0, null);
insert into T_DIMDEF (CODE, NAME, SEQID, PARENT, VALIDSTART, VALIDEND, TYPEID, ISDELETE, UPDATETIME, CREATE_BY, UPDATE_DATE, UPDATE_BY, CREATE_DATE, VERSION, DESCRIPTION)
values ('baiseqingrenjie', '��ɫ���˽�',  S_T_DIMDEF.nextval, null, null, null, 2014, 0, null, null, sysdate, null, sysdate, 0, null);
insert into T_DIMDEF (CODE, NAME, SEQID, PARENT, VALIDSTART, VALIDEND, TYPEID, ISDELETE, UPDATETIME, CREATE_BY, UPDATE_DATE, UPDATE_BY, CREATE_DATE, VERSION, DESCRIPTION)
values ('fuhuojie', '�����',  S_T_DIMDEF.nextval, null, null, null, 2014, 0, null, null, sysdate, null, sysdate, 0, null);
insert into T_DIMDEF (CODE, NAME, SEQID, PARENT, VALIDSTART, VALIDEND, TYPEID, ISDELETE, UPDATETIME, CREATE_BY, UPDATE_DATE, UPDATE_BY, CREATE_DATE, VERSION, DESCRIPTION)
values ('wanshengjie', '��ʥ��',  S_T_DIMDEF.nextval, null, null, null, 2014, 0, null, null, sysdate, null, sysdate, 0, null);
insert into T_DIMDEF (CODE, NAME, SEQID, PARENT, VALIDSTART, VALIDEND, TYPEID, ISDELETE, UPDATETIME, CREATE_BY, UPDATE_DATE, UPDATE_BY, CREATE_DATE, VERSION, DESCRIPTION)
values ('UP', '����',  S_T_DIMDEF.nextval, null, null, null, 2015, 0, null, null, sysdate, null, sysdate, 0, null);
insert into T_DIMDEF (CODE, NAME, SEQID, PARENT, VALIDSTART, VALIDEND, TYPEID, ISDELETE, UPDATETIME, CREATE_BY, UPDATE_DATE, UPDATE_BY, CREATE_DATE, VERSION, DESCRIPTION)
values ('DOWN', '����',  S_T_DIMDEF.nextval, null, null, null, 2015, 0, null, null, sysdate, null, sysdate, 0, null);
commit;


--���»��ֲ�������
delete from t_dimdef t where t.typeid=1021;
delete from t_dimdef t where t.typeid=212;

insert into T_DIMDEF (CODE, NAME, SEQID, PARENT, VALIDSTART, VALIDEND, TYPEID, ISDELETE, UPDATETIME, CREATE_BY, UPDATE_DATE, UPDATE_BY, CREATE_DATE, VERSION, DESCRIPTION)
values ('1', '����',  S_T_DIMDEF.nextval, null, null, null, 1021, 0, null, null, sysdate, null, sysdate, 0, null);
insert into T_DIMDEF (CODE, NAME, SEQID, PARENT, VALIDSTART, VALIDEND, TYPEID, ISDELETE, UPDATETIME, CREATE_BY, UPDATE_DATE, UPDATE_BY, CREATE_DATE, VERSION, DESCRIPTION)
values ('2', '����',  S_T_DIMDEF.nextval, null, null, null, 1021, 0, null, null, sysdate, null, sysdate, 0, null);
insert into T_DIMDEF (CODE, NAME, SEQID, PARENT, VALIDSTART, VALIDEND, TYPEID, ISDELETE, UPDATETIME, CREATE_BY, UPDATE_DATE, UPDATE_BY, CREATE_DATE, VERSION, DESCRIPTION)
values ('3', '����',  S_T_DIMDEF.nextval, null, null, null, 1021, 0, null, null, sysdate, null, sysdate, 0, null);
insert into T_DIMDEF (CODE, NAME, SEQID, PARENT, VALIDSTART, VALIDEND, TYPEID, ISDELETE, UPDATETIME, CREATE_BY, UPDATE_DATE, UPDATE_BY, CREATE_DATE, VERSION, DESCRIPTION)
values ('4', '����',  S_T_DIMDEF.nextval, null, null, null, 1021, 0, null, null, sysdate, null, sysdate, 0, null);
insert into T_DIMDEF (CODE, NAME, SEQID, PARENT, VALIDSTART, VALIDEND, TYPEID, ISDELETE, UPDATETIME, CREATE_BY, UPDATE_DATE, UPDATE_BY, CREATE_DATE, VERSION, DESCRIPTION)
values ('5', '����',  S_T_DIMDEF.nextval, null, null, null, 1021, 0, null, null, sysdate, null, sysdate, 0, null);
insert into T_DIMDEF (CODE, NAME, SEQID, PARENT, VALIDSTART, VALIDEND, TYPEID, ISDELETE, UPDATETIME, CREATE_BY, UPDATE_DATE, UPDATE_BY, CREATE_DATE, VERSION, DESCRIPTION)
values ('6', '���¶һ�',  S_T_DIMDEF.nextval, null, null, null, 1021, 0, null, null, sysdate, null, sysdate, 0, null);
insert into T_DIMDEF (CODE, NAME, SEQID, PARENT, VALIDSTART, VALIDEND, TYPEID, ISDELETE, UPDATETIME, CREATE_BY, UPDATE_DATE, UPDATE_BY, CREATE_DATE, VERSION, DESCRIPTION)
values ('7', '��վ�һ�',  S_T_DIMDEF.nextval, null, null, null, 1021, 0, null, null, sysdate, null, sysdate, 0, null);
insert into T_DIMDEF (CODE, NAME, SEQID, PARENT, VALIDSTART, VALIDEND, TYPEID, ISDELETE, UPDATETIME, CREATE_BY, UPDATE_DATE, UPDATE_BY, CREATE_DATE, VERSION, DESCRIPTION)
values ('8', '����',  S_T_DIMDEF.nextval, null, null, null, 1021, 0, null, null, sysdate, null, sysdate, 0, null);

insert into T_DIMDEF (CODE, NAME, SEQID, PARENT, VALIDSTART, VALIDEND, TYPEID, ISDELETE, UPDATETIME, CREATE_BY, UPDATE_DATE, UPDATE_BY, CREATE_DATE, VERSION, DESCRIPTION)
values ('1', '����',  S_T_DIMDEF.nextval, null, null, null, 212, 0, null, null, sysdate, null, sysdate, 0, null);
insert into T_DIMDEF (CODE, NAME, SEQID, PARENT, VALIDSTART, VALIDEND, TYPEID, ISDELETE, UPDATETIME, CREATE_BY, UPDATE_DATE, UPDATE_BY, CREATE_DATE, VERSION, DESCRIPTION)
values ('2', '����',  S_T_DIMDEF.nextval, null, null, null, 212, 0, null, null, sysdate, null, sysdate, 0, null);
insert into T_DIMDEF (CODE, NAME, SEQID, PARENT, VALIDSTART, VALIDEND, TYPEID, ISDELETE, UPDATETIME, CREATE_BY, UPDATE_DATE, UPDATE_BY, CREATE_DATE, VERSION, DESCRIPTION)
values ('3', '����',  S_T_DIMDEF.nextval, null, null, null, 212, 0, null, null, sysdate, null, sysdate, 0, null);
insert into T_DIMDEF (CODE, NAME, SEQID, PARENT, VALIDSTART, VALIDEND, TYPEID, ISDELETE, UPDATETIME, CREATE_BY, UPDATE_DATE, UPDATE_BY, CREATE_DATE, VERSION, DESCRIPTION)
values ('4', '����',  S_T_DIMDEF.nextval, null, null, null, 212, 0, null, null, sysdate, null, sysdate, 0, null);
insert into T_DIMDEF (CODE, NAME, SEQID, PARENT, VALIDSTART, VALIDEND, TYPEID, ISDELETE, UPDATETIME, CREATE_BY, UPDATE_DATE, UPDATE_BY, CREATE_DATE, VERSION, DESCRIPTION)
values ('5', '����',  S_T_DIMDEF.nextval, null, null, null, 212, 0, null, null, sysdate, null, sysdate, 0, null);
insert into T_DIMDEF (CODE, NAME, SEQID, PARENT, VALIDSTART, VALIDEND, TYPEID, ISDELETE, UPDATETIME, CREATE_BY, UPDATE_DATE, UPDATE_BY, CREATE_DATE, VERSION, DESCRIPTION)
values ('6', '���¶һ�',  S_T_DIMDEF.nextval, null, null, null, 212, 0, null, null, sysdate, null, sysdate, 0, null);
insert into T_DIMDEF (CODE, NAME, SEQID, PARENT, VALIDSTART, VALIDEND, TYPEID, ISDELETE, UPDATETIME, CREATE_BY, UPDATE_DATE, UPDATE_BY, CREATE_DATE, VERSION, DESCRIPTION)
values ('7', '��վ�һ�',  S_T_DIMDEF.nextval, null, null, null, 212, 0, null, null, sysdate, null, sysdate, 0, null);
insert into T_DIMDEF (CODE, NAME, SEQID, PARENT, VALIDSTART, VALIDEND, TYPEID, ISDELETE, UPDATETIME, CREATE_BY, UPDATE_DATE, UPDATE_BY, CREATE_DATE, VERSION, DESCRIPTION)
values ('8', '����',  S_T_DIMDEF.nextval, null, null, null, 212, 0, null, null, sysdate, null, sysdate, 0, null);

commit;


