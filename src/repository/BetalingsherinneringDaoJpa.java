package repository;

import java.util.List;

import domain.B2BBedrijf;
import domain.B2BBestelling;
import domain.B2BBetalingsherinnering;
import domain.Bestelling;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NoResultException;

public class BetalingsherinneringDaoJpa extends GenericDaoJpa<B2BBetalingsherinnering> implements BetalingsherinneringDao {
	public BetalingsherinneringDaoJpa(){
        super(B2BBetalingsherinnering.class);
    }

	@Override
	public List<B2BBetalingsherinnering> getBetalingsherinneringenByBestelling(Bestelling bestelling)
			throws EntityNotFoundException {
		try {
            return em.createNamedQuery("Betalingsherinnering.findByBestelling", B2BBetalingsherinnering.class)
                    .setParameter("bestelling", bestelling).getResultList();
        } catch (NoResultException ex) {
            throw new EntityNotFoundException("Geen Betalingsherinneringen voor deze bestelling gevonden");
        }
	}
}
