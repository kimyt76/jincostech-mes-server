package com.daehanins.mes.biz.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.daehanins.mes.biz.adm.entity.SysLogData;
import com.daehanins.mes.biz.adm.entity.SysLogJob;
import com.daehanins.mes.biz.adm.mapper.SysLogDataMapper;
import com.daehanins.mes.biz.adm.mapper.SysLogJobMapper;
import com.daehanins.mes.biz.common.service.SmartFactoryLogService;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class SmartFactoryLogServiceImpl implements SmartFactoryLogService {

    private static final Logger logger = LoggerFactory.getLogger(SmartFactoryLogServiceImpl.class);

    private final String URL_SMART = "https://log.smart-factory.kr/apisvc/sendLogDataXML.do";
    private final String SERVICE_KEY = "$5$API$ldmj0DoLdazF65mDfv9362ftUV0b9OqxlA2q6FvUp7B";

    @Autowired
    private CloseableHttpClient httpClient;

    @Autowired
    private SysLogDataMapper sysLogDataMapper;

    @Autowired
    private SysLogJobMapper sysLogJobMapper;

    @Override
    public void sendSystemLogData() {

        String logDate = getLogDate();

        QueryWrapper<SysLogData> wrapper = new QueryWrapper<SysLogData>().likeRight("log_dt", logDate);

        List<SysLogData> logDataList = sysLogDataMapper.selectList(wrapper);

        int logCnt = 0;
        try {

            for (SysLogData item : logDataList) {

                String data = URLEncoder.encode(item.getXmlData(SERVICE_KEY), "UTF-8");

                UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL_SMART);
                builder.queryParam("logData", data);
                UriComponents uri = builder.build();

                HttpGet httpGet = new HttpGet(uri.toUriString());

                try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                    int statusCode = response.getStatusLine().getStatusCode();
                    if (statusCode == 200) {
                        logCnt++;
                    }
                    logger.debug(response.toString(), statusCode);
                    logger.debug("Executed sendSystemLogData->inner-row!");
                } catch(Exception ex) {
                    logger.debug("ERROR::sendSystemLogData->innerFor" + ex.getMessage());
                }
            }
            // SysLog result저장
            // writeLog(logDate, logCnt);

        } catch (Exception ex) {
            ex.printStackTrace();
            logger.debug("ERROR::sendSystemLogData" + ex.getMessage());
        }
        logger.info("Executed sendSystemLogData::success" + logDate);
    }

    private void writeLog(String logDate, int logCnt) {
        SysLogJob logJob = new SysLogJob();
        SimpleDateFormat sDate2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
        logJob.setWorkTime(sDate2.format(new Date()));
        logJob.setLogDt(logDate);
        logJob.setLogDataCnt(logCnt);
        sysLogJobMapper.insertLogJob(logJob);
    }

    @Override
    public void saveSystemLogData() {

        String logDate = getLogDate();
        QueryWrapper<SysLogData> wrapper = new QueryWrapper<SysLogData>().likeRight("log_dt", logDate);
        sysLogDataMapper.delete(wrapper);

        try {
            int loginCnt = sysLogDataMapper.insertLoginLog(logDate);
            logger.debug("Executed saveSystemLogData:logcnt=(" + loginCnt + ")");
            int sysCnt = sysLogDataMapper.insertSysLog(logDate);
            logger.debug("Executed saveSystemLogData!sysCnt=(" + sysCnt + ")");

        } catch (Exception ex) {
            logger.debug("ERROR::saveSystemLogData" + ex.getMessage());
        }
        logger.info("Executed saveSystemLogData:" + logDate);
    }


    private String  getLogDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date now = new Date();

        return format.format(now);
    }


    public void sendSystemLogDataByDate(String logDate) {

        QueryWrapper<SysLogData> wrapper = new QueryWrapper<SysLogData>().likeRight("log_dt", logDate);

        List<SysLogData> logDataList = sysLogDataMapper.selectList(wrapper);
        int dataSize = logDataList.size();
        int logCnt = 0;
        try {
            for (SysLogData item : logDataList) {

                String data = URLEncoder.encode(item.getXmlData(SERVICE_KEY), "UTF-8");

                UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL_SMART);
                builder.queryParam("logData", data);
                UriComponents uri = builder.build();

                HttpGet httpGet = new HttpGet(uri.toUriString());

                try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                    logger.debug(response.toString());
                    int statusCode = response.getStatusLine().getStatusCode();
                    if (statusCode == 200) {
                        logCnt++;
                    }
                    logger.debug(response.toString(), statusCode);
                    logger.debug("Executed sendSystemLogData->inner-row!");
                } catch(Exception ex) {
                    logger.debug("ERROR::sendSystemLogData->innerFor" + ex.getMessage());
                }
            }
            // SysLog result저장
            // writeLog(logDate, logCnt);

        } catch (Exception ex) {
            ex.printStackTrace();
            logger.debug("ERROR::sendSystemLogData" + ex.getMessage());
        }
        logger.info("Executed sendSystemLogData::success" + logDate);
        logger.info("size:" + dataSize + " log_cnt:" + logCnt);

    }

    public void saveSystemLogDataByDate(String logDate) {
        int loginCnt = 0;
        try {
            loginCnt = sysLogDataMapper.insertLoginLog(logDate);
            logger.debug("Executed saveSystemLogData:logcnt=(" + loginCnt + ")");
            int sysCnt = sysLogDataMapper.insertSysLog(logDate);
            logger.debug("Executed saveSystemLogData!sysCnt=(" + sysCnt + ")");

        } catch (Exception ex) {
            logger.debug("ERROR::saveSystemLogData" + ex.getMessage());
        }
        logger.info("Executed saveSystemLogData:" + logDate);
        logger.debug("Executed saveSystemLogData:logcnt=(" + loginCnt + ")");

    }

    public void sendLastLogData() {
        SysLogData syslogData = sysLogDataMapper.selectLastData();
        try {
            String data = URLEncoder.encode(syslogData.getXmlData(SERVICE_KEY), "UTF-8");
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL_SMART);
            builder.queryParam("logData", data);
            UriComponents uri = builder.build();
            HttpGet httpGet = new HttpGet(uri.toUriString());

            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                int statusCode = response.getStatusLine().getStatusCode();

                logger.debug(response.toString(), statusCode);
                logger.debug("Executed sendLastLogData->inner-row!");

            } catch(Exception ex) {
                logger.debug("ERROR::sendLastLogData->innerFor" + ex.getMessage());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.debug("ERROR::sendLastLogData" + ex.getMessage());
        }
        logger.info("Executed sendLastLogData::success");
    }
}
