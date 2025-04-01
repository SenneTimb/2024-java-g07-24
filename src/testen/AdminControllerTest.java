package testen;

import domain.AdminController;
import domain.B2BBedrijf;
import domain.B2BGebruiker;
import domain.B2BPortaal;
import domain.Bedrijf;
import domain.InsertionError;
import dto.BedrijfDto;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityNotFoundException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import repository.GebruikerDaoJpa;
import repository.GenericDaoJpa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.mail.MessagingException;

@ExtendWith(MockitoExtension.class)
public class AdminControllerTest {

    @Mock
    private B2BPortaal portaalMock;

    @Mock
    private GenericDaoJpa daoMock;
    
    @Mock
    private B2BGebruiker gebruikerMock;

    @InjectMocks
    private AdminController adminController;

    @BeforeEach
    public void setUp() {
    	lenient().when(portaalMock.geefBedrijfBijNaam("test")).thenThrow(EntityNotFoundException.class);
    	
        adminController = new AdminController(portaalMock, gebruikerMock);
    }

    //test bij aanmaken 2 bedrijven, deze traint eerst de mock en verifieerd dan of de bedrijven worden teruggegeven
    @Test
    public void testGeefBedrijven() throws InsertionError, MessagingException {
    	List<BedrijfDto> bedrijvenDto = Arrays.asList(new BedrijfDto("test", null, "test", "test", "test@test.com", "BE0000111222"),
                new BedrijfDto("test2", null, "test2", "test2", "test1@test.com", "BE0000111223"));
    	
    	ObservableList<B2BBedrijf> b2Bbedrijven = FXCollections.observableArrayList(new B2BBedrijf("test", null, "test", "test", "test@test.com", "BE0000111222"),
                new B2BBedrijf("test2", null, "test2", "test2", "test1@test.com", "BE0000111223"));
    	
    	FilteredList<B2BBedrijf> filteredBedrijf = new FilteredList<>(b2Bbedrijven);
    	Mockito.when(portaalMock.geefBedrijfBijNaam("test2")).thenThrow(EntityNotFoundException.class);
		Mockito.when(portaalMock.geefBedrijven())
				.thenReturn(filteredBedrijf);
		
		adminController.maakBedrijf(bedrijvenDto.get(0));
		adminController.maakBedrijf(bedrijvenDto.get(1));
		
		assertEquals(b2Bbedrijven, adminController.geefBedrijven());
    }

    @Test
    public void testMaakBedrijf() throws InsertionError, MessagingException {
        BedrijfDto bedrijf = new BedrijfDto("test3", null, "test", "test", "test@test.com", "BE0000111222");
        
        ObservableList<B2BBedrijf> b2Bbedrijven = FXCollections.observableArrayList(new B2BBedrijf("test3", null, "test", "test", "test@test.com", "BE0000111222"));
    	
        FilteredList<B2BBedrijf> filteredBedrijf = new FilteredList<>(b2Bbedrijven);
        
        Mockito.when(portaalMock.geefBedrijfBijNaam("test3")).thenThrow(EntityNotFoundException.class);
        
    	Mockito.when(portaalMock.geefBedrijven())
			.thenReturn(filteredBedrijf);
        
        adminController.maakBedrijf(bedrijf);
        
        assertEquals(1, adminController.geefBedrijven().size());

        verify(portaalMock, times(1)).maakBedrijf(Mockito.any(B2BBedrijf.class), Mockito.any(B2BGebruiker.class), Mockito.any(B2BGebruiker.class));
    }

    @Test
    public void testUpdateBedrijf() throws InsertionError, MessagingException {
    	B2BBedrijf bedrijf = new B2BBedrijf("test10", null, "test", "test", "test@test.com", "BE0000111222");
    	ObservableList<B2BBedrijf> b2Bbedrijven = FXCollections.observableArrayList(bedrijf);
    	FilteredList<B2BBedrijf> filteredBedrijf = new FilteredList<>(b2Bbedrijven);
    	
    	Mockito.when(portaalMock.geefBedrijfBijNaam("test11")).thenThrow(EntityNotFoundException.class);
    	
    	Mockito.when(portaalMock.geefBedrijven())
			.thenReturn(filteredBedrijf);
    	
    	adminController.geefBedrijven();
        BedrijfDto bedrijf1 = new BedrijfDto("test11", null, "test", "test", "test@test.com", "BE0000111222");
        adminController.setSelectedBedrijf(0);
        BedrijfDto bedrijf2 = new BedrijfDto("test12", null, "test2", "test2", "test2@test.com", "BE0000111223");
        
        adminController.maakBedrijf(bedrijf1);
        adminController.updateBedrijf(bedrijf2);

        assertEquals(bedrijf2.naam(), adminController.getSelectedBedrijf().getNaam());
        
        verify(portaalMock, times(1)).updateBedrijf(Mockito.any(B2BBedrijf.class));
    }

    @Test
    public void testUpdateBedrijfGebruikerStatus() throws InsertionError, MessagingException {
        B2BBedrijf bedrijf = new B2BBedrijf("test", null, "test", "test", "test@test.com", "BE0000111222");
        ObservableList<B2BBedrijf> bedrijven = FXCollections.observableArrayList(bedrijf); 
        FilteredList<B2BBedrijf> filteredList = new FilteredList(bedrijven);
        Mockito.when(portaalMock.geefBedrijven()).thenReturn(filteredList);
        adminController.maakBedrijf(new BedrijfDto("test", null, "test", "test", "test@test.com", "BE0000111222"));
        adminController.setSelectedBedrijf(0);
        adminController.updateBedrijfGebruikerStatus(true);
        
        verify(daoMock, times(bedrijf.getGebruikers().size())).update(Mockito.any());
    }

    @Test
    public void testSetSelectedBedrijf() throws InsertionError, MessagingException {
    	B2BBedrijf bedrijf = new B2BBedrijf("test", null, "test", "test", "test@test.com", "BE0000111222");
    	Mockito.when(portaalMock.geefBedrijfBijNaam("test9")).thenThrow(EntityNotFoundException.class);
    	
    	ObservableList<B2BBedrijf> b2Bbedrijven = FXCollections.observableArrayList(bedrijf);
    	
    	FilteredList<B2BBedrijf> filteredbedrijf = new FilteredList<>(b2Bbedrijven);
    	
    	Mockito.when(portaalMock.geefBedrijven())
			.thenReturn(filteredbedrijf);
       
        adminController.maakBedrijf(new BedrijfDto("test9", null, "test", "test", "test@test.com", "BE0000111222"));
        adminController.geefBedrijven();
        adminController.setSelectedBedrijf(0);

        assertEquals((Bedrijf) bedrijf, adminController.getSelectedBedrijf());
    }

    @Test
    public void testGeefBedrijf() throws InsertionError, MessagingException {      
    	BedrijfDto bedrijfDto = new BedrijfDto("test", null, "test", "test", "test@test.com", "BE0000111222");
    	
    	B2BBedrijf b2Bbedrijf = new B2BBedrijf("test", null, "test", "test", "test@test.com", "BE0000111222");
    	
		Mockito.when(portaalMock.geefBedrijf(0))
				.thenReturn(b2Bbedrijf);
		
		adminController.maakBedrijf(bedrijfDto);
		
		assertEquals(b2Bbedrijf, adminController.geefBedrijf(0));
    }
}
