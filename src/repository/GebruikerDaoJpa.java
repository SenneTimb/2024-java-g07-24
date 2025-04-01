package repository;


import java.util.List;

import domain.B2BGebruiker;
import domain.Rol;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NoResultException;

public class GebruikerDaoJpa extends GenericDaoJpa<B2BGebruiker> implements GebruikerDao  {

	public GebruikerDaoJpa() {
		super(B2BGebruiker.class);
	}

	@Override
	public B2BGebruiker getGebruikerByEmail(String email) throws EntityNotFoundException {
		 try {
            return em.createNamedQuery("Gebruiker.findByEmail", B2BGebruiker.class)
                 .setParameter("gebruikerEmail", email)
                 .setParameter("gebruikerRol", Rol.KLANT)
                .getSingleResult();
        } catch (NoResultException ex) {
            throw new EntityNotFoundException();
        } 
	}
	
	public List<B2BGebruiker> getGebruikersByBedrijfId(int bedrijfId) {
		try {
			return em.createNamedQuery("Gebruiker.findByBedrijfId", B2BGebruiker.class)
					.setParameter("bedrijfId", bedrijfId)
					.getResultList();
		} catch (NoResultException e) {
			throw new EntityNotFoundException();
		}
	}
}
