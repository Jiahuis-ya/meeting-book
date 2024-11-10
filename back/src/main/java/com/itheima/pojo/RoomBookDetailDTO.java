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
public class RoomBookDetailDTO {
    @NotNull(groups = Update.class)
    private Integer id; //预约ID
    private String roomname; // 会议室名称
    private Integer roomid; // 会议室id
    private String capacity; // 容纳人数
    private String location; // 地点
    private String username; // 负责人
    private Integer userid; // 负责人id
    private String userphone; // 负责人电话
    private String bookstate; // 预约状态(“已预约”、“已取消”),
    private String state; // 审核状态(“待审批”、“已批准”、“未批准”)
    private String booker; // 预约人
    private Integer bookerid; //预约人id
    private String bookphone; // 负责人电话

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
