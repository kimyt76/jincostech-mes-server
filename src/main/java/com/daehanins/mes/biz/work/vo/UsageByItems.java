package com.daehanins.mes.biz.work.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class UsageByItems {

    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate prodDate;

    private String prodNo;

    private String lotNo;

    private String prodItemCd;

    private String prodItemName;

    private String prod_unit;

    private BigDecimal orderQty;

    private String itemCd;

    private String itemName;

    private BigDecimal qty;

    private String unit;

    private String spec;

}
