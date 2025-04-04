package com.daehanins.mes.biz.qt.vo;

import com.daehanins.mes.biz.qt.entity.QualityTestMethod;
import com.daehanins.mes.biz.qt.entity.QualityTestView;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class QualityTestReadWithItems {

    private QualityTestView qualityTest;

    private List<QualityTestMethod> qualityTestMethods = new ArrayList<>();


}
