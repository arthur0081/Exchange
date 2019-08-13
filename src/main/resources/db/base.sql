
INSERT INTO public.role(id, name, remark) VALUES (1, 'superAdmin', '超级管理员');
INSERT INTO public.role(id, name, remark) VALUES (1, 'normalAdmin', '普通管理员');
INSERT INTO public.role(id, name, remark) VALUES (1, 'projectThird', '项目方');
INSERT INTO public.role(id, name, remark) VALUES (1, 'normalUser', '普通用户');

INSERT INTO public.permission(id, name, per_code)VALUES (1,'首页' , 'user:edit');
INSERT INTO public.permission(id, name, per_code)VALUES (2,'币种' , 'user:edit');
INSERT INTO public.permission(id, name, per_code)VALUES (3,'币对' , 'user:edit');
INSERT INTO public.permission(id, name, per_code)VALUES (4,'挂单' , 'user:edit');
INSERT INTO public.permission(id, name, per_code)VALUES (5,'成交记录' , 'user:edit');
INSERT INTO public.permission(id, name, per_code)VALUES (6,'项目发布' , 'user:edit');
INSERT INTO public.permission(id, name, per_code)VALUES (7,'权限管理' , 'user:edit');
INSERT INTO public.permission(id, name, per_code)VALUES (8,'认购房产列表' , 'user:edit');
INSERT INTO public.permission(id, name, per_code)VALUES (9,'币币交易' , 'user:edit');
INSERT INTO public.permission(id, name, per_code)VALUES (10,'资金管理' , 'user:edit');
INSERT INTO public.permission(id, name, per_code)VALUES (11,'个人中心' , 'user:edit');

INSERT INTO public.role_permission(id, role_id, permission_id)VALUES (1, 1, 1);
INSERT INTO public.role_permission(id, role_id, permission_id)VALUES (2, 1, 2);
INSERT INTO public.role_permission(id, role_id, permission_id)VALUES (3, 1, 3);
INSERT INTO public.role_permission(id, role_id, permission_id)VALUES (4, 1, 4);
INSERT INTO public.role_permission(id, role_id, permission_id)VALUES (5, 1, 5);
INSERT INTO public.role_permission(id, role_id, permission_id)VALUES (6, 1, 6);
INSERT INTO public.role_permission(id, role_id, permission_id)VALUES (7, 1, 7);

INSERT INTO public.role_permission(id, role_id, permission_id)VALUES (8, 2, 1);
INSERT INTO public.role_permission(id, role_id, permission_id)VALUES (9, 2, 2);
INSERT INTO public.role_permission(id, role_id, permission_id)VALUES (10, 2, 3);
INSERT INTO public.role_permission(id, role_id, permission_id)VALUES (11, 2, 4);
INSERT INTO public.role_permission(id, role_id, permission_id)VALUES (12, 2, 5);
INSERT INTO public.role_permission(id, role_id, permission_id)VALUES (13, 2, 6);

INSERT INTO public.role_permission(id, role_id, permission_id)VALUES (14, 3, 8);
INSERT INTO public.role_permission(id, role_id, permission_id)VALUES (15, 3, 9);
INSERT INTO public.role_permission(id, role_id, permission_id)VALUES (16, 3, 10);
INSERT INTO public.role_permission(id, role_id, permission_id)VALUES (17, 3, 11);

INSERT INTO public.role_permission(id, role_id, permission_id)VALUES (18, 4, 8);
INSERT INTO public.role_permission(id, role_id, permission_id)VALUES (19, 4, 9);
INSERT INTO public.role_permission(id, role_id, permission_id)VALUES (20, 4, 10);
INSERT INTO public.role_permission(id, role_id, permission_id)VALUES (21, 4, 11);

INSERT INTO public.user_role(id, user_id, role_id)VALUES (1, 1, 1);
INSERT INTO public.user_role(id, user_id, role_id)VALUES (2, 2, 2);
INSERT INTO public.user_role(id, user_id, role_id)VALUES (3, 3, 3);
INSERT INTO public.user_role(id, user_id, role_id)VALUES (4, 4, 4);

INSERT INTO public."user"(id, account, password, username, reg_time, fund_password)
            VALUES(1, 'superAdmin', 'QUJBNUYyM0M3OTNEN0I4MUFBOTZBOTkwOEI1NDI0MUE=', '我是超级管理员', '2019-08-10 10:18:19','');
INSERT INTO public."user"(id, account, password, username, reg_time, email, fund_password)
             VALUES (2, 'normalAdmin', 'QUJBNUYyM0M3OTNEN0I4MUFBOTZBOTkwOEI1NDI0MUE=', '我是普通管理员', '2019-08-10 10:18:19', '3542312@qq.com', 'QUJBNUYyM0M3OTNEN0I4MUFBOTZBOTkwOEI1NDI0MUE=');
INSERT INTO public."user"(id, account, password, username, reg_time, email, fund_password)
             VALUES (3, '13035667829', 'QUJBNUYyM0M3OTNEN0I4MUFBOTZBOTkwOEI1NDI0MUE=', '我是项目方', '2019-08-10 10:18:19', '3542312@qq.com', 'QUJBNUYyM0M3OTNEN0I4MUFBOTZBOTkwOEI1NDI0MUE=');
INSERT INTO public."user"(id, account, password, username, reg_time, email, fund_password)
            VALUES (4, '13035667839', 'QUJBNUYyM0M3OTNEN0I4MUFBOTZBOTkwOEI1NDI0MUE=', '我是普通用户', '2019-08-10 10:18:19', '3542312@qq.com', 'QUJBNUYyM0M3OTNEN0I4MUFBOTZBOTkwOEI1NDI0MUE=');