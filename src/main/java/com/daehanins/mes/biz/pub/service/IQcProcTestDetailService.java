package com.daehanins.mes.biz.pub.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.daehanins.mes.biz.pub.entity.QcProcTestDetail;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * <p>
 * QcProcTestDetail 서비스
 * </p>
 *
 * @author jeonsj
 * @since 2023-05-11
 */
public interface IQcProcTestDetailService extends IService<QcProcTestDetail> {
    List<QcProcTestDetail> getQcDetail (String qcProcTestMasterId, String testType);

}
