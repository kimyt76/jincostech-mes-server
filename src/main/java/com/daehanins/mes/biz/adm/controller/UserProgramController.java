package com.daehanins.mes.biz.adm.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.daehanins.mes.base.BaseController;
import com.daehanins.mes.biz.adm.entity.UserProgram;
import com.daehanins.mes.biz.adm.entity.UserProgramView;
import com.daehanins.mes.biz.adm.service.IUserProgramService;
import com.daehanins.mes.biz.adm.service.IUserProgramViewService;
import com.daehanins.mes.common.utils.RestUtil;
import com.daehanins.mes.common.vo.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 사용자프로그램UserProgram Front 컨트롤러
 * </p>
 *
 * @author jeonsj
 * @since 2022-10-03
 */
@RestController
@RequestMapping("/adm/user-program")
public class UserProgramController extends BaseController<UserProgram, UserProgramView, String> {

    @Autowired
    private IUserProgramService userProgramService;

    @Autowired
    private IUserProgramViewService userProgramViewService;

    @Override
    public IUserProgramService getTableService() {
        return this.userProgramService;
    }

    @Override
    public IUserProgramViewService getViewService() {
    return this.userProgramViewService;
    }

    @RequestMapping(value = "/getByUser/{userId}",method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<List<UserProgramView>> getByUser(@PathVariable String userId) {
        QueryWrapper<UserProgramView> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.orderBy(true, true, "display_order");
        List<UserProgramView> userProgramViews = getViewService().list(queryWrapper);
        return new RestUtil<List<UserProgramView>>().setData(userProgramViews);
    }

    @RequestMapping(value = "/saveItems", method = {RequestMethod.POST, RequestMethod.PUT})
    @ResponseBody
    public RestResponse<List<UserProgramView>> saveItems(@RequestBody List<UserProgram> userPrograms) {

        getTableService().saveOrUpdateBatch(userPrograms);

        String userId = userPrograms.get(0).getUserId();

        QueryWrapper<UserProgramView> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.orderBy(true, true, "display_order");
        List<UserProgramView> userProgramViews = getViewService().list(queryWrapper);
        return new RestUtil<List<UserProgramView>>().setData(userProgramViews);
    }

}

