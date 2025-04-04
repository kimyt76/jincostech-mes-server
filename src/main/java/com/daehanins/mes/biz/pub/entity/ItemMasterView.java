package com.daehanins.mes.biz.pub.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
* <p>
* ItemMasterView 엔티티
* </p>
*
* @author jeonsj
* @since 2020-04-06
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("item_master_view")
public class ItemMasterView {

    @TableId
    private String itemCd;
    private String itemName;

    private String itemTypeCd;
    private String itemTypeName;
    private String erpItemTypeName;

    private String spec;
    private String unit;
    private BigDecimal unitWeight;

    private String itemGrp1;
    private String itemGrp1Name;
    private String itemGrp2;
    private String itemGrp2Name;
    private String itemGrp3;
    private String itemGrp3Name;

    private String itemCondition;
    private String itemConditionName;
    private String appearance;

    private String purchaseCustomerCd;
    private String customerName;

    private String useSafeStockYn;
    private BigDecimal safeStockQty;

    private BigDecimal inPrice;
    private BigDecimal outPrice;

    private String useYn;
    private String barcode;

    private String searchText;

    public String findUseYnErpName() {
        return this.useYn.equals("Y") ? "YES" : "NO";
    }

    /** 이론생산계수*/
    private BigDecimal theoryProdNumber1;
    private BigDecimal  theoryProdNumber2;

    /** 목형번호 */
    private String  woodenPattern;

    /** 제품타입 */
    private String  prodType;

    /** 시트 폭 너비 적층수 */
    private BigDecimal  sheetWidth;
    private BigDecimal  sheetLength;
    private Integer  sheetStacking;

    /** 기준무게 기준사이즈 */
    private String  stdWeight;
    private String  stdSize;

    /** 재료명, 규격정보, 외관, 단위별포장규격, 표시용량 */
    private String  matName;
    private String  specInfo;
    private String  exAppearance;
    private String  packingSpecValue;
    private String  packingSpecUnit;
    private String  displayCapacity;

    /** 작업공정도, 발행자특이사항 */
    private String  workFlow;
    private String  reminderMemo;

    /** 수율공식(표기용), 품목기준수율 */
    private String  displayYield;
    private BigDecimal  stdYield;

    private String cosTypeCd;
    private String functionalTypeCd;

    private String functionalTypeName;

    private BigDecimal prodPrice;

    private String weighType;

    private String weighTypeName;

    private String weighFuncType;

    private String weighFuncTypeName;
    private String history;
    private String relatedCustomer;

    private String relatedCustomerName;

    private String prodLgType;

    private String prodLgTypeName;

    private String prodMdType;

    private String prodMdTypeName;
    private String sheetSpec;
    private String chargeResearcher;
    private String chargeSalesman;
    private String labNo;

    private String qcProcTestType;
    private String qcProcTestTypeName;

    private String chargingQtys;
    private String chargingCnt;
    private String cappingRange;
    private String essenceStd;
    private String coolingTemp;

}
