package com.daehanins.mes.biz.pub.entity;

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
 * ScaleInfo 엔티티
 * </p>
 *
 * @author jeonsj
 * @since 2022-10-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("scale_info")
public class ScaleInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId
    private String scaleCd;

    private String scaleName;

    private String scaleNickname;

    private String scaleType;

    private String storageCd;

    private String nportAddress;

    private Integer responsePortNo;

    private Integer serialPortNo;

    private String model;

    private String equipNo;

    private String memo;

    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd HH:mm:ss.SSS")
    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss.SSS")
    private LocalDateTime lastTime;

    private BigDecimal lastValue;

}
