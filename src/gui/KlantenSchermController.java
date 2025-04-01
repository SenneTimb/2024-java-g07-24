package gui;


import java.io.ByteArrayInputStream;
import java.io.IOException;

import domain.B2BBestelling;
import domain.Bedrijf;
import domain.Bestelling;
import domain.DomeinController;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Callback;

public class KlantenSchermController extends VBox{

	@FXML
    private VBox bedrijvenscherm;
//    @FXML
//    private TextField zoekenTxt;
    @FXML
    private ListView<Bedrijf> listBedrijven;
    @FXML
    private VBox vboxBedrijf;
    @FXML
    private Text bedrijfNaam;
    @FXML
    private Button backToWelkomBtn;
    @FXML
    private ChoiceBox<String> filterChoiceBox;
    @FXML
    private TextField filterTextField;
    @FXML
    private Label lblNaam;

    private DomeinController dc;
    
    public KlantenSchermController(DomeinController dc) {
		this.dc = dc;
    	
		FXMLLoader loader = new FXMLLoader(this.getClass().getResource("KlantenScherm.fxml"));
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
		
		listBedrijven.setItems(dc.geefBedrijven());

		lblNaam.setText(dc.getGebruiker().getB().getNaam());
		
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
                            setText(item.getNaam());
                        }
                    }
                };

                return cell;
            }
		});
		
		listBedrijven.getSelectionModel().selectedIndexProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue != null) {
                dc.setSelectedBedrijf(newValue.intValue());
                setBedrijf();
            }
        });
    }
    
    @FXML
    private void handleConfirmFilter(ActionEvent event) {
    	String selectedOptie = filterChoiceBox.getValue();
        String filterWaarde = filterTextField.getText();       

        System.out.println("Selected optie: " + selectedOptie);
        System.out.println("Filter waarde: " + filterWaarde);
        //TODO: filter de lijst op basis van de value en de geselecteerde optie
        dc.filter(selectedOptie, filterWaarde, "Bedrijf");
//        ObservableList<Bedrijf> gefilterdeItems = dc.filter(selectedOptie, filterWaarde, "Bedrijf");
//        System.out.println(gefilterdeItems);
//
//        listBedrijven.setItems(gefilterdeItems);
//        listBedrijven.refresh();
    }
    
    @FXML
    void setBedrijf() {
    	Bedrijf bedrijf = dc.getSelectedBedrijf();
    	
    	ImageView logoView = new ImageView();
    	logoView.setPreserveRatio(true);
    	logoView.setFitHeight(80.0);
    	if(bedrijf.getLogo() != null) {
    		logoView.setImage(new Image(new ByteArrayInputStream(bedrijf.getLogo())));
    	}
    	
    	Label lblNaam = new Label(bedrijf.getNaam());
    	lblNaam.setStyle("-fx-font-weight: bold; -fx-font-size: 32px;");
    	
    	Label lblAdres = new Label(String.format("Adres: %s", bedrijf.getAdres()));
    	lblAdres.setStyle("-fx-font-size: 18px;");
    	
    	Label lblBTW = new Label(String.format("BTW-nummer: %s", bedrijf.getBTWnummer()));
    	lblBTW.setStyle("-fx-font-size: 18px;");
    	
    	Label lblGegevens = new Label(String.format("Contactgegevens: %s", bedrijf.getContactgegevens()));
    	lblGegevens.setStyle("-fx-font-size: 18px;");
    	
    	Label lblSector = new Label(String.format("Sector: %s", bedrijf.getSector()));
    	lblSector.setStyle("-fx-font-size: 18px;");
    	
    	Button btnbestellingen = new Button("Zie bestellingen");
    	btnbestellingen.getStyleClass().add("btn_primary");
    	btnbestellingen.setOnAction(evt -> {
    		String bedrijfsNaam = dc.getSelectedBedrijf().getNaam().toString();
        	BestellingenSchermController bestellingScherm = new BestellingenSchermController(dc, bedrijfsNaam);
        	Scene scene = this.getScene();
        	scene.setRoot(bestellingScherm);
    	});
    	
        vboxBedrijf.getChildren().setAll(logoView, lblNaam, lblAdres, lblGegevens, lblBTW, lblSector, btnbestellingen);
    }
	
    
    /*@FXML
    void handleBedrijfButton(ActionEvent event) {
		Bedrijf bedrijf = dc.getSelectedBedrijf();
		BestellingenSchermController bestellingScherm = new BestellingenSchermController(dc);
		Scene scene = this.getScene();
		scene.setRoot(bestellingScherm);
		bestellingScherm.setFilters(bedrijf, "Naam");
    }*/
    
    @FXML
    void handleBackToWelkom(ActionEvent event) {    
    	dc.filter("Naam", "", "Bedrijf");

        WelkomSchermController welkomRoot = new WelkomSchermController(dc);
        Scene scene = this.getScene();
        scene.setRoot(welkomRoot);
    }
    
//    
//    @FXML
//    void zoekenBedrijf(ActionEvent event) {
//    	// TODO
//    }

	
}
