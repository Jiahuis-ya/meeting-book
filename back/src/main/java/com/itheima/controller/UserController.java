package com.itheima.controller;

import com.itheima.pojo.Article;
import com.itheima.pojo.PageBean;
import com.itheima.pojo.Result;
import com.itheima.pojo.User;
import com.itheima.service.UserService;
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
@RequestMapping("/user")
@Validated
public class UserController {
  @Autowired
  private UserService userService;
  @Autowired
  private StringRedisTemplate stringRedisTemplate;

  @PostMapping("/register") //  注册接口的请求方式是post
//  5-16位的非空字符
  public Result register(@Pattern(regexp = "^\\S{2,16}$") String username, @Pattern(regexp = "^\\S{2,16}$") String password) {

    //查询用户
    User u = userService.findByUserName(username);
    if (u == null) {
      //没有占用
      //注册
      userService.register(username, password);
      return Result.success();
    } else {
      //占用
      return Result.error("用户名已被占用");
    }
  }



  @PostMapping("/login")
  public Result<String> login(@Pattern(regexp = "^\\S{2,16}$") String username, @Pattern(regexp = "^\\S{5,16}$") String password) {
    //根据用户名查询用户
    User loginUser = userService.findByUserName(username);
    //判断该用户是否存在
    if (loginUser == null) {
      return Result.error("用户名错误");
    }

    //判断密码是否正确  loginUser对象中的password是密文
    if (Md5Util.getMD5String(password).equals(loginUser.getPassword())) {
      //登录成功
      Map<String, Object> claims = new HashMap<>();
      claims.put("id", loginUser.getId());
      claims.put("username", loginUser.getUsername());
      String token = JwtUtil.genToken(claims);
      //把token存储到redis中
      ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
      operations.set(token,token,1, TimeUnit.HOURS); // 将token设置为键也设置为值，过期时间1小时
      return Result.success(token);
    }
    return Result.error("密码错误");
  }

  @GetMapping("/userInfo")
  public Result<User> userInfo(/*@RequestHeader(name = "Authorization") String token*/) {
    //根据用户名查询用户
//        Map<String, Object> map = JwtUtil.parseToken(token);
//        String username = (String) map.get("username");
    Map<String, Object> map = ThreadLocalUtil.get();
    String username = (String) map.get("username");
    User user = userService.findByUserName(username);
    return Result.success(user);
  }

  @GetMapping("/list")
  public Result<PageBean<User>> list(
          Integer pageNum,
          Integer pageSize,
          @RequestParam(required = false) Integer userId,
          @RequestParam(required = false) String username,
          @RequestParam(required = false) String phone
  ) {
    PageBean<User> pb =  userService.list(pageNum,pageSize,userId,username,phone);
    return Result.success(pb);
  }

   @PutMapping("/update")
    public Result update(@RequestBody @Validated User user) {
        userService.update(user);
        return Result.success();
    }

    @PatchMapping("updateAvatar")
//    @URL用于校验这个数据是否是URL地址
    public Result updateAvatar(@RequestParam @URL String avatarUrl) {
        userService.updateAvatar(avatarUrl);
        return Result.success();
    }

//    mvc自动将json数据转换为map集合对象
//  才能自动读取请求体里面的json格式数据然后转换为map对象
@PatchMapping("/updatePwd")
public Result updatePwd(@RequestParam Map<String, String> params, @RequestHeader("Authorization") String token) {
  //1.校验参数
  String oldPwd = params.get("old_pwd");// 旧的密码
  String newPwd = params.get("new_pwd");// 新的密码
  String rePwd = params.get("re_pwd"); // 确认的密码

  if (!StringUtils.hasLength(oldPwd) || !StringUtils.hasLength(newPwd) || !StringUtils.hasLength(rePwd)) {
    return Result.error("缺少必要的参数");
  }

  //原密码是否正确
  //调用userService根据用户名拿到原密码,再和old_pwd比对
  Map<String,Object> map = ThreadLocalUtil.get();
  String username = (String) map.get("username");
  User loginUser = userService.findByUserName(username);
//  loginUser.getPassword()是一个加密过的密码，oldPwd是未加密过的密码
//  要再将原密码进行加密过后再比对
  if (!loginUser.getPassword().equals(Md5Util.getMD5String(oldPwd))){
    return Result.error("原密码填写不正确");
  }

  //newPwd和rePwd是否一样
  if (!rePwd.equals(newPwd)){
    return Result.error("两次填写的新密码不一样");
  }

  //2.调用service完成密码更新
  userService.updatePwd(newPwd);

  //删除redis中对应的token
  ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
  operations.getOperations().delete(token);

  return Result.success();

  }

  @DeleteMapping("/delete")
  public Result delete(Integer id){
    userService.deleteById(id);
    return Result.success();
  }

  @PostMapping("/adminAdd") //  注册接口的请求方式是post
  public Result adminAdd(@RequestBody @Validated User user) {
      userService.adminAdd(user);
      return Result.success();
  }
}