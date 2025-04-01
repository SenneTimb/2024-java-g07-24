package repository;

import java.util.List;

import domain.B2BBedrijf;
import domain.B2BBestelling;
import domain.Bedrijf;
import domain.Bestelling;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NoResultException;

public class BestellingDaoJpa extends GenericDaoJpa<B2BBestelling> implements BestellingDao {

	public BestellingDaoJpa() {
		super(B2BBestelling.class);
		// TODO Auto-generated constructor stub
	}
	
    @Override
    public List<B2BBestelling> getBestellingByLeverancier(Bedrijf leverancier) throws EntityNotFoundException {
        try {
            return em.createNamedQuery("B2BBestelling.findByLeverancier", B2BBestelling.class)
                    .setParameter("leverancier", leverancier).getResultList();
        } catch (NoResultException ex) {
            throw new EntityNotFoundException("Geen bestellingen voor deze leverancier gevonden");
        }
	}
}
