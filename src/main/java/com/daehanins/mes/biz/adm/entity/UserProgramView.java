package com.daehanins.mes.biz.adm.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
* <p>
* UserProgramView 엔티티
* </p>
*
* @author jeonsj
* @since 2022-10-03
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("user_program_view")
public class UserProgramView {

    @TableId
    private Integer userProgramId;

    private String userId;

    private String programGrpCd;

    private String programGrpName;

    private String programCd;

    private String programName;

    private String path;

    private String readYn;

    private String writeYn;

    private String memo;

    private Integer displayOrder;

}
