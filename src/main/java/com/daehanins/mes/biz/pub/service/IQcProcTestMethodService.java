package com.daehanins.mes.biz.pub.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.daehanins.mes.biz.pub.entity.QcProcTestMethod;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * <p>
 * QcProcTestMethod 서비스
 * </p>
 *
 * @author jeonsj
 * @since 2023-05-11
 */
public interface IQcProcTestMethodService extends IService<QcProcTestMethod> {
    List<QcProcTestMethod> getQcMethod (String qcProcTestMasterId, String testType);

}
