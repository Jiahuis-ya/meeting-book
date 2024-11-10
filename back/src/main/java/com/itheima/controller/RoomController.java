package com.itheima.controller;

import com.itheima.pojo.Article;
import com.itheima.pojo.PageBean;
import com.itheima.pojo.Result;
import com.itheima.pojo.Room;
import com.itheima.service.RoomService;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import com.itheima.utils.Md5Util;
import com.itheima.utils.JwtUtil;
import com.itheima.utils.ThreadLocalUtil;
//import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@RestController
@RequestMapping("/room")
@Validated
public class RoomController {
  @Autowired
  private RoomService roomService;
  @Autowired
  private StringRedisTemplate stringRedisTemplate;

  @PostMapping("/add") //  注册接口的请求方式是post
  public Result add(@RequestBody @Validated Room room) {
      roomService.add(room);
      return Result.success();
  }

  @GetMapping("/list")
  public Result<PageBean<Room>> list(
          Integer pageNum,
          Integer pageSize,
          @RequestParam(required = false) String roomname,
          @RequestParam(required = false) String username
  ) {
    PageBean<Room> pb =  roomService.list(pageNum,pageSize,roomname,username);
    return Result.success(pb);
  }

   @PutMapping("/update")
    public Result update(@RequestBody @Validated Room room) {
        roomService.update(room);
        return Result.success();
    }


  @DeleteMapping("/delete")
  public Result delete(Integer id){
    roomService.deleteById(id);
    return Result.success();
  }

}