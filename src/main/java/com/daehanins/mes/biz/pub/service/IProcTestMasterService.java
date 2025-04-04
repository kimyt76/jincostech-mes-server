package com.daehanins.mes.biz.pub.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.daehanins.mes.biz.pub.entity.ProcTestMaster;
import com.daehanins.mes.biz.pub.vo.ProcTestWithItems;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

/**
 * <p>
 * ProcTestMaster 서비스
 * </p>
 *
 * @author jeonsj
 * @since 2023-03-14
 */
public interface IProcTestMasterService extends IService<ProcTestMaster> {

    ProcTestMaster getByWorkOrderItemId (String id);

    ResponseEntity<Resource> getProcTestExcel(String workOrderItemId) throws Exception;
}
