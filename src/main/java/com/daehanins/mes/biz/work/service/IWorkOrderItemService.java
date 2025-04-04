package com.daehanins.mes.biz.work.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.daehanins.mes.biz.tag.vo.EquipRunTagValue;
import com.daehanins.mes.biz.tag.vo.EquipRunVo;
import com.daehanins.mes.biz.work.entity.WorkOrderItem;
import com.daehanins.mes.biz.work.vo.*;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 작업지시상세WorkOrderItem 서비스
 * </p>
 *
 * @author jeonsj
 * @since 2020-04-28
 */
public interface IWorkOrderItemService extends IService<WorkOrderItem> {

    WorkOrderItemWithRecord getWorkOrderItemWithRecord (String workOrderItemId);

    List<WorkOrderItem> getByBatchId (String workOrderBatchId);

    boolean saveWorkOrderItem (WorkOrderItem workOrderItem) throws Exception;

    boolean updateWorkOrderItem (WorkOrderItem workOrderItem) throws Exception;

    boolean cancelWorkOrderItem (WorkOrderItem workOrderItem) throws Exception;

    boolean deleteWorkOrderItem (WorkOrderItem workOrderItem) throws Exception;

    String finishWeigh(String workOrderItemId);

    boolean startWorkOrderItem (WorkOrderItem workOrderItem) throws Exception;

    boolean finishWorkOrderItem (WorkOrderItem workOrderItem) throws Exception;

    boolean editWorkOrderItem (WorkOrderItem workOrderItem) throws Exception;

    ResponseEntity<Resource> getOrderExcel(String workOrderItemId) throws Exception;

    ResponseEntity<Resource> getProdLabel(ProdLabelItem[] labelItems) throws Exception;

    List<ReqItemQty> getItemQtyList(List<String> workOrderItemIdList);

    List<Map<String, Object>> getProdPerformance(PeriodVo periodVo);

    List<Map<String, Object>> getProdPerformanceByCustomer(PeriodVo periodVo);

    List<EquipRunTagValue> getEquipRunValues(EquipRunVo equipRunVo);

}
