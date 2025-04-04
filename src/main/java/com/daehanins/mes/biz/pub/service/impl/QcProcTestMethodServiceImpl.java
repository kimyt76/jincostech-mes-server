package com.daehanins.mes.biz.pub.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daehanins.mes.biz.pub.entity.ProcTestTypeMethod;
import com.daehanins.mes.biz.pub.entity.QcProcTestMethod;
import com.daehanins.mes.biz.pub.mapper.QcProcTestMethodMapper;
import com.daehanins.mes.biz.pub.service.IProcTestTypeMethodService;
import com.daehanins.mes.biz.pub.service.IQcProcTestMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * QcProcTestMethod 서비스 구현 클래스
 * </p>
 *
 * @author jeonsj
 * @since 2023-05-11
 */
@Service
public class QcProcTestMethodServiceImpl extends ServiceImpl<QcProcTestMethodMapper, QcProcTestMethod> implements IQcProcTestMethodService {

    @Autowired
    private IProcTestTypeMethodService procTestTypeMethodService;

    /**
     * testType
     * QRC003 : 코팅공정
     * QRC004 : 충전공정
     * QRC005 : 포장공정
     **/
    public List<QcProcTestMethod> getQcMethod (String qcProcTestMasterId, String testType) {

        List<QcProcTestMethod> result = new ArrayList<>();

        QueryWrapper<QcProcTestMethod> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("qc_proc_test_master_id",qcProcTestMasterId).eq("test_type", testType);

        if(this.count(queryWrapper) > 0){
            result = this.list(queryWrapper.orderByAsc("display_order"));

        } else {
            List<ProcTestTypeMethod> temp = procTestTypeMethodService.getByTestType(testType);
            for (ProcTestTypeMethod item : temp) {
                QcProcTestMethod setItem = new QcProcTestMethod();
                setItem.setQcProcTestMasterId(qcProcTestMasterId);
                setItem.setTestType(testType);
                setItem.setDisplayOrder(item.getDisplayOrder());
                setItem.setTestItem(item.getTestItem());
                setItem.setTestMethod(item.getTestMethod());
                result.add(setItem);
            }
        }
        return result;
    }

}
