package com.daehanins.mes.biz.work.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class UsageByItemVo {

    private String startDate;

    private String endDate;

    private String processCd;

}
