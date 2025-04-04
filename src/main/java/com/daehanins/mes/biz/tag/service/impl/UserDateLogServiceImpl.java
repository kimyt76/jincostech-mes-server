package com.daehanins.mes.biz.tag.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daehanins.mes.biz.tag.entity.UserDateLog;
import com.daehanins.mes.biz.tag.mapper.UserDateLogMapper;
import com.daehanins.mes.biz.tag.service.IUserDateLogService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * UserDateLog 서비스 구현 클래스
 * </p>
 *
 * @author jeonsj
 * @since 2022-10-30
 */
@Service
public class UserDateLogServiceImpl extends ServiceImpl<UserDateLogMapper, UserDateLog> implements IUserDateLogService {

}
