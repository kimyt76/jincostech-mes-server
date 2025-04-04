package com.daehanins.mes.biz.qt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daehanins.mes.biz.mat.entity.MatOrder;
import com.daehanins.mes.biz.mat.entity.MatOrderItem;
import com.daehanins.mes.biz.mat.service.IMatOrderItemService;
import com.daehanins.mes.biz.mat.vo.MatOrderSaveWithItems;
import com.daehanins.mes.biz.qt.entity.TestType;
import com.daehanins.mes.biz.qt.entity.TestTypeMethod;
import com.daehanins.mes.biz.qt.mapper.TestTypeMapper;
import com.daehanins.mes.biz.qt.service.ITestTypeMethodService;
import com.daehanins.mes.biz.qt.service.ITestTypeService;
import com.daehanins.mes.biz.qt.vo.TestTypeSaveWithItems;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 품질검사유형TestType 서비스 구현 클래스
 * </p>
 *
 * @author jeonsj
 * @since 2020-07-03
 */
@Service
public class TestTypeServiceImpl extends ServiceImpl<TestTypeMapper, TestType> implements ITestTypeService {

    @Autowired
    private ITestTypeMethodService testTypeMethodService;

    @Transactional
    public TestTypeSaveWithItems saveWithItems(TestType testType, List<TestTypeMethod> testTypeMethods, List<TestTypeMethod> deleteTestTypeMethods) {

        TestTypeSaveWithItems data = new TestTypeSaveWithItems();

//        if (this.saveOrUpdate(testType)) {
//            data.setTestType(testType);
//        }

        // item 삭제 처리
        deleteTestTypeMethods.forEach( item -> {
            if (StringUtils.isNotBlank(item.getTestTypeMethodId())) {
                this.testTypeMethodService.removeById(item.getTestTypeMethodId());
            }
        });
        // item 신규,수정 처리
        testTypeMethods.forEach( item -> {
            item.setItemCd(testType.getItemCd());
            this.testTypeMethodService.saveOrUpdate(item);
            data.getTestTypeMethods().add(item);
        });

        return data;
    }

    @Transactional
    public String deleteWithItems(List<String> idList) {
        String msg;

        // 검사유형의 검사방법 삭제
        for (String itemCd : idList) {
            testTypeMethodService.remove(new QueryWrapper<TestTypeMethod>().eq("item_cd", itemCd));
        }
        // 검사유형 삭제
        if (this.removeByIds(idList)) {
            msg = "success";
        } else {
            msg = "fail";
        }
        return msg;
    }

}
