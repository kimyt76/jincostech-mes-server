package com.daehanins.mes.common.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.daehanins.mes.base.ConditionVo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * @author jeonsj
 */
public class SearchUtil {

    public  static <E> QueryWrapper<E> parseWhereSql (String conditionJson) {
        QueryWrapper<E> queryWrapper = new QueryWrapper<E>();
        if (StringUtils.isNotBlank (conditionJson)) {
            // JSON 조건 파싱
            Gson gson = new Gson();
            Map<String, ConditionVo> conditionMap = gson.fromJson(conditionJson, new TypeToken<Map<String, ConditionVo>>(){}.getType());
            if (ObjectUtils.isNotEmpty(conditionMap)) {
                // 조건식 값에 따라 query 조합
                for (ConditionVo conditionVo : conditionMap.values()) {
                    queryWrapper = addCondition(conditionVo, queryWrapper);
                }
            }
        }
        return queryWrapper;
    }

    public static Map<String,Object> parseParam (String conditionJson) {
        Map<String,Object> param = new HashMap<String,Object>();

        if (StringUtils.isNotBlank (conditionJson)) {
            // JSON 조건 파싱
            Gson gson = new Gson();
            Map<String, ConditionVo> conditionMap = gson.fromJson(conditionJson, new TypeToken<Map<String, ConditionVo>>(){}.getType());
            if (ObjectUtils.isNotEmpty(conditionMap)) {
                for(Map.Entry<String, ConditionVo> elem : conditionMap.entrySet()){
                    String key = elem.getKey();
                    ConditionVo value = elem.getValue();
                    param.put(key, value.getValue());
                }
            }
        }
        return param;
    }


    private static <E> QueryWrapper<E> addCondition(ConditionVo conditionVo, QueryWrapper<E> queryWrapper ) {
        boolean bool = false;
        if (ObjectUtils.isNotEmpty(conditionVo.getValue ())) {
            switch (conditionVo.getType ()) {
                case  "eq" :
                    queryWrapper.eq(StringUtils.camelToUnderline(conditionVo.getColumn()), conditionVo.getValue());
                    break ;
                case  "ne" :
                    queryWrapper.ne(StringUtils.camelToUnderline(conditionVo.getColumn()), conditionVo.getValue());
                    break ;
                case  "gt" :
                    queryWrapper.gt(StringUtils.camelToUnderline(conditionVo.getColumn()), conditionVo.getValue());
                    break ;
                case  "lt" :
                    queryWrapper.lt(StringUtils.camelToUnderline(conditionVo.getColumn()), conditionVo.getValue());
                    break ;
                case  "ge" :
                    queryWrapper.ge(StringUtils.camelToUnderline(conditionVo.getColumn()), conditionVo.getValue());
                    break ;
                case  "le" :
                    queryWrapper.le(StringUtils.camelToUnderline(conditionVo.getColumn()), conditionVo.getValue());
                    break ;
                case  "in" :
                    String[] inValues = conditionVo.getValue().split(",");
                    String[] trimValues = Arrays.stream(inValues).map(String::trim).toArray(String[]::new);
                    queryWrapper.in(StringUtils.camelToUnderline(conditionVo.getColumn()), Arrays.asList(trimValues));
                    break ;
                case  "like" :
                    String likeWord = conditionVo.getValue().replace("[", "[[]");
                    //queryWrapper.like(StringUtils.camelToUnderline(conditionVo.getColumn()), conditionVo.getValue());
                    queryWrapper.like(StringUtils.camelToUnderline(conditionVo.getColumn()), likeWord);
                    break ;
                case  "notLike" :
                    queryWrapper.notLike(StringUtils.camelToUnderline(conditionVo.getColumn()), conditionVo.getValue());
                    break ;
                case  "likeLeft" :
                    queryWrapper.likeLeft(StringUtils.camelToUnderline(conditionVo.getColumn()), conditionVo.getValue());
                    break ;
                case  "likeRight" :
                    queryWrapper.likeRight(StringUtils.camelToUnderline(conditionVo.getColumn()), conditionVo.getValue());
                    break ;
                case  "isNull" :
                    if (conditionVo.getValue().equals("Y")) {
                        queryWrapper.isNull(StringUtils.camelToUnderline(conditionVo.getColumn()));
                    } else {
                        queryWrapper.isNotNull(StringUtils.camelToUnderline(conditionVo.getColumn()));
                    }
                    break;
                case  "isNotNull" :
                    if (conditionVo.getValue().equals("Y")) {
                        queryWrapper.isNotNull(StringUtils.camelToUnderline(conditionVo.getColumn()));
                    } else {
                        queryWrapper.isNull(StringUtils.camelToUnderline(conditionVo.getColumn()));
                    }
                    break ;
            }
        }
        return queryWrapper;
    }

}
