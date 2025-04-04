package com.daehanins.mes.biz.adm.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.daehanins.mes.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * SystemEnvConfig 엔티티
 * </p>
 *
 * @author jeonsj
 * @since 2022-07-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("system_env_config")
public class SystemEnvConfig extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId
    private String envConfigCd;

    private String varName;

    private String varValue;

    private String description;

}
