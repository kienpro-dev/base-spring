package com.example.projectbase.util;

import com.example.projectbase.domain.dto.common.DataMailDto;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class SendMailUtil {

    private final JavaMailSender mailSender;

    private final TemplateEngine templateEngine;

    /**
     * Gửi mail với file html
     *
     * @param mail     Thông tin của mail cần gửi
     * @param template Tên file html trong folder resources/template
     *                 Example: Index.html
     */
    public void sendEmailWithHTML(DataMailDto mail, String template) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());
        helper.setTo(mail.getTo());
        helper.setSubject(mail.getSubject());
        Context context = new Context();
        String htmlMsg = templateEngine.process(template, context);
        helper.setText(htmlMsg, true);
        mailSender.send(message);
    }

    /**
     * Gửi mail với tệp đính kèm
     *
     * @param mail  Thông tin của mail cần gửi
     * @param files File cần gửi
     */
    public void sendMailWithAttachment(DataMailDto mail, MultipartFile[] files) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
        helper.setTo(mail.getTo());
        helper.setSubject(mail.getSubject());
        if (files != null && files.length > 0) {
            for (MultipartFile file : files) {
                helper.addAttachment(Objects.requireNonNull(file.getOriginalFilename()), file);
            }
        }
        mailSender.send(message);
    }

    public void sendWithFreeTemplate(DataMailDto mail) throws MessagingException {
        // Tạo message
        MimeMessage message = mailSender.createMimeMessage();
        // Sử dụng Helper để thiết lập các thông tin cần thiết cho message
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
        helper.setFrom(mail.getFrom());
        helper.setTo(mail.getTo());
        helper.setSubject(mail.getSubject());
        try {
            String emailContent = getEmailContent(mail.getBody());
            helper.setText(emailContent, true);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
        helper.setReplyTo(mail.getFrom());
        String[] cc = mail.getCc();
        if (cc != null && cc.length > 0) {
            helper.setCc(cc);
        }
        String[] bcc = mail.getBcc();
        if (bcc != null && bcc.length > 0) {
            helper.setBcc(bcc);
        }
        String[] attachments = mail.getAttachments();
        if (attachments != null && attachments.length > 0) {
            for (String path : attachments) {
                File file = new File(path);
                helper.addAttachment(file.getName(), file);
            }
        }
        helper.addInline("image-1", new ClassPathResource("static/mail/image-1.png"));
        helper.addInline("image-2", new ClassPathResource("static/mail/image-2.png"));
        helper.addInline("image-3", new ClassPathResource("static/mail/image-3.png"));
        helper.addInline("image-4", new ClassPathResource("static/mail/image-4.png"));
        helper.addInline("forgot-password", new ClassPathResource("static/mail/forgot-password.png"));
        // Gửi message đến SMTP server
        mailSender.send(message);
    }

    public void sendSuccessfullyInfo(DataMailDto mail) throws MessagingException {
        // Tạo message
        MimeMessage message = mailSender.createMimeMessage();
        // Sử dụng Helper để thiết lập các thông tin cần thiết cho message
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
        helper.setFrom(mail.getFrom());
        helper.setTo(mail.getTo());
        helper.setSubject(mail.getSubject());
        try {
            String emailContent = getEmailSuccessfulContent(mail.getBody());
            helper.setText(emailContent, true);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
        helper.setReplyTo(mail.getFrom());
        String[] cc = mail.getCc();
        if (cc != null && cc.length > 0) {
            helper.setCc(cc);
        }
        String[] bcc = mail.getBcc();
        if (bcc != null && bcc.length > 0) {
            helper.setBcc(bcc);
        }
        String[] attachments = mail.getAttachments();
        if (attachments != null && attachments.length > 0) {
            for (String path : attachments) {
                File file = new File(path);
                helper.addAttachment(file.getName(), file);
            }
        }
        helper.addInline("image-1", new ClassPathResource("static/mail/image-1.png"));
        helper.addInline("image-2", new ClassPathResource("static/mail/image-2.png"));
        helper.addInline("image-3", new ClassPathResource("static/mail/image-3.png"));
        helper.addInline("image-4", new ClassPathResource("static/mail/image-4.png"));
        helper.addInline("service", new ClassPathResource("static/mail/service.png"));
        helper.addInline("rate", new ClassPathResource("static/mail/rate.png"));
        // Gửi message đến SMTP server
        mailSender.send(message);
    }

    private final Configuration configuration;

    private String getEmailContent(List<Object[]> body) throws IOException, TemplateException {
        StringWriter stringWriter = new StringWriter();
        Map<String, Object> model = new HashMap<>();
        String template = "forgot-password.ftlh";
        for (Object[] objects : body) {
            model.put("body", objects[0].toString());
        }
        configuration.getTemplate(template).process(model, stringWriter);
        return stringWriter.getBuffer().toString();
    }

    private String getEmailSuccessfulContent(List<Object[]> body) throws IOException, TemplateException {
        StringWriter stringWriter = new StringWriter();
        Map<String, Object> model = new HashMap<>();
        String template = "success-service.ftlh";
        for (Object[] objects : body) {
            model.put("urlLogin", objects[0].toString());
            model.put("username", objects[1].toString());
            model.put("content", objects[2].toString());
        }
        configuration.getTemplate(template).process(model, stringWriter);
        return stringWriter.getBuffer().toString();
    }

}
