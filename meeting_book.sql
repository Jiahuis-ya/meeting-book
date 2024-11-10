create database meeting_book;
use meeting_book;

-- 用户表
DROP TABLE IF EXISTS `user`;
create table `user` (
                      `id` int not null auto_increment comment 'ID',
                      `username` varchar(20) not null unique comment '用户名',
                      `user_role` varchar(5) default 'USER' comment '用户角色(ADMIN:管理员，USER:普通用户)',
                      `phone` varchar(11) comment '手机号',
                      `department` varchar(20)  comment '部门',
                      `position` varchar(20)  comment '职位',
                      `password` varchar(32)  comment '密码',
                      `nickname` varchar(10)  default '' comment '昵称',
                      `email` varchar(128) default '' comment '邮箱',
                      `user_pic` varchar(128) default '' comment '头像',
                      `create_time` datetime not null comment '创建时间',
                      `update_time` datetime not null comment '修改时间',
                      PRIMARY KEY (`id`) USING BTREE
) comment '用户表';

alter table `user` modify column `phone` varchar(11) comment '手机号' default null;

insert into `user` values (1, '吴佳', 'ADMIN', '13517894890', '技术部', '产品经理', 'e10adc3949ba59abbe56e057f20f883e', 'joyce', 'wujia@163.com', 'https://big-event-joyce.oss-cn-beijing.aliyuncs.com/c24e6718-3e32-43d5-bc9e-38fa369f0031.jpg', '2024-08-21 12:17:26', '2024-08-23 12:17:26');
insert into `user` values (2, '陈明',  'USER', '13545673890', '技术部', '前端', 'e10adc3949ba59abbe56e057f20f883e', 'Tom', 'tom@163.com', '', '2024-08-21 12:17:26', '2024-08-23 12:17:26');
insert into `user` values (3, '吴昕', 'ADMIN', '13517866666', '技术部', '前端', 'e10adc3949ba59abbe56e057f20f883e', 'xin', 'xin@163.com', '', '2024-08-21 12:17:26', '2024-08-23 12:17:26');
insert into `user` values (4, '陈梅',  'USER', '13566673890', '技术部', '前端', 'e10adc3949ba59abbe56e057f20f883e', 'Mei', 'tom@163.com', '', '2024-08-21 12:17:26', '2024-08-23 12:17:26');


-- 会议室 
DROP TABLE IF EXISTS `room`;
create table `room` (
  `id` int not null auto_increment comment 'ID',
  `roomname` varchar(255) not null comment '会议室名称',
  `capacity` int comment '容纳人数',
  `location` varchar(255) not null comment '地点',
  `username` varchar(20) comment '负责人',
  `userid` int comment '负责人id',
  `phone` varchar(11) not null comment '负责人电话',
  `creater` varchar(20) comment '创建人',
  `createrid` int comment '创建人id',
  `create_time` datetime not null comment '创建时间',
  `update_time` datetime not null comment '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  constraint fk_room_user foreign key (`createrid`) references `user`(`id`) -- 外键约束
) comment '会议室';

INSERT INTO `room` (`id`, `roomname`, `capacity`, `location`, `username`, `userid`, `phone`, `creater`, `createrid`, `create_time`, `update_time`) VALUES  
	(1, '第一会议室', 50, '办公大楼3楼',  '陈明', 2, '13517894890','吴佳', 1, '2023-04-01 08:00:00', '2023-04-01 08:00:00'),  
	(2, '第二会议室', 30, '办公大楼2楼', '陈梅', 4, '13517866666','吴昕', 3, '2023-04-02 09:30:00', '2023-04-02 09:30:00'),  
	(3, '第三会议室', 20, '科研楼A区', '陈明', 2, '13517894890','吴佳', 1, '2023-04-03 10:15:00', '2023-04-03 10:15:00'),  
	(4, '第四会议室（小型）', 10, '办公大楼1楼', '陈梅', 4, '13517866666', '吴昕', 3,'2023-04-04 11:45:00', '2023-04-04 11:45:00'),  
	(5, '多功能厅', 100, '会议中心', '陈明', 2, '13517894890', '吴佳', 1, '2023-04-05 14:00:00', '2023-04-05 14:00:00');

select * from room;

-- 会议室预约表
DROP TABLE IF EXISTS `book`;
create table `book` (
  `id` int not null auto_increment comment '预约ID',
  `roomid` int comment '会议室id',
  `bookstate` varchar(5) default '已预约' comment '预约状态(“已预约”、“已取消”)',
  `state` varchar(5) default '待审批' comment '审核状态(“待审批”、“已批准”、“不批准”)',
  `bookerid` int comment '预约人id',
  `start_time` datetime not null comment '开始时间',
  `end_time` datetime not null comment '结束时间',
  `create_time` datetime not null comment '创建时间',
  `update_time` datetime not null comment '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  constraint fk_book_user foreign key (`bookerid`) references `user`(`id`), -- 外键约束
  constraint fk_book_room foreign key (`roomid`) references `room`(`id`) -- 外键约束
) comment '会议室预约表';



INSERT INTO `book` (`roomid`,  `bookstate`,  `state`,   `bookerid`,   `start_time`,  `end_time`,  `create_time`,`update_time`) VALUES  
					(1,  '已预约', '已批准', 1,  '2023-04-01 09:00:00', '2023-04-01 11:00:00', NOW(), NOW()),  
					(2,  '已取消', '待审批', 1,  '2023-04-02 14:00:00', '2023-04-02 16:00:00', NOW(), NOW()),  
					(3,   '已预约',  '不批准',  1, '2023-04-03 10:00:00', '2023-04-03 12:00:00', NOW(), NOW()),  
					(3, '已预约',  '待审批',  2, '2023-04-03 10:00:00', '2023-04-03 12:00:00', NOW(), NOW()),  
					(3,'已预约',  '待审批',  2, '2023-04-03 10:00:00', '2023-04-03 12:00:00', NOW(), NOW()), 
					(3,   '已预约',  '待审批',  2, '2023-04-03 10:00:00', '2023-04-03 12:00:00', NOW(), NOW()),
					( 4, '已预约',  '已批准',  2,  '2023-04-04 15:00:00', '2023-04-04 17:00:00', NOW(), NOW());









