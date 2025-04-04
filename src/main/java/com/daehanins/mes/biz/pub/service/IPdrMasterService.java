package com.daehanins.mes.biz.pub.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.daehanins.mes.biz.mat.entity.MatStock;
import com.daehanins.mes.biz.pub.entity.PdrMaster;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

/**
 * <p>
 * PdrMaster 서비스
 * </p>
 *
 * @author jeonsj
 * @since 2022-12-21
 */
public interface IPdrMasterService extends IService<PdrMaster> {

    boolean checkExist (PdrMaster pdrMaster);

    PdrMaster initPdrMaster (PdrMaster pdrMaster);

    Boolean deleteWithItems (String id);

    ResponseEntity<Resource> getPdrExcel (String id) throws Exception;

}
