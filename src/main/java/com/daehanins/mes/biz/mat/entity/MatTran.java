package com.daehanins.mes.biz.mat.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.daehanins.mes.base.BaseEntity;
import com.daehanins.mes.common.utils.SnowFlakeUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 자재거래MatTran 엔티티
 * </p>
 *
 * @author jeonsj
 * @since 2020-04-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("mat_tran")
public class MatTran extends BaseEntity {

    private static final long serialVersionUID = -1282925776808674116L;

    /**
     * 자재거래ID
     */
    @TableId
    private String matTranId = String.valueOf(SnowFlakeUtil.getFlowIdInstance().nextId());

    /**
     * 자재요청(지시)ID
     */
    private String matOrderId;

    /**
     * 자재거래유형코드
     */
    private String tranCd;

    /**
     * 자재거래유형Sub코드
     */
    private Integer tranSign;

    /**
     * 거래일자
     */
    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate tranDate;

    /**
     * 일련번호
     */
    private Integer serNo;

    /**
     * 비고
     */
    private String memo;

    /**
     * 등록 담당자코드
     */
    private String memberCd;

    /**
     * 확인 담당자코드
     */
    private String confirmMemberCd;

    /**
     * 거래처코드
     */
    private String customerCd;


    /**
     * 구역(공장)
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
     * 발주구분
     */
    private String orderType;

    /**
     * 거래유형코드
     */
    private String vatType;

    /**
     * ERP연계여부
     */
    private String erpYn;

    /**
     * 인쇄여부
     */
    private String printYn;

    /**
     * 확인상태
     */
    private String confirmState;

    /**
     * 종료여부
     */
    private String endYn;

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
}
