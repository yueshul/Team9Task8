package mf.controller;
import java.util.Date;  
import java.util.Properties;  
import javax.mail.Authenticator;  
import javax.mail.Multipart;  
import javax.mail.internet.InternetAddress;  
import javax.mail.internet.MimeBodyPart;  
import javax.mail.internet.MimeMultipart;  
import javax.mail.PasswordAuthentication;
import javax.mail.Transport;  
  
  
/** 
 * 利用java.mail的邮件发送程序 
 */  
  
public class EmployeeSendEmail {  
    public static void sendResetPasswordEmail(String newpassword,String email) {
        String title = "New password for Carnegie Financial Service";
        String from = "futuregong@126.com";
        String sendTo[] = {email};
        String content = "Your new password for Carnegie Financial Mutual Fund Service is: "
                +newpassword+"  Please keep it safe.Best Regards";
        
        try {  
            System.out.println("ready to send mail");
            sendmail(title, from, sendTo, content, 
                    "text/html;charset=UTF-8");  
        } catch (Exception ex) {  
            ex.printStackTrace();  
        }  
    }
  
    public static void sendmail(String subject, String from, String[] to,  
            String text, String mimeType) throws Exception {  
        Properties props = new Properties();    
        String smtp = "smtp.126.com";
        String servername = "futuregong@126.com";  
        String serverpaswd = "Alishiqi9920";  
  
        javax.mail.Session mailSession;
        javax.mail.internet.MimeMessage mimeMsg;
  
        props = java.lang.System.getProperties();
        props.put("mail.smtp.host", smtp);
        props.put("mail.smtp.auth", "true");
        Email_Autherticatorbean myEmailAuther = new Email_Autherticatorbean(  
                servername, serverpaswd);  
        mailSession = javax.mail.Session.getInstance(props,  
                (Authenticator) myEmailAuther);  
        mailSession.setDebug(true);  
        javax.mail.Transport transport = mailSession.getTransport("smtp");  
        mimeMsg = new javax.mail.internet.MimeMessage(mailSession);  
        if (!from.isEmpty()) {  
  
            InternetAddress sentFrom = new InternetAddress(from);  
            mimeMsg.setFrom(sentFrom); 
        }  
  
        InternetAddress[] sendTo = new InternetAddress[to.length];  
        for (int i = 0; i < to.length; i++) {  
            sendTo[i] = new InternetAddress(to[i]);  
        }  
  
        mimeMsg.setRecipients(javax.mail.internet.MimeMessage.RecipientType.TO,  
                sendTo);  
        mimeMsg.setSubject(subject, "gb2312");  
  
        MimeBodyPart messageBodyPart1 = new MimeBodyPart();  
        messageBodyPart1.setContent(text, mimeType);  
  
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart1);  
        mimeMsg.setContent(multipart);  
        mimeMsg.setSentDate(new Date());  
        mimeMsg.saveChanges();  
        Transport.send(mimeMsg);  
        transport.close();  
    }  
  
}
class Email_Autherticatorbean extends Authenticator {  
    private String m_username = null;  
    private String m_userpass = null;    
    public void setUsername(String username) {  
        m_username = username;  
    }   
    public void setUserpass(String userpass) {  
        m_userpass = userpass;  
    }    
    public Email_Autherticatorbean(String username, String userpass) {  
        super();  
        setUsername(username);  
        setUserpass(userpass);    
    }   
    public PasswordAuthentication getPasswordAuthentication() {    
        return new PasswordAuthentication(m_username, m_userpass);  
    }  
}  