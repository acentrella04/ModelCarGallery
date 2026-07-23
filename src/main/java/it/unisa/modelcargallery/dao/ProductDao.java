package it.unisa.modelcargallery.dao;

import java.sql.SQLException;
import java.util.Collection;

import it.unisa.modelcargallery.model.ProductBean;

public interface ProductDao {

	public void doSave(ProductBean product) throws SQLException;

	public boolean doUpdateImage(ProductBean product) throws SQLException;

	public boolean doDelete(int code) throws SQLException;

	void doUpdate(ProductBean product) throws SQLException;

	public ProductBean doRetrieveByKey(int code) throws SQLException;

	public Collection<ProductBean> doRetrieveAll(String order) throws SQLException;

	public Collection<ProductBean> doRetrieveByDescription(String description) throws SQLException;

	public Collection<ProductBean> doRetrieveRandom() throws SQLException;
}
