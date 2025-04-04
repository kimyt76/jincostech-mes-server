package com.daehanins.mes.biz.common.code;

public class ItemGb {
    public static final String 원재료       = "1";   // 등록
    public static final String 부자재       = "2";   // 등록
    public static final String 반제품       = "3";   // 등록
    public static final String 완제품       = "4";   // 등록
    public static final String 벌크제품     = "5";   // 등록
    public static final String 포장품       = "6";   // 등록
    public static final String 제품         = "7";   // 등록

    public static String findItemGb(String processCd) {
        String result = "";
        switch (processCd) {
            case ProcessCd.칭량:
            case ProcessCd.제조: result = ItemGb.반제품; break;
            case ProcessCd.코팅: result = ItemGb.벌크제품; break;
            case ProcessCd.충전: result = ItemGb.포장품; break;
            case ProcessCd.포장: result = ItemGb.완제품; break;
            default: break;
        }
        return result;
    }
}
