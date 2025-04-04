package com.daehanins.mes.biz.pub.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daehanins.mes.biz.pub.entity.ProcTestEquip;
import com.daehanins.mes.biz.pub.mapper.ProcTestEquipMapper;
import com.daehanins.mes.biz.pub.service.IProcTestEquipService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * ProcTestEquip 서비스 구현 클래스
 * </p>
 *
 * @author jeonsj
 * @since 2023-03-14
 */
@Service
public class ProcTestEquipServiceImpl extends ServiceImpl<ProcTestEquipMapper, ProcTestEquip> implements IProcTestEquipService {

    public List<ProcTestEquip> getByMasterId (String id) {
        return this.list(new QueryWrapper<ProcTestEquip>().eq("proc_test_master_id", id).orderByAsc("display_order"));
    }

}
