package com.itheima.service;

import com.itheima.pojo.PageBean;
import com.itheima.pojo.RoomBook;
import com.itheima.pojo.RoomBookDetailDTO;

public interface RoomBookService {

  void add(RoomBook roomBook);

  // 条件分页列表查询--获取预约审核
  PageBean<RoomBookDetailDTO> listApprove(
    Integer pageNum,
    Integer pageSize,
    String roomname,
    String booker,
    String bookphone,
    Integer bookerid
  );

  // 条件分页列表查询--获取预约记录
  PageBean<RoomBookDetailDTO> listRecord(
          Integer pageNum,
          Integer pageSize,
          String roomname,
          String booker,
          String bookphone,
          Integer bookerid
  );



  void deleteById(Integer id);

  // 取消预约
  void cancel(RoomBook roomBook);

  // 审核通过&&不通过
  void approve(RoomBook roomBook);
}
