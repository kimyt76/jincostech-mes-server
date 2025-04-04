package com.daehanins.mes.biz.pub.vo;

import com.daehanins.mes.biz.pub.entity.*;
import lombok.Data;

import java.util.List;

@Data
public class ProcTestSaveItems {

    private ProcTestMaster procTestMaster;

    private List<ProcTestMethod> methodList;

    private List<ProcTestEquip> equipList;

    private List<ProcTestWorker> workerList;

}
