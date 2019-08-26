-- Table: public.attach_file

-- DROP TABLE public.attach_file;

CREATE TABLE public.attach_file
(
    id bigint NOT NULL DEFAULT nextval('attach_file_id_seq'::regclass),
    type character varying(255) COLLATE pg_catalog."default" NOT NULL,
    file_name character varying(500) COLLATE pg_catalog."default" NOT NULL,
    file_path character varying(500) COLLATE pg_catalog."default" NOT NULL,
    ref_id bigint NOT NULL,
    is_del character varying(1) COLLATE pg_catalog."default",
    create_user bigint,
    create_time timestamp with time zone,
    modify_user bigint,
    modify_time timestamp with time zone,
    CONSTRAINT attach_file_pkey PRIMARY KEY (id)
)
WITH (
         OIDS = FALSE
     )
    TABLESPACE pg_default;

ALTER TABLE public.attach_file
    OWNER to exchange;
COMMENT ON TABLE public.attach_file
    IS '附件';

-- Table: public.bought_amount

-- DROP TABLE public.bought_amount;

CREATE TABLE public.bought_amount
(
    id integer NOT NULL,
    user_id integer NOT NULL,
    project_id integer NOT NULL,
    coin_id integer NOT NULL,
    create_time timestamp with time zone,
    order_id character varying(100) COLLATE pg_catalog."default" NOT NULL,
    withdraw integer NOT NULL DEFAULT 0,
    coin_amount numeric(20,6) NOT NULL,
    hos_amount numeric(20,6),
    symbol_id integer NOT NULL,
    CONSTRAINT bought_amount_pkey PRIMARY KEY (id)
)
WITH (
         OIDS = FALSE
     )
    TABLESPACE pg_default;

ALTER TABLE public.bought_amount
    OWNER to exchange;
COMMENT ON TABLE public.bought_amount
    IS '认购房产表';

-- Table: public.permission

-- DROP TABLE public.permission;

CREATE TABLE public.permission
(
    id integer NOT NULL DEFAULT nextval('permission_id_seq'::regclass),
    name character varying(128) COLLATE pg_catalog."default",
    per_code character varying(128) COLLATE pg_catalog."default",
    CONSTRAINT function_pkey PRIMARY KEY (id)
)
WITH (
         OIDS = FALSE
     )
    TABLESPACE pg_default;

ALTER TABLE public.permission
    OWNER to exchange;
COMMENT ON TABLE public.permission
    IS '权限表';

-- Table: public.project

-- DROP TABLE public.project;

CREATE TABLE public.project
(
    id bigint NOT NULL DEFAULT nextval('project_id_seq'::regclass),
    name character varying(255) COLLATE pg_catalog."default" NOT NULL,
    addr character varying(500) COLLATE pg_catalog."default" NOT NULL,
    invest_amount numeric(16,2) NOT NULL,
    collect_amount numeric(20,6) NOT NULL,
    invest_tender character varying(500) COLLATE pg_catalog."default",
    project_type character varying(255) COLLATE pg_catalog."default",
    fund_use character varying(500) COLLATE pg_catalog."default",
    invest_period integer,
    start_time timestamp with time zone,
    expect_year_bonus numeric(10,4),
    bonus_share_period character varying(500) COLLATE pg_catalog."default",
    bonus_compute_time timestamp with time zone,
    receipt_way character varying(255) COLLATE pg_catalog."default",
    end_time timestamp with time zone,
    payback_way character varying(255) COLLATE pg_catalog."default",
    payback_time timestamp with time zone,
    payback_time_period character varying(255) COLLATE pg_catalog."default",
    tender_admin character varying(500) COLLATE pg_catalog."default",
    admin_fee numeric(16,2),
    hold_cost character varying(500) COLLATE pg_catalog."default",
    bonus_fee numeric(10,4),
    bonus_resource character varying(500) COLLATE pg_catalog."default",
    guarantee character varying(500) COLLATE pg_catalog."default",
    brief_introduction text COLLATE pg_catalog."default",
    perimeter_match text COLLATE pg_catalog."default",
    market_analysis text COLLATE pg_catalog."default",
    city_condition text COLLATE pg_catalog."default",
    cost_analysis text COLLATE pg_catalog."default",
    bonus_analysis text COLLATE pg_catalog."default",
    risk_analysis text COLLATE pg_catalog."default",
    admin_introduction text COLLATE pg_catalog."default",
    project_platform_account character varying(255) COLLATE pg_catalog."default",
    symbol bigint,
    status character varying(255) COLLATE pg_catalog."default",
    create_user bigint,
    create_time timestamp with time zone,
    modify_user bigint,
    modify_time timestamp with time zone,
    other character varying COLLATE pg_catalog."default",
    area_type integer,
    init_price numeric(20,6),
    coin integer,
    user_id integer NOT NULL,
    CONSTRAINT project_pkey PRIMARY KEY (id)
)
WITH (
         OIDS = FALSE
     )
    TABLESPACE pg_default;

ALTER TABLE public.project
    OWNER to exchange;
COMMENT ON TABLE public.project
    IS '项目表（项目发布信息采集）';

COMMENT ON COLUMN public.project.name
    IS '项目名称';

COMMENT ON COLUMN public.project.addr
    IS '项目地址';

-- Table: public.project_coin

-- DROP TABLE public.project_coin;

CREATE TABLE public.project_coin
(
    id bigint NOT NULL DEFAULT nextval('project_coin_id_seq'::regclass),
    amount numeric(20,6),
    contract_addr character varying(500) COLLATE pg_catalog."default",
    user_id integer NOT NULL,
    coin_id integer NOT NULL,
    coin_type character varying(128) COLLATE pg_catalog."default",
    CONSTRAINT t_project_coin_pkey PRIMARY KEY (id)
)
WITH (
         OIDS = FALSE
     )
    TABLESPACE pg_default;

ALTER TABLE public.project_coin
    OWNER to exchange;
COMMENT ON TABLE public.project_coin
    IS '项目币表（为了跟平台币表作一个区分）';

-- Table: public.role

-- DROP TABLE public.role;

CREATE TABLE public.role
(
    id integer NOT NULL DEFAULT nextval('role_id_seq'::regclass),
    name character varying(128) COLLATE pg_catalog."default" NOT NULL,
    remark character varying(255) COLLATE pg_catalog."default",
    type character varying(4) COLLATE pg_catalog."default",
    CONSTRAINT role_pkey PRIMARY KEY (id)
)
WITH (
         OIDS = FALSE
     )
    TABLESPACE pg_default;

ALTER TABLE public.role
    OWNER to exchange;
COMMENT ON TABLE public.role
    IS '角色表';

-- Table: public.role_permission

-- DROP TABLE public.role_permission;

CREATE TABLE public.role_permission
(
    id integer NOT NULL,
    role_id integer NOT NULL,
    permission_id integer NOT NULL,
    CONSTRAINT role_permission_pkey PRIMARY KEY (id)
)
WITH (
         OIDS = FALSE
     )
    TABLESPACE pg_default;

ALTER TABLE public.role_permission
    OWNER to exchange;

-- Table: public.sysuser

-- DROP TABLE public.sysuser;

CREATE TABLE public.sysuser
(
    id integer NOT NULL DEFAULT nextval('sysuser_id_seq'::regclass),
    account character varying(20) COLLATE pg_catalog."default" NOT NULL,
    password character varying(80) COLLATE pg_catalog."default" NOT NULL,
    username character varying(20) COLLATE pg_catalog."default",
    reg_time timestamp with time zone,
    email character varying(80) COLLATE pg_catalog."default",
    fund_password character varying(80) COLLATE pg_catalog."default" NOT NULL,
    create_user integer,
    modify_user integer,
    modify_time timestamp with time zone,
    salt character varying(255) COLLATE pg_catalog."default",
    wallet_addr character varying(255) COLLATE pg_catalog."default",
    invitation_code character varying(10) COLLATE pg_catalog."default",
    certificate_type integer,
    certificate_num character varying(255) COLLATE pg_catalog."default",
    nationality character varying(50) COLLATE pg_catalog."default",
    CONSTRAINT user_pkey PRIMARY KEY (id)
)
WITH (
         OIDS = FALSE
     )
    TABLESPACE pg_default;

ALTER TABLE public.sysuser
    OWNER to exchange;
COMMENT ON TABLE public.sysuser
    IS '用户表';

-- Table: public.user_role

-- DROP TABLE public.user_role;

CREATE TABLE public.user_role
(
    id integer NOT NULL DEFAULT nextval('user_role_id_seq'::regclass),
    user_id integer NOT NULL,
    role_id integer NOT NULL,
    CONSTRAINT user_role_pkey PRIMARY KEY (id)
)
WITH (
         OIDS = FALSE
     )
    TABLESPACE pg_default;

ALTER TABLE public.user_role
    OWNER to exchange;

-- Table: public.withdraw

-- DROP TABLE public.withdraw;

CREATE TABLE public.withdraw
(
    id integer NOT NULL,
    user_id integer NOT NULL,
    coin character varying COLLATE pg_catalog."default" NOT NULL,
    amount numeric(20,6) NOT NULL,
    txid character varying(255) COLLATE pg_catalog."default",
    "time" timestamp with time zone,
    status boolean DEFAULT false,
    CONSTRAINT withdraw_pkey PRIMARY KEY (id)
)
WITH (
         OIDS = FALSE
     )
    TABLESPACE pg_default;

ALTER TABLE public.withdraw
    OWNER to exchange;
COMMENT ON TABLE public.withdraw
    IS '提现表';