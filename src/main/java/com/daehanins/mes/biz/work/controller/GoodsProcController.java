package com.daehanins.mes.biz.work.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.daehanins.mes.base.BaseController;
import com.daehanins.mes.biz.mat.entity.MatOrderStateView;
import com.daehanins.mes.biz.pub.vo.GoodsProcWithItems;
import com.daehanins.mes.biz.work.entity.*;
import com.daehanins.mes.biz.work.service.IGoodsProcDetailService;
import com.daehanins.mes.biz.work.service.IGoodsProcService;
import com.daehanins.mes.biz.work.service.IGoodsProcViewService;
import com.daehanins.mes.biz.work.vo.GoodsBomReadWithItems;
import com.daehanins.mes.biz.work.vo.GoodsBomSaveWithItems;
import com.daehanins.mes.common.utils.ExcelPoiUtil;
import com.daehanins.mes.common.utils.RestUtil;
import com.daehanins.mes.common.utils.SearchUtil;
import com.daehanins.mes.common.vo.RestResponse;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * GoodsProc Front 컨트롤러
 * </p>
 *
 * @author jeonsj
 * @since 2023-02-15
 */
@RestController
@RequestMapping("/work/goods-proc")
public class GoodsProcController extends BaseController<GoodsProc, GoodsProcView, String> {

    @Autowired
    private IGoodsProcService goodsProcService;

    @Autowired
    private IGoodsProcViewService goodsProcViewService;

    @Autowired
    private IGoodsProcDetailService goodsProcDetailService;

    @Override
    public IGoodsProcService getTableService() {
        return this.goodsProcService;
    }

    @Override
    public IGoodsProcViewService getViewService() {
    return this.goodsProcViewService;
    }

    @RequestMapping(value = "/getByItemCd/{itemCd}", method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<List<GoodsProc>> getByItemCd(@PathVariable String itemCd){
        List<GoodsProc> list = getTableService().list(new QueryWrapper<GoodsProc>().eq("item_cd", itemCd));
        return new RestUtil<List<GoodsProc>>().setData(list);
    }

    @RequestMapping(value = "/getWithItems/{id}", method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<GoodsProcWithItems> getWithItems(@PathVariable String id){
        GoodsProcWithItems goodsProcWithItems = new GoodsProcWithItems();
        goodsProcWithItems.setGoodsProc(this.getTableService().getById(id));
        goodsProcWithItems.setGoodsProcDetailList(goodsProcDetailService.list(new QueryWrapper<GoodsProcDetail>().eq("goods_proc_id", id)));
        return new RestUtil<GoodsProcWithItems>().setData(goodsProcWithItems);
    }
    //getWithItems

    @RequestMapping(value = "/uploadGoodsProc", method = {RequestMethod.POST, RequestMethod.PUT})
    @ResponseBody
    public RestResponse<Object> uploadGoodsProc(@RequestBody GoodsProcWithItems goodsProcWithItems) {

        String msg = "";

        GoodsProc goodsProc = goodsProcWithItems.getGoodsProc();

        // defaultYn update
        if(goodsProc.getDefaultYn().equals("Y")){
            UpdateWrapper<GoodsProc> queryWrapper = new UpdateWrapper<>();
            queryWrapper.set("default_yn", "N").eq("item_cd", goodsProc.getItemCd());
            this.getTableService().update(queryWrapper);
        }
        // GoodsProc Save
        this.getTableService().save(goodsProc);

        String goodsProcId = goodsProc.getGoodsProcId();

        // GoodsProcDetail.goodsProcId SET
        for (GoodsProcDetail goodsProcDetail : goodsProcWithItems.getGoodsProcDetailList()) {
            goodsProcDetail.setGoodsProcId(goodsProcId);
        }
        // GoodsProcDetail save
        goodsProcDetailService.saveBatch(goodsProcWithItems.getGoodsProcDetailList());

        return new RestUtil<>().setMessage(msg);
    }


    @RequestMapping(value = "/saveWithItems", method = {RequestMethod.POST, RequestMethod.PUT})
    @ResponseBody
    public RestResponse<GoodsProcWithItems> saveWithItems(@RequestBody GoodsProcWithItems goodsProcWithItems){

        GoodsProcWithItems result = new GoodsProcWithItems();

        GoodsProc goodsProc = goodsProcWithItems.getGoodsProc();
        List<GoodsProcDetail> goodsProcDetailList = goodsProcWithItems.getGoodsProcDetailList();

        //GoodsProc Save
        if (getTableService().saveOrUpdate(goodsProc)) {
            result.setGoodsProc(goodsProc);
        }

        //GoodsProcItem 처리
        String goodsProcId = goodsProc.getGoodsProcId();

        // item 일괄 삭제 후, insert
        goodsProcDetailService.remove(new QueryWrapper<GoodsProcDetail>().eq("goods_proc_id", goodsProcId));
        for (GoodsProcDetail item : goodsProcDetailList) {
            item.setGoodsProcId(goodsProcId);
            goodsProcDetailService.save(item);
        }
        result.setGoodsProcDetailList(goodsProcDetailList);

        return new RestUtil<GoodsProcWithItems>().setData(result);
    }

    @RequestMapping(value = "/deleteWithItems/{id}",method = {RequestMethod.DELETE, RequestMethod.POST})
    @ResponseBody
    public RestResponse<Object> deleteWithItems(@PathVariable String id){
        String msg;
        goodsProcDetailService.remove(new QueryWrapper<GoodsProcDetail>().eq("goods_proc_id", id));
        if (this.getTableService().removeById(id)) {
            msg = "success";
        } else {
            msg = "fail";
        }
        return new RestUtil<>().setMessage(msg);
    }

    @RequestMapping(value = "/setDefaultVer/{id}", method = RequestMethod.POST)
    @ResponseBody
    public RestResponse<GoodsProc> setDefaultVer(@PathVariable String id){
        GoodsProc entity = getTableService().getById(id);
        // 다른 공정의  default 해제
        UpdateWrapper<GoodsProc> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("default_yn", "N").eq("item_cd", entity.getItemCd()).ne("goods_proc_id", id);
        getTableService().update(updateWrapper);

        entity.setDefaultYn("Y");
        getTableService().updateById(entity);
        return new RestUtil<GoodsProc>().setData(entity);
    }

    @RequestMapping(value = "/getExcelTemplate",method = RequestMethod.GET)
    public ResponseEntity<Resource> getExcel() throws Exception {
        try {
            byte[] excelContent = ExcelPoiUtil.toByteArray(
                    ExcelPoiUtil.createWorkbook(
                            getClass().getResourceAsStream("/excel/prod_proc_template.xlsx")));
            ByteArrayResource resource = new ByteArrayResource(excelContent);
            HttpHeaders header = ExcelPoiUtil.getHeader("prod_proc_template", excelContent.length);
            return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("엑셀파일 생성중 에러발생!");
        }
    }
}

