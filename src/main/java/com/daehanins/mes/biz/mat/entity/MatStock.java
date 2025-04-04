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

import java.time.LocalDate;

/**
 * <p>
 * MatStock 엔티티
 * </p>
 *
 * @author jeonsj
 * @since 2020-05-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("mat_stock")
public class MatStock extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 재고조사id
     */
    @TableId
    private String matStockId = String.valueOf(SnowFlakeUtil.getFlowIdInstance().nextId());

    /**
     * 구역(공장)코드
     */
    private String areaCd;

    /**
     * 창고코드
     */
    private String storageCd;

    /**
     * 재고일자
     */
    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate stockDate;

    /**
     * 담당자
     */
    private String memberCd;

    /**
     * 재고조사상태(등록,장부,실사,조정,완료)
     */
    private String stockState;

    /**
     * 조정 자재거래id
     */
    private String matTranId;

    /**
     * 조정반영여부 Y,N
     */
    private String adjYn;

    /**
     * 종료여부 Y,N
     */
    private String endYn;


}
