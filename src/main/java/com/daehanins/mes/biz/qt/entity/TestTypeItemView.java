package com.daehanins.mes.biz.qt.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
* <p>
* TestTypeItemView 엔티티
* </p>
*
* @author jeonsj
* @since 2020-10-05
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("test_type_item_view")
public class TestTypeItemView {

    @TableId
    private String itemCd;

    private String itemTypeCd;

    private String itemTypeName;

    private String itemName;

    private String regYn;

    private String testItemJoin;

    private String testItem;
}
