package gui;

import domain.DomeinController;
import domain.Filter;
import domain.InsertionError;
import domain.Orderstatus;
import domain.Product;
import dto.BedrijfDto;
import dto.BestellingDto;
import domain.Bedrijf;
import domain.Bestelling;
import domain.BestellingProduct;
import domain.Betalingsstatus;
import domain.Betalingsherinnering;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Callback;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

public class BestellingenSchermController<E> extends VBox {
    
	
	
	private Betalingsstatus[] betalingsStatusen = {Betalingsstatus.BETAALD, Betalingsstatus.FACTUURVERZONDEN, Betalingsstatus.ONVERWERKT}; // Initieel niet betaald
	
    private final Orderstatus[] orderStatussen = {Orderstatus.GEPLAATST, Orderstatus.VERWERKT, Orderstatus.VERZONDEN, Orderstatus.GELEVERD, Orderstatus.VOLTOOID, Orderstatus.UITVOORLEVERING}; // De statussen
	
    
    @FXML
    private Button betalingsStatusKnop;
    @FXML
    private Button orderStatusKnop; 
    @FXML 
    private Button stuurBetalingsherinneringButton;
	@FXML
    private VBox bestellingenScherm;
    @FXML
    private TableView<Bestelling> listBestellingen;
    @FXML
    private TableView<BestellingProduct> listProducten;
    @FXML
    private VBox vboxBestelling;
    @FXML
    private Text bestellingDetails;
    @FXML
    private Button backToWelkomBtn;
    @FXML
    private ChoiceBox<String> filterChoiceBox;
    @FXML
    private TextField filterTextField;
    @FXML
    private ChoiceBox<Orderstatus> choiceBoxOrderStatus = new ChoiceBox<>();
    
    @FXML
    private ChoiceBox<Betalingsstatus> choiceBoxBetalingsStatus = new ChoiceBox<>();
    
    @FXML
    private TableColumn<Bestelling, String> bestellingIDColumn;

    @FXML
    private TableColumn<Bestelling, String> klantColumn;

    @FXML
    private TableColumn<Bestelling, String> datumColumn;

    @FXML
    private TableColumn<Bestelling, String> statusColumn;

    @FXML
    private TableColumn<Bestelling, String> betalingColumn;
    
    @FXML
    private TableColumn<BestellingProduct, String> naamProductColom;
    
    @FXML
    private TableColumn<BestellingProduct, String> aantalProductColom;
    
    @FXML
    private TableColumn<BestellingProduct, String> stockProductColom;
    
    @FXML
    private TableColumn<BestellingProduct, String> prijsProductColom;
    
    @FXML
    private TableColumn<BestellingProduct, String> aantalBesteldCol;
    
    @FXML
    private Label lblNaam;

    private DomeinController dc;
    
    public BestellingenSchermController(DomeinController dc) {
        this.dc = dc;
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("BestellingScherm.fxml"));
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
        // Voorbeeld om bestellingen te tonen, je moet deze methode implementeren in je DomeinController
		 
		 lblNaam.setText(dc.getGebruiker().getB().getNaam());
		 listBestellingen.setItems(dc.geefBestellingenByLeverancier());
		 
		 bestellingIDColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.format("%d", cellData.getValue().getOrderId())));
		 klantColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getKlant().getNaam()));
		 datumColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDatumGeplaatst().toString()));
		 statusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOrderStatus().toString()));
		 betalingColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBetalingsstatus() == Betalingsstatus.BETAALD ? "Betaald" : (cellData.getValue().getBetalingsstatus() == Betalingsstatus.ONVERWERKT ? "Niet Betaald" : "Factuur Verzonden")));
		 
		 listBestellingen.getSelectionModel().selectedIndexProperty().addListener((observableValue, oldValue, newValue) -> {
			 if(newValue != null) {
         		dc.setSelectedBestelling(newValue.intValue());
         		setBestelling();
         	}
        });
        
        listProducten.setVisible(false);
		stuurAutomatischeBetalingsherinnering();
    }
    
    public BestellingenSchermController(DomeinController dc, String klantNaam) {
    	this(dc);
    	setFilters(klantNaam, "Klant");
    }

    @FXML
    private void handleConfirmFilter(ActionEvent event) {
        String selectedOptie = filterChoiceBox.getValue();
        String filterWaarde = filterTextField.getText();       

        //TODO: filter de lijst op basis van de value en de geselecteerde optie
        dc.filter(selectedOptie, filterWaarde, "Bestelling");
//        ObservableList<Bestelling> gefilterdeItems = dc.filter(selectedOptie, filterWaarde, "Bestelling");
//        System.out.println(gefilterdeItems);

//        listBestellingen.setItems(gefilterdeItems);
//        listBestellingen.refresh();
    }
    
    @FXML
    void handleBackToWelkom(ActionEvent event) {    	
    	dc.filter("Klant", "", "Bestelling");

        WelkomSchermController welkomRoot = new WelkomSchermController(dc);
        Scene scene = this.getScene();
        scene.setRoot(welkomRoot);
    }
    
    @FXML
    void setBestelling() {
        Bestelling bestelling = dc.getSelectedBestelling();
        Bedrijf klant = bestelling.getKlant();
        Betalingsstatus betaalStatus = bestelling.getBetalingsstatus();
        Orderstatus orderStatus = bestelling.getOrderStatus();
        List<Betalingsherinnering> betalingsherrineringen = dc.getBetalingsherrineringenByBestelling();
        
        Label lblKlantNaam = new Label("Klant: " + klant.getNaam().toString());
        lblKlantNaam.setFont(Font.font("System", FontWeight.LIGHT, 12.0));
        
        Label lblContactgegevens = new Label("Contactgegevens: " + klant.getContactgegevens().toString());
        lblContactgegevens.setFont(Font.font("System", FontWeight.LIGHT, 12.0));
        
        Label lblOrderId = new Label(String.valueOf("ID: " + bestelling.getOrderId()));
        lblOrderId.setFont(Font.font("System", FontWeight.LIGHT, 12.0));
        
        Label lblDatum = new Label("Bestellings Datum: " + bestelling.getDatumGeplaatst().toString());
        lblDatum.setStyle("-fx-font-weight: bold; -fx-font-size: 18px;");
        
        Label lblLeverAdres = new Label("Leverandres: " + bestelling.getLeverAdres());
        lblLeverAdres.setFont(Font.font("System", FontWeight.NORMAL, 12.0));
        
        Label lblOrderBedrag = new Label(String.format("Totaalprijs bestelling: €%.2f", bestelling.getTotaleOrderBedrag()));
        lblOrderBedrag.setPadding(new Insets(10, 0, 10, 0));
        lblOrderBedrag.setFont(Font.font("System", FontWeight.EXTRA_BOLD, 12.0));
        
        Label lblBetalingsDeadline = new Label("Bestelling te betalen tegen: " + String.valueOf(bestelling.getBetalingsDeadline()));
        lblBetalingsDeadline.setFont(Font.font("System", FontWeight.NORMAL, 12.0));

        VBox products = new VBox();
        products.setAlignment(Pos.CENTER);
        
        ObservableList<BestellingProduct> producten = FXCollections.observableArrayList(bestelling.getProducten());
        listProducten.setPrefHeight(producten.size() * 40 + 40);
        listProducten.setItems(producten);
        listProducten.setVisible(true);
        
        naamProductColom.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProduct().getNaam()));
        aantalProductColom.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getProduct().getAantal())));
        stockProductColom.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProduct().isInStock() ? "Ja" : "Nee"));
        prijsProductColom.setCellValueFactory(cellData -> new SimpleStringProperty(String.format("€%.2f", (cellData.getValue().getEenheidsprijs() * cellData.getValue().getAantal()))));
        aantalBesteldCol.setCellValueFactory(cellData -> new SimpleStringProperty(String.format("%d", cellData.getValue().getAantal())));
        products.getChildren().add(listProducten);
        
        /*
         * 		 bestellingIDColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.format("%d", cellData.getValue().getOrderId())));
		 klantColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getKlant().getNaam()));
		 datumColumn.setCellValueFactory(cellData -> new SimpleStringProperty(formatter.format(cellData.getValue().getDatumGeplaatst())));
		 statusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOrderStatus().toString()));
		 betalingColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBetalingsstatus() == Betalingsstatus.BETAALD ? "Betaald" : "Niet Betaald"));
		 
         * for(Product product : bestelling.getProducten()) {
            String productInfo = String.format("Product: %s, Aantal: %d, In Stock: %s, Eenheidsprijs: €%.2f", 
                                               product.getNaam(), product.getAantal(), 
                                               product.isInStock() ? "Ja" : "Nee", 
                                               product.getEenheidsprijs());
            Label lblProductInfo = new Label(productInfo);
            lblProductInfo.setFont(Font.font("System", FontWeight.NORMAL, 12.0));
            products.getChildren().add(lblProductInfo);
        }*/
        
        VBox herrineringen = new VBox();        

		if (betalingsherrineringen.size() != 0) {
			Label lblSubtitle = new Label("Betalingsherinneringen: ");
	        herrineringen = new VBox(lblSubtitle);        
	        for (Betalingsherinnering herrinering : betalingsherrineringen) {
	        	Label lblDatum1 = new Label(herrinering.getDatum().toString());
	        	herrineringen.getChildren().add(lblDatum1);
	        }
		}
        
        
        choiceBoxOrderStatus.getItems().setAll(orderStatussen);
        choiceBoxOrderStatus.setValue(orderStatus);
        
        choiceBoxBetalingsStatus.getItems().setAll(betalingsStatusen);
        choiceBoxBetalingsStatus.setValue(betaalStatus);
        
        Label lblBetalingStatus = new Label(" Betalings status: ");
        lblBetalingStatus.setPadding(new Insets(0, 0, 0, 10));
        Label lblOrderStatus = new Label(" Order status: ");
        
        /*
        Button knopOrderStatus = new Button(orderStatus.toString());
        knopOrderStatus.getStyleClass().add("btn_primary");
        knopOrderStatus.setOnAction(event ->{
        	volgendeOrderStatus(knopOrderStatus);
        });
        */
        
        HBox hboxButtons = new HBox(lblOrderStatus, choiceBoxOrderStatus, lblBetalingStatus, choiceBoxBetalingsStatus);
        hboxButtons.setAlignment(Pos.CENTER);
        ;
        
        Button wijzigingenOpslaan = new Button("Wijzigingen Opslaan");
        wijzigingenOpslaan.getStyleClass().add("btn_primary");
        wijzigingenOpslaan.setOnAction(event ->{
        	try {
				opslaan(wijzigingenOpslaan, bestelling);
			} catch (InsertionError e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        });
        HBox hboxOpslaan = new HBox(wijzigingenOpslaan);
        hboxOpslaan.setAlignment(Pos.CENTER);
        hboxOpslaan.setPadding(new Insets(50, 0, 50, 0));
        
        stuurBetalingsherinneringButton = new Button("Stuur betalingsherinnering");
        stuurBetalingsherinneringButton.setOnAction(event -> {
        	try {
				stuurBetalingsherinnering();
				// versturen is gelukt
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
		        alert.setTitle("Betalingsherinnering Verstuurd");
		        alert.setHeaderText(null); // Geen header tekst
		        alert.setContentText("Er is een betalingsherinnering verstuurd naar " + dc.getSelectedBestelling().getKlant().getContactgegevens() + ". Uw klant zal binnenkort een mail ontvangen.");
		        alert.showAndWait();
			} catch (InsertionError e) {
				
				// versturen is mislukt
				e.printStackTrace();
				Alert errorAlert = new Alert(Alert.AlertType.ERROR);
		        errorAlert.setTitle("Fout bij Versturen");
		        errorAlert.setHeaderText("Het versturen van de betalingsherinnering is mislukt.");
		        errorAlert.setContentText("Controleer de details van de bestelling en probeer het opnieuw.");
		        errorAlert.showAndWait();
			}
        });
        
        hboxOpslaan.getChildren().add(stuurBetalingsherinneringButton);
        
        // Voeg de bestaande labels toe aan de VBox
        vboxBestelling.getChildren().setAll(lblDatum, lblKlantNaam, lblContactgegevens, lblLeverAdres, lblOrderId, lblBetalingsDeadline, products, lblOrderBedrag, herrineringen, hboxButtons, hboxOpslaan);
    }
    
    public void stuurBetalingsherinnering() throws InsertionError {
    	dc.stuurBetalingsherinnering();
    }
    
    public void stuurAutomatischeBetalingsherinnering() {
		ObservableList<Bestelling> bestellingen = dc.geefBestellingenByLeverancier();
		
		
		for(int i = 0; i < bestellingen.size(); i++) {
			dc.setSelectedBestelling(i);
			Bestelling bestelling = bestellingen.get(i);
			List<Betalingsherinnering> betalingsHerrineringen = dc.getBetalingsherrineringenByBestelling();
			if(bestelling.getBetalingsstatus() != Betalingsstatus.BETAALD && isWithinThreeDaysOrLess(bestelling.getBetalingsDeadline()) && betalingsHerrineringen.stream().filter(h -> h.getDatum().toString().equals(LocalDate.now().toString())).collect(Collectors.toList()).size() == 0) {
				System.out.print(bestelling.getOrderId());
				System.out.print(bestelling.getBetalingsDeadline());
				try {
					stuurBetalingsherinnering();
				} catch (InsertionError e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
    }

    public static boolean isWithinThreeDaysOrLess(LocalDate targetDate) {
        LocalDate today = LocalDate.now(); // Get the current date
        LocalDate threeDaysFromNow = today.plusDays(3); // Calculate the date for 3 days from now

        // Check if the target date is before or equal to three days from now and after or equal to today
        return !targetDate.isAfter(threeDaysFromNow) && !targetDate.isBefore(today);
    }
	private void opslaan(Button wijzigingenOpslaan, Bestelling b) throws InsertionError {
		b.setBetalingsstatus(choiceBoxBetalingsStatus.getValue());
		b.setOrderStatus(choiceBoxOrderStatus.getValue());
		Bedrijf l = b.getLeverancier();
		BedrijfDto leverancier = new BedrijfDto(l.getNaam(),l.getLogo(), l.getSector(), l.getAdres(), l.getContactgegevens(), l.getBTWnummer() );
		Bedrijf k = b.getKlant();
		BedrijfDto klant = new BedrijfDto(k.getNaam(),k.getLogo(), k.getSector(), k.getAdres(), k.getContactgegevens(), k.getBTWnummer() );
		BestellingDto dto = new BestellingDto(b.getDatumGeplaatst(), b.getLeverAdres(), b.getOrderStatus(), b.getBetalingsstatus(),leverancier,klant, b.getBetalingsDeadline());
		dc.updateBestelling(dto);
    }


	public void setFilters(String naam, String string) {
        dc.filter("Klant", naam, "Bestelling");
	}
	
//	boolean showIsWorkingWarning() {
//    	Alert alert = new Alert(AlertType.CONFIRMATION);
//		alert.setTitle("Werk verloren");
//		alert.setHeaderText("Pas op! Uw werk zal kwijt zijn.");
//		alert.setContentText("Bent u zeker dat u uw werk wilt stoppen?");
//		alert.showAndWait();
//		
//		if (alert.getResult() == ButtonType.OK) {
//		    return true;
//		}
//		return false;
//    }

}