package com.daehanins.mes.biz.security.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daehanins.mes.biz.security.entity.UserView;
import com.daehanins.mes.biz.security.mapper.UserViewMapper;
import com.daehanins.mes.biz.security.service.IUserViewService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * UserView 서비스 구현 클래스
 * </p>
 *
 * @author jeonsj
 * @since 2020-07-21
 */
@Service
public class UserViewServiceImpl extends ServiceImpl<UserViewMapper, UserView> implements IUserViewService {

}
