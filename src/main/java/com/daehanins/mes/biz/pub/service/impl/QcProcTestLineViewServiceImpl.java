package com.daehanins.mes.biz.pub.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daehanins.mes.biz.pub.entity.QcProcTestLineView;
import com.daehanins.mes.biz.pub.mapper.QcProcTestLineViewMapper;
import com.daehanins.mes.biz.pub.service.IQcProcTestLineViewService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * QcProcTestLineView 서비스 구현 클래스
 * </p>
 *
 * @author jeonsj
 * @since 2023-05-17
 */
@Service
public class QcProcTestLineViewServiceImpl extends ServiceImpl<QcProcTestLineViewMapper, QcProcTestLineView> implements IQcProcTestLineViewService {

    public List<QcProcTestLineView> getQcDetailLines (String qcProcTestMasterId, String testType) {

        List<QcProcTestLineView> qcProcTestLineViews = new ArrayList<>();

        if (testType.equals("WE613") || testType.equals("WE616") || testType.equals("CA515") || testType.equals("ES515")) {

            QueryWrapper<QcProcTestLineView> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("qc_proc_test_master_id", qcProcTestMasterId).eq("test_type", testType);

            if (this.count(queryWrapper) > 0) {
                qcProcTestLineViews = this.list(queryWrapper.orderByAsc("display_order"));
            } else {
                int size = 0;
                if(testType.equals("CA515") || testType.equals("ES515")) {
                    size = 5;
                } else {
                    size = 6;
                }
                for (int i = 0; i < size; i ++) {
                    QcProcTestLineView temp = new QcProcTestLineView();
                    temp.setDisplayOrder(i + 1);
                    temp.setQcProcTestMasterId(qcProcTestMasterId);
                    temp.setTestType(testType);
                    qcProcTestLineViews.add(temp);
                }
            }
        }
        return qcProcTestLineViews;
    }
}
