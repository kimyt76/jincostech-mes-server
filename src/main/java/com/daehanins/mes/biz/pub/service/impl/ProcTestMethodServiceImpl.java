package com.daehanins.mes.biz.pub.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daehanins.mes.biz.pub.entity.ProcTestMethod;
import com.daehanins.mes.biz.pub.entity.ProcTestTypeMethod;
import com.daehanins.mes.biz.pub.mapper.ProcTestMethodMapper;
import com.daehanins.mes.biz.pub.service.IProcTestMethodService;
import com.daehanins.mes.biz.pub.service.IProcTestTypeMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * ProcTestMethod 서비스 구현 클래스
 * </p>
 *
 * @author jeonsj
 * @since 2023-03-14
 */
@Service
public class ProcTestMethodServiceImpl extends ServiceImpl<ProcTestMethodMapper, ProcTestMethod> implements IProcTestMethodService {

    @Autowired
    private IProcTestTypeMethodService procTestTypeMethodService;

    public List<ProcTestMethod> initProcTestMethod (String testType, String procTestMasterId) {
        List<ProcTestTypeMethod> list = procTestTypeMethodService.getByTestType(testType);

        List<ProcTestMethod> resultList = new ArrayList<>();

        for( ProcTestTypeMethod item : list ) {
            ProcTestMethod temp = new ProcTestMethod();
            temp.setProcTestMasterId(procTestMasterId);
            temp.setDisplayOrder(item.getDisplayOrder());
            temp.setTestItem(item.getTestItem());
            temp.setTestMethod(item.getTestMethod());
            temp.setTestTiming(item.getTestTiming());
            temp.setTestTime(item.getTestTime());
            resultList.add(temp);
        }
        this.saveBatch(resultList);
        return resultList;
    }

    public List<ProcTestMethod> getByMasterId (String id) {
        return this.list(new QueryWrapper<ProcTestMethod>().eq("proc_test_master_id", id).orderByAsc("display_order"));
    }

}
