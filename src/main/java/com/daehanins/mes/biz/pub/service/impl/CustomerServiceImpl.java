package com.daehanins.mes.biz.pub.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.daehanins.mes.biz.pub.entity.Customer;
import com.daehanins.mes.biz.pub.mapper.CustomerMapper;
import com.daehanins.mes.biz.pub.service.ICustomerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * Customer 서비스 구현 클래스
 * </p>
 *
 * @author jeonsj
 * @since 2020-04-01
 */
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements ICustomerService {

    @Transactional
    public List<Customer> saveItems(List<Customer> customerList) {
        customerList.forEach( customer -> {
            if (StringUtils.isNotBlank(customer.getCustomerCd())) {
                this.saveOrUpdate(customer);
            }
        });
        return customerList;
    }

}
