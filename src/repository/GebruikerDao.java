package repository;

import java.util.List;

import domain.B2BGebruiker;
import jakarta.persistence.EntityNotFoundException;

public interface GebruikerDao extends GenericDao<B2BGebruiker> {
	public B2BGebruiker getGebruikerByEmail(String email) throws EntityNotFoundException;
}
