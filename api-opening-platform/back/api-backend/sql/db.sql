use api_opening_platform;

-- 接口信息
create table if not exists api_opening_platform.`interface_info`
(
    `id` bigint not null auto_increment comment '主键' primary key,
    `name` varchar(256) not null comment '名称',
    `description` varchar(256) null comment '描述',
    `url` varchar(512) not null comment '接口地址',
    `requestHeader` text null comment '请求头',
    `responseHeader` text null comment '响应头',
    `status` int default 0 not null comment '接口状态（0-关闭，1-打开）',
    `method` varchar(256) not null comment '请求类型',
    `userId` bigint not null comment '创建人',
    `createTime` datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    `updateTime` datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    `isDelete` tinyint default 0 not null comment '是否删除(0-未删, 1-已删)'
    ) comment '接口信息';

insert into api_opening_platform.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`, `method`, `userId`) values ('姜胤祥', '徐烨霖', 'www.weston-upton.co', '高雪松', '徐立轩', 0, '毛峻熙', 580247605);
insert into api_opening_platform.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`, `method`, `userId`) values ('孙浩宇', '任荣轩', 'www.andria-shields.name', '顾子涵', '黎天翊', 0, '金绍辉', 3470755142);
insert into api_opening_platform.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`, `method`, `userId`) values ('程伟祺', '傅明哲', 'www.tony-marvin.co', '姜鹭洋', '冯鹏飞', 0, '方天翊', 1916);
insert into api_opening_platform.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`, `method`, `userId`) values ('苏子骞', '任建辉', 'www.kent-wintheiser.info', '杜弘文', '姚文', 0, '杨弘文', 4500935);
insert into api_opening_platform.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`, `method`, `userId`) values ('罗语堂', '韦弘文', 'www.doria-conn.net', '崔金鑫', '吕瑾瑜', 0, '韦思远', 9360656);
insert into api_opening_platform.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`, `method`, `userId`) values ('雷远航', '汪雨泽', 'www.lilla-reinger.biz', '何金鑫', '莫梓晨', 0, '雷建辉', 628905466);
insert into api_opening_platform.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`, `method`, `userId`) values ('顾天磊', '姜鹏涛', 'www.cathie-veum.name', '姚晋鹏', '程熠彤', 0, '陶语堂', 80708);
insert into api_opening_platform.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`, `method`, `userId`) values ('卢俊驰', '钱弘文', 'www.myles-braun.co', '蒋鹏涛', '朱熠彤', 0, '曾明轩', 116124);
insert into api_opening_platform.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`, `method`, `userId`) values ('叶耀杰', '何雨泽', 'www.meaghan-stehr.org', '何建辉', '何炎彬', 0, '郭文博', 810369055);
insert into api_opening_platform.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`, `method`, `userId`) values ('叶文昊', '吕昊天', 'www.charita-halvorson.net', '赖文昊', '吴浩宇', 0, '毛峻熙', 46703930);
insert into api_opening_platform.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`, `method`, `userId`) values ('贾子默', '侯哲瀚', 'www.krystyna-deckow.io', '郑懿轩', '洪昊焱', 0, '曾绍齐', 9804923987);
insert into api_opening_platform.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`, `method`, `userId`) values ('方昊天', '彭耀杰', 'www.hayden-hickle.io', '秦立果', '薛思远', 0, '覃智宸', 5194709844);
insert into api_opening_platform.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`, `method`, `userId`) values ('万晓博', '王鑫鹏', 'www.jerry-stokes.name', '毛智渊', '朱烨磊', 0, '李立果', 5);
insert into api_opening_platform.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`, `method`, `userId`) values ('谭哲瀚', '金志泽', 'www.zachery-bashirian.co', '曾致远', '顾鑫鹏', 0, '洪立辉', 63626);
insert into api_opening_platform.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`, `method`, `userId`) values ('史语堂', '邵弘文', 'www.luvenia-denesik.net', '方鑫鹏', '卢熠彤', 0, '杜智宸', 32460);
insert into api_opening_platform.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`, `method`, `userId`) values ('段晋鹏', '石潇然', 'www.dione-rippin.name', '孟旭尧', '杨俊驰', 0, '郭子轩', 19027);
insert into api_opening_platform.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`, `method`, `userId`) values ('严明轩', '毛雪松', 'www.homer-kautzer.biz', '何文博', '夏瑾瑜', 0, '洪健柏', 8899);
insert into api_opening_platform.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`, `method`, `userId`) values ('董峻熙', '马昊然', 'www.kevin-howe.info', '刘昊天', '尹天宇', 0, '彭凯瑞', 556);
insert into api_opening_platform.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`, `method`, `userId`) values ('陆弘文', '郝雪松', 'www.jae-mertz.biz', '高正豪', '袁峻熙', 0, '萧健雄', 2856);
insert into api_opening_platform.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`, `method`, `userId`) values ('张瑾瑜', '赵乐驹', 'www.hugo-bogisich.biz', '张笑愚', '郑伟祺', 0, '龚凯瑞', 3649);


create table if not exists api_opening_platform.`user_interface_info`
(
    `id` bigint not null auto_increment comment '主键' primary key,
    `userId` bigint not null comment '调用该接口的用户的id',
    `interfaceInfoId` bigint not null comment '被调用的接口的id',
    `totalNum` int default 0 not null comment '总调用次数',
    `leftNum` int default 0 not null comment '剩余调用次数',
    `status` int default 0 not null comment '0-正常，1-禁用',
    `createTime` datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    `updateTime` datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    `isDelete` tinyint default 0 not null comment '是否删除(0-未删, 1-已删)'
) comment '用户调用接口信息';