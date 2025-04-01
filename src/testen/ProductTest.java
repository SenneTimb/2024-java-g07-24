package testen;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import domain.Bedrijf;
import domain.B2BProduct;

public class ProductTest {
	
	@Test
	public void maakProductMetGoedeNaam() {
        Assertions.assertDoesNotThrow(() -> new B2BProduct("TestNaam", 1, false, 1));
	}
	
	@ParameterizedTest
	@NullAndEmptySource
	@ValueSource(strings= {"", "	"})
	public void maakProductMetFouteNaam(String naam) {
	Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new B2BProduct(naam, 0, false, 0);
        });
	}
	
	@ParameterizedTest
	@ValueSource(ints= {0, 1, 100})
	public void maakProductMetGoedAantal(int aantal) {
        Assertions.assertDoesNotThrow(() -> new B2BProduct("TestNaam", aantal, false, 1));
	}
	
	@ParameterizedTest
	@ValueSource(ints= {-1})
	public void maakProductMetFoutAantal(int aantal) {
	Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new B2BProduct("TestNaam", aantal, false, 0);
        });
	}
	
	@ParameterizedTest
	@ValueSource(doubles= {0.1, 100.2})
	public void maakProductMetGoedeEenheidsprijs(double prijs) {
        Assertions.assertDoesNotThrow(() -> new B2BProduct("TestNaam", 2, false, prijs));
	}
	
	@ParameterizedTest
	@ValueSource(doubles= {0, -0.1, -100})
	public void maakProductMetFouteEenheidsprijs(double prijs) {
	Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new B2BProduct("TestNaam", 2, false, prijs);
        });
	}
	
	
	@Test
	public void GetNaam() {
		B2BProduct product = new B2BProduct("testNaam", 2, false, 2);
		
		Assertions.assertEquals("testNaam", product.getNaam());
	}
	
	@Test
	public void GetAantal() {
		B2BProduct product = new B2BProduct("testNaam", 2, false, 2);
		
		Assertions.assertEquals(2, product.getAantal());
	}
	
	@Test
	public void GetInstock() {
		B2BProduct product = new B2BProduct("testNaam", 2, false, 2);
		
		Assertions.assertFalse(product.isInStock());
	}
	
	@Test
	public void GetEenheidsprijs() {
		B2BProduct product = new B2BProduct("testNaam", 2, false, 2);
		
		Assertions.assertEquals(2, product.getEenheidsprijs());
	}
	
}
