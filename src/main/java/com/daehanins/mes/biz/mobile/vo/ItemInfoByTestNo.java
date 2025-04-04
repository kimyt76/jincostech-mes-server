package com.daehanins.mes.biz.mobile.vo;

import com.daehanins.mes.biz.pub.entity.ItemMasterView;
import com.daehanins.mes.biz.qt.entity.ItemTestNoView;
import lombok.Data;

@Data
public class ItemInfoByTestNo {

    private ItemTestNoView itemTestNoView;

    private ItemMasterView itemMasterView;

}
