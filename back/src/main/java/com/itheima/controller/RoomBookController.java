package com.itheima.controller;

import com.itheima.pojo.RoomBook;
import com.itheima.pojo.RoomBookDetailDTO;
import com.itheima.pojo.Result;
import com.itheima.pojo.PageBean;
import com.itheima.service.RoomBookService;
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
@RequestMapping("/roomBook")
@Validated
public class RoomBookController {
  @Autowired
  private RoomBookService roomBookService;
  @Autowired
  private StringRedisTemplate stringRedisTemplate;

  @PostMapping("/add") //  注册接口的请求方式是post
  public Result add(@RequestBody @Validated RoomBook roomBook) {
    roomBookService.add(roomBook);
    return Result.success();
  }

  //    预约审核列表
  @GetMapping("/listApprove")
  public Result<PageBean<RoomBookDetailDTO>> list(
          Integer pageNum,
          Integer pageSize,
          @RequestParam(required = false) String roomname,
          @RequestParam(required = false) String booker,
          @RequestParam(required = false) String bookphone,
          @RequestParam(required = false) Integer bookerid
  ) {
    PageBean<RoomBookDetailDTO> pb =  roomBookService.listApprove(pageNum,pageSize,roomname, booker, bookphone,bookerid);
    return Result.success(pb);
  }


  //    预约历史列表
  @GetMapping("/listRecord")
  public Result<PageBean<RoomBookDetailDTO>> listRecord(
          Integer pageNum,
          Integer pageSize,
          @RequestParam(required = false) String roomname,
          @RequestParam(required = false) String booker,
          @RequestParam(required = false) String bookphone,
          @RequestParam(required = false) Integer bookerid
  ) {
    PageBean<RoomBookDetailDTO> pb =  roomBookService.listRecord(pageNum,pageSize,roomname, booker, bookphone,bookerid);
    return Result.success(pb);
  }

  @DeleteMapping("/delete")
  public Result delete(Integer id){
    roomBookService.deleteById(id);
    return Result.success();
  }

  // 取消预约
    @PutMapping("/cancel")
    public Result cancel(@RequestBody @Validated RoomBook roomBook) {
        roomBookService.cancel(roomBook);
        return Result.success();
    }

    // 审核通过&&不通过
    @PutMapping("/approve")
    public Result approve(@RequestBody @Validated RoomBook roomBook) {
        roomBookService.approve(roomBook);
        return Result.success();
    }

}