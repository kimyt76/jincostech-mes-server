package com.daehanins.mes.biz.mat.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
* <p>
* MatPointStockView 엔티티
* </p>
*
* @author jeonsj
* @since 2020-09-01
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mat_point_stock_view")
public class MatPointStockView {

    @TableId
    private String matPointStockId;

    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate stockDate;

    private String areaCd;

    private String areaName;

    private String storageCd;

    private String storageName;

    private Integer itemCount;

    private BigDecimal sumQty;

    private String procYn;

}
