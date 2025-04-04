package com.daehanins.mes.biz.pub.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daehanins.mes.biz.pub.entity.Process;
import com.daehanins.mes.biz.pub.mapper.ProcessMapper;
import com.daehanins.mes.biz.pub.service.IProcessService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 공정Process 서비스 구현 클래스
 * </p>
 *
 * @author jeonsj
 * @since 2020-05-04
 */
@Service
public class ProcessServiceImpl extends ServiceImpl<ProcessMapper, Process> implements IProcessService {

}
