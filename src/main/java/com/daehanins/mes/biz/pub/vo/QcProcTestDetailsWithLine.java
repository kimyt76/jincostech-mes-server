package com.daehanins.mes.biz.pub.vo;

import com.daehanins.mes.biz.pub.entity.*;
import lombok.Data;

import java.util.List;

@Data
public class QcProcTestDetailsWithLine {

    private List<QcProcTestLine> qcProcTestLines;

    private List<QcProcTestDetail> qcProcTestDetails;

}
