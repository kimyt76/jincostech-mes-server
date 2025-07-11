package com.daehanins.mes.biz.common.schedule;

import com.daehanins.mes.biz.common.service.SmartFactoryLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class BatchSchedule {

    private static final Logger logger = LoggerFactory.getLogger(BatchSchedule.class);

    @Value("${app.logs.fixedRate}")
    private boolean BATCH_FIXED_RATE;

    @Value("${app.logs.save}")
    private boolean BATCH_SYSLOG_SAVE;

    @Value("${app.logs.send}")
    private boolean BATCH_SYSLOG_SEND;

    @Autowired
    SmartFactoryLogService smartFactoryLogService;

//    @Scheduled(fixedRate = 5000)
//    public void fixedRateExecute() {
//        if (BATCH_FIXED_RATE) {
//            logger.info("Batch: fixedRate=5000  5초마다 실행");
//        }
//    }

//    @Scheduled(cron = "0 0 23 * * *")
//    public void cornSaveSysLog() {
//        // 실행여부 설정 첵크
//        if (BATCH_SYSLOG_SAVE) {
//            logger.info("Batch: cron 스마트1번가 sys_log 데이터 추출");
//            smartFactoryLogService.saveSystemLogData();
//        }
//
//    }

//    @Scheduled(cron = "0 10 23 * * *")
//    public void cornSendSysLog() {
//        // 실행여부 설정 첵크
//        if (BATCH_SYSLOG_SEND) {
//            logger.info("Batch: cron 스마트1번가 sys_log 전송 ");
//            smartFactoryLogService.sendSystemLogData();
//        }
//    }

    @Scheduled(cron = "0 0 0/1 * * *")
    public void cronSendSysLogAfterSave() {
        // 실행여부 설정 첵크
        if (BATCH_SYSLOG_SEND) {
            smartFactoryLogService.saveSystemLogData();
            smartFactoryLogService.sendLastLogData();
            logger.info("Batch: cron 스마트1번가 sys_log 전송 ");
        }
    }

    /* ======== Cron 설정 예 ========== **/
    //     "0 0 * * * *"            = the top of every hour of every day.
    //     "*/10 * * * * *"         = 매 10초마다 실행한다.
    //     "0 0 8-10 * * *"         = 매일 8, 9, 10시에 실행한다
    //     "0 0 6,19 * * *"         = 매일 오전 6시, 오후 7시에 실행한다.
    //     "0 0/30 8-10 * * *"      = 8:00, 8:30, 9:00, 9:30, 10:00 and 10:30 every day.
    //     "0 0 9-17 * * MON-FRI"   = 오전 9시부터 오후 5시까지 주중(월~금)에 실행한다.
    //     "0 0 0 25 12 ?"          = every Christmas Day at midnight

}

