package repository;

import java.util.List;

import domain.B2BBedrijf;
import domain.B2BBestelling;
import domain.Bedrijf;
import domain.Bestelling;
import jakarta.persistence.EntityNotFoundException;

public interface BestellingDao extends GenericDao<B2BBestelling> {

	List<B2BBestelling> getBestellingByLeverancier(Bedrijf leverancier) throws EntityNotFoundException;


}
