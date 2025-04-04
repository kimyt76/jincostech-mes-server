package com.daehanins.mes.biz.mobile.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.daehanins.mes.biz.adm.entity.CommonCode;
import com.daehanins.mes.biz.adm.service.ICommonCodeService;
import com.daehanins.mes.biz.common.code.UseState;
import com.daehanins.mes.biz.common.code.WorkItemState;
import com.daehanins.mes.biz.common.code.WorkOrderItemStatus;
import com.daehanins.mes.biz.mat.entity.*;
import com.daehanins.mes.biz.mat.service.*;
import com.daehanins.mes.biz.mat.vo.MatStockResultItems;
import com.daehanins.mes.biz.mat.vo.MatTranSaveWithItems;
import com.daehanins.mes.biz.mat.vo.StockStorageVo;
import com.daehanins.mes.biz.mobile.vo.*;
import com.daehanins.mes.biz.pub.entity.*;
import com.daehanins.mes.biz.pub.service.ICustomerViewService;
import com.daehanins.mes.biz.pub.service.IEquipmentViewService;
import com.daehanins.mes.biz.pub.service.IItemMasterViewService;
import com.daehanins.mes.biz.pub.service.IStorageViewService;
import com.daehanins.mes.biz.qt.entity.ItemTestNoView;
import com.daehanins.mes.biz.qt.service.IItemTestNoService;
import com.daehanins.mes.biz.qt.service.IItemTestNoViewService;
import com.daehanins.mes.biz.security.entity.User;
import com.daehanins.mes.biz.security.service.IUsersService;
import com.daehanins.mes.biz.work.entity.*;
import com.daehanins.mes.biz.work.service.*;
import com.daehanins.mes.common.utils.AuthUtil;
import com.daehanins.mes.common.utils.RestUtil;
import com.daehanins.mes.common.utils.SearchUtil;
import com.daehanins.mes.common.vo.RestResponse;
import jxl.write.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/mobile")
public class MobileController {

    @Autowired
    private IStorageViewService storageViewService;

    @Autowired
    private ICustomerViewService customerViewService;

    @Autowired
    private ICommonCodeService commonCodeService;

    @Autowired
    private IItemTestNoViewService iItemTestNoViewService;

    @Autowired
    private IItemMasterViewService iItemMasterViewService;

    @Autowired
    private IMatOrderService matOrderService;

    @Autowired
    private IMatOrderViewService matOrderViewService;

    @Autowired
    private IMatOrderItemViewService matOrderItemViewService;

    @Autowired
    private IMatTranService matTranService;

    @Autowired
    private IMatTranViewService matTranViewService;

    @Autowired
    private IMatTranItemService matTranItemService;

    @Autowired
    private IMatTranItemViewService matTranItemViewService;

    @Autowired
    private IEquipmentViewService equipmentViewService;

    @Autowired
    private IWorkOrderItemService workOrderItemService;

    @Autowired
    private IWorkOrderItemViewService workOrderItemViewService;

    @Autowired
    private IWorkOrderBatchViewService workOrderBatchViewService;

    @Autowired
    private IMatUseService matUseService;

    @Autowired
    private IMatUseViewService matUseViewService;

    @Autowired
    private IUsersService usersService;

    @Autowired
    private IProdIngService prodIngService;

    @Autowired
    private IProdRecordService prodRecordService;

    @Autowired
    private IProdIngViewService prodIngViewService;

    @Autowired
    private IItemTestNoService itemTestNoService;

    @Autowired
    private IMatStockService matStockService;

    /** 창고코드 조회 **/
    @RequestMapping(value = "/getStorageList", method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<List<StorageView>> getStorageList(){

        List<StorageView> resultList = storageViewService.list();
        return new RestUtil<List<StorageView>>().setData(resultList);
    }

    /** 거래처코드 조회 **/
    @RequestMapping(value = "/getCustomerList", method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<List<CustomerView>> getCustomerList(@RequestParam (name="condition", required=false ) String conditionJson){

        QueryWrapper<CustomerView> queryWrapper = SearchUtil.parseWhereSql(conditionJson);
        List<CustomerView> resultList = customerViewService.list(queryWrapper);
        return new RestUtil<List<CustomerView>>().setData(resultList);
    }

    /** 원료 정보 조회 **/
    @RequestMapping(value = "/getItemInfoByTestNo/{testNo}", method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<ItemInfoByTestNo> getItemInfoByTestNo(@PathVariable String testNo){

        ItemInfoByTestNo result = new ItemInfoByTestNo();

        ItemTestNoView itemTestNoView = iItemTestNoViewService.getById(testNo);

        QueryWrapper<ItemMasterView> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.camelToUnderline("itemCd"), itemTestNoView.getItemCd());
        ItemMasterView itemMasterView = iItemMasterViewService.getOne(queryWrapper);

        result.setItemTestNoView(itemTestNoView);
        result.setItemMasterView(itemMasterView);

        return new RestUtil<ItemInfoByTestNo>().setData(result);
    }

    /** 원료 재고 조회 **/
    @RequestMapping(value = "/getItemStockByTestNo/{testNo}",method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<ItemStockByTestNoVo> getItemStockByTestNo(@PathVariable String testNo){

        ItemStockByTestNoVo resultFin = new ItemStockByTestNoVo();

        ItemInfoByTestNo result = new ItemInfoByTestNo();

        ItemTestNoView itemTestNoView = iItemTestNoViewService.getById(testNo);

        QueryWrapper<ItemMasterView> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.camelToUnderline("itemCd"), itemTestNoView.getItemCd());
        ItemMasterView itemMasterView = iItemMasterViewService.getOne(queryWrapper);

        result.setItemTestNoView(itemTestNoView);
        result.setItemMasterView(itemMasterView);

        resultFin.setItemInfoByTestNo(result);

        MatStockResultItems matStockResultItems = new MatStockResultItems();
        StockStorageVo stockStorageVo = new StockStorageVo();

        //    private String areaCd;
        //    private String stdDate;
        //    private String stockDate;
        //    private String storageCd;
        //    private String storageName;
        //    private String itemTypeCd;
        //    private String matStockId;
        //    private String itemCd;
        //    private String itemName;

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date now = new Date();

        stockStorageVo.setAreaCd("");
        stockStorageVo.setItemTypeCd(itemMasterView.getItemTypeCd());
        stockStorageVo.setItemCd(itemMasterView.getItemCd());
        stockStorageVo.setItemName("");
        stockStorageVo.setStdDate(format.format(now));
        stockStorageVo.setItemName("");
        stockStorageVo.setStockDate("");
        stockStorageVo.setStorageCd("");
        stockStorageVo.setMatStockId("");

        List<StockStorageVo> stockStorageVoList = matStockService.getTargetStorageList(stockStorageVo);
        matStockResultItems.setStorageList(stockStorageVoList);

        List<Map<String, Object>> resultMap = matStockService.getStockItemListByTestNo(matStockResultItems);

        matStockResultItems.setResultMap(resultMap);

        resultFin.setMatStockResultItems(matStockResultItems);

        return new RestUtil<ItemStockByTestNoVo>().setData(resultFin);
    }

    /** 이동 요청 조회
     * @param = orderDate, orderState, tranCd, srcStorageCd, areaCd
     * **/
    @RequestMapping(value = "/getMatMoveReqList", method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<List<MatOrderView>> getMatMoveReqList(@RequestParam (name="condition", required=false ) String conditionJson ){

        QueryWrapper<MatOrderView> queryWrapper = SearchUtil.parseWhereSql(conditionJson);
        List<MatOrderView> resultList = matOrderViewService.list(queryWrapper);

        return new RestUtil<List<MatOrderView>>().setData(resultList);
    }

    /** 이동 요청 상세 조회
     * @param = matOrderId
     * **/
    @RequestMapping(value = "/getMatMoveReqDetail/{matOrderId}", method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<List<MatOrderItemView>> getMatMoveReqDetail(@PathVariable String matOrderId){

        QueryWrapper<MatOrderItemView> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.camelToUnderline("matOrderId"), matOrderId);
        List<MatOrderItemView> resultList = matOrderItemViewService.list(queryWrapper);

        return new RestUtil<List<MatOrderItemView>>().setData(resultList);
    }

    /** 이동 내역 조회 & 조정 내역 조회
     * @param = tranDate, confirmState, srcStorageCd, destStorageCd
     * **/
    @RequestMapping(value = "/getMatMoveList", method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<List<MatTranView>> getMatMoveList(@RequestParam(name="condition", required=false) String conditionJson ){

        QueryWrapper<MatTranView> queryWrapper = SearchUtil.parseWhereSql(conditionJson);
        List<MatTranView> resultList = matTranViewService.list(queryWrapper);

        return new RestUtil<List<MatTranView>>().setData(resultList);
    }

    /** 이동 내역 상세 조회 & 조정 내역 상세 조회
     * @param = matTranId
     * **/
    @RequestMapping(value = "/getMatMoveDetail/{matTranId}",method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<List<MatTranItemView>> getMatMoveDetail(@PathVariable String matTranId){

        QueryWrapper<MatTranItemView> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.camelToUnderline("matTranId"), matTranId);
        List<MatTranItemView> resultList = matTranItemViewService.list(queryWrapper);

        return new RestUtil<List<MatTranItemView>>().setData(resultList);
    }

    /** 이동 등록 & 조정 등록 **/
    @RequestMapping(value = "/saveMatTranWithItems", method = {RequestMethod.POST, RequestMethod.PUT})
    @ResponseBody
    public RestResponse<MatTranSaveWithItems> saveWithItems(@RequestBody MatTranSaveWithItems requestParam){

        MatTran matTran = requestParam.getMatTran();
        List<MatTranItem> matTranItems = requestParam.getMatTranItems();
        MatTranSaveWithItems data = matTranService.saveWithItemsByMobile(matTran, matTranItems);

        return new RestUtil<MatTranSaveWithItems>().setData(data);
    }
    
    /** 이동 요청 업데이트 및 이동확인 업데이트 **/
    @RequestMapping(value = "/getConfirmMatMove/{matTranId}",method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<MatTran> getConfirmMatMove(@PathVariable String matTranId){

        /*matTran 수정*/
        MatTran matTran = matTranService.getById(matTranId);
        matTran.setConfirmState("OK");
        User user = usersService.getById(AuthUtil.getUsername());
        matTran.setConfirmMemberCd(user.getMemberCd());
        matTranService.saveOrUpdate(matTran);

        /*matTranItem 수정*/
        QueryWrapper<MatTranItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.camelToUnderline("matTranId"), matTranId);
        List<MatTranItem> matTranItems = matTranItemService.list(queryWrapper);
        matTranItems.forEach( item -> {
            item.setConfirmState("OK");
        });
        matTranItemService.saveOrUpdateBatch(matTranItems);

        /*matOrder 수정*/
        MatOrder matOrder = matOrderService.getById(matTran.getMatOrderId());
        matOrder.setOrderState("END");
        matOrder.setEndYn("Y");
        matOrderService.saveOrUpdate(matOrder);

        return new RestUtil<MatTran>().setData(matTran);
    }

    /** 자재이동 품목별 확인처리 **/
    @RequestMapping(value = "/getConfirmMatMoveItem/{matTranItemId}",method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<List<MatTranItemView>> getConfirmMatMoveItem(@PathVariable String matTranItemId){

        MatTranItem matTranItem = matTranItemService.getById(matTranItemId);
        matTranItem.setConfirmState("END");
        matTranItemService.saveOrUpdate(matTranItem);

        QueryWrapper<MatTranItemView> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.camelToUnderline("matTranId"), matTranItem.getMatTranId());
        List<MatTranItemView> resultList = matTranItemViewService.list(queryWrapper);

        return new RestUtil<List<MatTranItemView>>().setData(resultList);
    }

    /** 제조공정 설비목록 조회 **/
    @RequestMapping(value = "/getEquipmentList/{areaCd}",method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<List<EquipmentView>> getEquipmentList(@PathVariable String areaCd ){
        QueryWrapper<EquipmentView> queryWrapper = new QueryWrapper<EquipmentView>();

        queryWrapper.eq(StringUtils.camelToUnderline("areaCd"), areaCd);
        queryWrapper.eq(StringUtils.camelToUnderline("prodYn"), "Y");
        queryWrapper.eq(StringUtils.camelToUnderline("useYn"), "Y");
        queryWrapper.orderByAsc(StringUtils.camelToUnderline("displayOrder"));

        List<EquipmentView> resultList = equipmentViewService.list(queryWrapper);
        return new RestUtil<List<EquipmentView>>().setData(resultList);
    }

    /** 제조공정 작업지시 조회 **/
    @RequestMapping(value = "/getWorkOrderItems", method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<List<WorkOrderBatchView>> getWorkOrderItems(@RequestParam(name="condition", required=false) String conditionJson ){

        QueryWrapper<WorkOrderBatchView> queryWrapper = SearchUtil.parseWhereSql(conditionJson);
        List<WorkOrderBatchView> workOrderItems = workOrderBatchViewService.list(queryWrapper);
        return new RestUtil<List<WorkOrderBatchView>>().setData(workOrderItems);
    }

    /** 제조공정 투입목록 조회 **/
    @RequestMapping(value = "/getMatUseList/{workOrderItemId}", method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<List<MatUseView>> getByWorkOrderItemId(@PathVariable String workOrderItemId){

        List<MatUseView> list = matUseViewService.getByWorkOrderItemId(workOrderItemId);
        return new RestUtil<List<MatUseView>>().setData(list);
    }
    
    /** 제조공정 원료 투입 in PDA **/
    @RequestMapping(value = "/investmentMatUse", method = {RequestMethod.POST, RequestMethod.PUT})
    @ResponseBody
    public RestResponse<List<MatUseView>> investmentMatUse(@RequestBody MatUseSaveItem requestParam){

        List<MatUseView> list = matUseService.investmentMatUse(requestParam);

        return new RestUtil<List<MatUseView>>().setData(list);
    }

    /** 제조공정 시작 in PDA **/
    @RequestMapping(value = "/startWorkOrderItem", method = {RequestMethod.POST, RequestMethod.PUT})
    @ResponseBody
    public RestResponse<WorkOrderItem> startWorkOrderItem(@RequestBody StartWorkOrderItem startWorkOrderItem) throws Exception {

        /* workOrderItem 시작요소 삽입 */
        String workOrderItemId = startWorkOrderItem.getWorkOrderItemId();
        String equipmentCd = startWorkOrderItem.getWorkEquipmentCd();
        WorkOrderItem workOrderItem = workOrderItemService.getById(workOrderItemId);
        EquipmentView equipmentView = equipmentViewService.getById(startWorkOrderItem.getWorkEquipmentCd());
        workOrderItem.setWorkEquipmentCd(equipmentCd);
        workOrderItem.setStorageCd(equipmentView.getStorageCd());

        /* workOrderItem 시작 처리 */
        workOrderItemService.startWorkOrderItem(workOrderItem);
        return new RestUtil<WorkOrderItem>().setData(workOrderItem);
    }
    
    /** 제조공정 종료 in PDA **/
    @RequestMapping(value = "/finishWorkOrderItem", method = {RequestMethod.POST, RequestMethod.PUT})
    @ResponseBody
    public RestResponse<WorkOrderItem> finishWorkOrderItem(@RequestBody FinishWorkOrderItem finishWorkOrderItem) throws Exception{
        
        /* workOrderItem 종료요소 삽입 */
        String workOrderItemId = finishWorkOrderItem.getWorkOrderItemId();
        WorkOrderItem workOrderItem = workOrderItemService.getById(workOrderItemId);

        ProdRecord prodRecord = new ProdRecord();
        prodRecord.setWorkOrderItemId(workOrderItemId);
        prodRecord.setProdQty(finishWorkOrderItem.getProdQty());
        prodRecord.setProdDate(workOrderItem.getProdDate());
        prodRecord.setItemCd(workOrderItem.getItemCd());
        prodRecord.setInputQty(workOrderItem.getOrderQty());
        prodRecordService.saveProdRecord(prodRecord);

        QueryWrapper<MatUse> matUseQueryWrapper = new QueryWrapper<>();
        matUseQueryWrapper.eq(StringUtils.camelToUnderline("workOrderItemId"), workOrderItemId);
        List<MatUse> matUseList = matUseService.list(matUseQueryWrapper);

        String confirmMemberCd = finishWorkOrderItem.getConfirmMemberCd();
        matUseList.forEach( item -> {
            //미리 등록된 확인자가 없을 경우에만
            if(item.getProdConfirmMemberCd() != null) item.setProdConfirmMemberCd(confirmMemberCd);
        });
        matUseService.saveOrUpdateBatch(matUseList);

        /* workOrderItem 작업종료 처리 */
        workOrderItemService.finishWorkOrderItem(workOrderItem);

        return new RestUtil<WorkOrderItem>().setData(workOrderItem);
    }

    /** 작업지시(공정조건) 조회*/
    @RequestMapping(value = "/getWorkOrderItemsByCondition", method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<List<WorkOrderItemView>> getWorkOrderItemsByCondition(@RequestParam(name="condition", required=false) String conditionJson ){

        QueryWrapper<WorkOrderItemView> queryWrapper = SearchUtil.parseWhereSql(conditionJson);
        List<WorkOrderItemView> resultList = workOrderItemViewService.list(queryWrapper);
        return new RestUtil<List<WorkOrderItemView>>().setData(resultList);
    }

    /** 공정조건 세부 조회 **/
    @RequestMapping(value = "/getProdIngCondition/{workOrderItemId}", method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<List<ProdIng>> getProdIngCondition(@PathVariable String workOrderItemId){

        QueryWrapper<ProdIng> prodIngQueryWrapper = new QueryWrapper<>();
        prodIngQueryWrapper.eq(StringUtils.camelToUnderline("workOrderItemId"), workOrderItemId);
        List<ProdIng> resultList = prodIngService.list(prodIngQueryWrapper);
        return new RestUtil<List<ProdIng>>().setData(resultList);
    }

    /** 공정조건 저장 **/
    @RequestMapping(value = "/saveProdIngCondition", method = {RequestMethod.POST, RequestMethod.PUT})
    @ResponseBody
    public RestResponse<List<ProdIng>> saveProdIngCondition(@RequestBody List<ProdIng> requestParam){

        /* 작업조건 저장처리 */
        prodIngService.saveOrUpdateBatch(requestParam);

        /* resultList 조회 */
        QueryWrapper<ProdIng> prodIngQueryWrapper = new QueryWrapper<>();
        prodIngQueryWrapper.eq(StringUtils.camelToUnderline("workOrderItemId"), requestParam.get(0).getWorkOrderItemId());
        List<ProdIng> resultList = prodIngService.list(prodIngQueryWrapper);

        return new RestUtil<List<ProdIng>>().setData(resultList);
    }
}
