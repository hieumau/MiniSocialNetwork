package sample.utils;

import sample.account.UsersBLO;
import sample.entity.Users;


import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;


public class EmailUtils {
    public static void sendEmail(String userId) {
        // Recipient's email ID needs to be mentioned.
        String to = userId;

        // Sender's email ID needs to be mentioned
        String from = "mauhieulowsercure@gmail.com";

        // Assuming you are sending email from through gmails smtp
        String host = "smtp.gmail.com";

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        // Get the Session object.// and pass username and password
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, "Hieu123321123");
            }

        });

        // Used to debug SMTP issues
        session.setDebug(true);

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // Set Subject: header field
            message.setSubject("Mini Social Network verification code!");

            // Now set the actual message
            UsersBLO usersBLO = new UsersBLO();
            Users user = usersBLO.get(userId);
            if (user != null){
                message.setText("Hi " + user.getFullName() +"." +
                        "Your verification code:  " + user.getVerifyCode(), "utf-8");
                Transport.send(message);
            }

        } catch (Exception mex) {
            mex.printStackTrace();
        }
    }

}
