package com.daehanins.mes.biz.pub.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.daehanins.mes.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * ProcTestType 엔티티
 * </p>
 *
 * @author jeonsj
 * @since 2023-04-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("proc_test_type")
public class ProcTestType extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId
    private String procTestTypeId;

    private String testType;

    private String testTypeName;

    private String memo;

}
