package com.daehanins.mes.biz.work.entity;

import com.baomidou.mybatisplus.annotation.TableId;
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
* ProdIngView 엔티티
* </p>
*
* @author jeonsj
* @since 2020-07-28
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("prod_ing_view")
public class ProdIngView {

    @TableId
    private String prodIngId;

    private String workOrderItemId;

    private String prodState;

    private Double rpm1;
    private Double rpm2;
    private Double rpm3;

    private Double temperature;

    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private LocalDateTime startDatetime;
    private String startDate;
    private String startTime;

    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private LocalDateTime endDatetime;
    private String endDate;
    private String endTime;

}
