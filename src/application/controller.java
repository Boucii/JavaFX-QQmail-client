package application;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeUtility;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

public class controller {
	@FXML
	private Button QuitButton;
	@FXML
	private Button SendButton;
	@FXML
	private Button BoxButton;
	@FXML
	private Button SettingButton;
	@FXML
	private Button AddAttachButton;
	@FXML
	private Button SendMailButton;
	@FXML
	private Button SaveInfo;
	@FXML
	private Button OpenWeb;
	@FXML
	private Button CloseWeb;
	@FXML
	private AnchorPane SendPane;
	@FXML
	private AnchorPane BoxPane;
	@FXML
	private AnchorPane SettingPane;
	@FXML
	private ListView AttachList;
	@FXML
	private ListView ChooseMail;
	@FXML
	private TextArea EditField;
	@FXML 
	private TextArea MailDisplay;
	@FXML
	private TextField AddrBox;
	@FXML
	private TextField ThemeBox;
	@FXML
	private TextField Account;
	@FXML
	private PasswordField Pwd;
	@FXML
	private WebView web;
	@FXML
	public ProgressIndicator loading;
	
	
	static int j=0;
	String account="";
	String pwd="";
	static Folder folder = null; 
	static ArrayList<String> ListofAttachments = new ArrayList<String>();//paths of attachs
	ObservableList<String> list = FXCollections.observableArrayList();//paths of attachs,for listview
	static ObservableList<String> Maillist = FXCollections.observableArrayList();//mails in the inbox
	
	@FXML 
	 public void initialize(){
		//initialize listview with text on its button
		this.AttachList.setCellFactory(param -> new ButtonList("DELETE"));
		this.ChooseMail.setCellFactory(param -> new MailBList("OPEN"));
		
		
	} 
	
	public TextArea GetMailDisplay() {
		return this.MailDisplay;
	}
	
	public void QuitApp() {
		System.exit(0);
	}
	//open send page
	public void OpenSend() {
		BoxPane.setDisable(true);
		BoxPane.setVisible(false);
		SettingPane.setDisable(true);
		SettingPane.setVisible(false);
		
		SendPane.setDisable(false);
		SendPane.setVisible(true);
		
	}
	//open inboxpage
	public void OpenBox() {
		//this.loading.setVisible(true);
		//this.loading.setDisable(false);
		
		SendPane.setDisable(true);
		SendPane.setVisible(false);
		SettingPane.setDisable(true);
		SettingPane.setVisible(false);
		
		BoxPane.setDisable(false);
		BoxPane.setVisible(true);
		
		if(account.length()+pwd.length()<2) {
			//this.loading.setVisible(false);
			//this.loading.setDisable(true);
			this.MailDisplay.setText("Please fill in your infos");
		}else {
			this.MailDisplay.clear();
			LoadMails(account,pwd);
			//this.loading.setVisible(false);
			//this.loading.setDisable(true);
		}
	}
	//load mails to listview
	public void LoadMails(String account,String pwd) {
		 try {  
	        	String host = "pop.qq.com";
	        	String username=account;
	        	String password=pwd;
	            Properties props = new Properties();
	            props.setProperty("mail.pop3.host", "pop.qq.com"); //pop3 protocol
	            props.setProperty("mail.pop3.port", "995");
	            // SSL安全连接参数
	            props.setProperty("mail.pop3.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
	            props.setProperty("mail.pop3.socketFactory.fallback", "true");
	            props.setProperty("mail.pop3.socketFactory.port", "995");

	            Session session = Session.getInstance(props);  
	              
	            Store store = (Store) session.getStore("pop3");  
	            store.connect(username,password);  
	            folder =  (Folder) store.getFolder("INBOX");  
	            if (folder.exists())  {
	            	folder.open(Folder.READ_ONLY);  
	            	}
	            Message[] messages = folder.getMessages(); 
	            int i=messages.length;
	            if(messages!=null&&messages.length>0){  
	                for (j=0;j<i;j++) {  
	                	Message message = messages[j];
	                	System.out.println("j="+j);
	                	
	                	
	                	boolean flag=message.getSubject().length()>30?true:false;
	                	String date = new SimpleDateFormat("yyyy-MM-dd").format(message.getSentDate());//邮件的接受时间
	                	String sub="";
	                	if (flag==true) {
	                		sub=message.getSubject().substring(0, 30);
	                		sub=sub+"...";
	                	}else {
	                		sub=message.getSubject();
	                	}
	                	
	                	//add index to the text,when open the mail ,locate the mail with the index
	                	String num=String.valueOf(j);      	
	                	Maillist.add(num+'.'+date+'\n'+sub+"\n________________");
	                	
	                }  ChooseMail.setItems(Maillist);
	            }  
	        } catch (Exception e) {  
	        	this.MailDisplay.setText("Fetch Mails Failed");
	            e.printStackTrace();  
	        }  
	}
	//open settings index
	public void OpenSetting() {
		SendPane.setDisable(true);
		SendPane.setVisible(false);
		BoxPane.setDisable(true);
		BoxPane.setVisible(false);
		
		SettingPane.setDisable(false);
		SettingPane.setVisible(true);
    }
	//send mail button
	public boolean SendMailAct() {
		if(account.isEmpty()||pwd.isEmpty()||this.AddrBox.getText().length()==0) {
			this.ThemeBox.setText("Please fill in the infos");
			return false;
		}
		MailSender mail=new MailSender(this.account,this.pwd,"smtp.qq.com",
				this.AddrBox.getText(),this.ThemeBox.getText(),this.EditField.getText());
		try{
			boolean flag=mail.SendMail();
			if(flag) {
				this.EditField.setText("Send Successful");
				this.AddrBox.clear();
				this.ThemeBox.clear();
				controller.ListofAttachments.clear();
				this.list.clear();
			}
			return flag==true?true:false;
			}
		catch(Exception e) {
			this.ThemeBox.setText("Send Failed");
			return false;
		}
	}
	//add attachment button
	public boolean AddAttachAct() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Choose an attachment");
		File file =  fileChooser.showOpenDialog(new Stage());
		 if(file == null){
			 return false;
			 }
		 else {
			 ListofAttachments.add(file.getAbsolutePath());
			 list.add(file.getName());
			 
			 for (String i : ListofAttachments) {
				 System.out.println(i);
			 }
			 
			 AttachList.setItems(list);
			 return true;
		 }
	}
	//save pwd and account
	public boolean Save() {
		account=Account.getText();
		pwd=Pwd.getText();
		if(account.length()+pwd.length()==0) {
			Account.setPromptText("Please Fill in your Account");
			Pwd.setPromptText("Please Fill in your Password");
			return false;
		}
		else if(account.length()==0) {
			Account.setPromptText("Please Fill in your Account");
			return false;
		}
		else if(pwd.length()==0) {
			Pwd.setPromptText("Please Fill in your Password");
			return false;
		}
		else {
			Account.setPromptText(account);
			Pwd.clear();
			Pwd.setPromptText("Registed Successfully");
			return true;
		}
	}
	
	public void appendMsg(String msg) {
		this.MailDisplay.appendText(msg);
	}
	public void clearMsg() {
		this.MailDisplay.clear();
	}
	//open html mail with browser
	public void OpenBrowser() {
		String msg=this.MailDisplay.getText();
		if(msg.length()==0||msg=="Please select mail first") {
			this.MailDisplay.setText("Please select mail first");
			return;
		}else {
			this.CloseWeb.setDisable(false);
			this.CloseWeb.setVisible(true);
			this.web.setDisable(false);
			this.web.setVisible(true);
			//get the html part from mail
			int n=msg.indexOf("<!DOCTYPE html>");
			int m=msg.indexOf("<html>");
			if(n>0) {
				msg=msg.substring(n, msg.length());
			}else if(n<=0&&m>0) {
				msg=msg.substring(m, msg.length());
			}
			WebEngine ene=this.web.getEngine();
			ene.loadContent(msg);
			return;
		}
	}
	public void CloseBrowser() {
		this.CloseWeb.setDisable(true);
		this.CloseWeb.setVisible(false);
		this.web.setDisable(true);
		this.web.setVisible(false);
	}
}
