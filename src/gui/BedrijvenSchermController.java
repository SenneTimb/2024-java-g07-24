package gui;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import javax.mail.MessagingException;

import domain.AdminController;
import domain.B2BBedrijf;
import domain.Bedrijf;
import domain.Bestelling;
import domain.DomeinController;
import domain.Gebruiker;
import domain.InsertionError;
import dto.BedrijfDto;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Task;

public class BedrijvenSchermController extends VBox {
	
    @FXML
    private VBox bedrijvenscherm;

    @FXML
    private ListView<Bedrijf> listBedrijven;

    @FXML
    private VBox vboxBedrijf;

    @FXML
    private ImageView logoView;

    @FXML
    private TextField naamTxt;

    @FXML
    private TextField sectorTxt;

    @FXML
    private TextField adresTxt;

    @FXML
    private TextField gegevensTxt;

    @FXML
    private TextField btwTxt;

    @FXML
    private Label lblLogo;

    @FXML
    private Button btnActief;

    @FXML
    private Button btnUpdate;
    
    @FXML
    private Button backToLoginBtn;
    
    @FXML
    private ChoiceBox<String> filterChoiceBox;
    @FXML
    private TextField filterTextField;

    private DomeinController dc;
    private byte[] logo;
    private Bedrijf bedrijf;
    private boolean isWorking = false;
    private boolean canceled = false;

    public BedrijvenSchermController(DomeinController domainController) {

        this.dc = domainController;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("BedrijvenScherm.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        listBedrijven.setItems(dc.geefBedrijven());
        
        listBedrijven.setCellFactory(new Callback<ListView<Bedrijf>, ListCell<Bedrijf>>() {
            @Override 
            public ListCell<Bedrijf> call(ListView<Bedrijf> list) {
                ListCell<Bedrijf> cell = new ListCell<Bedrijf>() {
                    @Override
                    public void updateItem(Bedrijf item, boolean empty) {
                        super.updateItem(item, empty);
                        if(item == null || empty) {
                        	setText( null);
                        }
                        else {
                        	Platform.runLater(new Runnable() {
								
								@Override
								public void run() {
		                        	setText(item.getNaam());
		                        	setStyle("-fx-margin: 10px 0px");
								}
							});
                        }
                    }
                };

                return cell;
            }
        }
    );
        
        listBedrijven.getSelectionModel().selectedIndexProperty().addListener((observableValue, oldValue, newValue) -> {
        	
        	if(isWorking) {
        		if(canceled) {
        			canceled = false;
        			return;
        		}
        		if(showIsWorkingWarning() && newValue != null) {
            		dc.setSelectedBedrijf(newValue.intValue());
            		setBedrijf();
            	} else {
//            		isWorking = false;
            		canceled = true;
            		listBedrijven.getSelectionModel().select(oldValue.intValue());
            		
            		//setBedrijf();
            	}
        	} else {
        		if(newValue != null) {
        			dc.setSelectedBedrijf(newValue.intValue());
            		setBedrijf();
        		}
        	}
        	
        });

    }
    
    @FXML
    private void handleConfirmFilter(ActionEvent event) {
    	String selectedOptie = filterChoiceBox.getValue();
        String filterWaarde = filterTextField.getText();       
        
        dc.filter(selectedOptie, filterWaarde, "Bedrijf");
    }
    
    @FXML
    void handleBackToLogin(ActionEvent event) {
    	dc.logOut();
    	dc.filter("Naam", "", "Bedrijf");
    	
        LoginSchermController loginRoot = new LoginSchermController(false);
        Scene scene = this.getScene();
        scene.setRoot(loginRoot);
    }
    
    @FXML
    void setBedrijf() {
    	bedrijf = dc.getSelectedBedrijf();
    	if(bedrijf != null) {
    		naamTxt.setText(bedrijf.getNaam());
    		naamTxt.setDisable(true);
    	  	
        	if(bedrijf.getLogo() != null) {
        		logoView.setImage(new Image(new ByteArrayInputStream(bedrijf.getLogo())));
        	}
        	
        	logo = bedrijf.getLogo();
        	
        	
        	sectorTxt.setText(bedrijf.getSector());
        	sectorTxt.setDisable(true);
        	
        	adresTxt.setText(bedrijf.getAdres());
        	adresTxt.setDisable(true);
        	
        	gegevensTxt.setText(bedrijf.getContactgegevens());
        	gegevensTxt.setDisable(true);
        	
        	btwTxt.setText(bedrijf.getBTWnummer());
        	btwTxt.setDisable(true);
        	
        	btnUpdate.setText("Bijwerken");
        	btnUpdate.setDisable(false);
        	btnUpdate.setOnAction(e -> updateBedrijf(e));
        	
        	if(dc.getSelectedBedrijfGebruikers().size() != 0 ? ((Gebruiker) dc.getSelectedBedrijfGebruikers().get(0)).isActief() : false) {
            	btnActief.setText("Actief");
            	btnActief.getStyleClass().clear();
            	btnActief.getStyleClass().add("lbl_succes");
            	btnActief.setDisable(false);
            	btnActief.setVisible(true);
        	} else {
            	btnActief.setText("Inactief");
            	btnActief.getStyleClass().clear();
            	btnActief.getStyleClass().add("lbl_danger");
            	btnActief.setDisable(false);
            	btnActief.setVisible(true);
            }
        	btnActief.setOnAction(null);
        	isWorking = false;
    	}
    }
    
    @FXML
    void onLogoUpload(ActionEvent event) {
    	final FileChooser fileChooser = new FileChooser();
    	
    	try {
    		File file = fileChooser.showOpenDialog(this.getScene().getWindow());
        	
        	if(file != null) {
        		lblLogo.setText(file.getName());
        		logo = Files.readAllBytes(Paths.get(file.toURI()));
        		logoView.setImage(new Image(new ByteArrayInputStream(logo)));
        	}
		} catch (IOException e) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Fout bij uploaden logo");
			alert.setHeaderText("Er is iets fout gegaan");
			alert.setContentText("Details: " + e.getMessage());
			alert.showAndWait();
		}
    }
    
    @FXML
    void bedrijfAanmaken(ActionEvent event) {
    	
    	if(isWorking) {
    		if(!showIsWorkingWarning()) {
    			return;
    		} else {
    			isWorking = false;
    		}
    	}
    	
    	listBedrijven.getSelectionModel().clearSelection();
    	logo = null;
    	naamTxt.setText("");
    	naamTxt.setDisable(false);
    	
    	sectorTxt.setText("");
    	sectorTxt.setDisable(false);
    	
    	adresTxt.setText("");
    	adresTxt.setDisable(false);
    	
    	gegevensTxt.setText("");
    	gegevensTxt.setDisable(false);
    	
    	btwTxt.setText("");
    	btwTxt.setDisable(false);
    	
    	btnActief.setVisible(false);
    	
    	btnUpdate.setText("Voeg toe");
    	btnUpdate.setDisable(false);
    	btnUpdate.setOnAction(e -> registreerBedrijf(e));
    	
    	isWorking = true;
    }
    
    @FXML
    void updateBedrijf(ActionEvent event) {
    	naamTxt.setDisable(false);
    	
    	sectorTxt.setDisable(false);
    	
    	adresTxt.setDisable(false);
    	
    	gegevensTxt.setDisable(false);
    	
    	btwTxt.setDisable(false);
    	
    	if(dc.getSelectedBedrijfGebruikers().size() != 0 ? ((Gebruiker) dc.getSelectedBedrijfGebruikers().get(0)).isActief() : false) {
        	btnActief.setText("Actief");
        	btnActief.getStyleClass().clear();
        	btnActief.getStyleClass().add("btn_succes");
        	btnActief.setDisable(false);
        	btnActief.setVisible(true);
        	btnActief.setOnAction(e -> {
        		try {
					dc.updateBedrijfGebruikerStatus(false);
					updateBedrijf(e);
				} catch (InsertionError e1) {
					showError("Fout bij gebruikers status veranderen", e1.getMessage());
				}
        	});
    	} else {
        	btnActief.setText("Inactief");
        	btnActief.getStyleClass().clear();
        	btnActief.getStyleClass().add("btn_danger");
        	btnActief.setDisable(false);
        	btnActief.setVisible(true);
        	btnActief.setOnAction(e -> {
        		try {
					dc.updateBedrijfGebruikerStatus(true);
					updateBedrijf(e);
				} catch (InsertionError e1) {
					showError("Fout bij gebruikers status veranderen", e1.getMessage());
				}
        	});
        }
    	
    	btnUpdate.setText("Bevestig");
    	btnUpdate.setOnAction(e -> werkBedrijfBij(e));
    	
    	isWorking = true;
    }
    
    private void registreerBedrijf(ActionEvent event) {
    	btnUpdate.setDisable(true);
    	
    	String naam = naamTxt.getText(), sector = sectorTxt.getText(), adres = adresTxt.getText(), gegevens = gegevensTxt.getText(), btwNummer = btwTxt.getText();
    	    	
    	Task<Void> domeinTask = new Task<Void>() {
    		@Override
    		protected Void call() throws Exception {
    			BedrijfDto bedrijf = new BedrijfDto(naam, logo, sector, adres, gegevens, btwNummer);
				dc.maakBedrijf(bedrijf);
				return null;
    		}
    	};
    	
    	domeinTask.setOnFailed(evt -> {
    		btnUpdate.setDisable(false);
    		if(domeinTask.getException() instanceof InsertionError || domeinTask.getException() instanceof MessagingException) {
    			
    			showError("Fout bij aanmaken", domeinTask.getException().getMessage());
    			
    		} else if(domeinTask.getException() instanceof IllegalArgumentException) {
    			Alert alert = new Alert(AlertType.WARNING);
    			alert.setTitle("Fout bij aanmelden");
    			alert.setHeaderText("Er is iets fout gegaan");
    			alert.setContentText(domeinTask.getException().getMessage());
    			alert.showAndWait();
    		}
    	});
    	
    	domeinTask.setOnSucceeded(evt -> {
    		// TODO update view / clear inputs
    		naamTxt.setText("");
        	naamTxt.setDisable(true);
        	
        	sectorTxt.setText("");
        	sectorTxt.setDisable(true);
        	
        	adresTxt.setText("");
        	adresTxt.setDisable(true);
        	
        	gegevensTxt.setText("");
        	gegevensTxt.setDisable(true);
        	
        	btwTxt.setText("");
        	btwTxt.setDisable(true);
        	
        	lblLogo.setText("Verander logo");
        	
        	btnActief.setVisible(true);
        	
        	btnUpdate.setText("Bijwerken");
        	btnUpdate.setDisable(true);
        	btnUpdate.setOnAction(e -> updateBedrijf(e));
        	
        	isWorking = false;
    	});
    	
    	new Thread(domeinTask).start();
    }
    
    private void werkBedrijfBij(ActionEvent event) {
    	btnUpdate.setDisable(true);
    	
    	String naam = naamTxt.getText(), sector = sectorTxt.getText(), adres = adresTxt.getText(), gegevens = gegevensTxt.getText(), btwNummer = btwTxt.getText();
    	
    	Task<Void> domeinTask = new Task<Void>() {
    		@Override
    		protected Void call() throws Exception {
    			BedrijfDto bedrijf = new BedrijfDto(naam, logo, sector, adres, gegevens, btwNummer);
    			dc.updateBedrijf(bedrijf);
				return null;
    		}
    	};
    	
    	domeinTask.setOnFailed(evt -> {
    		btnUpdate.setDisable(false);
    		if(domeinTask.getException() instanceof InsertionError) {
    			showError("Fout bij updaten" ,domeinTask.getException().getMessage());
    		} else if(domeinTask.getException() instanceof IllegalArgumentException) {
    			Alert alert = new Alert(AlertType.WARNING);
    			alert.setTitle("Fout bij updaten");
    			alert.setHeaderText("Er is iets fout gegaan");
    			alert.setContentText(domeinTask.getException().getMessage());
    			alert.showAndWait();
    		}
    	});
    	
    	domeinTask.setOnSucceeded(evt -> {
    		// TODO update view & clear or update inputs
    		naamTxt.clear();
    		sectorTxt.clear();
    		adresTxt.clear();
    		gegevensTxt.clear();
    		btwTxt.clear();
    		
        	naamTxt.setDisable(true);
        	sectorTxt.setDisable(true);
        	adresTxt.setDisable(true);
        	gegevensTxt.setDisable(true);
        	btwTxt.setDisable(true);
        	btnActief.setVisible(true);
        	btnUpdate.setDisable(true);
        	lblLogo.setText("Verander logo");
        	
        	isWorking = false;
    	});
    	
    	new Thread(domeinTask).start();
    };
    
    @FXML
    void deactiveerBedrijf(ActionEvent event) {
    	// TODO deactiveer accounts
    	try {
    		if(bedrijf.getGebruikers().size() != 0) {    			
    			// TODO aanpassen controller & update view
    			//dc.updateBedrijfGebruikerStatus(b, !b.getGebruikers().get(0).isActief());
    		} else {
    			Alert alert = new Alert(AlertType.WARNING);
    			alert.setTitle("Fout bij updaten");
    			alert.setHeaderText("Er klopt iets niet");
    			alert.setContentText("Bedrijf heeft geen gebruikers");
    			alert.showAndWait();
    		}
    		
		} catch (Exception e) {
			showError("Fout bij updaten", e.getMessage());
		}
    }
    
    void showError(String title, String content) {
    	Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText("Er is iets fout gegaan");
		alert.setContentText(content);
		alert.showAndWait();
    }
    
    boolean showIsWorkingWarning() {
    	Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Werk verloren");
		alert.setHeaderText("Pas op! Uw werk zal kwijt zijn.");
		alert.setContentText("Bent u zeker dat u uw werk wilt stoppen?");
		alert.showAndWait();
		
		if (alert.getResult() == ButtonType.OK) {
		    return true;
		}
		return false;
    }
}

