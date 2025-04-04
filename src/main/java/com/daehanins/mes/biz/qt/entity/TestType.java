package com.daehanins.mes.biz.qt.entity;

import com.baomidou.mybatisplus.annotation.TableField;
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
 * 품질검사유형TestType 엔티티
 * </p>
 *
 * @author jeonsj
 * @since 2020-07-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("test_type")
public class TestType extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 품질검사유형코드
     */
    @TableId
    private String testTypeId = String.valueOf(SnowFlakeUtil.getFlowIdInstance().nextId());

    /**
     * 품질검사유형명
     */
    private String testTypeName;

    /**
     * 사용여부
     */
    private String useYn;

    /**
     * 비고(설명)
     */
    private String memo;

    /**
     * 품목코드
     */
    @TableField(exist = false)
    private String itemCd;

}
