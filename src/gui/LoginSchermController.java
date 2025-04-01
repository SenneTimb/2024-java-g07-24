package gui;

import java.io.IOException;

import domain.AdminController;
import domain.DomeinController;
import domain.LoginController;
import domain.UserDataInvalidException;
import jakarta.persistence.EntityNotFoundException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class LoginSchermController extends GridPane {
	
	private LoginController lc;
	private DomeinController dc;

    @FXML
    private TextField emailTxt;

    @FXML
    private TextField wachtwoordTxt;

    @FXML
    private Button loginBtn;
    
    public LoginSchermController(boolean withInit) {
    	lc = new LoginController(withInit);
    	FXMLLoader loader = new FXMLLoader(this.getClass().getResource("LoginScherm.fxml"));
		 loader.setController(this);
		 loader.setRoot(this);
		 try
		 {
			 loader.load();
		 }
		 catch (IOException e)
		 {
			 throw new RuntimeException(e);
		 }
    }
    
    @FXML
    void BtnLogin_action(ActionEvent event) {
    	try {
			String email = emailTxt.getText();
			String wachtwoord = wachtwoordTxt.getText();
			
			dc = lc.meldAan(email, wachtwoord);
			
			if(dc.isAdmin())
				laadAdminScene();
			else 
				laadLeveranciersScene();
		} catch (IllegalArgumentException e) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Fout bij aanmelden");
			alert.setHeaderText("Er is iets fout gegaan");
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		} catch (EntityNotFoundException | UserDataInvalidException e) {
			// TODO (fout boodschap onder input velden?)
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Fout bij aanmelden");
			alert.setHeaderText("Er is iets fout gegaan");
			alert.setContentText("Email / wachtwoord kloppen niet");
			alert.showAndWait();
		}
    }
       
    private void laadLeveranciersScene() {
    	WelkomSchermController root = new WelkomSchermController(dc);
    	Scene scene = this.getScene();
		scene.setRoot(root);
    }
    
    private void laadAdminScene() {
    	BedrijvenSchermController root = new BedrijvenSchermController(dc);
    	Scene scene = this.getScene();
    	scene.setRoot(root);
    }

}
