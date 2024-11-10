package com.itheima.service;

import com.itheima.pojo.Article;
import com.itheima.pojo.PageBean;
import com.itheima.pojo.Room;

public interface RoomService {

  void add(Room room);

  // 条件分页列表查询--获取全部会议室
  PageBean<Room> list(Integer pageNum, Integer pageSize, String roomname, String username);
  
  //更新
  void update(Room room);
  
  void deleteById(Integer id);

}
