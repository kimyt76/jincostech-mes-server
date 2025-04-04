package com.daehanins.mes.biz.pub.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
* <p>
* EquipmentView 엔티티
* </p>
*
* @author jeonsj
* @since 2020-05-27
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("equipment_view")
public class EquipmentView {

    @TableId
    private String equipmentCd;

    private String equipmentName;

    private String areaCd;

    private String areaName;

    private String storageCd;

    private String storageName;

    private Integer displayOrder;

    private String prodYn;

    private String useYn;

    @TableField(exist = false)
    private String useYnName;

    public String getUseYnName () {
        return (useYn.equals("Y"))? "사용" : "미사용";
    }

    /*2021-12-29 추가*/
    private String workOrderItemId;

    private String prodNo;

    private String lotNo;

    private String itemCd;

    private String itemName;

    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate orderDate;

    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate prodDate;

    private BigDecimal orderQty;

    private BigDecimal prodQty;

    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private LocalDateTime workStartTime;

    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private LocalDateTime workEndTime;

    private String workOrderItemStatus;

    private String itemTestNo;


}
