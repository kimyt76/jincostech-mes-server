package com.daehanins.mes.common.utils;

import com.google.common.collect.Maps;
import org.springframework.cglib.beans.BeanMap;

import java.util.*;

/**
 * @author Exrickx
 */
public class ObjectUtil {

    public static <T> Map<String, Object> beanToMap(T bean) {
        Map<String, Object> map = Maps.newHashMap();
        if (bean != null) {
            BeanMap beanMap = BeanMap.create(bean);
            for (Object key : beanMap.keySet()) {
                map.put(key+"", beanMap.get(key));
            }
        }
        return map;
    }

    public static <T> Set<T> convertListToSet(List<T> list)
    {
        Set<T> set = new HashSet<>();
        set.addAll(list);

        return set;
    }


}
