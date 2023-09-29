package step.learning.servlets;

import com.google.inject.Singleton;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Properties;

@Singleton
public class MailServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // метод викликається до того, як відбувається розподіл за doXxx методами,
        // тут є можливість вплинути на цей розподіл
        switch (req.getMethod().toUpperCase()) {
            case "MAIL":
                this.doMail(req, resp);
                break;
            case "PATCH":
                this.doPatch(req, resp);
                break;
            default:
                super.service(req, resp);  // розподіл за замовчанням
        }
    }

    protected void doMail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Properties emailProperties = new Properties();
        emailProperties.put("mail.smtp.auth", "true");
        emailProperties.put("mail.smtp.starttls.enable", "true");
        emailProperties.put("mail.smtp.port", "587");
        emailProperties.put("mail.smtp.ssl.protocols", "TLSv1.2");
        emailProperties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        resp.getWriter().print("MAIL works");
        javax.mail.Session mailSession = javax.mail.Session.getInstance(emailProperties);
        mailSession.setDebug(true);  // виводити у консоль процес надсилання пошти
        try (Transport emailTransport = mailSession.getTransport("smtp")) {
            emailTransport.connect("smtp.gmail.com", "tomas.k.u90@gmail.com", "lrmraapcxarfubmv");
            javax.mail.internet.MimeMessage message = new MimeMessage(mailSession);
            message.setFrom(new javax.mail.internet.InternetAddress("proviryalovich@gmail.com"));
            message.setSubject("From site JavaWeb");
            message.setContent("Вітаємо з реєстрацією <a href='http://localhost:8080/JavaWeb_PU121/' >на сайті!</a>", "text/html; charset=UTF-8");
            // Надсилаємо його
            emailTransport.sendMessage(message, InternetAddress.parse("tom.k.u90@gmail.com"));
            resp.getWriter().print("MAIL sent");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Properties emailProperties = new Properties();
        emailProperties.put("mail.smtp.auth", "true");
        emailProperties.put("mail.smtp.starttls.enable", "true");
        emailProperties.put("mail.smtp.host", "smtp.gmail.com");
        emailProperties.put("mail.smtp.port", "587");
        emailProperties.put("mail.smtp.ssl.protocols", "TLSv1.2");
        emailProperties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        javax.mail.Session mailSession = javax.mail.Session.getInstance(emailProperties,
                new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("tomas.k.u90@gmail.com", "lrmraapcxarfubmv");
                    }
                }
        );
        MimeMessage message = new MimeMessage(mailSession);
        try {
            message.setFrom(new javax.mail.internet.InternetAddress("proviryalovich@gmail.com"));
            message.setSubject("Вітання JavaWeb");
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse("tom.k.u90@gmail.com"));


            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent("<b>Вітаємо</b> з реєстрацією <a href='http://localhost:8080/JavaWeb_PU121/' >на сайті!</a>", "text/html; charset=UTF-8");
            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setContent("Вітаємо з реєстраціє на сайті!", "text/html; charset=UTF-8");

            String name = "aboutURL.png";
//         String path = this.getClass().getClassLoader().getResource(name).getPath();
//                        \Users\thommy\IdeaProjects\JavaWeb_PU121\target\classes\aboutURL.png

            String path = "\\Users\\thommy\\IdeaProjects\\JavaWeb_PU121\\target\\classes\\aboutURL.png";

//         ServletContext context = getServletContext();
//         String relativePath = "WEB-INF/classes/aboutURL.png";
//         String path = context.getRealPath(relativePath);
//                       \Users\thommy\IdeaProjects\JavaWeb_PU121\target\classes\aboutURL.png


//            URL resourceUrl = getClass().getClassLoader().getResource("aboutURL.png");
//            String path2 = resourceUrl.getFile();
//            String path = path2.substring(1);
//                      \Users\thommy\IdeaProjects\JavaWeb_PU121\target\classes\aboutURL.png

            MimeBodyPart filePart = new MimeBodyPart();
            filePart.setDataHandler(new DataHandler(new FileDataSource(path)));
            filePart.setFileName(name);

            Multipart mailContent = new MimeMultipart();
            //mailContent.addBodyPart(textPart);
            //mailContent.addBodyPart(htmlPart);
            mailContent.addBodyPart(filePart);
            message.setContent(mailContent);
            Transport.send(message);
            resp.getWriter().print("PATCH works");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("pageName", "mail");
        req.getRequestDispatcher("WEB-INF/_layout.jsp").forward(req, resp);
    }
}
/*
Робота з електронною поштою
Оскільки надсилання пошти є потенційно вразливим інструментом, реалізуємо його
нестандартним методом запиту "MAIL"
 */