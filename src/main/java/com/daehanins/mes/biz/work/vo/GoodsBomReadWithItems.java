package com.daehanins.mes.biz.work.vo;

import com.daehanins.mes.biz.work.entity.GoodsBom;
import com.daehanins.mes.biz.work.entity.GoodsBomItemView;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GoodsBomReadWithItems {
    private GoodsBom goodsBom;

    private List<GoodsBomItemView> goodsBomItems = new ArrayList<>();

}
