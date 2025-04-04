package com.daehanins.mes.base;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.daehanins.mes.common.utils.RestUtil;
import com.daehanins.mes.common.utils.SearchUtil;
import com.daehanins.mes.common.vo.PageVo;
import com.daehanins.mes.common.vo.RestResponse;
import org.springframework.web.bind.annotation.*;

/**
 * @author jeonsj
 */
public abstract class BaseReadOnlyController<T, V, ID extends Serializable> {

    public abstract IService<T> getTableService();

    public abstract IService<V> getViewService();

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<T> get(@PathVariable ID id){

        T entity = getTableService().getById(id);
        return new RestUtil<T>().setData(entity);
    }

    @RequestMapping(value = "/getAll",method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<List<T>> getAll(@RequestParam (name="condition", required=false ) String conditionJson){

        QueryWrapper<T> queryWrapper = SearchUtil.parseWhereSql(conditionJson);
        List<T> list = getTableService().list(queryWrapper);
        return new RestUtil<List<T>>().setData(list);
    }

    @RequestMapping(value = "/getPage",method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<Page<T>> getPage(@ModelAttribute PageVo param, @RequestParam (name="condition", required=false ) String conditionJson){
        Page<T> page = new Page<>(param.getCurrentPage(), param.getPageSize());

        QueryWrapper<T> queryWrapper = SearchUtil.parseWhereSql(conditionJson);
        if (!StringUtils.isBlank(param.getSortColumn())) {
            queryWrapper.orderBy(true, param.isOrderAsc(), StringUtils.camelToUnderline(param.getSortColumn()));
        }
        Page<T> data = getTableService().page(page, queryWrapper );
        return new RestUtil<Page<T>>().setData(data);
    }

    @RequestMapping(value = "/getView/{id}", method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<V> getView(@PathVariable ID id){

        V entity = getViewService().getById(id);
        return new RestUtil<V>().setData(entity);
    }

    @RequestMapping(value = "/getViewAll",method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<List<V>> getViewAll(@RequestParam (name="condition", required=false ) String conditionJson){

        QueryWrapper<V> queryWrapper = SearchUtil.parseWhereSql(conditionJson);
        List<V> list = getViewService().list(queryWrapper);
        return new RestUtil<List<V>>().setData(list);
    }

    @RequestMapping(value = "/getViewPage",method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<Page<V>> getViewPage(@ModelAttribute PageVo param, @RequestParam (name="condition", required=false ) String conditionJson){
        Page<V> page = new Page<>(param.getCurrentPage(), param.getPageSize());

        QueryWrapper<V> queryWrapper = SearchUtil.parseWhereSql(conditionJson);
        if (!StringUtils.isBlank(param.getSortColumn())) {
            queryWrapper.orderBy(true, param.isOrderAsc(), StringUtils.camelToUnderline(param.getSortColumn()));
        }
        Page<V> data = getViewService().page(page, queryWrapper );
        return new RestUtil<Page<V>>().setData(data);
    }

}
