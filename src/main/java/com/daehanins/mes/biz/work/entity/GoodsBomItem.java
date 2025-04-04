package com.daehanins.mes.biz.work.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.daehanins.mes.base.BaseEntity;
import com.daehanins.mes.common.utils.SnowFlakeUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * <p>
 * 제품BOM품목GoodsBomItem 엔티티
 * </p>
 *
 * @author jeonsj
 * @since 2020-04-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("goods_bom_item")
public class GoodsBomItem extends BaseEntity {

    private static final long serialVersionUID = -9188444639007644354L;

    /**
     * 제품BOM품목ID
     */
    @TableId
    private String goodsBomItemId = String.valueOf(SnowFlakeUtil.getFlowIdInstance().nextId());

    /**
     * 제품BOM ID
     */
    private String goodsBomId;

    /**
     * 제조상태
     */
    private String prodState;

    /**
     * 품목코드
     */
    private String itemCd;

    /**
     * 수량
     */
    private BigDecimal qty;

    /**
     * 함량
     */
    private BigDecimal contentRatio;

    /**
     * 위치
     */
    private String location;

    /**
     * 적요
     */
    private String memo;


}
