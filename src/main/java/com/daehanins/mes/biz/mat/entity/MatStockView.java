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
* MatStockView 엔티티
* </p>
*
* @author jeonsj
* @since 2020-05-13
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mat_stock_view")
public class MatStockView {

    @TableId
    private String matStockId;

    private String areaCd;

    private String storageCd;

    private String storageName;

    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate stockDate;

    private String memberName;

    private String memberCd;

    private String stockState;

    private String stockStateName;

    private Integer itemCnt;

    private String adjYn;

    private String endYn;

    private String matTranId;

    private String tranNo;

    private BigDecimal sumAccountQty;

    private BigDecimal sumCheckQty;

    private BigDecimal sumRealQty;

    private BigDecimal diffQty;

    private BigDecimal sumAdjustQty;

}
