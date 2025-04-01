package repository;

import java.util.List;

import domain.B2BBedrijf;
import domain.B2BBestelling;
import domain.Bedrijf;
import jakarta.persistence.EntityNotFoundException;

public interface BedrijvenDao extends GenericDao<B2BBedrijf> {
	B2BBedrijf getBedrijfByName(String name) throws EntityNotFoundException;


}
