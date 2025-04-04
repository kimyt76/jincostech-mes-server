package com.daehanins.mes.biz.qt.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
* <p>
* TestTypeView 엔티티
* </p>
*
* @author jeonsj
* @since 2020-07-06
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("test_type_view")
public class TestTypeView {

    @TableId
    private String testTypeId;

    private String testItem;
    private String testTypeName;
    private String useYn;
    private String memo;

    private String testItemJoin;

}
