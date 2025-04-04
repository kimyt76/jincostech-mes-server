package com.daehanins.mes.biz.work.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ConsumptionProdItem {

    private String bomVer;

    private String goodsBomId;

    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate inputDate;

    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate orderDate;

    private String itemCd;

    private String itemName;

    private String unit;

    private BigDecimal prodQty;

    private BigDecimal stdQty;

    private String memo;

    private String checked;

}
