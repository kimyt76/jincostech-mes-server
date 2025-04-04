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
 * 제품BOMGoodsBom 엔티티
 * </p>
 *
 * @author jeonsj
 * @since 2020-04-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("goods_bom")
public class GoodsBom extends BaseEntity {


    private static final long serialVersionUID = -6113256355983407638L;

    /**
     * 제품BOM ID
     */
    @TableId
    private String goodsBomId = String.valueOf(SnowFlakeUtil.getFlowIdInstance().nextId());

    /**
     * 제품품목코드
     */
    private String itemCd;

    /**
     * 생산수량
     */
    private BigDecimal prodQty;

    /**
     * 생산공정
     */
    private String processCd;

    /**
     * BOM버전
     */
    private String bomVer;

    /**
     * 기본여부
     */
    private String defaultYn;

    /**
     * 비고
     */
    private String memo;


}
