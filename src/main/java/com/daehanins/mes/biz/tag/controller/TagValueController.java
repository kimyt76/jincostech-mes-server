package com.daehanins.mes.biz.tag.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.daehanins.mes.base.BaseController;
import com.daehanins.mes.biz.tag.entity.TagValue;
import com.daehanins.mes.biz.tag.service.ITagValueService;
import com.daehanins.mes.biz.tag.vo.EquipRunTagLists;
import com.daehanins.mes.biz.tag.vo.EquipRunVo;
import com.daehanins.mes.biz.tag.vo.MonitoringTagLists;
import com.daehanins.mes.biz.work.service.IWorkOrderItemService;
import com.daehanins.mes.common.utils.RestUtil;
import com.daehanins.mes.common.utils.SearchUtil;
import com.daehanins.mes.common.vo.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * TagValue Front 컨트롤러
 * </p>
 *
 * @author jeonsj
 * @since 2021-04-08
 */
@RestController
@RequestMapping("/tag/tag-value")
public class TagValueController extends BaseController<TagValue, TagValue, String> {

    @Autowired
    private ITagValueService tagValueService;

//    @Autowired
//    private ITagValueViewService tagValueViewService;

    @Override
    public ITagValueService getTableService() {
        return this.tagValueService;
    }

    @Override
    public ITagValueService getViewService() {
    return this.tagValueService;
    }

    @Autowired
    private IWorkOrderItemService workOrderItemService;


    @RequestMapping(value = "/getTagList", method = RequestMethod.POST)
    @ResponseBody
    public RestResponse<List<TagValue>> getTagList(@RequestBody EquipRunVo equipRunVo){
        QueryWrapper<TagValue> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("tag_cd", equipRunVo.getTagCd());
        queryWrapper.ge("measure_time", equipRunVo.getStartTime());
        queryWrapper.lt("measure_time", equipRunVo.getEndTime());
        queryWrapper.orderByAsc("measure_time");
        List<TagValue> list = getTableService().list(queryWrapper);
        return new RestUtil<List<TagValue>>().setData(list);
    }

    @RequestMapping(value = "/getMonitoringTagLists", method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<MonitoringTagLists> getTagValueList (@RequestParam(name="condition", required=false ) String conditionJson) {

        QueryWrapper<TagValue> queryWrapper_SL1P03 = SearchUtil.parseWhereSql(conditionJson);
        queryWrapper_SL1P03.eq("tag_cd", "SL1P04");
        queryWrapper_SL1P03.orderByAsc("measure_time");
        List<TagValue> SL1 = this.getTableService().list(queryWrapper_SL1P03);

        QueryWrapper<TagValue> queryWrapper_SL1P11 = SearchUtil.parseWhereSql(conditionJson);
        queryWrapper_SL1P11.eq("tag_cd", "SL2P02");
        queryWrapper_SL1P11.orderByAsc("measure_time");
        List<TagValue> SL2 = this.getTableService().list(queryWrapper_SL1P11);

        QueryWrapper<TagValue> queryWrapper_SL1P21 = SearchUtil.parseWhereSql(conditionJson);
        queryWrapper_SL1P21.eq("tag_cd", "SL3P02");
        queryWrapper_SL1P21.orderByAsc("measure_time");
        List<TagValue> SL3 = this.getTableService().list(queryWrapper_SL1P21);

        QueryWrapper<TagValue> queryWrapper_SL1P22 = SearchUtil.parseWhereSql(conditionJson);
        queryWrapper_SL1P22.eq("tag_cd", "SL4P02");
        queryWrapper_SL1P22.orderByAsc("measure_time");
        List<TagValue> SL4 = this.getTableService().list(queryWrapper_SL1P22);

        MonitoringTagLists monitoringTagLists = new MonitoringTagLists();
        monitoringTagLists.setSL1(SL1);
        monitoringTagLists.setSL2(SL2);
        monitoringTagLists.setSL3(SL3);
        monitoringTagLists.setSL4(SL4);

        return new RestUtil<MonitoringTagLists>().setData(monitoringTagLists);
    }


    @RequestMapping(value = "/getEquipRun", method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<EquipRunTagLists> getEquipRun () {

        EquipRunTagLists result = new EquipRunTagLists();

//        String today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("YYYY-MM-DD"));

        EquipRunVo equipRunVo = new EquipRunVo();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String today = formatter.format(new Date());
//        equipRunVo.setStartTime(today + " 00:00");
//        equipRunVo.setEndTime(today + " 23:59");
        equipRunVo.setStartTime(today + " 08:00");
        equipRunVo.setEndTime(today + " 23:59");
        equipRunVo.setTagCd("SL1P04");
        result.setSL1(workOrderItemService.getEquipRunValues(equipRunVo));
        equipRunVo.setTagCd("SL2P02");
        result.setSL2(workOrderItemService.getEquipRunValues(equipRunVo));
        equipRunVo.setTagCd("SL3P02");
        result.setSL3(workOrderItemService.getEquipRunValues(equipRunVo));
        equipRunVo.setTagCd("SL4P02");
        result.setSL4(workOrderItemService.getEquipRunValues(equipRunVo));

        return new RestUtil<EquipRunTagLists>().setData(result);
    }
}

