package com.daehanins.mes.biz.adm.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import com.daehanins.mes.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import com.daehanins.mes.base.BaseEntity;

/**
 * <p>
 * 프로그램기능Program 엔티티
 * </p>
 *
 * @author jeonsj
 * @since 2022-10-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("program")
public class Program extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 프로그램코드
     */
    @TableId
    private String programCd;

    /**
     * 프로그램그룹코드
     */
    private String programGrpCd;

    /**
     * 프로그램명
     */
    private String programName;

    /**
     * 경로
     */
    private String path;

    private Integer displayOrder;

    private String useYn;

}
