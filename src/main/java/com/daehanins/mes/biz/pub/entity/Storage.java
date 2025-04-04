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
 * 창고Storage 엔티티
 * </p>
 *
 * @author jeonsj
 * @since 2020-04-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("storage")
public class Storage extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 창고코드
     */
    @TableId
    private String storageCd;

    /**
     * 창고명
     */
    private String storageName;

    /**
     * 구역(공장)코드
     */
    private String areaCd;

    /**
     * 공정코드
     */
    private String processCd;

    /**
     * 작업처 여부
     */
    private String prodYn;

    /**
     * 원재료 취급 여부
     */
    private String aYn;

    /**
     * 부자재 취급 여부
     */
    private String bYn;

    /**
     * 제조품 취급 여부
     */
    private String cYn;

    /**
     * 코팅품 취급 여부
     */
    private String dYn;

    /**
     * 충전품 취급 여부
     */
    private String eYn;

    /**
     * 포장품 취급 여부
     */
    private String fYn;

    /**
     * 포장품 취급 여부
     */
    private String ioYn;

    /**
     * 표시순서
     */
    private Integer displayOrder;
    
    /**
     * 사용여부
     */
    private String useYn;

    /**
     * 이전코드
     */
    private String exCode;

}
