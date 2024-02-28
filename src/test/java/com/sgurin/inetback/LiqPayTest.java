package com.sgurin.inetback;

import com.liqpay.LiqPay;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;

@SpringBootTest(classes = {
        InetBackApplication.class})
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class LiqPayTest {
    private static final String PUBLIC_KEY = "sandbox_i82161209706";
    private static final String PRIVATE_KEY = "sandbox_bUPHh7MyqTflUKvLzz8fMQ3c3vO9Tu35K5Heqyzi";

    @Test
    @Rollback(value = false)
    public void name() throws Exception {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("action", "pay");
        params.put("amount", "1");
        params.put("currency", "UAH");
        params.put("description", "description text");
        params.put("order_id", "order_id_1");
        params.put("version", "3");
        params.put("key", PUBLIC_KEY);
        LiqPay liqpay = new LiqPay(PUBLIC_KEY, PRIVATE_KEY);
        String html = liqpay.cnb_form(params);
        System.out.println(html);

    }
}
