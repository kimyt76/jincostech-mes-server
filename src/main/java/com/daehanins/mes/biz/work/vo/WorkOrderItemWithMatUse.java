package com.daehanins.mes.biz.work.vo;
import com.daehanins.mes.biz.work.entity.MatUseView;
import com.daehanins.mes.biz.work.entity.WorkOrderItemView;
import lombok.Data;

import java.util.List;


@Data
public class WorkOrderItemWithMatUse {

    private List<MatUseView> matUseViewList;

    private WorkOrderItemView workOrderItemView;

}