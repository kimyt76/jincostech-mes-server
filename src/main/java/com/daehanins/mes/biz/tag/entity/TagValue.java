package com.daehanins.mes.biz.tag.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * TagValue 엔티티
 * </p>
 *
 * @author jeonsj
 * @since 2021-04-08
 */
@Data
@Accessors(chain = true)
@TableName("tag_value")
public class TagValue {

    private static final long serialVersionUID = 1L;

    @TableId
    private String tagValueId;

    private String tagCd;

    private BigDecimal measureValue;

    private String stringValue;

    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private LocalDateTime measureTime;

    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private LocalDateTime regTime;


}
