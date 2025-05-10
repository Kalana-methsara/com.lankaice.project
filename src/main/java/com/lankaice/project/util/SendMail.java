package com.lankaice.project.util;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.Properties;
import javax.mail.Session;


public class SendMail {

    public static void main(String[] args) {
        ArrayList<String> emails = new ArrayList<>();
        emails.add("admin1234@gmail.com");
        emails.add("thanushkadilshan1795@gmail.com");
        emails.add("kalanamethsara53@gmail.com");
        emails.add("tharushikadevindi754@gmail.com");

        String subject = "Login Credentials - Dry Ice Management System";
        String message = """
                Dear User,
                
                Your login details for the Dry Ice Management System are as follows:
                Username: %s
                Password: %s
                Role: %s
                
                Please change your password after logging in for the first time.
                
                Regards,
                System Admin
                """;

        String[][] credentials = {
                {"Admin", "#00ADpw", "Admin"},
                {"Dilshan", "#00SPpw", "Supervisor"},
                {"Kalana", "#00MKpw", "Manager"},
                {"Tharushika", "#00TDpw", "Cashier"}
        };

        try {
            for (int i = 0; i < credentials.length; i++) {
                String personalizedMsg = String.format(message, credentials[i][0], credentials[i][1], credentials[i][2]);
                outMail(personalizedMsg, emails.get(i), subject);
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public static void outMail(String msg, String to, String subject) throws MessagingException {
       // final String senderEmail = "kalanamethsara53@gmail.com"; // 🔐 Replace with your email
      //  final String senderPassword = "esnt itgm pjnp zbww";      // 🔐 App password only
        final String senderEmail = "lankaice1@gmail.com"; // 🔐 Replace with your email
        final String senderPassword = "xjzv ilfj wtcs dyfg";      // 🔐 App password only
        // IceLanka@28

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(senderEmail));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);
        message.setText(msg);

        Transport.send(message);
        System.out.println("Email sent to: " + to);
    }

    public static void outMail(String msg, ArrayList<String> toList, String subject) throws MessagingException {
        for (String to : toList) {
            outMail(msg, to, subject);
        }
    }
}
