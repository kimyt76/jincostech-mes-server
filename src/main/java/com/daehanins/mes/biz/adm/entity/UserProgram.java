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
 * 사용자프로그램UserProgram 엔티티
 * </p>
 *
 * @author jeonsj
 * @since 2022-10-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("user_program")
public class UserProgram extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 사용자프로그램ID
     */
    @TableId
    private Integer userProgramId;

    /**
     * 사용자ID
     */
    private String userId;

    /**
     * 기능코드
     */
    private String programCd;

    /**
     * 읽기권한
     */
    private String readYn;

    /**
     * 쓰기권한
     */
    private String writeYn;

    private String memo;

}
