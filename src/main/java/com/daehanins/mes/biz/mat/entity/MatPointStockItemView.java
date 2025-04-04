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
* MatPointStockItemView 엔티티
* </p>
*
* @author jeonsj
* @since 2020-09-02
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mat_point_stock_item_view")
public class MatPointStockItemView {

    @TableId
    private String matPointStockItemId;

    private String matPointStockId;

    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate stockDate;

    private String areaCd;
    private String storageCd;

    private String itemTypeCd;
    private String itemCd;
    private String itemName;
    private String lotNo;
    private String testNo;

    private BigDecimal stockQty;

}
