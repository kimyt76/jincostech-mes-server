package com.daehanins.mes.common.utils;


import org.apache.commons.lang3.StringUtils;

public class BizUtil {

    private static String[] HAN_TBL1 = {"", "일", "이", "삼", "사", "오", "육", "칠", "팔", "구"};
    private static String[] HAN_TBL2 = {"", "십", "백", "천"};
    private static String[] HAN_TBL3 = {"", "만", "억", "조", "경"};

    public static String getHangulNumber(String money) {
        StringBuffer sf = new StringBuffer();

        int num;
        int digit = money.length();
        for (int i = 0; i < money.length(); i++) {
            digit--;
            num = Integer.parseInt(money.charAt(i) +"");
            sf.append(HAN_TBL1[num]);
            if (num > 0)
                sf.append(HAN_TBL2[digit % 4]);
            if (digit % 4 == 0)
                sf.append(HAN_TBL3[digit / 4]);
        }

        return sf.toString();
    }

}
