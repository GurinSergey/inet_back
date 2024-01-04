package com.sgurin.inetback;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import com.twilio.Twilio;
import com.twilio.converter.Promoter;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@SpringBootTest(classes = {
        InetBackApplication.class})
@RunWith(SpringRunner.class)
@Transactional
public class TwilioTest {
    public static final String ACCOUNT_SID = "AC32c6f0744430c33fc31c35d47f845bec";
    public static final String AUTH_TOKEN = "c85ff48508cf1cfc4199a3efd82f9***";

    @Test
    public void name() {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message message = Message.creator(
                new com.twilio.type.PhoneNumber("+380660314300"),
                new com.twilio.type.PhoneNumber("+14083009835"),
                "Hello")
                .create();

        System.out.println(message.getSid());
    }
}
