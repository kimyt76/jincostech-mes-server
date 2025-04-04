package com.daehanins.mes.biz.security.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.daehanins.mes.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * UserRoles 엔티티
 * </p>
 *
 * @author jeonsj
 * @since 2020-05-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("user_roles")
public class UserRole extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String userId;

    private String roleId;

}
