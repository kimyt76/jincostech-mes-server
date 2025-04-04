package com.daehanins.mes.test;


import com.daehanins.mes.common.utils.BizUtil;
import com.daehanins.mes.common.utils.AuthUtil;

import java.math.BigDecimal;

public class Test {

    public static void main(String[] args) {

//        System.out.println(ERole.ROLE_ADMIN.name());

//        BigDecimal amount = new BigDecimal(123456789).add(new BigDecimal(12345678));
//        String strAmount = amount.toString();
//        System.out.println(BizUtil.getHangulNumber("1"));
//        System.out.println(BizUtil.getHangulNumber("12"));
//        System.out.println(BizUtil.getHangulNumber("1234"));
//        System.out.println(BizUtil.getHangulNumber("12345"));
//        System.out.println(BizUtil.getHangulNumber(strAmount));


        System.out.println(AuthUtil.encodePassword("1234"));

        System.out.println(AuthUtil.encodePassword("1"));
    }
}
