package com.daehanins.mes.biz.mat.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.daehanins.mes.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 자재거래유형TranType 엔티티
 * </p>
 *
 * @author jeonsj
 * @since 2020-06-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("tran_type")
public class TranType extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 자재거래유형코드
     */
    @TableId
    private String tranCd;

    /**
     * 자재거래유형명
     */
    private String tranName;

    /**
     * 재고사인
     */
    private Integer sign;

}
