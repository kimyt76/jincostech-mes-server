package com.daehanins.mes.biz.qt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.daehanins.mes.biz.qt.entity.ItemTestNo;
import com.daehanins.mes.biz.qt.vo.ReqPrinting;
import com.daehanins.mes.biz.work.entity.ProdResult;
import com.daehanins.mes.biz.work.entity.WorkOrderItem;
import com.daehanins.mes.biz.work.entity.WorkOrderItemView;

import java.time.LocalDate;

/**
 * <p>
 * 품목시험번호ItemTestNo 서비스
 * </p>
 *
 * @author jeonsj
 * @since 2020-07-03
 */
public interface IItemTestNoService extends IService<ItemTestNo> {

    Integer getNextSeq(LocalDate testDate, String areaGb, String itemGb);

    String getNextTestNo(LocalDate testDate, String areaGb, String itemGb, Integer serNo);

    ItemTestNo saveTestNoWithWorkStart (WorkOrderItemView workOrderItemView);

    boolean updateQtyByWorkEnd (WorkOrderItemView workorderItemView);

    byte[] createQrCodeLabels (ReqPrinting[] reqPrintings) throws Exception;
}
