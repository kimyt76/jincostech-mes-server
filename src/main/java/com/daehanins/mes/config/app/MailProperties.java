package com.daehanins.mes.config.app;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

//@Configuration
//@ConfigurationProperties(prefix="app.mail")
//@Validated
public class MailProperties {

    private String host;

    private int port;

    @Email
    @NotBlank
    private String username;

    private String password;

    private boolean security;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isSecurity() {
        return security;
    }

    public void setSecurity(boolean security) {
        this.security = security;
    }
}

