package com.daehanins.mes.biz.common.vo;

import lombok.Data;

@Data
public class PoSheetMailParam {
    String matOrderId;
    String receiver;
    String receiverEmail;
    String receiverCc;
    String senderEmail;
    String memo;
}
