package com.daehanins.mes.biz.common.vo;

import lombok.Data;

@Data
public class PoSheetMailVo {
    String customerName;
    String orderNo;
    String orderDate;
    String senderEmail;
    String memo;
}
