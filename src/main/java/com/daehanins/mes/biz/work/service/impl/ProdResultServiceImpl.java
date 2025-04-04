package com.daehanins.mes.biz.work.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daehanins.mes.biz.common.code.PassState;
import com.daehanins.mes.biz.common.code.TestState;
import com.daehanins.mes.biz.common.util.BizDateUtil;
import com.daehanins.mes.biz.pub.service.IStorageService;
import com.daehanins.mes.biz.qt.entity.ItemTestNo;
import com.daehanins.mes.biz.qt.entity.QualityTest;
import com.daehanins.mes.biz.qt.service.IItemTestNoService;
import com.daehanins.mes.biz.qt.service.IQualityTestService;
import com.daehanins.mes.biz.work.entity.ProdResult;
import com.daehanins.mes.biz.work.mapper.ProdResultMapper;
import com.daehanins.mes.biz.work.service.IProdResultService;
import com.daehanins.mes.common.utils.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 생산실적ProdResult 서비스 구현 클래스
 * </p>
 *
 * @author jeonsj
 * @since 2020-05-25
 */
@Service
public class ProdResultServiceImpl extends ServiceImpl<ProdResultMapper, ProdResult> implements IProdResultService {

    @Autowired
    private IItemTestNoService itemTestNoService;

    @Autowired
    private IQualityTestService qualityTestService;

    @Autowired
    private IStorageService storageService;

    @Transactional
    public ProdResult saveData(ProdResult entity) {

        ProdResult data;

        if (StringUtils.isBlank(entity.getUpdId())) {
            entity.setUpdId(AuthUtil.getMemberCd());
        }

        if (StringUtils.isBlank(entity.getTestNo())) {
            // 시험번호, 일련번호 채번
            Integer serNo = this.itemTestNoService.getNextSeq(entity.getProdDate(), entity.findAreaGb(), entity.findItemGb());
            String testNo = this.itemTestNoService.getNextTestNo(entity.getProdDate(), entity.findAreaGb(), entity.findItemGb(), serNo);

            // 반제품 생산,  시험번호 생성
            ItemTestNo itemTestNo = new ItemTestNo();
            itemTestNo.setTestNo(testNo);
            itemTestNo.setCreateDate(entity.getProdDate());
            itemTestNo.setAreaGb(entity.findAreaGb());
            itemTestNo.setItemGb(entity.findItemGb());
            itemTestNo.setSerNo(serNo);
            itemTestNo.setItemCd(entity.getItemCd());
            itemTestNo.setLotNo(entity.getLotNo());
            // 제조번호, 입고량 추가사항 반영   2020.10.28
            itemTestNo.setProdNo(entity.getProdNo());
            itemTestNo.setQty(entity.getProdQty());
            if (entity.getExpiryDate() != null) {
                itemTestNo.setExpiryDate(entity.getExpiryDate());
                // shelfLife  입고일로 부터 2년 또는 유효기간 중 작은 값
                itemTestNo.setShelfLife(BizDateUtil.calcShelfLife(entity.getProdDate(), entity.getExpiryDate()));
            }
            itemTestNo.setTestState(TestState.REQ);
            itemTestNo.setPassState(PassState.REQ);
            itemTestNo.setEndYn("N");
            this.itemTestNoService.saveOrUpdate(itemTestNo);

            // 품질검사요청 생성
            QualityTest newTest = new QualityTest();
            newTest.setTestNo(testNo);

            newTest.setReqDate(entity.getProdDate());
            newTest.setReqMemberCd(entity.getUpdId());
            newTest.setAreaCd(entity.getAreaCd());
            newTest.setStorageCd(entity.getSrcStorageCd());
            newTest.setReqQty(entity.getProdQty());
            newTest.setTestState(TestState.REQ);
            newTest.setPassState(PassState.REQ);
            newTest.setRetestYn("N");   // 재검사여부 - 최초등록이므로 "N"
            newTest.setTranYn("N");

            this.qualityTestService.saveOrUpdate(newTest);
            // 제조실적에 시험번호 설정
            entity.setTestNo(testNo);

        } else {

            ItemTestNo itemTestNo = this.itemTestNoService.getById(entity.getTestNo());
            itemTestNo.setAreaGb(entity.findAreaGb());
            itemTestNo.setItemGb(entity.findItemGb());
            itemTestNo.setItemCd(entity.getItemCd());
            itemTestNo.setLotNo(entity.getLotNo());
            if (entity.getExpiryDate() != null) {
                itemTestNo.setExpiryDate(entity.getExpiryDate());
                // shelfLife  입고일로 부터 2년 또는 유효기간 중 작은 값
                itemTestNo.setShelfLife(BizDateUtil.calcShelfLife(entity.getProdDate(), entity.getExpiryDate()));
            }
            this.itemTestNoService.saveOrUpdate(itemTestNo);

            
            QualityTest updTest = this.qualityTestService.getOne(new QueryWrapper<QualityTest>()
                    .eq("test_no", entity.getTestNo())
                    .eq("req_date", entity.getProdDate()));
            if (updTest != null) {
                updTest.setAreaCd(entity.getAreaCd());
                updTest.setStorageCd(entity.getSrcStorageCd());
                updTest.setReqQty(entity.getProdQty());
                updTest.setReqMemberCd(entity.getUpdId());

                this.qualityTestService.saveOrUpdate(updTest);
            }
        }

        if (this.saveOrUpdate(entity)) {
            data = entity;
        } else {
            data = null;
        }

        return data;
    }

    @Transactional
    public String deleteData(List<String> idList) {
        String msg;
        // 시험번호 있는 경우 품질검사요청 삭제
        for (String prodResultId : idList) {
            ProdResult prodResult = this.getById(prodResultId);
            // 시험번호가 있는 경우
            if (StringUtils.isNotBlank(prodResult.getTestNo())) {
                // 시험번호 삭제
//                        this.itemTestNoService.removeById(item.getTestNo());
                ItemTestNo itemTestNo = this.itemTestNoService.getById(prodResult.getTestNo());
                itemTestNo.setEndYn("Y");
                //queryWrapper.
                this.itemTestNoService.saveOrUpdate(itemTestNo);
                // 품질검사요청을 삭제
                QualityTest qualityTest = qualityTestService.getOne(new QueryWrapper<QualityTest>().eq("test_no", prodResult.getTestNo()));
                qualityTest.setTestState("CXL");
                this.qualityTestService.saveOrUpdate(qualityTest);
            }
        }
        // 삭제
        if (this.removeByIds(idList)) {
            msg = "success";
        } else {
            msg = "fail";
        }
        return msg;
    }

}
