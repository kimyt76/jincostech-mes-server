package com.daehanins.mes.biz.work.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
* <p>
* GoodsProcView 엔티티
* </p>
*
* @author jeonsj
* @since 2023-02-15
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("goods_proc_view")
public class GoodsProcView {

    @TableId
    private String goodsProcId;

    private String itemCd;

    private String itemName;

    private String processVer;

    private String defaultYn;

    private String memo;




}
