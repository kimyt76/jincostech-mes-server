package com.daehanins.mes.biz.pub.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.daehanins.mes.base.BaseController;
import com.daehanins.mes.biz.pub.entity.MemberView;
import com.daehanins.mes.biz.pub.entity.Storage;
import com.daehanins.mes.biz.pub.service.IMemberViewService;
import com.daehanins.mes.common.utils.RestUtil;
import com.daehanins.mes.common.vo.RestResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * <p>
 * Member Front 컨트롤러
 * </p>
 *
 * @author jeonsj
 * @since 2020-04-01
 */
@RestController
@RequestMapping("/pub/member")
public class MemberController extends BaseController<MemberView, MemberView, String> {

    @Autowired
    private IMemberViewService memberViewService;

    @Override
    public IMemberViewService getTableService() { return this.memberViewService; }

    @Override
    public IMemberViewService getViewService() {
        return this.memberViewService;
    }

    @RequestMapping(value = "/getByMemberCd/{memberCd}",method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<MemberView> getWeigh(@PathVariable String memberCd){
        MemberView member = getTableService().getOne(new QueryWrapper<MemberView>().eq("member_cd", memberCd));
        return new RestUtil<MemberView>().setData(member);
    }

}
