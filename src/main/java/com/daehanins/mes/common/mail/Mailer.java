package com.daehanins.mes.common.mail;

import javax.activation.DataSource;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface Mailer {

  /**
   * Send a message
   *
   * @param message the message instance
   */
  void send(Message message);
  /**
   * Send a messag with attach files
   *
   * @param message the message instance
   * @param files  the attach files name,file map
   */
  void send(Message message, Map<String, DataSource> files);
}
