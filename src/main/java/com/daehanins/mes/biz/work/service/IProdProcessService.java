package com.daehanins.mes.biz.work.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.daehanins.mes.biz.work.entity.ProdProcess;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

/**
 * <p>
 * ProdProcess 서비스
 * </p>
 *
 * @author jeonsj
 * @since 2023-02-17
 */
public interface IProdProcessService extends IService<ProdProcess> {

    ResponseEntity<Resource> getProdWeighExcel (String workOrderItemId) throws Exception;

    ResponseEntity<Resource> getProdProcExcel (String workOrderItemId) throws Exception;

    ResponseEntity<Resource> getProdInputExcel (String workOrderItemId) throws Exception;

}
