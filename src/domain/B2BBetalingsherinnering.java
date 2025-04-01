package domain;

import java.io.Serializable;

import java.time.LocalDate;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;

@Entity(name="Betalingsherinnering")
@NamedQuery(name = "Betalingsherinnering.findByBestelling", query = "SELECT b FROM Betalingsherinnering b WHERE b.bestelling = :bestelling")
public class B2BBetalingsherinnering implements Betalingsherinnering, Serializable{

	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name="orderId")
	private B2BBestelling bestelling;
	
	private LocalDate datum;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int herrineringid;
	
	public B2BBetalingsherinnering(B2BBestelling bestelling ,LocalDate datum) {
		this.bestelling = bestelling;
		this.datum = datum;
	}
	
	protected B2BBetalingsherinnering() {
		
	}

	public LocalDate getDatum(){
		return datum;
	}

	@Override
	public int hashCode() {
		return Objects.hash(bestelling, datum);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		B2BBetalingsherinnering other = (B2BBetalingsherinnering) obj;
		return Objects.equals(bestelling, other.bestelling) && Objects.equals(datum, other.datum);
	}
	
}
