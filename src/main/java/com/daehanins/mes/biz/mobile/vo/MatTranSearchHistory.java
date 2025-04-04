package com.daehanins.mes.biz.mobile.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class MatTranSearchHistory {

    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate tranDate;

    private String tranNo;

    private String tranCd;

    private String tranName;

    private String itemCd;

    private String itemName;

    private String testNo;

    private String lotNo;

    private String srcStorageName;

    private String destStorageName;

    private BigDecimal qty;

    private String itemMemo;

}
