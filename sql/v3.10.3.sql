alter table tb_building_archives
    add type varchar(50) null comment '楼盘类型';

INSERT INTO db_smart_park.system_option_type (id, name, system_option_type, deleted_flag) VALUES (16, '楼盘类型', 16, 0);

