package it.unisa.modelcargallery.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CartBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<ProductBean> products;

	public CartBean() {
		products = new ArrayList<ProductBean>();
	}

	public void addProduct(ProductBean product) {
		products.add(product);
	}

	public void deleteProduct(ProductBean product) {
		for (ProductBean prod : products) {
			if (prod.getCode() == product.getCode()) {
				products.remove(prod);
				break;
			}
		}
	}

	public List<ProductBean> getProducts() {
		return products;
	}
}
