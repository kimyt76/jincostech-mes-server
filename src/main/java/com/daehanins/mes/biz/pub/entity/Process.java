package com.daehanins.mes.biz.pub.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.daehanins.mes.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 공정Process 엔티티
 * </p>
 *
 * @author jeonsj
 * @since 2020-05-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("process")
public class Process extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 공정코드
     */
    @TableId
    private String processCd;

    /**
     * 공정명
     */
    private String processName;

    /**
     * 표시순서
     */
    private Integer processOrder;

    /**
     * 생산공정 여부
     */
    private String prodYn;

    /**
     * 공정검사 최대 항목수 (양식으로 인한)
     */
    private int maxCnt;

    /**
     * 사용 여부
     */
    private String useYn;

}
