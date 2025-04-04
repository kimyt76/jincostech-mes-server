package com.daehanins.mes.biz.security.entity;

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
 * 역할Roles 엔티티
 * </p>
 *
 * @author jeonsj
 * @since 2020-05-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("roles")
public class Role extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 역할코드
     */
    @TableId("role_id")
    private String id = String.valueOf(SnowFlakeUtil.getFlowIdInstance().nextId());

    /**
     * 역할명
     */
    @TableField("role_name")
    private String name;

    /**
     * 설명
     */
    private String description;

}
