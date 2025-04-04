package com.daehanins.mes.biz.pub.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.daehanins.mes.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * ScaleValue 엔티티
 * </p>
 *
 * @author jeonsj
 * @since 2022-10-28
 */
@Data
@Accessors(chain = true)
@TableName("scale_value")
public class ScaleValue {

    private static final long serialVersionUID = 1L;

    @TableId(value = "scale_value_id", type = IdType.AUTO)
    private Long scaleValueId;

    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd HH:mm:ss.SSS")
    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss.SSS")
    private LocalDateTime measureTime;

    private String scaleCd;

    private BigDecimal measureValue;

}
