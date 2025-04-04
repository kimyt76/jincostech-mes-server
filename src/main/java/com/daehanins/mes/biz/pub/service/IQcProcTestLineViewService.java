package com.daehanins.mes.biz.pub.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.daehanins.mes.biz.pub.entity.QcProcTestLineView;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * <p>
 * QcProcTestLineView 서비스
 * </p>
 *
 * @author jeonsj
 * @since 2023-05-17
 */
public interface IQcProcTestLineViewService extends IService<QcProcTestLineView> {

    List<QcProcTestLineView> getQcDetailLines (String qcProcTestMasterId, String testType);

}
