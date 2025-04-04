package com.daehanins.mes.common.mail;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.daehanins.mes.biz.adm.entity.SystemEnvConfig;
import com.daehanins.mes.biz.adm.service.ISystemEnvConfigService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.Assert;

import javax.activation.DataSource;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DefaultMailManager implements MailManager {

  private final static Logger log = LoggerFactory.getLogger(DefaultMailManager.class);

  private String mailFrom = "purchase@jincostech.com";
  private Mailer mailer;    // AsyncMailer
  private Configuration configuration;

  public DefaultMailManager(Mailer mailer, Configuration configuration) {
//    this.mailFrom = mailFrom;
    this.mailer = mailer;
    this.configuration = configuration;
  }

  @Override
  public void send(String emailAddress, String cc, String subject, String template, MessageVariable... variables) {
    Assert.hasText(emailAddress, "Parameter `emailAddress` must not be blank");
    Assert.hasText(subject, "Parameter `subject` must not be blank");
    Assert.hasText(template, "Parameter `template` must not be blank");

    String messageBody = createMessageBody(template, variables);
    Message message = new SimpleMessage(emailAddress, cc, subject, messageBody, mailFrom);
    mailer.send(message);
  }

  @Override
  public void send(String emailAddress, String cc, String subject, String template, Map<String, DataSource> files, MessageVariable... variables) {
    Assert.hasText(emailAddress, "Parameter `emailAddress` must not be blank");
    Assert.hasText(subject, "Parameter `subject` must not be blank");
    Assert.hasText(template, "Parameter `template` must not be blank");

    String messageBody = createMessageBody(template, variables);
    Message message = new SimpleMessage(emailAddress, cc, subject, messageBody, mailFrom);
    mailer.send(message, files);
  }

  private String createMessageBody(String templateName, MessageVariable... variables) {
    try {
      Template template = configuration.getTemplate(templateName);
      Map<String, Object> model = new HashMap<>();
      if (variables != null) {
        for (MessageVariable variable : variables) {
          model.put(variable.getKey(), variable.getValue());
        }
      }
      return FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
    } catch (Exception e) {
      log.error("Failed to create message body from template `" + templateName + "`", e);
      return null;
    }
  }

}
