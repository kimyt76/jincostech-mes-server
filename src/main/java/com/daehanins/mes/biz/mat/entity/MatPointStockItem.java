package com.daehanins.mes.biz.mat.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.daehanins.mes.base.BaseEntity;
import com.daehanins.mes.common.utils.SnowFlakeUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * MatPointStockItem 엔티티
 * </p>
 *
 * @author jeonsj
 * @since 2020-08-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("mat_point_stock_item")
public class MatPointStockItem extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 자재시점재고상세id
     */
    @TableId
    private String matPointStockItemId  = String.valueOf(SnowFlakeUtil.getFlowIdInstance().nextId());

    /**
     * 자재시점재고생성id
     */
    private String matPointStockId;

    /**
     * 재고일자
     */
    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate stockDate;

    /**
     * 구역(공장)코드
     */
    private String areaCd;

    /**
     * 창고코드
     */
    private String storageCd;

    /**
     * 품목코드
     */
    private String itemCd;

    /**
     * 품목구분코드
     */
    private String itemTypeCd;

    /**
     * 로트(제조)번호
     */
    private String lotNo;

    /**
     * 시험번호
     */
    private String testNo;

    /**
     * 재고수량
     */
    private BigDecimal stockQty;

}
