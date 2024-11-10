package com.itheima.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.groups.Default;
import lombok.Data;

import java.time.LocalDateTime;

//@NotNull :不能不传
//@NotEmpty：必须要传， 如果是字符串，不能是空字符串
@Data
public class Category {

    @NotNull(groups = Update.class) // 只校验更新的时候id不能为空，新增不用校验
    private Integer id;//主键ID

    @NotEmpty
    // @Pattern(regexp = "^\\S{1,10}$", groups={Add.class, Update.class})
    private String categoryName;//分类名称

    @NotEmpty
    // @Pattern(regexp = "^\\S{1,10}$", groups={Add.class, Update.class})
    private String categoryAlias;//分类别名

    private Integer createUser;//创建人ID

    //  将事件对象2024-08-21T12:44:34转换成json字符串   指定日期格式
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;//创建时间

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;//更新时间

    //如果说某个校验项没有指定分组,默认属于Default分组
    //分组之间可以继承, A extends B  那么A中拥有B中所有的校验项


    public interface Add extends Default {

    }

    public interface Update extends Default{

    }
}
