package com.itheima.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.mapper.UserMapper;
import com.itheima.pojo.Article;
import com.itheima.pojo.PageBean;
import com.itheima.pojo.User;
import com.itheima.service.UserService;
import com.itheima.utils.Md5Util;
import com.itheima.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service //@Service： 将Service的实现对象交给IOD容器
public class UserServiceImpl implements UserService {
  @Autowired
  private UserMapper userMapper;
  @Override
  public User findByUserName(String username) {
    User u = userMapper.findByUserName(username);
    return u;
  }

  @Override
  public void register(String username, String password) {
    //加密
    String md5String = Md5Util.getMD5String(password);
    //添加
    userMapper.add(username,md5String);
  }

  @Override
  public void update(User user) {
    user.setUpdateTime(LocalDateTime.now()); // 获取到系统当前时间
    userMapper.update(user);
  }

  @Override
  public void updateAvatar(String avatarUrl) {
    Map<String,Object> map = ThreadLocalUtil.get();
    Integer id = (Integer) map.get("id"); // 获取用户id
    userMapper.updateAvatar(avatarUrl,id);
  }

  @Override
  public void updatePwd(String newPwd) {
    Map<String,Object> map = ThreadLocalUtil.get();
    Integer id = (Integer) map.get("id");
    userMapper.updatePwd(Md5Util.getMD5String(newPwd),id);
  }

  @Override
  public PageBean<User> list(Integer pageNum, Integer pageSize, Integer userId, String username, String phone) {
    //1.创建PageBean对象
    PageBean<User> pb = new PageBean<>();

    //2.开启分页查询 PageHelper
    PageHelper.startPage(pageNum,pageSize);

    //3.调用mapper
    Map<String,Object> map = ThreadLocalUtil.get();
//    Integer userId = (Integer) map.get("id");
    List<User> as = userMapper.list(userId,username,phone);
    //Page中提供了方法,可以获取PageHelper分页查询后 得到的总记录条数和当前页数据
    Page<User> p = (Page<User>) as;
//        PageHelper查询最后返回的就是Page对象，这个Page是List的实现类，只有向下强转才能去调用子类的方法

    //把数据填充到PageBean对象中
    pb.setTotal(p.getTotal());
    pb.setItems(p.getResult());
    return pb;
  }

  @Override
  public void deleteById(Integer id) {
    userMapper.deleteById(id);
  }

  @Override
  public void adminAdd(User user) {
    userMapper.adminAdd(user);
  }


}
