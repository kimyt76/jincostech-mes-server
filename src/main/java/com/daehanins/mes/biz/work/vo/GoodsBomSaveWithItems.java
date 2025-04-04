package com.daehanins.mes.biz.work.vo;

import com.daehanins.mes.biz.work.entity.GoodsBom;
import com.daehanins.mes.biz.work.entity.GoodsBomItem;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GoodsBomSaveWithItems {

    private GoodsBom goodsBom;

    private List<GoodsBomItem> goodsBomItems = new ArrayList<>();

    private List<GoodsBomItem> goodsBomDeleteItems = new ArrayList<>();
}
