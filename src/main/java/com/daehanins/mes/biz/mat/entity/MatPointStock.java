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
import java.time.LocalDateTime;

/**
 * <p>
 * MatPointStock 엔티티
 * </p>
 *
 * @author jeonsj
 * @since 2020-08-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("mat_point_stock")
public class MatPointStock extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 자재재고id
     */
    @TableId
    private String matPointStockId = String.valueOf(SnowFlakeUtil.getFlowIdInstance().nextId());

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
     * 정상처리여부
     */
    private String procYn;

 }
