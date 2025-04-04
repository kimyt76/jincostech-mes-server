package com.daehanins.mes.biz.common.util;

import java.time.LocalDate;

public class BizDateUtil {
    // 보관기한 산출 :  2년후 일자와 사용기한 중 작은 값
    public static LocalDate calcShelfLife(LocalDate tranDate, LocalDate expiryDate) {
        LocalDate shelfLife = tranDate.plusDays(365 * 2);   // 2년
        return shelfLife.isBefore(expiryDate) ? shelfLife : expiryDate;
    }
}
