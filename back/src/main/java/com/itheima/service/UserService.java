package com.itheima.service;

import com.itheima.pojo.Article;
import com.itheima.pojo.PageBean;
import com.itheima.pojo.User;

public interface UserService {
  //根据用户名查询用户
  User findByUserName(String username);

  //注册
  void register(String username, String password);

  //更新
  void update(User user);

  //更新头像
  void updateAvatar(String avatarUrl);

  //更新密码
  void updatePwd(String newPwd);

//    条件分页列表查询--获取全部用户
PageBean<User> list(Integer pageNum, Integer pageSize, Integer userId, String username, String phone);
  void deleteById(Integer id);

  void adminAdd(User user);
}
