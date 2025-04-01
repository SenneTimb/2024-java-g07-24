package domain;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class GmailProvider {

	private final static String SMTP_HOST = "smtp.gmail.com";
    private final static String SMTP_PORT = "587";
    private final static String GMAIL_USERNAME = "noreplydelaware@gmail.com";
    private final static String GMAIL_PASSWORD = "tdlu ydey ljii htow";
	
    public static void sendEmail(String toUser, String subject, String body) throws Exception {
    	Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);

//        System.out.println("Props : " + props);
        
        Session session = Session.getDefaultInstance(props,
            new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(GMAIL_USERNAME, GMAIL_PASSWORD);
                }
            });
//        session.setDebug(true);
	
//        System.out.println("Got Session : " + session);

    	Message message = new MimeMessage(session);
//        System.out.println("before sending");
        message.setFrom(new InternetAddress(GMAIL_USERNAME));
        message.addRecipients(Message.RecipientType.TO,
                InternetAddress.parse(toUser));
        message.setSubject(subject);
        message.setContent(body, "text/html; charset=utf-8");
        message.saveChanges();
//        System.out.println("Before transport");
      
        Transport transport = session.getTransport("smtp");
        String smtp_server = props.getProperty("mail.smtp.host");
        transport.connect(smtp_server, GMAIL_USERNAME, GMAIL_PASSWORD);

        try {
            transport.sendMessage(message, message.getAllRecipients());
        }
        catch (Exception e) {
            System.out.println("Error" + e.getMessage());
        }
        transport.close();
//        System.out.println("message Object : " + message);
        System.out.println("Email Sent Successfully");
    }
    
}
