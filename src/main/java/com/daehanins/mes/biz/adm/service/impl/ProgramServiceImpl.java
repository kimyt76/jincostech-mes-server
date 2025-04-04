package com.daehanins.mes.biz.adm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daehanins.mes.biz.adm.entity.Program;
import com.daehanins.mes.biz.adm.mapper.ProgramMapper;
import com.daehanins.mes.biz.adm.service.IProgramService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 프로그램기능Program 서비스 구현 클래스
 * </p>
 *
 * @author jeonsj
 * @since 2022-07-25
 */
@Service
public class ProgramServiceImpl extends ServiceImpl<ProgramMapper, Program> implements IProgramService {

}
