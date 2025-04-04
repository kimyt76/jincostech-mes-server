package com.daehanins.mes.biz.common.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface SmartFactoryLogService {

    void saveSystemLogData();

    void sendSystemLogData();

    void saveSystemLogDataByDate(String logDate);

    void sendSystemLogDataByDate(String logDate);

    void sendLastLogData();

}
