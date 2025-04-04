package com.daehanins.mes.biz.pub.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
* <p>
* MemberView 엔티티
* </p>
*
* @author jeonsj
* @since 2020-06-24
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("member_view")
public class MemberView {

    @TableId
    private String userId;

    private String orderedId;

    private String memberCd;

    private String memberName;

    private String jobPosition;

    private String department;

    private String cellPhone;

    private String email;

    private String useYn;

    private String memo;
}
