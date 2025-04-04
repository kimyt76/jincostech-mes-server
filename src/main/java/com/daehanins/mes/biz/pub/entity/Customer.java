package com.daehanins.mes.biz.pub.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.daehanins.mes.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * Customer 엔티티
 * </p>
 *
 * @author jeonsj
 * @since 2020-04-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("customer")
public class Customer extends BaseEntity {

    private static final long serialVersionUID = -7699336767696467315L;

    /**
     * 거래처코드
     */
    @TableId
    private String customerCd;

    /**
     * 상호(이름)
     */
    private String customerName;

    /**
     * 거래처구분코드
     */
    private String customerType;

    /**
     * 사업자등록번호
     */
    private String businessNo;

    /**
     * 업태
     */
    private String businessType;

    /**
     * 종목
     */
    private String businessItem;

    /**
     * 전화번호
     */
    private String tel;

    /**
     * 팩스
     */
    private String fax;

    /**
     * 이메일
     */
    private String email;

    /**
     * 대표자명
     */
    private String president;

    /**
     * 대표자전화번호
     */
    private String presidentTel;

    /**
     * 거래처담당자
     */
    private String customerManager;

    /**
     * 거래처담당자전화번호
     */
    private String customerManagerTel;

    /**
     * 우편번호
     */
    private String zipCode;

    /**
     * 주소
     */
    private String address;

    /**
     * (담당)사원코드
     */
    private String memberCd;

    /**
     * 거래처구분1
     */
    private String customerGrp1;

    /**
     * 거래처구분2
     */
    private String customerGrp2;

    /**
     * 검색창 내용
     */
    private String searchText;

    /**
     * 메모
     */
    private String memo;

}
