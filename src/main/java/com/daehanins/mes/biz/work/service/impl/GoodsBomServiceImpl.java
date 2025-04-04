package com.daehanins.mes.biz.work.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daehanins.mes.biz.pub.entity.Customer;
import com.daehanins.mes.biz.work.entity.GoodsBom;
import com.daehanins.mes.biz.work.entity.GoodsBomItem;
import com.daehanins.mes.biz.work.entity.GoodsBomItemView;
import com.daehanins.mes.biz.work.mapper.GoodsBomMapper;
import com.daehanins.mes.biz.work.service.IGoodsBomItemService;
import com.daehanins.mes.biz.work.service.IGoodsBomItemViewService;
import com.daehanins.mes.biz.work.service.IGoodsBomService;
import com.daehanins.mes.biz.work.vo.GoodsBomExcel;
import com.daehanins.mes.biz.work.vo.GoodsBomReadWithItems;
import com.daehanins.mes.common.exception.BizException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 제품BOMGoodsBom 서비스 구현 클래스
 * </p>
 *
 * @author jeonsj
 * @since 2020-04-23
 */
@Service
public class GoodsBomServiceImpl extends ServiceImpl<GoodsBomMapper, GoodsBom> implements IGoodsBomService {

    @Autowired
    IGoodsBomItemService goodsBomItemService;

    @Autowired
    IGoodsBomItemViewService goodsBomItemViewService;

    public GoodsBomReadWithItems getWithItems (String id) {

        GoodsBomReadWithItems bomItems = new GoodsBomReadWithItems();
        GoodsBom goodsBom = this.getById(id);

        QueryWrapper<GoodsBomItemView> queryWrapper = new QueryWrapper<GoodsBomItemView>()
                .eq(StringUtils.camelToUnderline("goodsBomId"), id)
                .orderByAsc("prod_state","goods_bom_item_id");  // 조회 순서는 등록순으로 표시해야 한다.
        List<GoodsBomItemView> goodsBomItems = this.goodsBomItemViewService.list(queryWrapper);
        bomItems.setGoodsBom(goodsBom);
        bomItems.setGoodsBomItems(goodsBomItems);
        return bomItems;
    }

    @Transactional
    public GoodsBomReadWithItems uploadItems(List<GoodsBomExcel> goodsBomExcelList) {

        // 마스터 생성
        GoodsBomExcel bom = goodsBomExcelList.get(0);

        int bomCnt = this.count(new QueryWrapper<GoodsBom>().eq("item_cd", bom.getProdItemCd()).eq("bom_ver", bom.getBomVer()));

//        List<GoodsBom> bomList = this.list(new QueryWrapper<GoodsBom>().eq("item_cd", bom.getProdItemCd()).eq("bom_ver", bom.getBomVer()));

        if (bomCnt > 0) {
            throw new BizException("해당 제품의 BOM이 이미 존재합니다.");
        }

        // 이전에 등록된 것들은 기본에서 제외함
        this.update(new UpdateWrapper<GoodsBom>()
                .eq("item_cd", bom.getProdItemCd())
                .set("default_yn","N")
        );

        // 신규 BOM 마스터 등록
        GoodsBom newBom = new GoodsBom();
        newBom.setItemCd(bom.getProdItemCd());
        newBom.setBomVer(bom.getBomVer());
        newBom.setDefaultYn(bom.getDefaultYn());
        this.save(newBom);

        String goodsBomId = newBom.getGoodsBomId();

        goodsBomExcelList.forEach( item -> {
            GoodsBomItem newBomItem = new GoodsBomItem();
            newBomItem.setGoodsBomId(goodsBomId);
            newBomItem.setItemCd(item.getItemCd());
            newBomItem.setProdState(item.getProdState());
            newBomItem.setContentRatio(item.getContentRatio());
            newBomItem.setQty(item.getQty());
            this.goodsBomItemService.saveOrUpdate(newBomItem);
        });

        return this.getWithItems(goodsBomId);
    }
}
