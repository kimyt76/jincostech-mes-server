package com.daehanins.mes.biz.work.entity;

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
* GoodsBomItemView 엔티티
* </p>
*
* @author jeonsj
* @since 2020-04-23
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("goods_bom_item_view")
public class GoodsBomItemView {

    @TableId
    private String goodsBomItemId;

    private String itemCd;

    private String itemName;

    private String prodState;

    private String goodsBomId;

    private BigDecimal qty;

    private BigDecimal contentRatio;

    private String location;

    private String unit;

    private String memo;

    private String regId;

    private String updId;

    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private LocalDateTime regTime;

    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private LocalDateTime updTime;

}
