package com.daehanins.mes.biz.pub.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
* <p>
* PdrMatSubView 엔티티
* </p>
*
* @author jeonsj
* @since 2023-01-19
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("pdr_mat_sub_view")
public class PdrMatSubView {

    private String pdrMatSubId;

    private String pdrSellId;

    private String pdrMasterId;

    private Integer displayOrder;

    private String itemCd;

    private String itemName;

    private Double amt;

    private Double price;

    private Double m2Cost;

}
