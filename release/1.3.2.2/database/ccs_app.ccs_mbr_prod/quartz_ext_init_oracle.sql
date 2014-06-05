
-- Interting T_CODE_LIST...
insert into T_CODE_LIST (CODE_LIST_ID, TYPE_ID, CODE, NAME, DESCRIPTION, DELETED, SORT_INDEX)
values (1, 'JOB_HISTORY_PHASE', 'TO_BE_FIRED', 'To Be Fired', null, '0', 1);
insert into T_CODE_LIST (CODE_LIST_ID, TYPE_ID, CODE, NAME, DESCRIPTION, DELETED, SORT_INDEX)
values (2, 'JOB_HISTORY_PHASE', 'SUCCESS', 'Success', null, '0', 2);
insert into T_CODE_LIST (CODE_LIST_ID, TYPE_ID, CODE, NAME, DESCRIPTION, DELETED, SORT_INDEX)
values (3, 'JOB_HISTORY_PHASE', 'FAILED', 'Failed', null, '0', 3);
insert into T_CODE_LIST (CODE_LIST_ID, TYPE_ID, CODE, NAME, DESCRIPTION, DELETED, SORT_INDEX)
values (4, 'JOB_HISTORY_PHASE', 'VETOED', 'Vetoed', null, '0', 4);
insert into T_CODE_LIST (CODE_LIST_ID, TYPE_ID, CODE, NAME, DESCRIPTION, DELETED, SORT_INDEX)
values (5, 'TRUE_FALSE', 'true', 'True', null, '0', 1);
insert into T_CODE_LIST (CODE_LIST_ID, TYPE_ID, CODE, NAME, DESCRIPTION, DELETED, SORT_INDEX)
values (6, 'TRUE_FALSE', 'false', 'False', null, '0', 2);
insert into T_CODE_LIST (CODE_LIST_ID, TYPE_ID, CODE, NAME, DESCRIPTION, DELETED, SORT_INDEX)
values (7, 'JOB_MODULE', 'member-jobs', 'member-jobs', null, '0', 1);
insert into T_CODE_LIST (CODE_LIST_ID, TYPE_ID, CODE, NAME, DESCRIPTION, DELETED, SORT_INDEX)
values (8, 'JOB_MODULE', 'point-jobs', 'point-jobs', null, '0', 2);
commit;
-- prompt 8 records loaded
