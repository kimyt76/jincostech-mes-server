package com.daehanins.mes.biz.common.controller;

import com.daehanins.mes.common.mail.Mailer;
import com.daehanins.mes.common.mail.SimpleMessage;
import com.daehanins.mes.common.utils.RestUtil;
import com.daehanins.mes.common.vo.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * <p>
 * Email발송 Front 컨트롤러
 * </p>
 *
 * @author jeonsj
 * @since 2020-05-13
 */
@RestController
@RequestMapping("/email")
public class EmailController {

    @Autowired
    Mailer mailer;

    @RequestMapping(value = "/sendTest", method = RequestMethod.GET)
    public RestResponse<String> sendTest() throws IOException {

        String from = "purchase@jincostech.com";
        String to = "jeonsj@daehanins.com";
        String cc = "";
        String subject = "A test message";
        String body = "Username: test, Email Address: test@taskagile.com";

        SimpleMessage message = new SimpleMessage(to, cc, subject, body, from);
        mailer.send(message);

        return new RestUtil<String>().setData("OK");
    }



}
