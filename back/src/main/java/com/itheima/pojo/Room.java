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

@Data
public class Room {
    @NotNull(groups = Update.class)
    private Integer id; //主键ID
    private String roomname; // 会议室名称
    private String capacity; // 容纳人数
    private String location; // 地点
    private String username; // 负责人
    private Integer userid; // 负责人id
    private String phone; // 负责人电话
    private String creater; // 创建人
    private Integer createrid; // 创建人id

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime; //创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime; //更新时间

    public interface Add extends Default {

    }

    public interface Update extends Default{

    }
}
