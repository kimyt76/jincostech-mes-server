package com.daehanins.mes.biz.pub.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.daehanins.mes.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 품목마스터ItemMaster 엔티티
 * </p>
 *
 * @author jeonsj
 * @since 2020-04-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("item_master")
public class ItemMaster extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /////////////////  ERP 품목기본정보  /////////////////////
    /** 품목코드 **/
    @TableId
    private String itemCd;

    /** 품명 **/
    private String itemName;

    /** 품목구분코드 **/
    private String itemTypeCd;

    /** 구매거래처코드 **/
    private String purchaseCustomerCd;

    /** 단위 **/
    private String unit;

    /** 규격 **/
    private String spec;

    /** 입고단가 **/
    private BigDecimal inPrice;

    /** 출고단가 **/
    private BigDecimal outPrice;

    /** 바코드 **/
    private String barcode;

    /** 검색창내용 **/
    private String searchText;

    /** 품목그룹1 **/
    private String itemGrp1;

    /** 품목그룹2 **/
    private String itemGrp2;

    /** 품목그룹3 **/
    private String itemGrp3;

    /** 사용여부 **/
    private String useYn;


    /////////////////  품목추가정보  /////////////////////

    /** 안전재고 **/
    private BigDecimal safeStockQty;

    /** 안전재고사용여부 **/
    private String useSafeStockYn;

    /** 단위중량 **/
    private BigDecimal unitWeight;

    /** 보관조건 **/
    private String itemCondition;

    /** 성상 **/
    private String appearance;

    /** 제품타입 */
    private String  prodType;

    /** 이론생산계수*/
    private BigDecimal theoryProdNumber1;
    private BigDecimal  theoryProdNumber2;

    /** 목형번호 */
    private String  woodenPattern;

    /** 시트 폭 너비 적층수 */
    private BigDecimal  sheetWidth;
    private BigDecimal  sheetLength;
    private Integer  sheetStacking;

    /** 기준무게 기준사이즈 */
    private String  stdWeight;
    private String  stdSize;

    /** 재료명, 규격정보, 외관, 단위별포장규격값, 단위별포장규격단위, 표시용량 */
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

    private BigDecimal prodPrice;

    private String weighType;
    private String weighFuncType;
    private String history;
    private String relatedCustomer;
    private String prodLgType;
    private String prodMdType;
    private String sheetSpec;
    private String chargeResearcher;
    private String chargeSalesman;
    private String labNo;

    private String qcProcTestType;

    private String chargingQtys;
    private String chargingCnt;
    private String cappingRange;
    private String essenceStd;
    private String coolingTemp;

}
