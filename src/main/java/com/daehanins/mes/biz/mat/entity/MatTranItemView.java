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
import java.time.LocalDateTime;

/**
* <p>
* MatTranItemView 엔티티
* </p>
*
* @author jeonsj
* @since 2020-04-13
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mat_tran_item_view")
public class MatTranItemView {

    @TableId
    private String matTranItemId;

    private String matTranId;

    private String itemTypeCd;

    private String itemCd;

    private String itemName;

    private String spec;

    private String lotNo;

    private String testNo;

    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate expiryDate;

    private BigDecimal qty;

    private BigDecimal price;

    private BigDecimal supplyAmt;

    private BigDecimal vat;

    private String memo;

    private String confirmState;
}
