package application;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

//Subclass of lis tview cell for "add attachment",similar to maiblist class
public class ButtonList extends ListCell<String>{
	HBox hbox = new HBox();
    Label label = new Label("(empty)");
    Pane pane = new Pane();
    Button button = new Button();
    String lastItem;
    
    
    
    public ButtonList(String msg) {
        super();
        //add css to cell and its components
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
        button.setOnAction(event -> deleteAttachment());
      
    }
    private void deleteAttachment() {
    	String path=this.label.getText();//path of the attachment
    	getListView().getItems().remove(getItem());   	
    	for (int i=0;i<controller.ListofAttachments.size();i++) {
    		if(controller.ListofAttachments.get(i).endsWith(path)) {
    			controller.ListofAttachments.remove(i);//delete attachment
    			break;
    		}
    	}
    	
    	for (String i :controller.ListofAttachments) {
			 System.out.println(i);
		 }
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
}
