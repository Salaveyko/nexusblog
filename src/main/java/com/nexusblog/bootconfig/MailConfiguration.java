package com.nexusblog.bootconfig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfiguration {
    @Value("${spring.mail.host}")
    private String host;
    @Value("${spring.mail.port}")
    private int port;
    @Value("${spring.mail.username}")
    private String username;
    @Value("${spring.mail.password}")
    private String password;
    @Value("${mail.debug}")
    private String debug;
    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    private String starttlsEnable;
    @Value("${spring.mail.protocol}")
    private String protocol;
    @Value("${spring.mail.properties.mail.smtp.auth}")
    private String auth;

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);

        mailSender.setUsername(username);
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", protocol);
        props.put("mail.smtp.starttls.enable", starttlsEnable);
        props.put("mail.smtp.auth", auth);
        props.put("mail.smtp.ssl.trust", host);
        props.put("mail.debug", debug);

        return mailSender;
    }
}
