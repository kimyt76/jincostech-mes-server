package com.daehanins.mes.biz.pub.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daehanins.mes.biz.pub.entity.QcProcTestDetail;
import com.daehanins.mes.biz.pub.mapper.QcProcTestDetailMapper;
import com.daehanins.mes.biz.pub.service.IQcProcTestDetailService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * QcProcTestDetail 서비스 구현 클래스
 * </p>
 *
 * @author jeonsj
 * @since 2023-05-11
 */
@Service
public class QcProcTestDetailServiceImpl extends ServiceImpl<QcProcTestDetailMapper, QcProcTestDetail> implements IQcProcTestDetailService {

    /**
     * testType
     * WE115 : 중량 검사 (1X15)
     * WE613 : 중량 검사 (6X13) use Line
     * WE616 : 중량 검사 (6X16) use Line
     * GA115 : 겔 수량 검사 (1X15)
     * CA515 : 캡핑세기(완제품) (5X15) use Line
     * ES515 : 중량검사(에센스) (5X15) use Line
     **/
    public List<QcProcTestDetail> getQcDetail (String qcProcTestMasterId, String testType) {
        List<QcProcTestDetail> qcProcTestDetails = new ArrayList<>();

        QueryWrapper<QcProcTestDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("qc_proc_test_master_id", qcProcTestMasterId).eq("test_type", testType);

        if(this.count(queryWrapper) > 0){
            qcProcTestDetails = this.list(queryWrapper.orderByAsc("display_order"));
        } else {
            int size = 0;
            if (testType.equals("WE613")) {
                size = 13;
            } else if (testType.equals("WE616")) {
                size = 16;
            } else {
                size = 15;
            }
            for (int i = 0; i < size; i++) {
                QcProcTestDetail temp = new QcProcTestDetail();
                temp.setDisplayOrder(i + 1);
                temp.setQcProcTestMasterId(qcProcTestMasterId);
                temp.setTestType(testType);
                qcProcTestDetails.add(temp);
            }
        }
        return qcProcTestDetails;
    }

}
