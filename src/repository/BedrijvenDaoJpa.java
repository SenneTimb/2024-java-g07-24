package repository;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import domain.B2BBedrijf;
import domain.B2BBestelling;
import domain.Bedrijf;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceException;


public class BedrijvenDaoJpa extends GenericDaoJpa<B2BBedrijf> implements BedrijvenDao {

    public BedrijvenDaoJpa() {
        super(B2BBedrijf.class);
    }

    @Override
    public B2BBedrijf getBedrijfByName(String name) throws EntityNotFoundException {
        try {
            return em.createNamedQuery("B2BBedrijf.findByName", B2BBedrijf.class)
                    .setParameter("bedrijfNaam", name)
                    .getSingleResult();
        } catch (NoResultException ex) {
            throw new EntityNotFoundException("Bedrijf not found for name: " + name);
        }
    }

	

   }

