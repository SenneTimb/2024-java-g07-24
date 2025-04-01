package testen;

import java.time.LocalDate;
import java.util.Date;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import domain.B2BBedrijf;
import domain.B2BBestelling;
import domain.B2BProduct;
import domain.Bedrijf;
import domain.Bestelling;
import domain.Betalingsstatus;
import domain.Orderstatus;
import domain.Product;

public class B2BBestellingTest {
	
	LocalDate datum = LocalDate.now();
	
	B2BBestelling bestelling = new B2BBestelling(datum, "testAdres", Orderstatus.VERWERKT, Betalingsstatus.ONVERWERKT, null, null, datum);
	
	@Test
	public void aanmakenBestelling() {
		Assertions.assertDoesNotThrow(() -> new B2BBestelling(datum, "testAdres", Orderstatus.VERWERKT, Betalingsstatus.ONVERWERKT, null, null, datum));
	}
	
	@Test
	public void voegTestProductToe() {
        B2BProduct p = new B2BProduct("testProduct", 5, false, 10.0);
        Assertions.assertDoesNotThrow(() -> bestelling.voegTestProductToe(p, 2));
    }
	
	@Test
	public void getDatumGeplaatst() {
		Assertions.assertEquals(datum, bestelling.getDatumGeplaatst());
	}
	
	@Test
	public void getLeverAdres() {
		Assertions.assertEquals("testAdres", bestelling.getLeverAdres());
	}
	
	@Test
	public void getOrderStatus() {
		Assertions.assertEquals("verwerkt", bestelling.getOrderStatus().toString().toLowerCase());
	}
	
//	@Test
//	public void getBetaalGegevens() {
//		Assertions.assertEquals("testBetaalwijze", bestelling.getBetaalGegevens());
//	}
//	
	@Test
	public void getProducten() {
		B2BProduct p = new B2BProduct("testProduct", 5, false, 10.0);
		bestelling.voegTestProductToe(p, 1);
		Assertions.assertEquals(p, bestelling.getProducten().get(0));
	}
	
	@Test
	public void getTotaleOrderBedrag() {
		B2BProduct p = new B2BProduct("testProduct", 5, false, 10.0);
		bestelling.voegTestProductToe(p, 1);
		Assertions.assertEquals(50.0, bestelling.getTotaleOrderBedrag());
	}
	
	@Test
	public void getLeverancier() {
		B2BBedrijf bedrijf = new B2BBedrijf("testBedrijf", null, "testsector", "testAdres", "testGegevens", "BE0999999999");
		B2BBestelling bestelling2 = new B2BBestelling(datum, "testAdres", Orderstatus.VERWERKT, Betalingsstatus.ONVERWERKT, bedrijf, null, datum);
		Assertions.assertEquals((Bedrijf) bedrijf, bestelling2.getLeverancier());
	}

}
