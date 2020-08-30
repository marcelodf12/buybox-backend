package py.com.buybox.trackingSystem.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;

@Service
public class SenderMailService {

    @Autowired
    JavaMailSender javaMailSender;

    public void sendEmail(String subject, String text, String to) throws MessagingException, IOException {

        MimeMessage msg = javaMailSender.createMimeMessage();

        // true = multipart message
        MimeMessageHelper helper = new MimeMessageHelper(msg, true);

        helper.setTo(to);
        helper.setFrom("no-reply@buybox.com.py");
        helper.setSubject(subject);
        helper.setText(text, true);

        javaMailSender.send(msg);

    }

}
