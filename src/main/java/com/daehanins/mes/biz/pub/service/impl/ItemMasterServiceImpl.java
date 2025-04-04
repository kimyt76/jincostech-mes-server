package com.daehanins.mes.biz.pub.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daehanins.mes.biz.pub.entity.Customer;
import com.daehanins.mes.biz.pub.entity.ItemMaster;
import com.daehanins.mes.biz.pub.mapper.ItemMasterMapper;
import com.daehanins.mes.biz.pub.service.IItemMasterService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 품목마스터ItemMaster 서비스 구현 클래스
 * </p>
 *
 * @author jeonsj
 * @since 2020-04-06
 */
@Service
public class ItemMasterServiceImpl extends ServiceImpl<ItemMasterMapper, ItemMaster> implements IItemMasterService {

    @Transactional
    public  List<ItemMaster> saveItems(List<ItemMaster> itemMasterList) {
        itemMasterList.forEach( itemMaster -> {
            if (StringUtils.isNotBlank(itemMaster.getItemCd())) {
                this.saveOrUpdate(itemMaster);
            }
        });

        return itemMasterList;
    }

}
