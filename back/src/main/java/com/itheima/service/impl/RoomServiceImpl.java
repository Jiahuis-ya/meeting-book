package com.itheima.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.mapper.RoomMapper;
import com.itheima.pojo.Article;
import com.itheima.pojo.PageBean;
import com.itheima.pojo.Room;
import com.itheima.service.RoomService;
import com.itheima.utils.Md5Util;
import com.itheima.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service //@Service： 将Service的实现对象交给IOD容器
public class RoomServiceImpl implements RoomService {
  @Autowired
  private RoomMapper roomMapper;

  @Override
  public void add(Room room) {
    roomMapper.add(room);
  }

  @Override
  public PageBean<Room> list(Integer pageNum, Integer pageSize, String roomname, String username) {
    //1.创建PageBean对象
    PageBean<Room> pb = new PageBean<>();

    //2.开启分页查询 PageHelper
    PageHelper.startPage(pageNum,pageSize);

    //3.调用mapper
    Map<String,Object> map = ThreadLocalUtil.get();
    List<Room> as = roomMapper.list(roomname, username);
    Page<Room> p = (Page<Room>) as;

    pb.setTotal(p.getTotal());
    pb.setItems(p.getResult());
    return pb;
  }

  @Override
  public void update(Room room) {
    room.setUpdateTime(LocalDateTime.now()); // 获取到系统当前时间
    roomMapper.update(room);
  }


  @Override
  public void deleteById(Integer id) {
    roomMapper.deleteById(id);
  }



}
