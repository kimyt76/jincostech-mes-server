package com.daehanins.mes.biz.pub.vo;

import com.daehanins.mes.biz.pub.entity.*;
import com.daehanins.mes.biz.work.entity.WorkOrderItemView;
import lombok.Data;

import java.util.List;

@Data
public class ProcTestWithItems {

    private WorkOrderItemView workOrderItemView;

    private ProcTestMaster procTestMaster;

    private List<ProcTestMethod> methodList;

    private List<ProcTestEquip> equipList;

    private List<ProcTestWorker> workerList;

}
