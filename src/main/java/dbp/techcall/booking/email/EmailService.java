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

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import java.time.ZonedDateTime;
import java.util.Locale;


@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Async
    public void sendEmailAsync(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("teachcallutec@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    @Async
    public void sendBookingTeacherEmailAsync(String to, String subject, String professor, String student, String date, String course){
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(date);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy 'a las' HH:mm 'hrs'", new Locale("es", "PE"));
        String formattedDate = zonedDateTime.withZoneSameInstant(ZoneId.of("America/Lima")).format(formatter);
        Context context = new Context();
        context.setVariable("professorName", professor);
        context.setVariable("student", student);
        context.setVariable("date", formattedDate);
        context.setVariable("course", course);
        String body = templateEngine.process("bookingTeacherTemplate", context);

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
            messageHelper.setFrom("teachcallutec@gmail.com");
            messageHelper.setTo(to);
            messageHelper.setSubject(subject);
            messageHelper.setText(body, true);
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Async
    public void sendBookingStudentEmailAsync(String to, String subject, String professor, String student, String date, String course){
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(date);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy 'a las' HH:mm 'hrs'", new Locale("es", "PE"));
        String formattedDate = zonedDateTime.withZoneSameInstant(ZoneId.of("America/Lima")).format(formatter);
        Context context = new Context();
        context.setVariable("professorName", professor);
        context.setVariable("student", student);
        context.setVariable("date", date);
        context.setVariable("course", course);
        String body = templateEngine.process("bookingStudentTemplate", context);

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
            messageHelper.setFrom("teachcallutec@gmail.com");
            messageHelper.setTo(to);
            messageHelper.setSubject(subject);
            messageHelper.setText(body, true);
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            messageHelper.setFrom("teachcallutec@gmail.com");
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
