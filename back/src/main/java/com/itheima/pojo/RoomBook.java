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
public class RoomBook {
    @NotNull(groups = Update.class)
    private Integer id; //预约ID
    private Integer roomid; // 会议室id
    private String bookstate; // 预约状态(“已预约”、“已取消”),
    private String state; // 审核状态(“待审批”、“已批准”、“未批准”)
    private Integer bookerid; //预约人id

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime; //开始时间

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime; //结束时间

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime; //创建时间

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime; //更新时间

    public interface Add extends Default {

    }

    public interface Update extends Default{

    }
}
