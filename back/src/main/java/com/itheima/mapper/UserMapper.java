package com.itheima.mapper;

import com.itheima.pojo.Article;
import com.itheima.pojo.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {
  //根据用户名查询用户
  @Select("select * from user where username=#{username}")
  User findByUserName(String username);

  //添加(id不用写，直接让它自动添加就可以了)
//  now()获取当前数据库的当前时间
  @Insert("insert into user(username,password,create_time,update_time)" +
          " values(#{username},#{password},now(),now())")
  void add(String username, String password);

  @Update("update user set user_role=#{userRole},department=#{department},position=#{position},email=#{email},update_time=now() where id=#{id}")
  void update(User user);

  @Update("update user set user_pic=#{avatarUrl},update_time=now() where id=#{id}")
  void updateAvatar(String avatarUrl,Integer id);

  @Update("update user set password=#{md5String},update_time=now() where id=#{id}")
  void updatePwd(String md5String, Integer id);

  //  查询的语句在resources/com.itheima.mapper/ArticleMapper
  List<User> list(Integer userId, String username, String phone);

  @Delete("delete from user where id=#{id}")
  void deleteById(Integer id);

  //  管理员手动加用户
  @Insert("insert into user(username,user_role,phone,department,position,password,nickname,email,user_pic,create_time,update_time)" +
          " values(#{username},#{userRole},#{phone},#{department},#{position},#{password},#{nickname},#{email},#{userPic},now(),now())")
  void adminAdd(User user);

}
