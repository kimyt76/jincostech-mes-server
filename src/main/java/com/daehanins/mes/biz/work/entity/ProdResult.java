package com.daehanins.mes.biz.work.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.daehanins.mes.base.BaseEntity;
import com.daehanins.mes.common.utils.SnowFlakeUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * <p>
 * 생산실적ProdResult 엔티티
 * </p>
 *
 * @author jeonsj
 * @since 2020-05-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("prod_result")
public class ProdResult extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 생산실적ID
     */
    @TableId
    private String prodResultId = String.valueOf(SnowFlakeUtil.getFlowIdInstance().nextId());

    /**
     * 작업지시상세ID
     */
    private String workOrderItemId;

    /**
     * 생산(작업)일자
     */
    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate prodDate;

    /**
     * 사용기한
     */
    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate expiryDate;

    /**
     * 제품품목코드
     */
    private String itemCd;

    /**
     * 품목구분
     */
    private String itemTypeCd;

    /**
     * 구역(공장)코드
     */
    private String areaCd;

    /**
     * 소스창고코드
     */
    private String srcStorageCd;

    /**
     * 대상창고코드
     */
    private String destStorageCd;

    /**
     * 생산량
     */
    private BigDecimal prodQty;

    /**
     * 불량량
     */
    private BigDecimal badQty;

    /**
     * 단위중량
     */
    private BigDecimal unitWeight;

    /**
     * 생산환산중량
     */
    private BigDecimal prodConvQty;

    /**
     * 실적 tran 반영 여부
     */
    private String prodTranYn;

    /**
     * 실적 tran 반영 id
     */
    private String prodTranId;

    /**
     * 시험번호
     */
    private String testNo;

    /**
     * 로트번호
     */
    private String lotNo;

    /**
     * 제조번호
     */
    private String prodNo;

    /**
     * 제형id
     */
    private String cosFormulaId;

    /**
     * 적요
     */
    private String memo;

    /**
     * 구역정보을 시험번호 생성의 areaGb로 매칭하여 반환
     */
    public String findAreaGb() {
        String areaGb;
        switch(areaCd) {
            case "A001" :  areaGb = "1"; break;
            case "A002" :  areaGb = "2"; break;
            default: areaGb = "1";
        }
        return areaGb;
    }

    /**
     * itemType을 시험번호 생성의 itemGb로 매칭하여 반환
     */
    public String findItemGb() {
        String itemGb;
        switch(itemTypeCd) {
            case "M0" :  itemGb = "4"; break;
            case "M1" :  itemGb = "1"; break;
            case "M2" :  itemGb = "2"; break;
            case "M3" :  itemGb = "3"; break;
            default: itemGb = "5";
        }
        return itemGb;
    }
}
