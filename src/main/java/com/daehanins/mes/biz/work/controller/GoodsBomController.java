package com.daehanins.mes.biz.work.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.daehanins.mes.base.BaseController;
import com.daehanins.mes.biz.pub.entity.Customer;
import com.daehanins.mes.biz.work.entity.GoodsBom;
import com.daehanins.mes.biz.work.entity.GoodsBomItem;
import com.daehanins.mes.biz.work.entity.GoodsBomItemView;
import com.daehanins.mes.biz.work.entity.GoodsBomView;
import com.daehanins.mes.biz.work.service.IGoodsBomItemService;
import com.daehanins.mes.biz.work.service.IGoodsBomItemViewService;
import com.daehanins.mes.biz.work.service.IGoodsBomService;
import com.daehanins.mes.biz.work.service.IGoodsBomViewService;
import com.daehanins.mes.biz.work.vo.GoodsBomExcel;
import com.daehanins.mes.biz.work.vo.GoodsBomReadWithItems;
import com.daehanins.mes.biz.work.vo.GoodsBomSaveWithItems;
import com.daehanins.mes.common.utils.RestUtil;
import com.daehanins.mes.common.vo.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 제품BOMGoodsBom Front 컨트롤러
 * </p>
 *
 * @author jeonsj
 * @since 2020-04-23
 */
@RestController
@RequestMapping("/work/goods-bom")
public class GoodsBomController extends BaseController<GoodsBom, GoodsBomView, String> {

    @Autowired
    private IGoodsBomService goodsBomService;

    @Autowired
    private IGoodsBomViewService goodsBomViewService;

    @Autowired
    private IGoodsBomItemService goodsBomItemService;

    @Autowired
    private IGoodsBomItemViewService goodsBomItemViewService;

    @Override
    public IGoodsBomService getTableService() {
        return this.goodsBomService;
    }

    @Override
    public IGoodsBomViewService getViewService() {
    return this.goodsBomViewService;
    }

    @RequestMapping(value = "/getByItemCd/{itemCd}", method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<List<GoodsBom>> getByItemCd(@PathVariable String itemCd){

        QueryWrapper<GoodsBom> queryWrapper = new QueryWrapper<GoodsBom>().eq("item_cd", itemCd);
        List<GoodsBom> list = getTableService().list(queryWrapper);
        return new RestUtil<List<GoodsBom>>().setData(list);
    }

    @RequestMapping(value = "/getWithItems/{id}", method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<GoodsBomReadWithItems> getWithItems(@PathVariable String id){

        GoodsBomReadWithItems bomItems = getTableService().getWithItems(id);
        return new RestUtil<GoodsBomReadWithItems>().setData(bomItems);
    }

    @RequestMapping(value = "/saveWithItems", method = {RequestMethod.POST, RequestMethod.PUT})
    @ResponseBody
    public RestResponse<GoodsBomSaveWithItems> saveWithItems(@RequestBody GoodsBomSaveWithItems requestParam){

        GoodsBomSaveWithItems data = new GoodsBomSaveWithItems();
        GoodsBom bom = requestParam.getGoodsBom();
        List<GoodsBomItem> bomItems = requestParam.getGoodsBomItems();
        List<GoodsBomItem> bomDeleteItems = requestParam.getGoodsBomDeleteItems();

        if (getTableService().saveOrUpdate(bom)) {
            data.setGoodsBom(bom);
        }

        // item 삭제 처리
        bomDeleteItems.forEach( item -> {
            if (StringUtils.isNotBlank(item.getGoodsBomItemId())) {
                this.goodsBomItemService.removeById(item.getGoodsBomItemId());
            }
        });
        // item 신규,수정 처리
        bomItems.forEach( item -> {
            item.setGoodsBomId(bom.getGoodsBomId());
            this.goodsBomItemService.saveOrUpdate(item);
            data.getGoodsBomItems().add(item);
        });

        return new RestUtil<GoodsBomSaveWithItems>().setData(data);
    }


    @RequestMapping(value = "/deleteWithItems/{ids}",method = {RequestMethod.DELETE, RequestMethod.POST})
    @ResponseBody
    public RestResponse<Object> deleteWithItems(@PathVariable String[] ids){
        String msg;
        List<String> idList = Arrays.asList(ids);

        // BOM 아이템 삭제
        for (String goodsBomId : idList) {
            goodsBomItemService.remove(new QueryWrapper<GoodsBomItem>().eq("goods_bom_id", goodsBomId));
        }
        // BOM 삭제
        if (getTableService().removeByIds(idList)) {
            msg = "success";
        } else {
            msg = "fail";
        }
        return new RestUtil<>().setMessage(msg);
    }

    @RequestMapping(value = "/defaultBom/{id}", method = RequestMethod.POST)
    @ResponseBody
    public RestResponse<GoodsBom> defaultBom(@PathVariable String id){
        GoodsBom entity = getTableService().getById(id);
        // 다른 BOM 의  default 해제
        UpdateWrapper<GoodsBom> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("default_yn", "N").eq("item_cd", entity.getItemCd()).ne("goods_bom_id", id);
        getTableService().update(updateWrapper);
//
//        List<GoodsBom> bomList = getTableService().list(new QueryWrapper<GoodsBom>().eq("item_cd", entity.getItemCd()).ne("goods_bom_id", id));
//        for (GoodsBom bom : bomList) {
//            bom.setDefaultYn("N");
//        }
//        getTableService().saveOrUpdateBatch(bomList);
        // 선택 BOM 을 default 설정
        entity.setDefaultYn("Y");
        getTableService().updateById(entity);
        return new RestUtil<GoodsBom>().setData(entity);
    }


    @RequestMapping(value = "/uploadItems", method = {RequestMethod.POST, RequestMethod.PUT})
    @ResponseBody
    public RestResponse<GoodsBomReadWithItems> uploadItems(@RequestBody List<GoodsBomExcel> goodsBomExcelList) throws Exception {

        GoodsBomReadWithItems data = getTableService().uploadItems(goodsBomExcelList);

        return new RestUtil<GoodsBomReadWithItems>().setData(data);
    }


}
