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
 * 사용자로그UserLog 엔티티
 * </p>
 *
 * @author jeonsj
 * @since 2022-07-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("user_log")
public class UserLog extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId
    private String logId;

    /**
     * 사용일시
     */
    private LocalDateTime useDatetime;

    /**
     * 사용자ID
     */
    private String userId;

    /**
     * 기능코드
     */
    private String programCd;

    /**
     * 행위
     */
    private String action;

    /**
     * 사용내용
     */
    private String details;

}
