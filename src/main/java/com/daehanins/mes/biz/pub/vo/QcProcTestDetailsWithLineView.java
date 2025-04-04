package com.daehanins.mes.biz.pub.vo;

import com.daehanins.mes.biz.pub.entity.QcProcTestDetail;
import com.daehanins.mes.biz.pub.entity.QcProcTestLineView;
import lombok.Data;

import java.util.List;

@Data
public class QcProcTestDetailsWithLineView {

    private List<QcProcTestLineView> qcProcTestLineViews;

    private List<QcProcTestDetail> qcProcTestDetails;

}
