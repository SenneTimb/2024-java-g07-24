package repository;

import java.util.List;

import domain.B2BBetalingsherinnering;
import domain.Bestelling;
import jakarta.persistence.EntityNotFoundException;

public interface BetalingsherinneringDao extends GenericDao<B2BBetalingsherinnering> {
	
	List<B2BBetalingsherinnering> getBetalingsherinneringenByBestelling(Bestelling bestelling) throws EntityNotFoundException;
	
}
