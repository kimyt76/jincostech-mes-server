package com.daehanins.mes.biz.pub.service;

import com.daehanins.mes.biz.pub.entity.Customer;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * Customer 서비스
 * </p>
 *
 * @author jeonsj
 * @since 2020-04-01
 */
public interface ICustomerService extends IService<Customer> {

    List<Customer> saveItems(List<Customer> customerList);
}
