package com.daehanins.mes.config.mail;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.daehanins.mes.biz.adm.entity.SystemEnvConfig;
import com.daehanins.mes.biz.adm.service.ISystemEnvConfigService;
import com.daehanins.mes.biz.adm.service.impl.SystemEnvConfigServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.List;
import java.util.Properties;

/**
 * @author jeonsj
 */
@Configuration
public class JavaMailConfig {

    @Autowired
    private ISystemEnvConfigService systemEnvConfigService;

    @Bean
    public JavaMailSender javaMailSender() {

        QueryWrapper<SystemEnvConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("env_config_cd", "mail.%");
        List<SystemEnvConfig> systemEnvConfigList = systemEnvConfigService.list(queryWrapper);

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        String securityVal = "";

        for( SystemEnvConfig conf : systemEnvConfigList) {
            if (conf.getEnvConfigCd().equals("mail.host")) mailSender.setHost(conf.getVarValue());
            if (conf.getEnvConfigCd().equals("mail.port")) mailSender.setPort(Integer.parseInt(conf.getVarValue()));
            if (conf.getEnvConfigCd().equals("mail.username")) mailSender.setUsername(conf.getVarValue());
            if (conf.getEnvConfigCd().equals("mail.password")) mailSender.setPassword(conf.getVarValue());
            if (conf.getEnvConfigCd().equals("mail.security")) securityVal = conf.getVarValue();
        }

        if (securityVal.equals("Y")) {
            Properties props = new Properties();
            props.setProperty("mail.smtp.auth", "true");
            props.setProperty("mail.smtp.starttls.enable", "true");
            mailSender.setJavaMailProperties(props);
        }

        return mailSender;
    }
}
