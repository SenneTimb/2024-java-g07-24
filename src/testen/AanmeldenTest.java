	package testen;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import domain.LoginController;
import domain.DomeinController;
import domain.B2BGebruiker;
import domain.Rol;
import domain.UserDataInvalidException;
import jakarta.persistence.EntityNotFoundException;
import repository.GebruikerDao;

@ExtendWith(MockitoExtension.class)
public class AanmeldenTest {

	@Mock
	private GebruikerDao gebruikerRepo;
	
	@InjectMocks
	private LoginController domein;
	
	@Test
	public void TestAanmelden_geldigeGegevens_returnsTrue() throws UserDataInvalidException {
		final String EMAIL = "admin@admin.com", PASSWORD = "1234";
		B2BGebruiker g = new B2BGebruiker(EMAIL, PASSWORD, true, Rol.KLANT);
		
		Mockito.when(gebruikerRepo.getGebruikerByEmail(EMAIL)).thenReturn(g);
		
		assertInstanceOf(DomeinController.class, domein.meldAan(EMAIL, PASSWORD));
		
		Mockito.verify(gebruikerRepo).getGebruikerByEmail(EMAIL);
	}
	
	@Test
	public void TestAanmelden_geldigeGegevensNietActief_ReturnsFalse() throws UserDataInvalidException {
		final String EMAIL = "admin@admin.com", PASSWORD = "1234";
		B2BGebruiker g = new B2BGebruiker(EMAIL, PASSWORD, false, Rol.KLANT);
		
		Mockito.when(gebruikerRepo.getGebruikerByEmail(EMAIL)).thenReturn(g);
		
		assertThrows(IllegalArgumentException.class, () -> domein.meldAan(EMAIL, PASSWORD));
		
		Mockito.verify(gebruikerRepo).getGebruikerByEmail(EMAIL);
	}
	
	@ParameterizedTest
	@NullAndEmptySource
	@ValueSource(strings= {" ", "     ", "", "  "})
	public void TestAanmelden_emailLeeg_throwsException(String email) {
		final String PASSWORD = "1234";
				
		assertThrows(IllegalArgumentException.class, () -> domein.meldAan(email, PASSWORD));
	}

	
	@ParameterizedTest
	@NullAndEmptySource
	@ValueSource(strings= {" ", "     ", "", "  "})
	public void TestAanmelden_wachtwoordLeeg_throwsException(String wachtwoord) {
		final String EMAIL = "admin@admin.com";
				
		assertThrows(IllegalArgumentException.class, () -> domein.meldAan(EMAIL, wachtwoord));
	}
		
	
	@ParameterizedTest
	@ValueSource(strings={"123", "12345", "wachtwoord"})
	public void TestAanmelden_ongeldigeWachtwoord_throwsException(String wachtwoord) {
		final String EMAIL = "admin@admin.com", PASSWORD = "1234";
		B2BGebruiker g = new B2BGebruiker(EMAIL, PASSWORD, true, Rol.KLANT);
		
		Mockito.when(gebruikerRepo.getGebruikerByEmail(EMAIL)).thenReturn(g);
		
		assertThrows(UserDataInvalidException.class, () -> domein.meldAan(EMAIL, wachtwoord));
		
		Mockito.verify(gebruikerRepo).getGebruikerByEmail(EMAIL);
	}
	
	@ParameterizedTest
	@ValueSource(strings= {"admin@admi.com", "admin.com", "klant@admin.com"})
	public void TestAanmelden_ongeldigeEmail_throwsException(String email) {
		final String PASSWORD = "1234";
		
		Mockito.when(gebruikerRepo.getGebruikerByEmail(email)).thenThrow(EntityNotFoundException.class);
		
		assertThrows(EntityNotFoundException.class, () -> domein.meldAan(email, PASSWORD));
		
		Mockito.verify(gebruikerRepo).getGebruikerByEmail(email);
	}
}
