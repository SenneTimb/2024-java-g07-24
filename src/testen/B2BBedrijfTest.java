package testen;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import domain.B2BBedrijf;
import domain.Bedrijf;

public class B2BBedrijfTest {
	
	@Test
	public void GetnaamBedrijf() {
		Bedrijf bedrijf = new B2BBedrijf("testBedrijf", null, "testsector", "testAdres", "testGegevens", "BE0999999999");
		
		Assertions.assertEquals("testBedrijf", bedrijf.getNaam());
	}
	
	@ParameterizedTest
	@NullAndEmptySource
	@ValueSource(strings= {"andereNaamBedrijf", "", "		"})
	public void veranderNaamBedrijf(String naam) {
		B2BBedrijf bedrijf = new B2BBedrijf("testBedrijf", null, "testsector", "testAdres", "testGegevens", "BE0999999999");
		bedrijf.setNaam(naam);
		
		Assertions.assertEquals(naam, bedrijf.getNaam());
	}

}
