package com.daehanins.mes.biz.common.code;

public class OrderState {
    public static final String REQ = "REQ";   // 요청
    public static final String ING = "ING";   // 진행
    public static final String END = "END";   // 완료

    public static String getOrderStateName (String orderState) {
        String result = "";
        if(orderState.equals(REQ)) result = "요청";
        if(orderState.equals(ING)) result = "진행";
        if(orderState.equals(END)) result = "완료";
        return result;
    }
}
