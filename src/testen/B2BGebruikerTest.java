package testen;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import domain.B2BBedrijf;
import domain.B2BGebruiker;
import domain.Bedrijf;
import domain.Gebruiker;
import domain.Rol;

public class B2BGebruikerTest {
	
	@ParameterizedTest
	@NullAndEmptySource
	@ValueSource(strings= {"testEmail", "	"})
	public void GetEmail(String email) {
		Gebruiker gebruiker = new B2BGebruiker(email, "testWachtwoord", false, Rol.LEVERANCIER);
		
		Assertions.assertEquals(email, gebruiker.getEmail());
	}
	
	@ParameterizedTest
	@NullAndEmptySource
	@ValueSource(strings= {"testWachtwoord", "	"})
	public void GetWachtwoord(String wachtwoord) {
		Gebruiker gebruiker = new B2BGebruiker("testEmail", wachtwoord, false, Rol.LEVERANCIER);
		
		Assertions.assertEquals(wachtwoord, gebruiker.getWachtwoord());
	}
	
	@ParameterizedTest
	@ValueSource(booleans= {true, false})
	public void GetIsActief(Boolean isActief) {
		Gebruiker gebruiker = new B2BGebruiker("testEmail", "testWachtwoord", isActief, Rol.LEVERANCIER);
		
		Assertions.assertEquals(isActief, gebruiker.isActief());
	}
	
	@ParameterizedTest
	@EnumSource(value = Rol.class, names = {"ADMIN", "KLANT", "LEVERANCIER"})
	public void GetRol(Rol rol) {
		Gebruiker gebruiker = new B2BGebruiker("testEmail", "testWachtwoord", false, rol);
		
		Assertions.assertEquals(rol, gebruiker.getRol());
	}
	
	@Test
	public void VeranderEmail() {
		B2BGebruiker gebruiker = new B2BGebruiker("test", "testWachtwoord", false, Rol.LEVERANCIER);
		
		gebruiker.setEmail("anderEmail");
		
		Assertions.assertEquals("anderEmail", gebruiker.getEmail());
	}
	
	@Test
	public void VeranderWachtwoord() {
		B2BGebruiker gebruiker = new B2BGebruiker("testEmail", "testWachtwoord", false, Rol.LEVERANCIER);
		
		gebruiker.setWachtwoord("anderWachtwoord");
		
		Assertions.assertEquals("anderWachtwoord", gebruiker.getWachtwoord());
	}
	
	@Test
	public void VeranderIsActief() {
		B2BGebruiker gebruiker = new B2BGebruiker("testEmail", "testWachtwoord", false, Rol.LEVERANCIER);
		
		gebruiker.setActief(true);

		Assertions.assertEquals(true, gebruiker.isActief());
	}
	
	@Test
	public void VeranderRol() {
		B2BGebruiker gebruiker = new B2BGebruiker("testEmail", "testWachtwoord", false, Rol.LEVERANCIER);
		
		gebruiker.setRol(Rol.KLANT);

		Assertions.assertEquals(Rol.KLANT, gebruiker.getRol());
	}
	
	@Test
	public void SetBedrijf() {
		B2BGebruiker gebruiker = new B2BGebruiker("testEmail", "testWachtwoord", false, Rol.LEVERANCIER);

		B2BBedrijf bedrijf = new B2BBedrijf("testBedrijf", null, "testSector", "testAdres", "testGegevens", "BE0999999999");
		
		gebruiker.setB(bedrijf);
		
		Assertions.assertEquals(bedrijf, gebruiker.getB());
	}

}
