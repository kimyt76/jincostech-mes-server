package com.daehanins.mes.common.mail;

import javax.activation.DataSource;
import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface MailManager {

  /**
   * Send a message to a recipient
   *
   * @param emailAddress the recipient's email address
   * @param cc the cc recipient's email address
   * @param subject the subject key of the email
   * @param template the template file name of the email
   * @param variables message variables in the template file
   */
  void send(String emailAddress, String cc, String subject, String template, MessageVariable... variables);

  /**
   * Send a message to a recipient
   *
   * @param emailAddress the recipient's email address
   * @param cc the cc recipient's email address
   * @param subject the subject key of the email
   * @param template the template file name of the email
   * @param files  the attach files name,fileInputStream map
   * @param variables message variables in the template file
   */
  void send(String emailAddress, String cc, String subject, String template, Map<String, DataSource> files, MessageVariable... variables);

}
