package com.daehanins.mes.biz.adm.mapper;

import com.daehanins.mes.biz.adm.entity.SysLogJob;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
* <p>
*  SQL Mapper 인터페이스
* </p>
*
* @author jeonsj
* @since 2022-12-01
*/
@Repository
public interface SysLogJobMapper extends BaseMapper<SysLogJob> {

    void insertLogJob(SysLogJob sysLogJob);
}
