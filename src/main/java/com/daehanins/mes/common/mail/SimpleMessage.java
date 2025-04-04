package com.daehanins.mes.common.mail;

import java.util.Objects;

public class SimpleMessage implements Message {

  private String to;
  private String subject;
  private String body;
  private String from;
  private String cc;

  public SimpleMessage(String to, String cc, String subject, String body, String from) {
    this.to = to;
    this.cc = cc;
    this.subject = subject;
    this.body = body;
    this.from = from;

  }

  @Override
  public String getTo() {
    return to;
  }

  @Override
  public String getSubject() {
    return subject;
  }

  @Override
  public String getBody() {
    return body;
  }

  public String getFrom() {
    return from;
  }

  @Override
  public String[] getCc() {
    String[] resultCc = null;
    resultCc = this.cc.split(",");
    return resultCc;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof SimpleMessage)) return false;
    SimpleMessage that = (SimpleMessage) o;
    return Objects.equals(to, that.to) &&
      Objects.equals(subject, that.subject) &&
      Objects.equals(body, that.body);
  }

  @Override
  public int hashCode() {
    return Objects.hash(to, subject, body);
  }

  @Override
  public String toString() {
    return "SimpleMessage{" +
      "to='" + to + '\'' +
      ", subject='" + subject + '\'' +
      ", body='" + body + '\'' +
      '}';
  }
}
