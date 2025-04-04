package com.daehanins.mes.biz.work.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
* <p>
* MatUseView 엔티티
* </p>
*
* @author jeonsj
* @since 2020-05-10
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mat_use_view")
public class MatUseView {

    @TableId
    private String matUseId;

    private String workOrderItemId;

    private String storageCd;

    private String itemCd;

    private String itemName;

    private String appearance;

    private String prodState;

    private int serNo;

    private BigDecimal reqQty;

    private BigDecimal bagWeight;

    private BigDecimal weighQty;

    private String testNoJoin;

    private String weighMemberCd;

    private String weighConfirmMemberCd;    // 20200908 추가

    private String weighYn;

    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private LocalDateTime weighDatetime;

    private String weighTime;

    private String memo;

    private String prodMemberCd;

    private String prodConfirmMemberCd; //20200826 추가

    private String prodYn;

    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private LocalDateTime prodDatetime;

    private String prodTime;

    private String useState;

    private String useStateName;

    private String reminderMemo;

    public BigDecimal getTotalQty() {
        return this.weighQty.add(this.bagWeight);
    }
}
