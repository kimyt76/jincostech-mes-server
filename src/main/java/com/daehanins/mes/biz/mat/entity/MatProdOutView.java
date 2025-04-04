package com.daehanins.mes.biz.mat.entity;

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
* MatProdOutView 엔티티
* </p>
*
* @author jeonsj
* @since 2022-10-24
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mat_prod_out_view")
public class MatProdOutView {

    private String matTranId;

    private String areaCd;

    private String areaName;

    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate tranDate;

    private String tranCd;

    private String itemNameStr;

    private String itemCdJoin;

    private String itemNameJoin;

    private String srcStorageCd;

    private String srcStorageName;

    private Integer cnt;

    private BigDecimal sumQty;

    private String processCd;

    private String prodNo;

    private String lotNo;

    private String lotNo2;

    private String itemCd;

    private String itemName;

    private String memo;

}
