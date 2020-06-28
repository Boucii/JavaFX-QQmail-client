# JavaFX-QQmail-client
an qq mail client with inbox and sending main using javamail

QQ邮箱客户端，javamail以及javafx实现

this is a simple qqmail client using javafx and javamail .
the protocol i used are smtp and pop3

**Comments are written in english**
**注释是用英语写的**

there are totally 3 pages, which are inbox,send mail,settings
the src structure is : 

assets:
- icons 

application: 

- buttonlist.java 

subclass of listview cell,for all the attachment you added
- mailblist.java

subclass of listview cell, for list all the mails in the inbox
- mailsender.java

class to send mail
- main.java

main class to load ui
- mainui.fxml

ui file
- application.css

css file
- controller.java

controller of the ui ,core datas  , controls mailbox


the outcome is like below:


![](https://github.com/Boucii/JavaFX-QQmail-client/raw/master/image/settings.png)
![](https://github.com/Boucii/JavaFX-QQmail-client/raw/master/image/inbox.png)
![](https://github.com/Boucii/JavaFX-QQmail-client/raw/master/image/inbox2.png)
![](https://github.com/Boucii/JavaFX-QQmail-client/raw/master/image/sendmail.png)
