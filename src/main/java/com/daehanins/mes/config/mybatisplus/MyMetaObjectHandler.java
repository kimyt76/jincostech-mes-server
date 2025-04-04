package com.daehanins.mes.config.mybatisplus;

import com.daehanins.mes.common.utils.AuthUtil;
import org.apache.ibatis.reflection.MetaObject;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
// import java.util.Date;

@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        String userName = AuthUtil.getUsername();
        this.setFieldValByName("regTime",  LocalDateTime.now(), metaObject);
        this.setFieldValByName("regId",  userName, metaObject);
        this.setFieldValByName("updTime", LocalDateTime.now(), metaObject);
        this.setFieldValByName("updId",  userName, metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        String userName = AuthUtil.getUsername();
        this.setFieldValByName("updTime", LocalDateTime.now(), metaObject);
        this.setFieldValByName("updId",  userName, metaObject);
    }


}
