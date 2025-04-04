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
 * 품질검사상세QualityTestMethod 엔티티
 * </p>
 *
 * @author jeonsj
 * @since 2020-07-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("quality_test_method")
public class QualityTestMethod extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 품질검사상세id
     */
    @TableId
    private String qualityTestMethodId = String.valueOf(SnowFlakeUtil.getFlowIdInstance().nextId());;

    /**
     * 품질검사id
     */
    private String qualityTestId;

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

    /**
     * 검사일자
     */
    private String testDateString;

    /**
     * 검사자
     */
    private String testMemberName;

    /**
     * 판정상태
     */
    private String passState;

    /**
     * 판정상태명
     */
    @TableField(exist = false)
    private String passStateName;

}
