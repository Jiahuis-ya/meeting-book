package com.itheima.mapper;

import com.itheima.pojo.Room;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RoomMapper {


  // @Insert("insert into room(`roomname`, `capacity`, `location`, `username`, `userid`, `phone`, `create_time`, `update_time`)" +
  @Insert("insert into room(roomname, capacity, location, username, userid, phone, creater, createrid, create_time, `update_time`)" +
          " values(#{roomname},#{capacity},#{location},#{username},#{userid},#{phone},#{creater},#{createrid},now(),now())")
  void add(Room room);

  @Update("update room set roomname=#{roomname},capacity=#{capacity},location=#{location},username=#{username},userid=#{userid},phone=#{phone},update_time=now() where id=#{id}")
  void update(Room room);

  //  查询的语句在resources/com.itheima.mapper/RoomMapper
  List<Room> list(String roomname, String username);

  @Delete("delete from room where id=#{id}")
  void deleteById(Integer id);


}
