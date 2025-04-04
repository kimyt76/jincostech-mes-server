package com.daehanins.mes.base;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * @author jeonsj
 */
@Data
public abstract class BaseEntity implements Serializable{

    @TableField(fill = FieldFill.INSERT)
    private String regId;

    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime regTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updId;

    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updTime;

}
