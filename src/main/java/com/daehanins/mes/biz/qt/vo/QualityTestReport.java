package com.daehanins.mes.biz.qt.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class QualityTestReport {


    //자재유형?, 재질?, 자급구분
    //시험구분.
    private String testGbName;
    //시험번호
    private String testNo;
    //재시험 serNo.
    private String retestSerNo;
    // 자재코드(제품코드) 품목코드, 원료코드
    private String itemCd;
    // 품명
    private String itemName;
    // Lot. No.
    private String lotNo;

    private String shelfLife;

    private String expiryDate;
    //창고명
    private String storageName;
    //공급업체
    private String customerName;
    //시험의뢰부서
    private String reqDept;
    //시험의뢰자
    private String reqMember;
    //시험접수자
    private String testMember;
    //시험지시자
    private String orderMember;
    //시험확인자
    private String confirmMember;
    //입고일자, 생산일자
    private String reqDate;
    //제조량, 생산량, 입고량
    private String reqQty;
    //검체 채취자
    private String sampleMember;
    //검체 채취량
    private String sampleQty;
    //시험일자
    private String testDate;
    //시험결과
    private String testResult;
    //확인일자.
    private String confirmDate;
    //검사항목 List.
    private List<QualityTestReportItem> testReportItems;
}
