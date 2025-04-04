package com.daehanins.mes.biz.work.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
* <p>
* GoodsBomView 엔티티
* </p>
*
* @author jeonsj
* @since 2020-04-23
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("goods_bom_view")
public class GoodsBomView {

    @TableId
    private String goodsBomId;

    private String itemTypeCd;

    private String itemTypeName;

    private String itemCd;

    private String itemName;

    private String processCd;

    private String bomVer;

    private BigDecimal prodQty;

    private Integer cnt;

    private BigDecimal sumQty;

    private String defaultYn;

    private String memo;

    private String unit;

    private String regId;

    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private LocalDateTime regTime;

    private String updId;

    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private LocalDateTime updTime;
}
