package application;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.text.DateFormat;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.internet.MimeUtility;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

//subclass of listcell of listview,for loading the list of mail in the inbox
public class MailBList extends ListCell<String>{
	HBox hbox = new HBox();
    Label label = new Label("(empty)");
    Pane pane = new Pane();
    Button button = new Button();
    String lastItem;
    static controller Control;
    
    
    public MailBList(String msg) {
        super();      
        this.button.setText(msg);
        String css = this.getClass().getResource("application.css")
                .toExternalForm();
        
        this.getStylesheets().add(css);
        this.getStyleClass().add("buttonlist");
        this.label.getStylesheets().add(css);
        this.label.getStyleClass().add("buttonlist");
        this.pane.setOpacity(0);
        this.button.getStylesheets().add(css);
        this.button.getStyleClass().add("add-attach");
        //this.button.setMaxSize(50, 30);
        
        hbox.getChildren().addAll(label, pane, button);
        HBox.setHgrow(pane, Priority.ALWAYS);
        button.setOnAction(event -> OpenMail());
    }
    private void OpenMail() {
    	Control.clearMsg();
    	Control.CloseBrowser();
    	get(controller.folder); 	
    }
    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        setText(null);  // No text in label of super class
        if (empty) {
            lastItem = null;
            setGraphic(null);
        } else {
            lastItem = item;
            label.setText(item!=null ? item : "<null>");
            setGraphic(hbox);
        }
    }
    public void get(Folder folder) {
    	try {
    		int msgnum=0;
    		int comma=this.label.getText().indexOf('.');//fetch the mail through its index
    		String num=this.label.getText().substring(0,comma);
    		System.out.println(num);
    		msgnum=Integer.parseInt(num);
    		Message msg = folder.getMessage(msgnum+1);
					
            String from = msg.getFrom()[0].toString();
            String subject = msg.getSubject();
            String sendDate = DateFormat.getInstance().format(msg.getSentDate());
                    
            Control.appendMsg("邮件主题：" + subject + "\n");
            Control.appendMsg("发件人:" + from + "\n");
          
            System.out.println("contentType：" + msg.getContentType());

            if (msg.isMimeType("multipart/*")) {
                Multipart mp = (Multipart) msg.getContent();

                for (int i = 0; i < mp.getCount(); i++) {
                    BodyPart bp = mp.getBodyPart(i);

                    if (bp.getDisposition() != null) {
                        String filename = bp.getFileName();
                        System.out.println("filename：" + filename);

                        if (filename.startsWith("=?")) {
                            filename = MimeUtility.decodeText(filename);
                        }

                        Control.appendMsg(filename);                       
  
                    }
                }
            }
      
            if (!msg.isMimeType("multipart/*")) {
            	Control.appendMsg((String)(msg.getContent())); 
            } else {
            	boolean flag=controller.GetAttachStatus();
                Multipart mp = (Multipart) msg.getContent();
                int bodynum = mp.getCount();
                for (int i = 0; i < bodynum; i++) {
                    BodyPart bp = mp.getBodyPart(i);
                    if (bp.isMimeType("application/*")) {     
                        String disposition = bp.getDisposition();     
                        System.out.println(disposition);     
                        if (disposition.equalsIgnoreCase(BodyPart.ATTACHMENT)&&flag) {     
                            String fileName = bp.getFileName();     
                            InputStream is = bp.getInputStream();  
                            File AttachPath=controller.GetAttachPath();
                            copy(is, new FileOutputStream(AttachPath+fileName));  //attachment path   
                        }     
                    }     
                    
                    else if (!bp.isMimeType("multipart/mixed") && bp.getDisposition() == null) {
                    	Control.appendMsg((String)(bp.getContent())); 
                    }
                    

                }
            }
        } catch (Exception e) {
        	Control.appendMsg("Failed to open"); 
            e.printStackTrace();
        }
				
		}
    public static void copy(InputStream is, OutputStream os) throws IOException {     
        byte[] bytes = new byte[1024];     
        int len = 0;     
        while ((len=is.read(bytes)) != -1 ) {     
            os.write(bytes, 0, len);     
        }     
        if (os != null)     
            os.close();     
        if (is != null)     
            is.close();     
    } 

}
