package com.daehanins.mes.biz.security.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
* <p>
* UserView 엔티티
* </p>
*
* @author jeonsj
* @since 2020-07-21
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("user_view")
public class UserView {

    @TableId
    private String userId;

    private String orderedId;

    private String memberName;

    private String jobPosition;

    private String department;

    private String memberCd;

    private String cellPhone;

    private String email;

    private String useYn;

    private String memo;

}
