package com.daehanins.mes.biz.mat.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class WeighClosingDetailItem {

    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate tranDate;

    private String tranCd;

    private String tranName;

    private String srcStorageCd;

    private String srcStorageName;

    private String destStorageCd;

    private String destStorageName;

    private String testNo;

    private BigDecimal qty;

    private String prodNo;

    private String itemCd;

    private String itemName;

}
