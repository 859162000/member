--插入 会员观影交易渠道 
insert into ccs_app.t_dimtypedef values(2010,'会员观影交易渠道',0,sysdate);
insert into ccs_app.t_dimdef values('01','POS',ccs_app.s_t_dimdef.nextval,0,null,null,2010,0,sysdate,'admin',sysdate,'admin',sysdate,0,null);
insert into ccs_app.t_dimdef values('05','门户网站',ccs_app.s_t_dimdef.nextval,0,null,null,2010,0,sysdate,'admin',sysdate,'admin',sysdate,0,null);
insert into ccs_app.t_dimdef values('06','手机客户端',ccs_app.s_t_dimdef.nextval,0,null,null,2010,0,sysdate,'admin',sysdate,'admin',sysdate,0,null);
insert into ccs_app.t_dimdef values('07','WAP网站',ccs_app.s_t_dimdef.nextval,0,null,null,2010,0,sysdate,'admin',sysdate,'admin',sysdate,0,null);
insert into ccs_app.t_dimdef values('08','IVRS',ccs_app.s_t_dimdef.nextval,0,null,null,2010,0,sysdate,'admin',sysdate,'admin',sysdate,0,null);
insert into ccs_app.t_dimdef values('09','电话客服',ccs_app.s_t_dimdef.nextval,0,null,null,2010,0,sysdate,'admin',sysdate,'admin',sysdate,0,null);
insert into ccs_app.t_dimdef values('10','总部系统',ccs_app.s_t_dimdef.nextval,0,null,null,2010,0,sysdate,'admin',sysdate,'admin',sysdate,0,null);
insert into ccs_app.t_dimdef values('11','会员系统',ccs_app.s_t_dimdef.nextval,0,null,null,2010,0,sysdate,'admin',sysdate,'admin',sysdate,0,null);
insert into ccs_app.t_dimdef values('12','其它',ccs_app.s_t_dimdef.nextval,0,null,null,2010,0,sysdate,'admin',sysdate,'admin',sysdate,0,null);
insert into ccs_app.t_dimdef values('未知','未知',ccs_app.s_t_dimdef.nextval,0,null,null,2010,0,sysdate,'admin',sysdate,'admin',sysdate,0,null);


--插入观影时段的未知维数据
insert into ccs_app.t_dimdef (SEQID, CODE, NAME, PARENT, TYPEID, ISDELETE, VERSION, DESCRIPTION)
values (ccs_app.s_t_dimdef.nextval, '-999', '未知', '0', 2007, 0, 0, '');

--插入 插入客群状态
insert into ccs_app.t_dimtypedef (TYPEID, TYPENAME, ISDELETE, UPDATETIME)
values (1030, '客群状态', '0', systimestamp);

insert into ccs_app.t_dimdef (SEQID, CODE, NAME, PARENT, TYPEID, ISDELETE, VERSION, DESCRIPTION)
values (ccs_app.s_t_dimdef.nextval, '10', '未计算', '0', 1030, 0, 0, '');

insert into ccs_app.t_dimdef (SEQID, CODE, NAME, PARENT, TYPEID, ISDELETE, VERSION, DESCRIPTION)
values (ccs_app.s_t_dimdef.nextval, '20', '计算中', '0', 1030, 0, 0, '');

insert into ccs_app.t_dimdef (SEQID, CODE, NAME, PARENT, TYPEID, ISDELETE, VERSION, DESCRIPTION)
values (ccs_app.s_t_dimdef.nextval, '30', '计算完成', '0', 1030, 0, 0, '');

insert into ccs_app.t_dimdef (SEQID, CODE, NAME, PARENT, TYPEID, ISDELETE, VERSION, DESCRIPTION)
values (ccs_app.s_t_dimdef.nextval, '40', '计算失败', '0', 1030, 0, 0, '');

commit;

