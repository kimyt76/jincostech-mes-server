package com.daehanins.mes.biz.mat.entity;

import com.baomidou.mybatisplus.annotation.TableField;
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
import java.time.LocalDateTime;

/**
 * <p>
 * 자재거래품목MatTranItem 엔티티
 * </p>
 *
 * @author jeonsj
 * @since 2020-04-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("mat_tran_item")
public class MatTranItem extends BaseEntity {

    private static final long serialVersionUID = -2081464450879070522L;

    /**
     * 자재거래품목ID
     */
    @TableId
    private String matTranItemId = String.valueOf(SnowFlakeUtil.getFlowIdInstance().nextId());

    /**
     * 자재거래ID
     */
    private String matTranId;

    /**
     * 품목코드
     */
    private String itemCd;

    /**
     * 품목코드명
     */
    private String itemName;

    /**
     * 품목구분코드
     */
    private String itemTypeCd;

    /**
     * 수량
     */
    private BigDecimal qty;

    /**
     * 로트번호
     */
    private String lotNo;

    /**
     * 시험번호
     */
    private String testNo;

    /**
     * 사용기한
     */
    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate expiryDate;

    /**
     * 규격
     */
    private String spec;

    /**
     * 단가
     */
    private BigDecimal price;

    /**
     * 공급액
     */
    private BigDecimal supplyAmt;

    /**
     * 부가세
     */
    private BigDecimal vat;

    /**
     * 적요
     */
    private String memo;

    /**
     * 확인상태
     */
    private String confirmState;

    /**
     * itemType을 시험번호 생성의 itemGb로 매칭하여 반환
     */
    public String findItemGb() {
        String itemGb;
        switch(itemTypeCd) {
            case "M0" :  itemGb = "6"; break;
            case "M1" :  itemGb = "1"; break;
            case "M2" :  itemGb = "2"; break;
            case "M3" :  itemGb = "3"; break;
            case "M4" :  itemGb = "7"; break;
            case "M5" :  itemGb = "4"; break;
            case "M6" :  itemGb = "5"; break;
            case "M7" :  itemGb = "2"; break;
            default: itemGb = "6";
        }
        return itemGb;
    }

    public String findProcName() {
        String procName;
        switch(confirmState) {
            case "Y" :  procName = "합격"; break;
            case "N" :  procName = "불합격"; break;
            case "G" :  procName = "시험중"; break;
            default: procName = "시험대기";
        }
        return procName;
    }
}
