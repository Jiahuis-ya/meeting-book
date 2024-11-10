package com.itheima.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.mapper.RoomBookMapper;
import com.itheima.pojo.PageBean;
import com.itheima.pojo.RoomBook;
import com.itheima.pojo.RoomBookDetailDTO;
import com.itheima.service.RoomBookService;
import com.itheima.utils.Md5Util;
import com.itheima.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service //@Service： 将Service的实现对象交给IOD容器
public class RoomBookServiceImpl implements RoomBookService {
  @Autowired
  private RoomBookMapper roomBookMapper;

  @Override
  public void add(RoomBook roomBook) {
    roomBookMapper.add(roomBook);
  }

  @Override
  public PageBean<RoomBookDetailDTO> listApprove(
    Integer pageNum, 
    Integer pageSize, 
    String roomname, 
    String booker, 
    String bookphone,
    Integer bookerid
  ) {
    //1.创建PageBean对象
    PageBean<RoomBookDetailDTO> pb = new PageBean<>();

    //2.开启分页查询 PageHelper
    PageHelper.startPage(pageNum,pageSize);

    //3.调用mapper
    Map<String,Object> map = ThreadLocalUtil.get();
    List<RoomBookDetailDTO> as = roomBookMapper.listApprove(roomname, booker, bookphone, bookerid);
    Page<RoomBookDetailDTO> p = (Page<RoomBookDetailDTO>) as;

    pb.setTotal(p.getTotal());
    pb.setItems(p.getResult());
    return pb;
  }

  @Override
  public PageBean<RoomBookDetailDTO> listRecord(
          Integer pageNum,
          Integer pageSize,
          String roomname,
          String booker,
          String bookphone,
          Integer bookerid
  ) {
    //1.创建PageBean对象
    PageBean<RoomBookDetailDTO> pb = new PageBean<>();

    //2.开启分页查询 PageHelper
    PageHelper.startPage(pageNum,pageSize);

    //3.调用mapper
    Map<String,Object> map = ThreadLocalUtil.get();
    List<RoomBookDetailDTO> as = roomBookMapper.listRecord(roomname, booker, bookphone, bookerid);
    Page<RoomBookDetailDTO> p = (Page<RoomBookDetailDTO>) as;

    pb.setTotal(p.getTotal());
    pb.setItems(p.getResult());
    return pb;
  }

  @Override
  public void deleteById(Integer id) {
    roomBookMapper.deleteById(id);
  }

  // 取消预约
  @Override
  public void cancel(RoomBook roomBook) {
    roomBookMapper.cancel(roomBook);
  };

  // 审核通过&&不通过
  @Override
  public void approve(RoomBook roomBook) {
    roomBookMapper.approve(roomBook);
  };
}
