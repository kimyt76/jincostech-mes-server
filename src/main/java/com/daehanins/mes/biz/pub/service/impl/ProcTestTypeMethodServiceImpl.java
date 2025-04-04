package com.daehanins.mes.biz.pub.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daehanins.mes.biz.pub.entity.ProcTestTypeMethod;
import com.daehanins.mes.biz.pub.mapper.ProcTestTypeMethodMapper;
import com.daehanins.mes.biz.pub.service.IProcTestTypeMethodService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * ProcTestTypeMethod 서비스 구현 클래스
 * </p>
 *
 * @author jeonsj
 * @since 2023-03-14
 */
@Service
public class ProcTestTypeMethodServiceImpl extends ServiceImpl<ProcTestTypeMethodMapper, ProcTestTypeMethod> implements IProcTestTypeMethodService {

    public List<ProcTestTypeMethod> getByTestType (String testType) {
        return this.list(new QueryWrapper<ProcTestTypeMethod>().eq("test_type", testType).orderByAsc("display_order"));
    }
}
