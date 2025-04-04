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
 * 설비Equipment 엔티티
 * </p>
 *
 * @author jeonsj
 * @since 2020-05-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("equipment")
public class Equipment extends BaseEntity {

    private static final long serialVersionUID = 2557427617677315321L;

    /**
     * 설비코드
     */
    @TableId
    private String equipmentCd;

    /**
     * 설비명
     */
    private String equipmentName;

    /**
     * 창고및작업처 코드
     */
    private String storageCd;

    /**
     * 표시순서
     */
    private Integer displayOrder;

    /**
     * 제조설비여부
     */
    private String prodYn;

    /**
     * 사용여부
     */
    private String useYn;

}
