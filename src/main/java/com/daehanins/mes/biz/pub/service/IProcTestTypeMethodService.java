package com.daehanins.mes.biz.pub.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.daehanins.mes.biz.pub.entity.ProcTestTypeMethod;

import java.util.List;

/**
 * <p>
 * ProcTestTypeMethod 서비스
 * </p>
 *
 * @author jeonsj
 * @since 2023-03-14
 */
public interface IProcTestTypeMethodService extends IService<ProcTestTypeMethod> {

    List<ProcTestTypeMethod> getByTestType (String processCd);

}
