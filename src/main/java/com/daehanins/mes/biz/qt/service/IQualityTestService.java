package com.daehanins.mes.biz.qt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.daehanins.mes.biz.qt.entity.ItemTestNo;
import com.daehanins.mes.biz.qt.entity.QualityTest;
import com.daehanins.mes.biz.qt.entity.QualityTestMethod;
import com.daehanins.mes.biz.qt.vo.QualityTestSaveWithItems;
import com.daehanins.mes.biz.qt.vo.ReqPrinting;
import com.daehanins.mes.biz.work.entity.WorkOrderItemView;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * 품질검사QualityTest 서비스
 * </p>
 *
 * @author jeonsj
 * @since 2020-07-03
 */
public interface IQualityTestService extends IService<QualityTest> {

    QualityTestSaveWithItems saveWithItems(QualityTest qualityTest, List<QualityTestMethod> qualityTestMethods, List<QualityTestMethod> deleteQualityTestMethods);

    String deleteWithItems(List<String> idList);

    boolean makeSampleUse(LocalDate testDate);

    boolean saveQualityTestWithWorkStart (String testNo, String areaCd);

    boolean saveQualityTestWithWorkStart2 (ItemTestNo itemTestNo, WorkOrderItemView workOrderItemView);

    boolean updateReqQtyByWorkEnd (WorkOrderItemView workOrderItemView);

    byte[] printQualityTestReports (String qualityTestId) throws Exception;

    ResponseEntity<Resource> getQtReport (String qualityTestId) throws Exception;

    ResponseEntity<Resource> getQtJournal (String qualityTestId) throws Exception;

}
