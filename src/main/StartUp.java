package main;

import gui.LoginSchermController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class StartUp extends Application {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		LoginSchermController root = new LoginSchermController(true);
//		FXMLLoader root = new FXMLLoader(getClass().getResource("/gui/LoginScherm.fxml"));
//		root.setController(new LoginSchermController());
////		root.setRoot(new LoginSchermController());
		Scene scene = new Scene(root);
		
		primaryStage.setTitle("Leverancier / klant portaal");
		primaryStage.setScene(scene);
	    primaryStage.setMaximized(true);
		primaryStage.show();
	}

}
