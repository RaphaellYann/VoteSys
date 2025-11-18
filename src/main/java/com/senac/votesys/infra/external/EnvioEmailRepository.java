package com.senac.votesys.infra.external;

import com.senac.votesys.domain.interfaces.IEnvioEmail;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Component
public class EnvioEmailRepository implements IEnvioEmail {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private JavaMailSender javaMailSender;

    @Async
    @Override
    public void enviarEmailSimples(String para, String assunto, String texto) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("nao-responda@meusite.com");
        message.setTo(para);
        message.setSubject(assunto);
        message.setText(texto);
        mailSender.send(message);
    }

    @Async
    @Override
    public void enviarEmailTemplate(String para, String assunto, String linkRecuperacao) {
        try {
            MimeMessage mensagem = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensagem, true, "UTF-8");

            String htmlTemplate = carregarTemplateEmail();

            String htmlFinal = htmlTemplate
                    .replace("${linkRecuperacao}", linkRecuperacao)
                    .replace("${dataEnvio}", String.valueOf(LocalDateTime.now()));

            helper.setTo(para);
            helper.setSubject(assunto);
            helper.setText(htmlFinal, true);
            helper.setFrom("nao-responda@meusite.com");

            javaMailSender.send(mensagem);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao enviar email", e);
        }
    }

    private String carregarTemplateEmail() throws IOException {
        ClassPathResource resource = new ClassPathResource("templates/email-template.html");
        try (InputStream inputStream = resource.getInputStream()) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
