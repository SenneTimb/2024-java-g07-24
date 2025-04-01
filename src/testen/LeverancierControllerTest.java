package testen;

import domain.*;
import dto.BedrijfDto;
import dto.BestellingDto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import javax.mail.MessagingException;

@ExtendWith(MockitoExtension.class)
public class LeverancierControllerTest {

    @Mock
    private B2BPortaal portaalMock;

    @Mock
    private B2BGebruiker gebruikerMock;

    private LeverancierController leverancierController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        when(portaalMock.geefBestellingen()).thenReturn(FXCollections.observableArrayList());
        leverancierController = new LeverancierController(portaalMock, gebruikerMock);
    }
    
    @Test
    public void testConstructor() {
        assertEquals(portaalMock, leverancierController.getPortaal());
        assertEquals(gebruikerMock, leverancierController.getGebruiker());
    }
    
    @Test
    public void testGetSelectedBestelling() {
        B2BBestelling bestelling = mock(B2BBestelling.class);
        when(portaalMock.geefBestellingen()).thenReturn(FXCollections.observableArrayList(bestelling));
        leverancierController.setSelectedBestelling(0);
        assertEquals(bestelling, leverancierController.getSelectedBestelling());
    }

    @Test
    public void testGetSelectedBedrijf() {
        ObservableList<B2BBedrijf> b2Bbedrijven = FXCollections.observableArrayList(new B2BBedrijf("test", null, "test", "test", "test@test.com", "BE0000111222"),
                new B2BBedrijf("test2", null, "test2", "test2", "test1@test.com", "BE0000111223"));
        
        FilteredList<B2BBedrijf> filteredList = new FilteredList(b2Bbedrijven);
        
        B2BGebruiker gebruikermock = new B2BGebruiker("test@test.com", "1234", false, Rol.LEVERANCIER);
        gebruikermock.setB(b2Bbedrijven.get(0));
        
        leverancierController = new LeverancierController(portaalMock, gebruikermock);
        lenient().when(portaalMock.geefBedrijvenByLeverancier(gebruikermock.getB())).thenReturn((FilteredList<Bedrijf>)(Object) filteredList);
        leverancierController.setSelectedBedrijf(0);
        assertEquals(b2Bbedrijven.get(0), leverancierController.getSelectedBedrijf());
    } 

    @Test 
    public void testGeefBedrijven() throws InsertionError, MessagingException {
        ObservableList<B2BBedrijf> b2Bbedrijven = FXCollections.observableArrayList(new B2BBedrijf("test", null, "test", "test", "test@test.com", "BE0000111222"),
                new B2BBedrijf("test2", null, "test2", "test2", "test1@test.com", "BE0000111223"));

        FilteredList<B2BBedrijf> filteredlistBedrijf = new FilteredList<>(b2Bbedrijven);

        ObservableList<B2BBestelling> B2BBestelling = FXCollections.observableArrayList(new B2BBestelling(null, null, null, null, b2Bbedrijven.get(1), b2Bbedrijven.get(0), LocalDate.now().plusDays(2)), 
        		new B2BBestelling(null, null, null, null, b2Bbedrijven.get(1), b2Bbedrijven.get(0), LocalDate.now().plusDays(2)));
        
        FilteredList<B2BBestelling> filteredlist = new FilteredList<>(B2BBestelling);
        
        B2BGebruiker gebruikermock = new B2BGebruiker("test@test.com", "1234", false, Rol.LEVERANCIER);
        gebruikermock.setB(b2Bbedrijven.get(1));
        leverancierController = new LeverancierController(portaalMock, gebruikermock);
        
        Mockito.when(portaalMock.geefBedrijvenByLeverancier(gebruikermock.getB())).thenReturn((FilteredList<Bedrijf>) (Object) filteredlistBedrijf);

        assertEquals(new FilteredList(b2Bbedrijven), leverancierController.geefBedrijven());
    }
    
    @Test
    public void testGetBetalingsherinneringenByBestelling() {
        // Arrange
//    	BestellingDto bestellingDto = new BestellingDto(null, null, null, null, null, null, LocalDate.now().plusDays(2));
    	B2BBestelling bestellingB2B = new B2BBestelling(null, null, null, null, null, null, LocalDate.now().plusDays(2));
    	B2BBetalingsherinnering betalingsherinnering = new B2BBetalingsherinnering(bestellingB2B, LocalDate.now());
    	List<B2BBetalingsherinnering> betalingsherinneringen = Arrays.asList(betalingsherinnering);
        
    	when(portaalMock.geefBestellingen()).thenReturn(FXCollections.observableArrayList(bestellingB2B));
    	Mockito.when(portaalMock.getBetalingsherrineringenByBestelling(bestellingB2B)).thenReturn(betalingsherinneringen);
        // Act
        leverancierController.setSelectedBestelling(0); 
        List<Betalingsherinnering> actualBetalingsherinneringen = leverancierController.getBetalingsherrineringenByBestelling();

        // Assert
        // Controleer of de werkelijke betalingsherinneringen gelijk zijn aan de verwachte betalingsherinneringen
        assertEquals(actualBetalingsherinneringen.get(0), betalingsherinnering);
    }
}
