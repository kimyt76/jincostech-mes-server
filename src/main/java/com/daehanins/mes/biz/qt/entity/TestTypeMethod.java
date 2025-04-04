package com.daehanins.mes.biz.qt.entity;

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
 * 품질검사유형상세TestTypeMethod 엔티티
 * </p>
 *
 * @author jeonsj
 * @since 2020-07-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("test_type_method")
public class TestTypeMethod extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 품질검사유형상세id
     */
    @TableId
    private String testTypeMethodId = String.valueOf(SnowFlakeUtil.getFlowIdInstance().nextId());

    /**
     * 품질검사유형id
     */
    private String testTypeId;

    /**
     * 품목코드
     */
    private String itemCd;

    /**
     * 표시순서
     */
    private Integer displayOrder;

    /**
     * 검사항목
     */
    private String testItem;

    /**
     * 시험방법
     */
    private String testMethod;

    /**
     * 시험기준
     */
    private String testSpec;

    /**
     * 시험결과
     */
    private String testResult;

}
