package com.daehanins.mes.biz.work.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
* <p>
* ProdRecordView 엔티티
* </p>
*
* @author jeonsj
* @since 2023-02-08
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("prod_record_view")
public class ProdRecordView {

    @TableId
    private String workOrderItemId;

    private String orderNo;

    private String areaCd;

    private String areaName;

    private String customerCd;

    private String customerName;

    private Integer batchSerNo;

    private String prodNo;

    private String lotNo;

    private String itemCd;

    private String itemName;

    private String itemTypeCd;

    private LocalDate prodDate;

    private Double prodQty;

    private Double orderQty;

    private String workEquipmentCd;

    private String equipmentName;

    private LocalDateTime workStartTime;

    private LocalDateTime workEndTime;

    private String workOrderItemStatus;

    private Double prodPrice;

}
