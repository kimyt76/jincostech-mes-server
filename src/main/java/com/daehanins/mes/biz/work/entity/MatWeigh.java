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
 * MatWeigh 엔티티
 * </p>
 *
 * @author jeonsj
 * @since 2020-05-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("mat_weigh")
public class MatWeigh extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 칭량ID
     */
    @TableId
    private String matWeighId;

    /**
     * 자재소요량ID
     */
    private String matUseId;

    /**
     * 창고코드
     */
    private String storageCd;

    /**
     * 칭량량
     */
    private BigDecimal weighQty;

    /**
     * 품목코드
     */
    private String itemCd;

    /**
     * 로트(제조)번호
     */
    private String lotNo;

    /**
     * 시험번호
     */
    private String testNo;

    /**
     * 적요
     */
    private String memo;

}
