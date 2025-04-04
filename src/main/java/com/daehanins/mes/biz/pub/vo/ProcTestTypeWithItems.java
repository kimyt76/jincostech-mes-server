package com.daehanins.mes.biz.pub.vo;

import com.daehanins.mes.biz.pub.entity.*;
import com.daehanins.mes.biz.work.entity.WorkOrderItemView;
import lombok.Data;

import java.util.List;

@Data
public class ProcTestTypeWithItems {

    private ProcTestType procTestType;

    private List<ProcTestTypeMethod> methodList;

}
