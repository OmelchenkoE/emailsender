package org.example;

import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

import java.io.File;
import java.util.Collections;
import java.util.Properties;

public class EmailService {

    private static final String USERNAME = "api";
    private static final String PASSWORD = "9b9cc3c795d67db29973e224d3706f65";

    public void sendMessage(Email email) throws MessagingException {
        //provide Mailtrap's host address
        String host = "live.smtp.mailtrap.io";
        //configure Mailtrap's SMTP server details
        Properties props = getProperties(host);
        //create the Session object
        Session session = Session.getInstance(props,
                new jakarta.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(USERNAME, PASSWORD);
                    }
                });
        try {
            Message message = getMessage(email, session);
            //send the email message
            Transport.send(message);
            System.out.println("Email Message Sent Successfully");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private Message getMessage(Email email, Session session) throws MessagingException {
        //create a MimeMessage object
        Message message = new MimeMessage(session);
        //set From email field
        message.setFrom(new InternetAddress(email.from()));
        //set To email field
        message.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(email.to()));
        //set email subject field
        message.setSubject("Here comes an attachment!");
        //create the message body part
        BodyPart messageBodyPart = new MimeBodyPart();
        //set the actual message
        messageBodyPart.setText(email.text());
        //create an instance of multipart object
        Multipart multipart = new MimeMultipart();
        //set the first text message part
        multipart.addBodyPart(messageBodyPart);
        //set the second part, which is the attachment
        for (File file : email.attachments()) {
            addAttachment(multipart, file.getPath());
        }
        //send the entire message parts
        message.setContent(multipart);
        return message;
    }

    private Properties getProperties(String host) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");
        return props;
    }

    private void addAttachment(Multipart multipart, String filename) throws MessagingException {
        DataSource source = new FileDataSource(filename);
        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(filename);
        multipart.addBodyPart(messageBodyPart);
    }

    public Email getEmail() {
        Email email = new Email("mailtrap@demomailtrap.com",
                "your@email.com",
                "TestSubject",
                "I'm testing the email service by sending this email with an attachment.",
                "Test",
                Collections.singletonList(new File("src/main/resources/file.jpg")));
        return email;
    }

    public void sendEmail() throws MessagingException {
        Email email = getEmail();
        sendMessage(email);
    }
}
