package com.daehanins.mes.biz.pub.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.daehanins.mes.biz.pub.entity.Customer;
import com.daehanins.mes.biz.pub.entity.ItemMaster;

import java.util.List;

/**
 * <p>
 * 품목마스터ItemMaster 서비스
 * </p>
 *
 * @author jeonsj
 * @since 2020-04-06
 */
public interface IItemMasterService extends IService<ItemMaster> {

    List<ItemMaster> saveItems(List<ItemMaster> itemMasterList);

}
