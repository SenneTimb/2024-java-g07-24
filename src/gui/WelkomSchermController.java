package gui;

import java.io.IOException;
import domain.DomeinController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

public class WelkomSchermController extends GridPane {

    private DomeinController dc;
    
    @FXML
    private Button backToLoginBtn;

   
    @FXML
    private void handleBestellingen(ActionEvent event) {
    	BestellingenSchermController root = new BestellingenSchermController(dc);
    	Scene scene = this.getScene();
		scene.setRoot(root);
    }



    @FXML
    private void handleKlanten(ActionEvent event) {
    	KlantenSchermController root = new KlantenSchermController(dc);
    	Scene scene = this.getScene();
		scene.setRoot(root);
    }
    
    @FXML
    void handleBackToLogin(ActionEvent event) {
    	dc.logOut();
    	
        LoginSchermController loginRoot = new LoginSchermController(false);
        Scene scene = this.getScene();
        scene.setRoot(loginRoot);
    }

    public WelkomSchermController(DomeinController dc) {
        this.dc = dc;

        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("WelkomScherm.fxml"));
        loader.setController(this);
        loader.setRoot(this);
        try {
            loader.load();
          
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
