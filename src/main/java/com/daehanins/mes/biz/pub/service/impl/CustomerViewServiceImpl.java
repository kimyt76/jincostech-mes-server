package com.daehanins.mes.biz.pub.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daehanins.mes.biz.pub.entity.CustomerView;
import com.daehanins.mes.biz.pub.mapper.CustomerViewMapper;
import com.daehanins.mes.biz.pub.service.ICustomerViewService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * CustomerView 서비스 구현 클래스
 * </p>
 *
 * @author jeonsj
 * @since 2020-04-03
 */
@Service
public class CustomerViewServiceImpl extends ServiceImpl<CustomerViewMapper, CustomerView> implements ICustomerViewService {

}
