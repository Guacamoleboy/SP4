package App;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;
import java.util.Random;

public class ResetConfirmation {

    // Attributes
    private static final String ourEmail = "sp4@datamatiker-hjaelpen.dk";
    private static final String ourPassword = "Andreaslugter123!";
    private static final Random random = new Random();

    // ________________________________________________________

    public static void sendPassword(String email) {
        int code = generateRandomCode();

        HashMapStorage.saveCode(email, String.valueOf(code));

        String subject = "Password confirmation email from Elevtiden (SP4)";
        String msg = "Your password confirmation code is:\n\n" + code +
        "\n\nCode is valid for 5 minutes";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "send.one.com");
        props.put("mail.smtp.port", "587");

        // Authenticated session
        Session session = Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(ourEmail, ourPassword);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(ourEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject(subject);
            message.setText(msg);

            Transport.send(message);
            System.out.println("Confirmation email sent to " + email);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    // ________________________________________________________

    public static void sendUsername(String email) {
        int code = generateRandomCode();

        HashMapStorage.saveCode(email, String.valueOf(code));

        String subject = "Username reset email from Elevtiden (SP4)";
        String msg = "Your username reset code is:\n\n" + code +
                "\n\nCode is valid for 5 minutes";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "send.one.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(ourEmail, ourPassword);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(ourEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject(subject);
            message.setText(msg);

            Transport.send(message);
            System.out.println("Confirmation email sent to " + email);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    // ________________________________________________________

    private static int generateRandomCode() {
        return 100000 + random.nextInt(900000);
    }

} // Class end