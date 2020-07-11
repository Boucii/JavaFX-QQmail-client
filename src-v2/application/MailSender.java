package application;

import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.BodyPart;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

public class MailSender {
    public String myEmailAccount = "";
    public String myEmailPassword = "";
    public String myEmailSMTPHost = "smtp.qq.com";
    public String receiveMailAccount = "";
    public String subject = "";
    public String content = "";
    MailSender(String myEmailAccount,String myEmailPassword,String myEmailSMTPHost,
    String receiveMailAccount,String subject,String content){
    	this.myEmailAccount=myEmailAccount;
    	this.myEmailPassword=myEmailPassword;
    	this.myEmailSMTPHost=myEmailSMTPHost;
    	this.receiveMailAccount=receiveMailAccount;
    	this.subject=subject;
    	this.content=content;    	
    }
    boolean SendMail(){
    	try {
    	Properties props = new Properties();                    
        props.setProperty("mail.transport.protocol", "smtp");   
        props.setProperty("mail.smtp.host", myEmailSMTPHost);   
        props.setProperty("mail.smtp.auth", "true");            
        //open ssl
        final String smtpPort = "465";
        props.setProperty("mail.smtp.port", smtpPort);
        props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.socketFactory.port", smtpPort);
        Session session = Session.getInstance(props);     
        session.setDebug(true);//open mail debug
        MimeMessage message;	
		message = createMimeMessage(session, myEmailAccount, receiveMailAccount,controller.ListofAttachments,subject,content);
		    
        Transport transport = session.getTransport();//get transport from session
        transport.connect(myEmailAccount, myEmailPassword);
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
        return true;
    	} catch (Exception e) {
			e.printStackTrace();
			return false;
		} 
    }
    public static MimeMessage createMimeMessage(Session session, String sendMail, String receiveMail,ArrayList<String> ListofAttachments,String subject,String content) throws Exception {
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(sendMail, "SMTP客户端", "UTF-8"));
        message.addRecipient(RecipientType.TO, new InternetAddress(receiveMail, "收件人", "UTF-8"));
        message.setSubject(subject, "UTF-8");

   
        MimeBodyPart text = new MimeBodyPart();
        text.setContent(content, "text/plain;charset=UTF-8");
        //创建附件“节点”
        
       // MimeBodyPart attachment = new MimeBodyPart();
        MimeMultipart mm = new MimeMultipart();
        mm.addBodyPart(text);
        
        
			for (int i = 0; i < ListofAttachments.size(); i++) {
				BodyPart attachmentBodyPart = new MimeBodyPart();
				DataSource source = new FileDataSource(ListofAttachments.get(i));
				DataHandler dh = new DataHandler(source);
				attachmentBodyPart.setDataHandler(dh);
				attachmentBodyPart.setFileName(MimeUtility.encodeWord(dh.getName()));
				mm.addBodyPart(attachmentBodyPart);
			}
       
        mm.setSubType("mixed");  
        message.setContent(mm);
        message.setSentDate(new Date());
        message.saveChanges();
 
        return message;   
}}
