package com.daehanins.mes.biz.common.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ErpPurchase {
    /*
        @JsonProperty 추가함,  없는 경우 key값이 lowerCamelCase로 변경됨
        이카운트 ERP는 key값을 대문자 그대로 사용하고 있음
     */
    @JsonProperty("ORD_DATE")
    private String ORD_DATE = "";            // 발주일자
    @JsonProperty("ORD_NO")
    private String ORD_NO = "";              // 발주번호
    @JsonProperty("IO_DATE")
    private String IO_DATE = "";             // 입고일자
    @JsonProperty("UPLOAD_SER_NO")
    private String UPLOAD_SER_NO = "";       // 순번
    @JsonProperty("CUST")
    private String CUST = "";                // 거래처코드
    @JsonProperty("CUST_DES")
    private String CUST_DES = "";            // 거래처명
    @JsonProperty("EMP_CD")
    private String EMP_CD = "";              // 담당자코드
    @JsonProperty("WH_CD")
    private String WH_CD = "";               // 입고창고코드
    @JsonProperty("IO_TYPE")
    private String IO_TYPE = "";
    @JsonProperty("EXCHANGE_TYPE")
    private String EXCHANGE_TYPE = "";       // 외화종류, 입력없으면 내자
    @JsonProperty("EXCHANGE_RATE")
    private String EXCHANGE_RATE = "";       // 환율
    @JsonProperty("PJT_CD")
    private String PJT_CD = "";
    @JsonProperty("U_MEMO1")
    private String U_MEMO1 = "";
    @JsonProperty("U_MEMO2")
    private String U_MEMO2 = "";
    @JsonProperty("U_MOMO3")
    private String U_MOMO3 = "";
    @JsonProperty("U_MEMO4")
    private String U_MEMO4 = "";
    @JsonProperty("U_MEMO5")
    private String U_MEMO5 = "";
    @JsonProperty("U_TXT1")
    private String U_TXT1 = "";
    @JsonProperty("PROD_CD")
    private String PROD_CD = "";             // 품목코드
    @JsonProperty("PROD_DES")
    private String PROD_DES = "";            // 품목명
    @JsonProperty("SIZE_DES")
    private String SIZE_DES = "";            // 규격
    @JsonProperty("UQTY")
    private String UQTY = "";                // 추가수량
    @JsonProperty("QTY")
    private String QTY = "";                 // 수량
    @JsonProperty("PRICE")
    private String PRICE = "";               // 단가
    @JsonProperty("USER_PRICE_VAT")
    private String USER_PRICE_VAT = "";
    @JsonProperty("SUPPLY_AMT")
    private String SUPPLY_AMT = "";          // 공급가액(원화)
    @JsonProperty("SUPPLY_AMT_F")
    private String SUPPLY_AMT_F = "";
    @JsonProperty("VAT_AMT")
    private String VAT_AMT = "";             // 부가세
    @JsonProperty("REMARKS")
    private String REMARKS = "";             // 적요
    @JsonProperty("ITEM_CD")
    private String ITEM_CD = "";
    @JsonProperty("P_AMT1")
    private String P_AMT1 = "";
    @JsonProperty("P_AMT2")
    private String P_AMT2 = "";
    @JsonProperty("P_REMARKS1")
    private String P_REMARKS1 = "";
    @JsonProperty("P_REMARKS2")
    private String P_REMARKS2 = "";
    @JsonProperty("P_REMARKS3")
    private String P_REMARKS3 = "";
    @JsonProperty("P_REMARKS4")
    private String P_REMARKS4 = "";
    @JsonProperty("CUST_AMT")
    private String CUST_AMT = "";
}
