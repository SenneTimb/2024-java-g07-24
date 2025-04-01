package domain;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import dto.BedrijfDto;
import dto.BestellingDto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

public class LeverancierController extends DomeinController{
	private B2BBestelling selectedBestelling;
	private FilteredList<B2BBestelling> bestellingen;
	private FilteredList<B2BBedrijf> klanten;
	private Bedrijf selectedBedrijf;
	
	public LeverancierController(B2BPortaal portaal, B2BGebruiker gebruiker) {
		super(portaal, gebruiker);
		bestellingen = new FilteredList<>(portaal.geefBestellingen());
	}


	// geeft de bedrijven met openstaande bestellingen (klanten) bij ingelogde leverancier terug
	@Override
	public FilteredList<Bedrijf> geefBedrijven()  {
	    if (portaal.geefBedrijvenByLeverancier(this.getGebruiker().getB()) == null || portaal.geefBedrijvenByLeverancier(this.getGebruiker().getB()).isEmpty()) {
	        System.out.println("Er zijn geen klanten");
	    }

	    
//	    Set<B2BBedrijf> uniqueBedrijven = this.portaal.geefBestellingenByLeverancier(this.getGebruiker().getB()).stream()
//	            .filter(bestelling -> bestelling.getKlant() != null)
//	            .map(bestelling -> (B2BBedrijf) bestelling.getKlant())
//	            .collect(Collectors.toCollection(LinkedHashSet::new));

	    return (FilteredList<Bedrijf>) (Object) this.portaal.geefBedrijvenByLeverancier(this.getGebruiker().getB());
	}

	@Override
	public void setSelectedBedrijf(int bedrijf) {
		selectedBedrijf = portaal.geefBedrijvenByLeverancier((B2BBedrijf) this.getGebruiker().getB()).get(bedrijf);
		System.out.println(selectedBedrijf.getNaam());
		
	}
	
	@Override
	public Bedrijf getSelectedBedrijf() {
		return (Bedrijf) selectedBedrijf;
	}
	
	@Override
	public void setSelectedBestelling(int bestelling) {
		selectedBestelling = portaal.geefBestellingen().get(bestelling);
	}


	@Override
	public FilteredList<Bestelling> geefBestellingen() {
		return (FilteredList<Bestelling>) (Object) bestellingen;
	}
	
	@Override
	public Bestelling getSelectedBestelling() {
		return (Bestelling) selectedBestelling;
	}


	@Override
	public Bedrijf geefBedrijf(int id) {
		return geefBedrijven().get(id);
	}
	
	@Override
    public FilteredList<Bestelling> geefBestellingenByLeverancier(){
		return (FilteredList<Bestelling>) (Object) this.portaal.geefBestellingenByLeverancier((B2BBedrijf) gebruiker.getB());
    }
	
	@Override
	public List<Betalingsherinnering> getBetalingsherrineringenByBestelling() {
		return (List<Betalingsherinnering>) (Object) this.portaal.getBetalingsherrineringenByBestelling(this.selectedBestelling);
	}
	
	public void stuurBetalingsherinnering() throws InsertionError {
	    ExecutorService emailVerzendExecutor = Executors.newSingleThreadExecutor();
	    
	    B2BBetalingsherinnering betalingsherinnering = new B2BBetalingsherinnering(this.selectedBestelling, LocalDate.now());
	    this.portaal.stuurBetalingsherinnering(betalingsherinnering);

	    Bedrijf klant = this.selectedBestelling.getKlant();
	    Bedrijf leverancier = this.selectedBestelling.getLeverancier();

	    String productNamenInLijst = this.selectedBestelling.getProducten().stream()
	            .map(p -> "<li>" + p.getProduct().getNaam() + "</li>")
	            .collect(Collectors.joining("", "<ul>", "</ul>"));

	    String content = "<h1>Betalingsherinnering</h1>"
	            + "<p>Geachte heer/mevrouw,</p>"
	            + "<p>Graag attenderen wij u op de openstaande betalingsverplichting bij " + leverancier.getNaam() + ". Volgens onze administratie is de betaling van onderstaande bestelling nog niet voldaan. Wij verzoeken u vriendelijk het verschuldigde bedrag te voldoen voor de betalingsdeadline op " + this.selectedBestelling.getBetalingsDeadline() + ".</p>"
	            + "<p>Omschrijving van uw bestelling:</p>"
	            + productNamenInLijst
	            + "<p>Indien u reeds heeft betaald, gelieve dit bericht als niet verzonden te beschouwen. Voor vragen kunt u contact opnemen met onze administratie.</p>"
	            + "<p>Met vriendelijke groet,</p>"
	            + "<p>" + leverancier.getNaam() + "</p>";

	    emailVerzendExecutor.submit(() -> {
	        try {
	            GmailProvider.sendEmail(klant.getContactgegevens(), "Delaware portaal - Betalingsherinnering", content);
	            System.out.println("Email verzonden op: " + Thread.currentThread().getName());
	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	            emailVerzendExecutor.shutdown();
	        }
	    });
	}

	
	@Override
	public void updateBestelling(BestellingDto dto) throws InsertionError {
		selectedBestelling.setDatumGeplaatst(dto.datumGeplaatst());
		selectedBestelling.setBetalingsstatus(dto.betalingsstatus());
		B2BBedrijf klant = portaal.geefBedrijfBijNaam(dto.klant().naam());
		selectedBestelling.setKlant(klant);
		B2BBedrijf leverancier = portaal.geefBedrijfBijNaam(dto.leverancier().naam());
		selectedBestelling.setLeverancier(leverancier);
		selectedBestelling.setLeverAdres(dto.leverAdres());
		this.portaal.updateBestelling(selectedBestelling);
		B2BBestelling bestelling = selectedBestelling;
	}
	
	public B2BPortaal getPortaal() {
		return this.portaal;
	}


	@Override
	public void filter(String selectedOptie, String filterWaarde, String filterenOp) {
		
		if (filterenOp.equals("Bestelling")) {
			Filter<Bestelling> filter = filterFactory.CreateFilter(selectedOptie);
			filter.filter(geefBestellingenByLeverancier(), filterWaarde);
		}
		else {
			Filter<Bedrijf> filter = filterFactory.CreateFilter(selectedOptie);
			filter.filter(geefBedrijven(), filterWaarde);
		}
		
	}
}
