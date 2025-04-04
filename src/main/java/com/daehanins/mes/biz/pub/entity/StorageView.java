package com.daehanins.mes.biz.pub.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
* <p>
* StorageView 엔티티
* </p>
*
* @author jeontm
* @since 2021-02-04
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("storage_view")
public class StorageView {

    @TableId
    private String storageCd;
    private String storageName;

    private String areaCd;
    private String areaName;

    private String processCd;
    private String processName;

    private String aYn;
    private String bYn;
    private String cYn;
    private String dYn;
    private String eYn;
    private String fYn;
    private String ioYn;

    private Integer displayOrder;

    private String prodYn;

    @TableField(exist = false)
    private String prodYnName;
    
    public String getProdYnName() {
        return (this.prodYn.equals("Y"))? "작업처" : "창고";
    }
    
    private String useYn;

    @TableField(exist = false)
    private String useYnName;

    public String getUseYnName() {
        return (this.useYn.equals("Y"))? "사용중" : "미사용";
    }

}
