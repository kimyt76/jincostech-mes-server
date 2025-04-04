package com.daehanins.mes.biz.work.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.daehanins.mes.base.BaseEntity;
import com.daehanins.mes.common.utils.SnowFlakeUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * ProdProcess 엔티티
 * </p>
 *
 * @author jeonsj
 * @since 2023-02-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("prod_process")
public class ProdProcess extends BaseEntity {

/*
 * 확인할 사항:
 *     1) PK컬럼에 @TableId 어노테이션 설정
 *     2) BaseEntity에서 상속받은 컬럼은 삭제, regId, regTime, updId, updTime
 */
    private static final long serialVersionUID = 1L;

    @TableId
    private String prodProcessId = String.valueOf(SnowFlakeUtil.getFlowIdInstance().nextId());

    private String workOrderItemId;

    private Integer prodOrder;

    private String prodState;

    private String detail;

    private String prodStartTime;

    private String prodEndTime;

    private String prodMember;

    private String confirmMember;

}
