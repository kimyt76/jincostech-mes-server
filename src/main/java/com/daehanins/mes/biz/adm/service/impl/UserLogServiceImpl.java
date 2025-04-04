package com.daehanins.mes.biz.adm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daehanins.mes.biz.adm.entity.UserLog;
import com.daehanins.mes.biz.adm.mapper.UserLogMapper;
import com.daehanins.mes.biz.adm.service.IUserLogService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 사용자로그UserLog 서비스 구현 클래스
 * </p>
 *
 * @author jeonsj
 * @since 2022-07-25
 */
@Service
public class UserLogServiceImpl extends ServiceImpl<UserLogMapper, UserLog> implements IUserLogService {

}
