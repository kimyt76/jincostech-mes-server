package com.daehanins.mes.biz.common.code;

public class ProdStorageCd {

    public static final String 시화칭량 = "WS102";   // 등록
    public static final String 시화제조 = "WS103";   // 등록
    public static final String 시화코팅 = "WS104";   // 등록
    public static final String 시화현장 = "WS005";   // 등록

    public static final String 안산칭량 = "WA101";   // 등록
    public static final String 안산제조 = "WA102";   // 등록
    public static final String 안산코팅 = "WA103";   // 등록
    public static final String 안산현장 = "WA005";   // 등록


    /** 공정코드별 각 공정에 해당하는 시작상태를 반환한다. **/
    public static String getFieldStorageCd(String areaCd) {
        String storageCd = "";
        switch ( areaCd ) {
            case AreaCd.시화공장 : storageCd = ProdStorageCd.시화현장; break;
            case AreaCd.안산공장 : storageCd = ProdStorageCd.안산현장; break;
            default: break;
        }
        return storageCd;
    }
}
