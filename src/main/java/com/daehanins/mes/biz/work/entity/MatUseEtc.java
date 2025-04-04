package com.daehanins.mes.biz.work.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.daehanins.mes.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.math.BigDecimal;

/**
* <p>
* MatUseEtc 엔티티
* </p>
*
* @author jeonsj
* @since 2021-05-03
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mat_use_etc")
public class MatUseEtc extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId
    private String matUseEtcId;

    private String workOrderItemId;

    private Integer serNo;

    private String storageCd;

    private String itemCd;

    private BigDecimal reqQty;

    private String memo;

}
