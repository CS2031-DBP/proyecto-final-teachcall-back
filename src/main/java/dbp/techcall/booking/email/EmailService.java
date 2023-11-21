package dbp.techcall.booking.email;

import jakarta.mail.internet.MimeMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.context.Context;


@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Async
    public void sendEmailAsync(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("myemail@example.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    @Async
    public void sendWelcomeEmail(String to, String firstName) {
        // Crear el contexto con los datos para la plantilla
        Context context = new Context();
        context.setVariable("firstName", firstName);
        context.setVariable("appName", "Teachcall");

        // Procesar la plantilla HTML con Thymeleaf
        String body = templateEngine.process("welcomeTemplate", context);

        try {
            // Crear un mensaje MIME para enviar contenido HTML
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
            messageHelper.setFrom("myemail@example.com");
            messageHelper.setTo(to);
            messageHelper.setSubject("Bienvenido a Teachcall");
            messageHelper.setText(body, true); // true indica que el contenido es HTML

            // Enviar el correo electrónico
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
