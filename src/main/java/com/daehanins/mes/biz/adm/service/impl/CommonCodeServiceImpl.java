package com.daehanins.mes.biz.adm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daehanins.mes.biz.adm.entity.CommonCode;
import com.daehanins.mes.biz.adm.mapper.CommonCodeMapper;
import com.daehanins.mes.biz.adm.service.ICommonCodeService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 공통코드CommonCode 서비스 구현 클래스
 * </p>
 *
 * @author jeonsj
 * @since 2020-04-04
 */
@Service
public class CommonCodeServiceImpl extends ServiceImpl<CommonCodeMapper, CommonCode> implements ICommonCodeService {

    public List<CommonCode> getByCodeType (String codeType) {
        QueryWrapper<CommonCode> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("code_type", codeType);
        queryWrapper.orderByAsc("display_order");
        return this.list(queryWrapper);
    }

}
