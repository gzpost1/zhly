-- auto-generated definition
create table tb_contract_intention
(
    id                bigint      not null primary key,
    contract_no       varchar(30) null comment '合同编号',
    name              varchar(60) not null comment '合同名称',
    cantract_date     date        null comment '签订日期',
    begin_date        date        not null comment '合同开始日期',
    end_date          date        not null comment '合同结束日期',
    follow_up         varchar(30) not null comment '跟进人',
    client            varchar(30) null comment '签订客户',
    contract_lease_id bigint      null comment '租赁合同关联id',
    remark            longtext    null comment '意向备注',
    create_time       datetime    null,
    update_time       datetime    null,
    create_user       bigint      null,
    update_user       bigint      null,
    status            tinyint(1)  null,
    deleted           tinyint(1)  null,
    contract_status   int         null comment '合同状态',
    audit_status      int         null comment '审核状态 1审核中,待审核 2 审核通过 3.未通过'
)
    comment '意向合同';

-- auto-generated definition
create table tb_contract_cancel
(
    id          bigint       not null
        primary key,
    contract_no varchar(30)  null comment '合同编号',
    name        varchar(60)  not null comment '合同名称',
    date        date         null comment '退订日期',
    reason      varchar(255) null comment '退订原因',
    remark      longtext     null comment '退订说明',
    path        varchar(512) not null comment '退订附件',
    create_time datetime     null,
    update_time datetime     null,
    create_user bigint       null,
    update_user bigint       null,
    status      tinyint(1)   null,
    deleted     tinyint(1)   null,
    type        int          null default 0 comment '0.退订 1.作废'
)
    comment '退订或作废信息';




-- auto-generated definition
create table tb_contract_intention_bind_info
(
    intention_id bigint null comment '意向合同id',
    bind_id      bigint null comment '关联id',
    type         bigint null comment '1.房屋id 2.意向金id'
)
    comment '意向合同关联信息';

create table tb_contract_intention_money
(
    id      bigint         not null primary key,
    project varchar(50)    null comment '收费项目',
    bid     varchar(50)    null comment '意向标的',
    amount  decimal(10, 2) null comment '应收金额',
    remark  longtext       null comment '备注'
)
    comment '意向合同关联信息';

-- auto-generated definition
create table tb_contract_log
(
    id             bigint       not null primary key,
    operation      varchar(255) null comment '操作项',
    operator       varchar(255) null comment '操作人',
    oper_time      datetime     null comment '操作时间',
    oper_desc      longtext     null comment '操作详情',
    path           text         null comment '其他功能-附件',
    create_user    bigint(1)    null comment '创建人',
    create_time    datetime     null comment '创建时间',
    ext_id         varchar(30)  null comment '相关id',
    operation_type varchar(30)  null comment '操作类型'
)
    comment '合同操作日志';


-- auto-generated definition
create table tb_contract_lease
(
    id              bigint      not null primary key,
    contract_no     varchar(30) null comment '合同编号',
    name            varchar(60) not null comment '合同名称',
    cantract_date   date        null comment '签订日期',
    begin_date      date        not null comment '合同开始日期',
    end_date        date        not null comment '合同结束日期',
    follow_up       varchar(30) not null comment '跟进人
',
    client          varchar(30) null comment '签订客户',
    remark          longtext    null comment '意向备注',
    create_time     datetime    null,
    update_time     datetime    null,
    create_user     bigint      null,
    update_user     bigint      null,
    status          tinyint(1)  null,
    deleted         tinyint(1)  null,
    contract_status int         null comment '合同状态',
    audit_status    int         null comment '审核状态 1审核中,待审核 2 审核通过 3.未通过'
)
    comment '租赁合同';



