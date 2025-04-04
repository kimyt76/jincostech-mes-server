package com.daehanins.mes.biz.pub.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.daehanins.mes.biz.pub.entity.QcProcTestMaster;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

/**
 * <p>
 * QcProcTestMaster 서비스
 * </p>
 *
 * @author jeonsj
 * @since 2023-04-18
 */
public interface IQcProcTestMasterService extends IService<QcProcTestMaster> {

    ResponseEntity<Resource> getQcProcTestExcelByType(String workOrderBatchId) throws Exception;
}
