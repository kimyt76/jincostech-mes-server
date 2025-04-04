package com.daehanins.mes.biz.tag.vo;

import com.daehanins.mes.biz.tag.entity.TagValue;
import lombok.Data;

import java.util.List;

@Data
public class EquipRunTagLists {

    //SL1P03
    private List<EquipRunTagValue> SL1;

    //SL1P11
    private List<EquipRunTagValue> SL2;

    //SL1P21
    private List<EquipRunTagValue> SL3;

    //SL1P22
    private List<EquipRunTagValue> SL4;

}
