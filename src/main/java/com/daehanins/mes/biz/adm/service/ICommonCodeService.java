package com.daehanins.mes.biz.adm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.daehanins.mes.biz.adm.entity.CommonCode;

import java.util.List;

/**
 * <p>
 * 공통코드CommonCode 서비스
 * </p>
 *
 * @author jeonsj
 * @since 2020-04-04
 */
public interface ICommonCodeService extends IService<CommonCode> {

    List<CommonCode> getByCodeType (String codeType);

}
