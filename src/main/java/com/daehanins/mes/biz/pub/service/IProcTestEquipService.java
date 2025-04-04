package com.daehanins.mes.biz.pub.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.daehanins.mes.biz.pub.entity.ProcTestEquip;

import java.util.List;

/**
 * <p>
 * ProcTestEquip 서비스
 * </p>
 *
 * @author jeonsj
 * @since 2023-03-14
 */
public interface IProcTestEquipService extends IService<ProcTestEquip> {

    List<ProcTestEquip> getByMasterId (String id);

}
