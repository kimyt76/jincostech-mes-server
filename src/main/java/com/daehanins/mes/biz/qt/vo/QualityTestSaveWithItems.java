package com.daehanins.mes.biz.qt.vo;

import com.daehanins.mes.biz.qt.entity.QualityTest;
import com.daehanins.mes.biz.qt.entity.QualityTestMethod;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class QualityTestSaveWithItems {

    private QualityTest qualityTest;

    private List<QualityTestMethod> qualityTestMethods = new ArrayList<>();

    private List<QualityTestMethod> deleteQualityTestMethods = new ArrayList<>();

}
