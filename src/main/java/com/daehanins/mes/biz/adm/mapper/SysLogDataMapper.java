package com.daehanins.mes.biz.adm.mapper;

import com.daehanins.mes.biz.adm.entity.SysLogData;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
* <p>
*  SQL Mapper 인터페이스
* </p>
*
* @author jeonsj
* @since 2022-12-28
*/
@Repository
public interface SysLogDataMapper extends BaseMapper<SysLogData> {

    int insertLoginLog(String logDate);

    int insertSysLog(String logDate) ;

    SysLogData selectLastData();

}
