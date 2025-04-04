package com.daehanins.mes.biz.work.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.daehanins.mes.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 자재소요량MatUse 엔티티
 * </p>
 *
 * @author jeonsj
 * @since 2020-05-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("mat_use")
public class MatUse extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 자재소요량ID
     */
    @TableId
    private String matUseId;

    /**
     * 작업지시상세ID
     */
    private String workOrderItemId;

    /**
     * 품목코드
     */
    private String itemCd;

    /**
     * 창고코드
     */
    private String storageCd;

    /**
     * 제조상태
     */
    private String prodState;

    /**
     * 제조상태 일련번호
     */
    private int serNo;


    /**
     * 소요량
     */
    private BigDecimal reqQty;

    /**
     * 소요량
     */
    private BigDecimal bagWeight;

    /**
     * 칭량량
     */
    private BigDecimal weighQty;

    /**
     * 칭량여부
     */
    private String weighYn;

    /**
     * 칭량일시
     */
    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private LocalDateTime weighDatetime;

    /**
     * 시험번호Join
     */
    private String testNoJoin;

    /**
     * 칭량자
     */
    private String weighMemberCd;

    /**
     * 칭량확인자   20200909 추가 
     */
    private String weighConfirmMemberCd;

    /**
     * 적요
     */
    private String memo;

    /**
     * 제조투입자
     */
    private String prodMemberCd;

    /**
     * 제조확인자   20200826 추가
     */
    private String prodConfirmMemberCd;

    /**
     * 제조투입여부
     */
    private String prodYn;

    /**
     * 제조투입일시
     */
    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private LocalDateTime prodDatetime;

    /**
     * 자재사용상태
     */
    private String useState;

}
