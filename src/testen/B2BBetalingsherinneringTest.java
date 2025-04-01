package testen;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import domain.B2BBestelling;
import domain.B2BBetalingsherinnering;
import domain.Betalingsstatus;
import domain.Orderstatus;

public class B2BBetalingsherinneringTest {
	private B2BBestelling bestelling;
	private LocalDate datum;

	@BeforeEach
	public void setUp() {
		bestelling = new B2BBestelling(LocalDate.now(), "Test Adres", Orderstatus.GELEVERD, Betalingsstatus.BETAALD,
				null, null, LocalDate.now().plusDays(10));
		datum = LocalDate.of(2024, 3, 26);
	}

	@Test
	public void testGetDatum() {
		B2BBetalingsherinnering herinnering = new B2BBetalingsherinnering(bestelling, datum);
		assertEquals(datum, herinnering.getDatum());
	}

	
}