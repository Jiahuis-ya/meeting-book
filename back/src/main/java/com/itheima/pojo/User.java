package com.itheima.pojo;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.groups.Default;
import lombok.Data;

//lombok  在编译阶段,为实体类自动生成setter  getter toString
// 1.pom文件中引入依赖   2.在实体类上添加注解@Data

//在实体参数完成校验@NotNull、@NotEmpty、@Pattern、@Email
@Data
public class User {
    @NotNull(groups = Update.class)
    private Integer id;//主键ID
    private String username;//用户名
    private String userRole;//用户角色
    private String phone;// 手机
    private String department;//部门
    private String position;//职位

    @JsonIgnore//让springmvc把当前对象转换成json字符串的时候,忽略password,最终的json字符串中就没有password这个属性了
    private String password;//密码

    @NotEmpty // 非空校验并且不能是空字符串
    @Pattern(regexp = "^\\S{1,10}$") // 内容不能为空（满足1-10的非空字符）
    private String nickname;//昵称

    @NotEmpty
    @Email // 满足邮箱格式
    private String email;//邮箱
    private String userPic;//用户头像地址

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;//创建时间

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;//更新时间

    public interface Add extends Default {

    }

    public interface Update extends Default{

    }
}
