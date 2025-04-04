package com.daehanins.mes.biz.common.code;

public class AreaCd {

    public static final String 시화공장 = "A001";
    public static final String 안산공장 = "A002";

    public static String findAreaGb (String areaCd) {
        String result = "";
        if(areaCd.equals(AreaCd.시화공장)) result = "1";
        if(areaCd.equals(AreaCd.안산공장)) result = "2";
        return result;
    }

}
