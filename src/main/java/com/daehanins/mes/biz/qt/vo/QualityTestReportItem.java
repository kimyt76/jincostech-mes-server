package com.daehanins.mes.biz.qt.vo;

import lombok.Data;

@Data
public class QualityTestReportItem {
    private Integer rowNo;
    private String testItem;
    private String testMethod;
    private String testSpec;
    private String testResult;
    private String testDateString;
    private String testMember;
    private String confirmMember;
    private String passStateName;
}
