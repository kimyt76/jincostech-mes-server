package com.daehanins.mes.biz.qt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.daehanins.mes.biz.mat.entity.MatOrder;
import com.daehanins.mes.biz.mat.entity.MatOrderItem;
import com.daehanins.mes.biz.mat.vo.MatOrderSaveWithItems;
import com.daehanins.mes.biz.qt.entity.TestType;
import com.daehanins.mes.biz.qt.entity.TestTypeMethod;
import com.daehanins.mes.biz.qt.vo.TestTypeSaveWithItems;

import java.util.List;

/**
 * <p>
 * 품질검사유형TestType 서비스
 * </p>
 *
 * @author jeonsj
 * @since 2020-07-03
 */
public interface ITestTypeService extends IService<TestType> {

    TestTypeSaveWithItems saveWithItems(TestType testType, List<TestTypeMethod> testTypeMethods, List<TestTypeMethod> deleteTestTypeMethods);

    String deleteWithItems(List<String> idList);

}
