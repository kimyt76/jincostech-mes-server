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
* MatStockRealView 엔티티
* </p>
*
* @author jeonsj
* @since 2020-05-14
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mat_stock_real_view")
public class MatStockRealView {

    @TableId
    private String matStockRealId;

    private String matStockId;

    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate stockDate;

    private Integer serNo;

    private String stockRealNo;

    private String memberCd;

    private String memberName;

    private Integer itemCnt;

    private BigDecimal sumRealQty;

    private BigDecimal sumAddQty;

    private String areaCd;

    private String storageCd;

    private String storageName;

    private String endYn;

}
