package com.daehanins.mes.biz.tag.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.daehanins.mes.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * TagInfo 엔티티
 * </p>
 *
 * @author jeonsj
 * @since 2021-04-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("tag_info")
public class TagInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId
    private String tagCd;

    private String tagName;

    private String dataType;

    private String gatherType;

    private String source;

    private String address;

    private String itemId;

    private Integer readSize;

    private Integer conversionFactor;

    private BigDecimal readValue;

    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd HH:mm:ss.SSS")
    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss.SSS")
    private LocalDateTime readTime;

    private String manageYn;

}
