package repository;

import domain.B2BProduct;

public class ProductDaoJpa extends GenericDaoJpa<B2BProduct> implements ProductDao {

	public ProductDaoJpa() {
		super(B2BProduct.class);
		// TODO Auto-generated constructor stub
	}



}
