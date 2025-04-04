package com.daehanins.mes.biz.common.code;

public class ItemTypeCd {
    public static final String 원재료 = "M1";   // 원재료
    public static final String 부재료 = "M2";   // 부재료
    public static final String 반제품 = "M3";   // 반제품
    public static final String 벌크제품 = "M5";   // 반제품
    public static final String 포장품 = "M6";   // 반제품
    public static final String 완제품 = "M0";     // 제품
    public static final String 상품 = "M4";     // 상품

    public static String getLableTitle(String itemTypeCd) {
        String labelTitle = "원    료";
        switch (itemTypeCd) {
            case 원재료:
                labelTitle = "원    료"; break;
            case 부재료:
                labelTitle = "부 재 료"; break;
            case 반제품:
                labelTitle = "반 제 품"; break;
            case 벌크제품:
                labelTitle = "벌크제품"; break;
            case 포장품:
                labelTitle = "포 장 품"; break;
            case 완제품:
                labelTitle = "완 제 품"; break;
            case 상품:
                labelTitle = "상    품"; break;
            default:
        }
        return labelTitle;
    }
}
