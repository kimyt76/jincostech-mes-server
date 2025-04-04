package com.daehanins.mes.biz.pub.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
* <p>
* ScaleValueView 엔티티
* </p>
*
* @author jeonsj
* @since 2022-10-28
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("scale_value_view")
public class ScaleValueView {

    private Long scaleValueId;

    private String areaCd;

    private String areaName;

    private String storageCd;

    private String storageName;

    private String scaleCd;

    private String scaleName;

    private String scaleNickname;

    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd HH:mm:ss.SSS")
    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss.SSS")
    private LocalDateTime measureTime;

    private BigDecimal measureValue;


}
