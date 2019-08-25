CREATE SEQUENCE attach_file_id_seq
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;
alter table attach_file alter column id set default nextval('attach_file_id_seq');

CREATE SEQUENCE bought_amount_id_seq
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;
alter table bought_amount alter column id set default nextval('bought_amount_id_seq');

CREATE SEQUENCE bought_amount_id_seq
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;
alter table bought_amount alter column id set default nextval('bought_amount_id_seq');

CREATE SEQUENCE permission_id_seq
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;
alter table permission alter column id set default nextval('permission_id_seq');

CREATE SEQUENCE project_id_seq
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;
alter table project alter column id set default nextval('project_id_seq');

CREATE SEQUENCE project_coin_id_seq
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;
alter table project_coin alter column id set default nextval('project_coin_id_seq');

CREATE SEQUENCE role_id_seq
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;
alter table role alter column id set default nextval('role_id_seq');

CREATE SEQUENCE role_permission_id_seq
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;
alter table role_permission alter column id set default nextval('role_permission_id_seq');

CREATE SEQUENCE sysuser_id_seq
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;
alter table sysuser alter column id set default nextval('sysuser_id_seq');

CREATE SEQUENCE user_role_id_seq
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;
alter table user_role alter column id set default nextval('user_role_id_seq');

CREATE SEQUENCE withdraw_id_seq
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;
alter table withdraw alter column id set default nextval('withdraw_id_seq');

