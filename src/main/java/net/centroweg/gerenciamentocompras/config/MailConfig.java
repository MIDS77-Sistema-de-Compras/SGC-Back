package net.centroweg.gerenciamentocompras.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * Configuração do cliente de envio de e-mail via JavaMail.
 *
 * @see JavaMailSender
 */
@Configuration
public class MailConfig {

    /**
     * Endereço do servidor SMTP (ex: {@code smtp.gmail.com}).
     */
    @Value("${spring.mail.host}")
    private String host;

    /**
     * Porta do servidor SMTP (ex: {@code 587} para STARTTLS).
     */
    @Value("${spring.mail.port}")
    private int port;

    /**
     * Endereço de e-mail utilizado para autenticação no servidor SMTP.
     */
    @Value("${spring.mail.username}")
    private String username;

    /**
     * Senha ou token de aplicação para autenticação (nunca deve ser exposta publicamente).
     */
    @Value("${spring.mail.password}")
    private String password;

    /**
     * Expõe o {@link JavaMailSender} como um bean do Spring, configurado com as credenciais e propriedades SMTP da aplicação.
     * @return instância configurada de {@link JavaMailSender}.
     */
    @Bean
    public JavaMailSender javaMailSender(){
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(username);
        mailSender.setPassword(password);
        mailSender.setJavaMailProperties(getMailProperties());
        return mailSender;
    }

    /**
     * Define as propriedades adicionais do protocolo SMTP.
     * @return {@link Properties} com as configurações do protocolo SMTP.
     */
    private Properties getMailProperties(){
        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "false");
        return props;
    }

}
