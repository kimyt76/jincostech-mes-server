package com.daehanins.mes.biz.qt.vo;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ReqPrinting {

    private String testNo;
    private BigDecimal qty;
    private int printCnt;

}
