package com.daehanins.mes.biz.tag.vo;

import com.daehanins.mes.biz.tag.entity.TagValue;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class EquipRunTagValue {

    private String tagValueId;

    private String tagCd;

    private BigDecimal measureValue;

    private String stringValue;

    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private LocalDateTime measureTime;

    private int diff;

}
