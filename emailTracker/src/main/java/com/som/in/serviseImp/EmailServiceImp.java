package com.som.in.serviseImp;

import com.som.in.config.EmailServer;
import com.som.in.dto.EmailRequest;
import com.som.in.dto.StandardResponse;
import com.som.in.entity.EmailAccount;
import com.som.in.entity.EmailRecipient;
import com.som.in.exception.CustomException;
import com.som.in.exception.GatewayException;
import com.som.in.repository.EmailRecipientRepository;
import com.som.in.repository.EmailServerRepository;
import com.som.in.service.EmailService;
import com.som.in.util.EmailUtil;
import jakarta.annotation.PostConstruct;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;

@Service
public class EmailServiceImp implements EmailService {
    private static Properties prop = new Properties();
    @Autowired
    private EmailServer emailServer;

    @Autowired
    private EmailRecipientRepository emailRecipientRepository;

    @Autowired
    private EmailServerRepository emailServerRepository;

    @PostConstruct
    public void init() {
        prop.put("mail.smtp.auth", emailServer.isAuth());
        prop.put("mail.smtp.host", emailServer.getHost());
        prop.put("mail.smtp.port", emailServer.getPort());
        prop.put("mail.smtp.ssl.enable", emailServer.isSsl());
    }



    @Override
    public StandardResponse sendEmail(EmailRequest request) {
        if (!EmailUtil.isEmailAddressValid(request.toAddress())) {
            throw new CustomException("email address is not acceptable");
        }
        CompletableFuture.runAsync(() -> processEmailCommunication(request));
        return new StandardResponse(true);
    }

    private void processEmailCommunication(EmailRequest request) {
        try {
            String pixelId = request.toAddress().split("@")[0] + System.currentTimeMillis();
            EmailRecipient rec = new EmailRecipient(pixelId, request.toAddress(), request.subject(), LocalDate.now());
            emailRecipientRepository.save(rec);

            Optional<EmailAccount> emailAcctOptional = emailServerRepository.findById("admin");
            if (emailAcctOptional.isEmpty()) {
                throw new GatewayException("Attempt to fetch server details failed");
            }
            EmailAccount emailAcct = emailAcctOptional.get();

            // Sending email

            Session session = Session.getInstance(prop, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(emailAcct.getEmailId(), emailAcct.getAppPwd());
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(emailAcct.getEmailId()));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(request.toAddress()));
            message.setSubject(request.subject());

            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            String formattedContent = getEmailBodyWithTracker(request, pixelId);
            mimeBodyPart.setContent(formattedContent, "text/html; charset=utf-8");
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);

            message.setContent(multipart);
            try {
                Transport.send(message);
            } catch (MessagingException ex) {
                EmailRecipient recipient = emailRecipientRepository.findById(pixelId).get();
                recipient.setFailedToSend(true);
                emailRecipientRepository.save(recipient);
            }
        } catch (Exception ex) {
            //TODO add logs
            System.out.println(ex);
            throw new GatewayException("Failed to process Email, Try Again!");
        }
    }

    private static String getEmailBodyWithTracker(EmailRequest request, String pixelId) {
        String imgUrl = "http://localhost:8086/tracker/"+ pixelId;
        String formattedContent = String.format("<html><body>%1s<img src="+imgUrl+" width=\"1\" height=\"1\"></body>\n</html>", request.content());
        return formattedContent;
    }
}
