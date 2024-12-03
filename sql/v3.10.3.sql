alter table db_smart_park.tb_building_archives
    add type varchar(50) null comment '楼盘类型';


INSERT INTO db_smart_park.menu (id, menu_name, menu_url, component_uri, api_url, icon, menu_type, permission_code,
                                parent_id, sort, hidden, description, external_link, status, created_on, created_by,
                                updated_on, updated_by)
VALUES (1312105943160324096, '删除', '1', '1', '/app/call/party/deletePortrait', null, 1,
        '/app/pages/managementPKG/personManageYuFan/main:delete', 1312087424469303296, 3, 1, null, 0, 1,
        '2024-11-29 17:20:35', '1', '2024-11-29 17:20:35', '1');
INSERT INTO db_smart_park.menu (id, menu_name, menu_url, component_uri, api_url, icon, menu_type, permission_code,
                                parent_id, sort, hidden, description, external_link, status, created_on, created_by,
                                updated_on, updated_by)
VALUES (1312105725236871168, '编辑', '1', '1', '/app/call/party/updatePortrait', null, 1,
        '/app/pages/managementPKG/personManageYuFan/main:edit', 1312087424469303296, 2, 0, null, 0, 1,
        '2024-11-29 17:19:43', '1', '2024-11-29 17:20:07', '1');
INSERT INTO db_smart_park.menu (id, menu_name, menu_url, component_uri, api_url, icon, menu_type, permission_code,
                                parent_id, sort, hidden, description, external_link, status, created_on, created_by,
                                updated_on, updated_by)
VALUES (1312088023831150592, '添加人员', '1', '1', '/app/call/party/createPortrait', null, 2,
        '/app/pages/managementPKG/personManageYuFan/main:add', 1312087424469303296, 1, 1, null, 0, 1,
        '2024-11-29 16:09:22', '1', '2024-11-29 16:09:40', '1');
INSERT INTO db_smart_park.menu (id, menu_name, menu_url, component_uri, api_url, icon, menu_type, permission_code,
                                parent_id, sort, hidden, description, external_link, status, created_on, created_by,
                                updated_on, updated_by)
VALUES (1312087424469303296, '人员管理（宇泛）', '1', '1',
        '/app/call/party/queryPortraitInputInfoPage;/app/personGroup/queryForList;/app/access/queryDevices;/app/party/queryPortraitInputDetail',
        null, 1, '/app/pages/managementPKG/personManageYuFan/main', 1253422200053039104, 24, 1, null, 0, 1,
        '2024-11-29 16:06:59', '1', '2024-11-29 17:18:26', '1');

INSERT INTO db_smart_park.custom_config (id, name, system_option_type, status, created_on, created_by, created_by_type,
                                         updated_on, updated_by, updated_by_type, deleted_flag, deleted_on, deleted_by,
                                         delete_by_type)
VALUES (1722210915354824715, '楼盘类型', 1, 1, '2024-05-22 17:22:54', '1', null, '2024-12-03 10:56:27',
        '1792452525714616322', null, 0, null, null, null);

