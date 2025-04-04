package com.daehanins.mes.biz.pub.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
* <p>
* ProcessView 엔티티
* </p>
*
* @author jeonsj
* @since 2022-06-20
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("process_view")
public class ProcessView {

    private String processCd;

    private String processName;

    private Integer processOrder;

    private Integer maxCnt;

    private Integer regCnt;

}
