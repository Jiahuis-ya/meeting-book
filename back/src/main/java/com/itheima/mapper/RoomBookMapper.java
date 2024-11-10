package com.itheima.mapper;

import com.itheima.pojo.RoomBook;
import com.itheima.pojo.RoomBookDetailDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RoomBookMapper {

  @Insert("insert into book(roomid, bookstate, state, bookerid,  start_time, end_time,  create_time, `update_time`)" +
          " values(#{roomid},#{bookstate},#{state},#{bookerid}, #{startTime}, #{endTime}, now(),now())")
  void add(RoomBook roomBook);

// 获取预约审核
  //  查询的语句在resources/com.itheima.mapper/RoomMapper
   List<RoomBookDetailDTO> listApprove(String roomname, String booker, String bookphone, Integer bookerid);

//  获取预约记录
  List<RoomBookDetailDTO> listRecord(String roomname, String booker, String bookphone, Integer bookerid);


  @Delete("delete from book where id=#{id}")
  void deleteById(Integer id);

  // 取消预约
  @Update("update book set bookstate=#{bookstate}, update_time=now() where id=#{id}")
  void cancel(RoomBook roomBook);

  // 审核通过&&不通过
  @Update("update book set state=#{state}, update_time=now() where id=#{id}")
  void approve(RoomBook roomBook);

}
