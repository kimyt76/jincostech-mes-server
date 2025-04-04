package com.daehanins.mes.biz.tag.vo;

import com.daehanins.mes.biz.tag.entity.TagValue;
import lombok.Data;

import java.util.List;

@Data
public class MonitoringTagLists {

    //SL1P03
    private List<TagValue> SL1;

    //SL1P11
    private List<TagValue> SL2;

    //SL1P21
    private List<TagValue> SL3;

    //SL1P22
    private List<TagValue> SL4;

}
