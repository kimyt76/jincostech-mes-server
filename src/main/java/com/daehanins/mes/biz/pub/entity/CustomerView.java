package com.daehanins.mes.biz.pub.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
* <p>
* CustomerView 엔티티
* </p>
*
* @author jeonsj
* @since 2020-04-03
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("customer_view")
public class CustomerView {

    @TableId
    private String customerCd;

    private String customerName;

    private String customerType;
    private String customerTypeName;

    private String president;
    private String presidentTel;

    private String tel;
    private String fax;

    private String businessType;
    private String businessItem;
    private String businessNo;

    private String customerManager;
    private String email;
    private String customerManagerTel;

    private String zipCode;
    private String address;

    private String memberCd;
    private String memberName;

    private String customerGrp1;
    private String customerGrp1Name;

    private String customerGrp2;
    private String customerGrp2Name;

    private String searchText;
    private String memo;
}
