package com.sgurin.inetback;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;

import javax.net.ssl.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

@SpringBootTest(classes = {
        InetBackApplication.class})
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@Slf4j
public class CertTest {
    public static final String CERTIFICATE_ISSUERS = getProperty("certificate_issuers");
    public static final Set<String> CERTIFICATE_ISSUERS_SET = new HashSet<>();

    static {
        if (!StringUtils.isEmpty(CERTIFICATE_ISSUERS)) {
            for (String certificateIssuer : CERTIFICATE_ISSUERS.split(",")) {
                CERTIFICATE_ISSUERS_SET.add(StringUtils.trimWhitespace(certificateIssuer));
            }
        }
    }

    static String getProperty(String propertyName) {
        String result = null;
        FileInputStream fis = null;
        Properties property = new Properties();

        String filePath = System.getProperty("user.dir") + File.separator + "conf" + File.separator + "conf.properties";
        try {
            fis = new FileInputStream(filePath);
            property.load(fis);

            result = property.getProperty(propertyName);

        } catch (IOException e) {
            log.error("Property file {} not found. {}", filePath, e);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    @Test
    public void whenGetExpireDate_whenReturn() {
        Date expireDate;
        try {
            expireDate = getExpireDate("https://cp4-wifi.net/");
        } catch (Exception e) {
            e.printStackTrace();
            expireDate = new Date();
        }

        System.out.println(expireDate);
    }

    private Date getExpireDate(String host) throws Exception {
        Date expireDate = new Date(0L);
        SSLContext ctx = SSLContext.getInstance("TLS");
        ctx.init(new KeyManager[0], new TrustManager[]{new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        }}, new SecureRandom());
        SSLContext.setDefault(ctx);

        URL url = new URL(host);
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setConnectTimeout(60_000);

        conn.getResponseCode();
        X509Certificate[] certs = (X509Certificate[])conn.getServerCertificates();
        for (X509Certificate cert : certs) {
            String hostCertificateIssuer = cert.getSubjectDN().getName();
            for (String certificateIssuer : CERTIFICATE_ISSUERS_SET) {
                if(hostCertificateIssuer.contains(certificateIssuer)){
                    expireDate = cert.getNotAfter();
                }
            }
        }

        conn.disconnect();
        return expireDate;
    }
}
