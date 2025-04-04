package com.daehanins.mes.biz.common.code;

public class ProcessCd {
    public static final String 칭량 = "PRC001";   // 등록
    public static final String 제조 = "PRC002";   // 등록
    public static final String 코팅 = "PRC003";   // 등록
    public static final String 충전 = "PRC004";   // 등록
    public static final String 포장 = "PRC005";   // 등록

    /** 공정코드별 시험검사 유무를 boolean으로 반환한다. **/
    public static boolean getTestYn(String processCd) {
        boolean result = false;
        if ( processCd.equals(ProcessCd.제조) || processCd.equals(ProcessCd.충전) ) {
            result = true;
        }
        return result;
    }
}
