package com.daehanins.mes.biz.work.vo;

import lombok.Data;

import java.util.List;

@Data
public class ReqMatOrderVo {

    private List<String> workOrderItemIds;

    private String matOrderId;

}
