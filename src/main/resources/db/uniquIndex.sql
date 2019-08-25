-- 仔细检查sql语句，然后为其建立索引

CREATE UNIQUE INDEX attach_file_ref_id_idx
    ON public.attach_file USING btree
(ref_id COLLATE pg_catalog."default")
TABLESPACE pg_default;